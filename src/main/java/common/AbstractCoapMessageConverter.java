package common;

import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;

/**
 * Created by javac on 14.03.16.
 */
public abstract class AbstractCoapMessageConverter implements MessageConverter {

    public Message<?> toMessage(Object o, MessageHeaders messageHeaders) {
        return toMessage((CoapExchange) o);
    }

    public Object fromMessage(Message<?> message, Class<?> aClass) {
        return null;
    }

    public abstract Message<?> toMessage(CoapExchange exchange);
}
