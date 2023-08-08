package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.CredentialsDto;
import by.pavel.imageannotationbe.dto.RegistrationDto;
import by.pavel.imageannotationbe.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {

    private static final Map<String, String> ACCESS_TOKEN = Map.of("access_token", "token");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getToken() throws Exception {
        //given
        CredentialsDto credentialsDto = new CredentialsDto("email", "password");
        when(userService.generateToken(credentialsDto)).thenReturn(ACCESS_TOKEN);
        //when
        mockMvc.perform(post("/token")
                        .content(objectMapper.writeValueAsString(credentialsDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("token"));
    }

    @Test
    void register() throws Exception {
        //given
        RegistrationDto registrationDto = new RegistrationDto("email", "password", "name", "surname");
        //when
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(registrationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(userService).register(registrationDto);
    }
}
