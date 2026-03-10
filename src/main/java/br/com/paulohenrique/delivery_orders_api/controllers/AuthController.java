package br.com.paulohenrique.delivery_orders_api.controllers;

import br.com.paulohenrique.delivery_orders_api.dto.request.AuthRequest;
import br.com.paulohenrique.delivery_orders_api.dto.response.AuthResponse;
import br.com.paulohenrique.delivery_orders_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(
            @Valid
            @RequestBody
            AuthRequest authRequest
    ) {
        String accessToken = authService.authenticate(
                authRequest.username(),
                authRequest.password()
        );

        return new AuthResponse(accessToken);
    }
}
