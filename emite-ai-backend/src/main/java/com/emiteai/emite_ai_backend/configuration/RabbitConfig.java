package com.emiteai.emite_ai_backend.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue reportQueue() {
        return new Queue("reportQueue", true);
    }

    @Bean
    public Exchange fanoutExchange() {
        return new FanoutExchange("reportExchange");
    }
}
