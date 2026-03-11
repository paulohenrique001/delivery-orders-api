package br.com.paulohenrique.delivery_orders_api.controllers;

import br.com.paulohenrique.delivery_orders_api.dto.request.AuthRequest;
import br.com.paulohenrique.delivery_orders_api.dto.response.AuthResponse;
import br.com.paulohenrique.delivery_orders_api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Recursos de autenticação")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário e retorna um token JWT"
    )
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token JWT retornado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas.",
                    content = @Content
            )
    })
    @SecurityRequirements
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
