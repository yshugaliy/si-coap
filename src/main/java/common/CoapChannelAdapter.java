package common;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;

/**
 * Created by javac on 11.03.16.
 */
public class CoapChannelAdapter extends MessageProducerSupport {

    private static final String TRUST_STORE_PASSWORD = "rootPass";
    private static final String KEY_STORE_PASSWORD = "endPass";
    private static final String KEY_STORE_LOCATION = "certs/keyStore.jks";
    private static final String TRUST_STORE_LOCATION = "certs/trustStore.jks";

    private CoapServer server;
    private AbstractCoapMessageConverter converter;
    private String host;
    private List<Integer> ports;

    public CoapChannelAdapter(String host, List<Integer> ports) {
        this.host = host;
        this.ports = ports;
    }

    public void setConverter(AbstractCoapMessageConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void doStart() {
        super.doStart();
        initServer();
        server.start();

        System.out.println("[ADAPTER] : Coap server started");
    }

    @Override
    protected void doStop() {
        super.doStop();
        if (server != null) {
            server.stop();

            System.out.println("[ADAPTER] : Coap server stoped");
        }
    }

    private void initServer() {
        if (server == null) {
            server = new DefaultCoapServer(new DefaultResource());
            for (Integer port : ports) {
                server.addEndpoint(new CoapEndpoint(createDtlsConnector(port), NetworkConfig.getStandard()));
            }
        }
    }

    private class DefaultResource extends CoapResource {

        public DefaultResource() {
            super("");
        }

        @Override
        public void handlePOST(CoapExchange exchange) {
            System.out.println("[ADAPTER] : received POST message <> " + exchange.getRequestText());
            Message message = converter.toMessage(exchange);
            sendMessage(message); // send message to channel
            exchange.accept();
        }

        @Override
        public Resource getChild(String name) {
            return this;
        }
    }

    private DTLSConnector createDtlsConnector(int port) {
        DTLSConnector dtlsConnector = null;
        try {
            // Pre-shared secrets
            InMemoryPskStore pskStore = new InMemoryPskStore();
            pskStore.setKey("password", "sesame".getBytes()); // from ETSI Plugtest test spec

            // load the trust store
            KeyStore trustStore = KeyStore.getInstance("JKS");
            InputStream inTrust = CoapChannelAdapter.class.getClassLoader().getResourceAsStream(TRUST_STORE_LOCATION);
            trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());

            // You can load multiple certificates if needed
            Certificate[] trustedCertificates = new Certificate[1];
            trustedCertificates[0] = trustStore.getCertificate("root");

            // load the key store
            KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream in = CoapChannelAdapter.class.getClassLoader().getResourceAsStream(KEY_STORE_LOCATION);
            keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());

            DtlsConnectorConfig.Builder config = new DtlsConnectorConfig.Builder(new InetSocketAddress(host, port));
            config.setSupportedCipherSuites(new CipherSuite[]{CipherSuite.TLS_PSK_WITH_AES_128_CCM_8,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8});
            config.setPskStore(pskStore);
            config.setIdentity((PrivateKey) keyStore.getKey("server", KEY_STORE_PASSWORD.toCharArray()),
                    keyStore.getCertificateChain("server"), true);
            config.setTrustStore(trustedCertificates);

            dtlsConnector = new DTLSConnector(config.build());

        } catch (GeneralSecurityException | IOException e) {
            System.err.println("Could not load the keystore");
            e.printStackTrace();
        }
        return dtlsConnector;
    }
}

