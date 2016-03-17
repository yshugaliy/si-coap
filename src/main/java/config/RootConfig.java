package config;

import common.CoapChannelAdapter;
import common.CoapChannelInterceptor;
import common.CoapOutputMessageHadler;
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
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;

/**
 * Created by javac on 10.03.16.
 */
@Configuration
@EnableIntegration
@ComponentScan(basePackages = {"services"})
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
        adapter.setOutputChannel(coapInputChannel());
        adapter.setConverter(new DefaultCoapMessageConverter());
        return adapter;
    }

    @Bean
    public SubscribableChannel coapInputChannel() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(coapChannelInterceptor());
        return channel;
    }

    @Bean
    public SubscribableChannel coapOutputChannel() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(coapChannelInterceptor());
        return channel;
    }

    @Bean
    public ChannelInterceptor coapChannelInterceptor() {
        return new CoapChannelInterceptor();
    }

    @Bean
    @ServiceActivator(inputChannel = "coapOutputChannel")
    public MessageHandler coapOutputMessageHandler() {
        return new CoapOutputMessageHadler();
    }

}
