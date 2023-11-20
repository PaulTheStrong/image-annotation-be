package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.CredentialsDto;
import by.pavel.imageannotationbe.dto.RegistrationDto;
import by.pavel.imageannotationbe.dto.UserDto;
import by.pavel.imageannotationbe.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {

    private static final Map<String, String> ACCESS_TOKEN = Map.of("access_token", "token");
    private static final UserDto USER_DTO = new UserDto(1L, "email", "name", "surname", "passwordHash", List.of());

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

    @Test
    void getMe() throws Exception {
        //given
        when(userService.getMe()).thenReturn(USER_DTO);
        //when
        mockMvc.perform(get("/me"))
        //then
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(USER_DTO)));
    }

    @Test
    void updateMe() throws Exception {
        when(userService.updateMe(USER_DTO)).thenReturn(USER_DTO);
        //when
        mockMvc.perform(post("/me")
                        .content(objectMapper.writeValueAsString(USER_DTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(USER_DTO)));

        verify(userService).updateMe(USER_DTO);
    }
}
