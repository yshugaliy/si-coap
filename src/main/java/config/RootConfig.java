package config;

import adapter.CoapChannelAdapter;
import interceptor.CoapChannelInterceptor;
import common.CoapOutputMessageHandler;
import common.DefaultCoapMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;

/**
 * Created by javac on 10.03.16.
 */
@Configuration
@EnableIntegration
@ComponentScan(basePackages = {"service","router"})
@PropertySource("classpath:server.properties")
public class RootConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MessageProducerSupport coapChannelAdapter(@Value("${host}") String host,
                                                     @Value("#{'${ports}'.split(',')}") List<Integer> ports) {
        CoapChannelAdapter adapter = new CoapChannelAdapter(host, ports);
        adapter.setOutputChannel(inputChannel());
        adapter.setConverter(new DefaultCoapMessageConverter());
        return adapter;
    }

    @Bean
    public MessageChannel inputChannel() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(coapChannelInterceptor());
        return channel;
    }

    @Bean
    public MessageChannel outputChannel() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(coapChannelInterceptor());
        return channel;
    }

    @Bean
    public MessageChannel coapV1InputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel coapV2InputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel coapDefaultVersionInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public ChannelInterceptor coapChannelInterceptor() {
        return new CoapChannelInterceptor();
    }

    @Bean
    @ServiceActivator(inputChannel = "outputChannel")
    public MessageHandler coapOutputMessageHandler() {
        return new CoapOutputMessageHandler();
    }

}
