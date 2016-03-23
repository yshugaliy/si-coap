package interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * Created by javac on 10.03.16.
 */
public class CoapChannelInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CoapChannelInterceptor.class);

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
        logger.info("[INTERCEPTOR] <> " + channel + " " + action + ": received message");
    }
}
