package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.CredentialsDto;
import by.pavel.imageannotationbe.dto.RegistrationDto;
import by.pavel.imageannotationbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/token")
    public Map<String, String> getToken(@RequestBody CredentialsDto credentialsDto) {
        return userService.generateToken(credentialsDto);
    }

    @PostMapping(value = "/register")
    public void register(@RequestBody RegistrationDto registrationDto) {
        userService.register(registrationDto);
    }

}
