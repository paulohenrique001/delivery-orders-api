package br.com.paulohenrique.delivery_orders_api.controllers;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import br.com.paulohenrique.delivery_orders_api.dto.PageResponse;
import br.com.paulohenrique.delivery_orders_api.dto.mapper.OrderMapper;
import br.com.paulohenrique.delivery_orders_api.dto.request.CreateOrderRequest;
import br.com.paulohenrique.delivery_orders_api.dto.request.UpdateStatusOrderRequest;
import br.com.paulohenrique.delivery_orders_api.dto.response.OrderResponse;
import br.com.paulohenrique.delivery_orders_api.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Tag(name = "Pedidos", description = "Gerenciamento de pedidos de entrega")
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @Operation(summary = "Criar pedido", description = "Cria novo pedido de entrega")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @Valid
            @RequestBody
            CreateOrderRequest createOrderRequest
    ) {
        Order order = orderService.create(
                createOrderRequest.customerName(),
                createOrderRequest.address()
        );
        return orderMapper.toResponse(order);
    }

    @Operation(summary = "Listar pedidos", description = "Lista pedidos paginados com os filtros opcionais")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Algum parâmetro dos filtros é inválido", content = @Content)
    })
    @SecurityRequirements
    public PageResponse<OrderResponse> findAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(required = false)
            OrderStatus status,
            @RequestParam(required = false)
            String customerName,
            @RequestParam(required = false)
            Instant startDate,
            @RequestParam(required = false)
            Instant endDate
    ) {
        Page<Order> page = orderService.findAll(
                pageable,
                status,
                customerName,
                startDate,
                endDate
        );

        return PageResponse.from(page.map(orderMapper::toResponse));
    }

    @Operation(summary = "Buscar por ID", description = "Busca pedido por identificador único")
    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não localizado pelo identificador", content = @Content)
    })
    @SecurityRequirements
    public OrderResponse findById(
            @PathVariable
            Long id
    ) {
        Order order = orderService.findByIdCached(id);
        return orderMapper.toResponse(order);
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de entrega do pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não localizado pelo identificador", content = @Content),
            @ApiResponse(responseCode = "422", description = "Atualização do status é inválida", content = @Content)
    })
    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateStatusOrderRequest updateStatusOrderRequest

    ) {
        Order order = orderService.updateStatus(id, updateStatusOrderRequest.status());
        return orderMapper.toResponse(order);
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido se ainda não foi entregue")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não localizado pelo identificador", content = @Content),
            @ApiResponse(responseCode = "422", description = "Pedido entregue não pode ser cancelado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(
            @PathVariable
            Long id
    ) {
        orderService.cancel(id);
    }
}
