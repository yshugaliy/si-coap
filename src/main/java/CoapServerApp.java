import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import config.RootConfig;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.endpoint.MessageProducerSupport;

/**
 * Created by javac on 10.03.16.
 */
public class CoapServerApp {

    public static void main(String[] args) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        CoapServerApp client = new CoapServerApp();
        client.start();
    }

    public void start() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
        MessageProducerSupport channelAdapter = ctx.getBean("coapChannelAdapter", MessageProducerSupport.class);
        channelAdapter.start();
    }

}
