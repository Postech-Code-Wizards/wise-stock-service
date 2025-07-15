package br.com.wise.stock_service.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "stock-service";
    public static final String STOCK_REPOR_QUEUE = "stock-repor-queue";
    public static final String STOCK_BAIXA_QUEUE = "stock-baixa-queue";
    public static final String STOCK_RESPOSTA_QUEUE = "stock-resposta-queue";
    public static final String STOCK_RESPOSTA_ROUNTING_KEY = "stock-resposta";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue respostaQueue() {
        return new Queue(STOCK_RESPOSTA_QUEUE, true);
    }

    @Bean
    public Binding stockBinding(Queue respostaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(respostaQueue).to(exchange).with(STOCK_RESPOSTA_ROUNTING_KEY);
    }

    @Bean
    public Queue historyQueue() {
        return new Queue(STOCK_REPOR_QUEUE, true);
    }

    @Bean
    public Queue historyUpdateQueue() {
        return new Queue(STOCK_BAIXA_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
