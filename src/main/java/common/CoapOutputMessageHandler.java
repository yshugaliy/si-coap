package common;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import type.CoapHeader;

/**
 * Created by javac on 15.03.16.
 */
public class CoapOutputMessageHandler implements MessageHandler {

    public void handleMessage(Message<?> message) throws MessagingException {
        CoapExchange exchange = message.getHeaders().get(CoapHeader.EXCHANGE.toString(), CoapExchange.class);
        exchange.respond(CoAP.ResponseCode.CONTENT, (String) message.getPayload());
    }
}
