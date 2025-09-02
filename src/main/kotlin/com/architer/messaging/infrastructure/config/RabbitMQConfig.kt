package com.architer.messaging.infrastructure.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Value("\${rabbitmq.exchange.name}")
    private lateinit var exchangeName: String

    @Bean
    fun topicExchange(): TopicExchange {
        return TopicExchange(exchangeName)
    }

    // Interview queues
    @Bean
    fun interviewEventsQueue(): Queue {
        return Queue("interview.queue", true)
    }

    @Bean
    fun interviewEventsBinding(interviewEventsQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(interviewEventsQueue)
            .to(exchange)
            .with("interview.*")
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = Jackson2JsonMessageConverter()
        return template
    }
}