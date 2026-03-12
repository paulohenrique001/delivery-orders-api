package br.com.paulohenrique.delivery_orders_api.integration;

import br.com.paulohenrique.delivery_orders_api.TestcontainersConfig;
import br.com.paulohenrique.delivery_orders_api.config.SecurityProperties;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import br.com.paulohenrique.delivery_orders_api.dto.request.AuthRequest;
import br.com.paulohenrique.delivery_orders_api.dto.request.CreateOrderRequest;
import br.com.paulohenrique.delivery_orders_api.dto.request.UpdateStatusOrderRequest;
import br.com.paulohenrique.delivery_orders_api.infrastructure.messaging.OrderConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoSpyBean
    private OrderConsumer orderConsumer;
    @Autowired
    private SqsAsyncClient sqsAsyncClient;
    @Autowired
    private SecurityProperties securityProperties;

    private String accessToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setUp() throws Exception {
        AuthRequest authRequest = new AuthRequest(securityProperties.getUsername(), securityProperties.getPassword());

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(authRequest)))
                .andReturn();

        this.accessToken = objectMapper.readTree(mvcResult.getResponse().getContentAsString())
                .get("accessToken").asText();
    }

    @Test
    @DisplayName("Deve criar pedido com dados válidos e retornar 201")
    void create_withValidRequest_returnsOrderResponse() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest("John Doe", "123 Main St, Springfield, IL 62701");

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsBytes(createOrderRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @DisplayName("Deve listar pedidos e retornar página")
    void findAll_authenticated_returnsPageResponse() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Deve retornar pedido pelo ID existente")
    void findById_withExistingId_returnsOrderResponse() throws Exception {
        Long id = createOrder();

        mockMvc.perform(get("/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @DisplayName("Deve atualizar status do pedido de CREATED para PROCESSING")
    void updateStatus_withValidTransition_returnsOrderResponse() throws Exception {
        Long orderId = createOrder();
        UpdateStatusOrderRequest updateStatusOrderRequest = new UpdateStatusOrderRequest(OrderStatus.PROCESSING);

        mockMvc.perform(patch("/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsBytes(updateStatusOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }

    @Test
    @DisplayName("Deve publicar evento quando alterar status para SHIPPED")
    void updateStatus_toShipped_publishesEventToQueue() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(patch("/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsBytes(new UpdateStatusOrderRequest(OrderStatus.PROCESSING))))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsBytes(new UpdateStatusOrderRequest(OrderStatus.SHIPPED))))
                .andExpect(status().isOk());

        verify(orderConsumer, timeout(5000))
                .onOrderShippedEvent(argThat(e -> e.orderId().equals(orderId) && e.status() == OrderStatus.SHIPPED));
    }

    @Test
    @DisplayName("Deve cancelar pedido e retornar 204")
    void cancel_withCancelableOrder_returnsNoContent() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(delete("/orders/{id}", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }

    private Long createOrder() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest("Jane Doe", "123 Main St, Springfield, IL 62701");

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsBytes(createOrderRequest))
                )
                .andReturn();

        return objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("id").asLong();
    }
}
