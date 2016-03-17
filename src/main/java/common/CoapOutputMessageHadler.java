package common;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import types.CoapHeader;

/**
 * Created by javac on 15.03.16.
 */
public class CoapOutputMessageHadler implements MessageHandler {

    //todo: remove binding to CoapExchange
    public void handleMessage(Message<?> message) throws MessagingException {
        CoapExchange exchange = (CoapExchange) message.getHeaders().get(CoapHeader.EXCHANGE.toString());
        exchange.respond(CoAP.ResponseCode.CONTENT, (String) message.getPayload());
    }
}
