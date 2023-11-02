package bootiful.processor;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class PicturesIntegrationFlowConfiguration {
    public static final String PICTURES = "pictures";

    @Bean
    IntegrationFlow inboundAmqpRequestsPicturesIntegrationFlow(MessageChannel pictureMessages, ConnectionFactory connectionFactory) {
        var inboundAmqpAdapter = Amqp
            .inboundAdapter(connectionFactory, PICTURES);

        return IntegrationFlow
            .from(inboundAmqpAdapter)
            .channel(pictureMessages)
            .get();
    }
    @Bean
    IntegrationFlow picturesIntegrationFlow(MessageChannel pictureMessages) {
        return IntegrationFlow
            .from(pictureMessages)//
            .handle((payload, headers) -> {
                System.out.println("-PICTURES-------------------------------------------------------------------------");
                System.out.println(payload.toString());
                //headers.forEach((key, value) -> System.out.println(key + '=' + value));
                return null;
            })//
            .get();
    }
    @Bean
    DirectChannelSpec pictureMessages() {
        return MessageChannels.direct();
    }

    @Bean
    Queue picturesQueue() {
        return QueueBuilder.durable(PICTURES).build();
    }

    @Bean
    Exchange picturesExchange() {
        return ExchangeBuilder.directExchange(PICTURES).build();
    }

    @Bean
    Binding picturesBinding() {
        return BindingBuilder.bind(picturesQueue()).to(picturesExchange()).with(PICTURES).noargs();
    }
}
