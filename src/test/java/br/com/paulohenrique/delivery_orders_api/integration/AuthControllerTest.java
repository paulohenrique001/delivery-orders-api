package br.com.paulohenrique.delivery_orders_api.integration;

import br.com.paulohenrique.delivery_orders_api.TestcontainersConfig;
import br.com.paulohenrique.delivery_orders_api.config.SecurityProperties;
import br.com.paulohenrique.delivery_orders_api.dto.request.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecurityProperties securityProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve retornar token com credenciais válidas")
    void login_withValidCredentials_returnsToken() throws Exception {
        AuthRequest authRequest = new AuthRequest(securityProperties.getUsername(), securityProperties.getPassword());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar 401 com credenciais inválidas")
    void login_withInvalidCredentials_returnsUnauthorized() throws Exception {
        AuthRequest authRequest = new AuthRequest(securityProperties.getUsername(), UUID.randomUUID().toString());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(authRequest)))
                .andExpect(status().isUnauthorized());
    }
}
