import config.RootConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.web.client.RestTemplate;

/**
 * Created by javac on 10.03.16.
 */
public class CoapServerApp {

    public static void main(String[] args) {
        CoapServerApp client = new CoapServerApp();
        client.start();
    }

    public void start() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
        MessageProducerSupport channelAdapter = ctx.getBean("coapChannelAdapter", MessageProducerSupport.class);
        channelAdapter.start();
    }

}
