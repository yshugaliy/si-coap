package service;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * Created by javac on 11.03.16.
 */
@Service
@SuppressWarnings("unchecked")
public class MessageServiceImpl implements MessageService {

    @Override
    @ServiceActivator(inputChannel = "coapV1InputChannel", outputChannel = "outputChannel")
    public Message<?> coapV1Handler(Message message) {
        return processMessage(message, " !V1 RECEIVED!");
    }

    @Override
    @ServiceActivator(inputChannel = "coapV2InputChannel", outputChannel = "outputChannel")
    public Message<?> coapV2Handler(Message message) {
        return processMessage(message, " !V2 RECEIVED!");
    }

    @Override
    @ServiceActivator(inputChannel = "coapDefaultVersionInputChannel", outputChannel = "outputChannel")
    public Message<?> coapDefaultHandler(Message message) {
        return processMessage(message, " !DEFAULT VERSION RECEIVED!");
    }

    private Message<?> processMessage(Message message, String appendable) {
        return new GenericMessage(new String((byte[]) message.getPayload()) + appendable, message.getHeaders());
    }

}
