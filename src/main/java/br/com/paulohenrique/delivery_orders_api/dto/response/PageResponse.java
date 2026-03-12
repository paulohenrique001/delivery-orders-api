package br.com.paulohenrique.delivery_orders_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Resposta paginada padrão")
public record PageResponse<T>(
        @Schema(description = "Lista de itens da página atual")
        List<T> content,
        @Schema(description = "Total de elementos em todas as páginas", example = "100")
        long totalElements,
        @Schema(description = "Total de páginas disponíveis", example = "10")
        int totalPages
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
