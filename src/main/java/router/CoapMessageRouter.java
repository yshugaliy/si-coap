package router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by javac on 18.03.16.
 */
@MessageEndpoint
public class CoapMessageRouter {

    @Autowired
    private MessageChannel coapV1InputChannel;

    @Autowired
    private MessageChannel coapV2InputChannel;

    @Autowired
    private MessageChannel coapDefaultVersionInputChannel;

    @Router(inputChannel = "inputChannel")
    public MessageChannel route(@Header(name = "path_parts") List<String> uriParts,
                                Message message) {
        String version = getVersion(uriParts);
        MessageChannel routeChannel;
        switch (version) {
            case "v1":
                routeChannel = coapV1InputChannel;
                break;
            case "v2":
                routeChannel = coapV2InputChannel;
                break;
            default:
                routeChannel = coapDefaultVersionInputChannel;
        }
        printLog(version, routeChannel);
        return routeChannel;
    }

    private String getVersion(List<String> uriParts) {
        String version = "";
        if (!CollectionUtils.isEmpty(uriParts) && uriParts.size() > 1) {
            version = uriParts.get(1);
        }
        return version;
    }

    private void printLog(String version, MessageChannel routeChannel) {
        StringBuilder logBuilder = new StringBuilder("[ROUTER] : ");
        logBuilder.append(" route message with version -").append(version);
        logBuilder.append(" to ").append(routeChannel.toString());
        System.out.println(logBuilder);
    }
}
