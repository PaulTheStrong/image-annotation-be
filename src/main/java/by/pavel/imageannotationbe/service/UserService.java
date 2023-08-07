package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.CredentialsDto;
import by.pavel.imageannotationbe.dto.RegistrationDto;
import by.pavel.imageannotationbe.dto.UserDto;
import by.pavel.imageannotationbe.exception.BadRequestException;
import by.pavel.imageannotationbe.model.License;
import by.pavel.imageannotationbe.model.LicenseType;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.LicenseRepository;
import by.pavel.imageannotationbe.repository.UserRepository;
import by.pavel.imageannotationbe.security.UserDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, UserAware {

    public static final int ACCESS_TOKEN_DAYS = 30;
    public static final int PREVIEW_LICENSE_DURATION_DAYS = 7;
    public static final long PREVIEW_LICENSE_ID = 1L;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LicenseRepository licenseRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                licenseRepository.existsLicenseByOwnerEmailAndEndDateAfter(username, LocalDateTime.now())
        );
    }

    @Transactional
    public void register(RegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.email()).isPresent()) {
            throw new BadRequestException("User with this email exists");
        }
        User user = new User(null,
                registrationDto.name(),
                registrationDto.surname(),
                registrationDto.email(),
                passwordEncoder.encode(registrationDto.password()),
                List.of()
        );
        User saved = userRepository.save(user);
        License previewLicense = License.builder()
                .licenseType(LicenseType.builder().id(PREVIEW_LICENSE_ID).build())
                .owner(saved)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(PREVIEW_LICENSE_DURATION_DAYS))
                .build();
        licenseRepository.save(previewLicense);
    }

    @SneakyThrows
    public Map<String, String> generateToken(CredentialsDto credentials) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                credentials.email(),
                credentials.password());
        UserDetailsImpl user = (UserDetailsImpl) (authenticationManager.authenticate(authentication).getPrincipal());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        Instant accessTokenExpiresAt = OffsetDateTime.now().plusDays(ACCESS_TOKEN_DAYS).toInstant();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(accessTokenExpiresAt.toEpochMilli()))
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        return tokens;
    }

    public UserDto getMe() {
        Long userId = getCurrentUser().getId();
        User user = userRepository.findById(userId).get();
        return UserDto.toDto(user);
    }

    public UserDto updateMe(UserDto userDto) {
        Long currentUserId = getCurrentUser().getId();
        User user = userRepository.findById(currentUserId).get();
        if (StringUtils.isNotBlank(userDto.name())) {
            user.setName(userDto.name());
        }

        if (StringUtils.isNotBlank(userDto.surname())) {
            user.setSurname(userDto.surname());
        }

        if (StringUtils.isNotBlank(userDto.password())) {
            user.setPasswordHash(passwordEncoder.encode(userDto.password()));
        }

        return UserDto.toDtoNoLicenses(userRepository.save(user));
    }
}
