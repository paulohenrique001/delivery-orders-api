package br.com.paulohenrique.delivery_orders_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DeliveryOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryOrdersApplication.class, args);
	}

}
