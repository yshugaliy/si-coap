package service;

import org.springframework.messaging.Message;

/**
 * Created by javac on 11.03.16.
 */
public interface MessageService {

    Message<?> coapV1Handler(Message message);

    Message<?> coapV2Handler(Message message);

    Message<?> coapDefaultHandler(Message message);
}
