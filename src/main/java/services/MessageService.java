package services;

import org.springframework.messaging.Message;

/**
 * Created by javac on 11.03.16.
 */
public interface MessageService {

    Message<?> coapHandler(Message message);
}
