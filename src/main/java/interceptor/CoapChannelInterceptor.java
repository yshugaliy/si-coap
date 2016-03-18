package interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * Created by javac on 10.03.16.
 */
public class CoapChannelInterceptor implements ChannelInterceptor {

    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        printLog("preSend", messageChannel.toString());
        return message;
    }

    public void postSend(Message<?> message, MessageChannel messageChannel, boolean b) {
        printLog("postSend", messageChannel.toString());
    }

    public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b, Exception e) {
        printLog("afterSendCompletion", messageChannel.toString());
    }

    public boolean preReceive(MessageChannel messageChannel) {
        printLog("preReceive", messageChannel.toString());
        return true;
    }

    public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
        printLog("postReceive", messageChannel.toString());
        return message;
    }

    public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel, Exception e) {
        printLog("afterReceive", messageChannel.toString());
    }

    private void printLog(String action, String channel) {
        StringBuilder logBuilder = new StringBuilder("[INTERCEPTOR] : ");
        logBuilder.append(channel).append(" ");
        logBuilder.append(action);
        logBuilder.append(" <> received message");
        System.out.println(logBuilder);
    }
}
