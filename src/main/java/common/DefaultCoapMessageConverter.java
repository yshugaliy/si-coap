package common;

import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;

import static type.CoapHeader.*;

/**
 * Created by javac on 11.03.16.
 */
public class DefaultCoapMessageConverter extends AbstractCoapMessageConverter {

    public Message<?> toMessage(CoapExchange exchange) {
        try {
            MessageBuilder messageBuilder = MessageBuilder
                    .withPayload(exchange.getRequestPayload())
                    .setHeader(EXCHANGE.toString(), exchange)
                    .setHeader(CODE.toString(), exchange.getRequestCode())
                    .setHeader(PATH.toString(), exchange.getRequestOptions().getUriPathString())
                    .setHeader(PATH_PARTS.toString(), exchange.getRequestOptions().getUriPath())
                    .setHeader(TOKEN.toString(), exchange.advanced().getRequest().getToken())
                    .setHeader(SERVER_ADDRESS.toString(), exchange.advanced().getEndpoint().getAddress())
                    .setHeader(SOURCE_ADDRESS.toString(), exchange.getSourceAddress())
                    .setHeader(SOURCE_PORT.toString(), exchange.getSourcePort());
            return messageBuilder.build();
        } catch (Exception e) {
            throw new MessageConversionException("failed to convert coapExchange to Message", e);
        }
    }
}
