package com.stm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "TicketService API",
                description = "Service to buy tickets(test task for STMLabs)", version = "0.0.1",
                contact = @Contact(
                        name = "Ilia Doktorov",
                        email = "whiteoverlord@yandex.ru"
                )
        )
)
public class TicketServiceSwaggerConfig {
}
