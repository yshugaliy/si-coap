package common;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * Created by javac on 10.03.16.
 */
public class CoapChannelInterceptor implements ChannelInterceptor {

    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        System.out.println("[INTERCEPTOR] : preSend <> received message");
        return message;
    }

    public void postSend(Message<?> message, MessageChannel messageChannel, boolean b) {
        System.out.println("[INTERCEPTOR] : postSend <> received message");
    }

    public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b, Exception e) {
        System.out.println("[INTERCEPTOR] : afterSendCompletion <> received message");
    }

    public boolean preReceive(MessageChannel messageChannel) {
        System.out.println("[INTERCEPTOR] : preReceive <> received message");
        return true;
    }

    public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
        System.out.println("[INTERCEPTOR] : postReceive <> received message");
        return message;
    }

    public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel, Exception e) {
        System.out.println("[INTERCEPTOR] : afterReceiveCompletion <> received message");
    }
}
