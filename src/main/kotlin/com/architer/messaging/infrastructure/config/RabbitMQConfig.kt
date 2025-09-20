package com.architer.messaging.infrastructure.config

import com.architer.messaging.infrastructure.constants.MessagingConstants
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig(
    private val rabbitMQProperties: RabbitMQProperties,
) {
    @Bean
    fun topicExchange(): TopicExchange {
        return TopicExchange(rabbitMQProperties.exchange)
    }

    @Bean
    fun feedbackRequestedQueue(): Queue = Queue(MessagingConstants.Queues.INTERVIEW_FEEDBACK_REQUESTED, true)

    @Bean
    fun interviewStartedQueue(): Queue = Queue(MessagingConstants.Queues.INTERVIEW_STARTED, true)

    @Bean
    fun interviewCompletedQueue(): Queue = Queue(MessagingConstants.Queues.INTERVIEW_COMPLETED, true)

    @Bean
    fun interviewCancelledQueue(): Queue = Queue(MessagingConstants.Queues.INTERVIEW_CANCELLED, true)

    @Bean
    fun feedbackRequestedBinding(feedbackRequestedQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(feedbackRequestedQueue).to(exchange).with(MessagingConstants.RoutingKeys.INTERVIEW_FEEDBACK_REQUESTED)
    }

    @Bean
    fun interviewStartedBinding(interviewStartedQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(interviewStartedQueue).to(exchange).with(MessagingConstants.RoutingKeys.INTERVIEW_STARTED)
    }

    @Bean
    fun interviewCompletedBinding(interviewCompletedQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(interviewCompletedQueue).to(exchange).with(MessagingConstants.RoutingKeys.INTERVIEW_COMPLETED)
    }

    @Bean
    fun interviewCancelledBinding(interviewCancelledQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(interviewCancelledQueue).to(exchange).with(MessagingConstants.RoutingKeys.INTERVIEW_CANCELLED)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = Jackson2JsonMessageConverter()
        return template
    }

    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory {
        val factory = org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(Jackson2JsonMessageConverter())
        return factory
    }
}