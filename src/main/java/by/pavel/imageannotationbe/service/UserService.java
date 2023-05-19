package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.controller.exceptionHandling.BadRequestException;
import by.pavel.imageannotationbe.dto.CredentialsDto;
import by.pavel.imageannotationbe.dto.RegistrationDto;
import by.pavel.imageannotationbe.model.License;
import by.pavel.imageannotationbe.model.LicenseType;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.LicenseRepository;
import by.pavel.imageannotationbe.repository.UserRepository;
import by.pavel.imageannotationbe.security.UserDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LicenseRepository licenseRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPasswordHash(), licenseRepository.existsLicenseByOwnerEmail(username));
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
                .licenseType(LicenseType.builder().id(1L).build())
                .owner(saved)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .build();
        licenseRepository.save(previewLicense);
    }

    @SneakyThrows
    public Map<String, String> generateToken(CredentialsDto credentials) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
        UserDetailsImpl user = (UserDetailsImpl) (authenticationManager.authenticate(authentication).getPrincipal());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        Instant accessTokenExpiresAt = OffsetDateTime.now().plusDays(30).toInstant();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(accessTokenExpiresAt.toEpochMilli()))
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        return tokens;
    }
}
