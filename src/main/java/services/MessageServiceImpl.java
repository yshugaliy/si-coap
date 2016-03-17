package services;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by javac on 11.03.16.
 */
@Component
public class MessageServiceImpl implements MessageService {

    @SuppressWarnings("unchecked")
    @ServiceActivator(inputChannel = "coapInputChannel", outputChannel = "coapOutputChannel")
    public Message<?> coapHandler(Message message) {
        //some logic
        return new GenericMessage(new String((byte[]) message.getPayload()) + " !RECEIVED!", message.getHeaders());
    }
}
