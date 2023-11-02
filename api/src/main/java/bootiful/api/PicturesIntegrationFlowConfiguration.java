package bootiful.api;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
class PicturesIntegrationFlowConfiguration {

    private final String ROUTING_KEY = "pictures";

    @Bean
    IntegrationFlow picturesIntegrationFlow(@Qualifier("pictures") MessageChannel picturesChannel, AmqpTemplate rabbit) {

        return IntegrationFlow.from(picturesChannel).handle(
            Amqp.outboundAdapter(rabbit).routingKey(this.ROUTING_KEY)
        ).get();
    }

    @Bean
    DirectChannelSpec pictures() {
        return MessageChannels.direct();
    }
}
