import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by javac on 11.03.16.
 */
public class CoapClientApp {

    private static Logger logger;

    private static final String TRUST_STORE_PASSWORD = "rootPass";
    private static final String KEY_STORE_PASSWORD = "endPass";
    private static final String KEY_STORE_LOCATION = "certs/keyStore.jks";
    private static final String TRUST_STORE_LOCATION = "certs/trustStore.jks";

    private static List<String> requestData = Arrays.asList("Spring", "Integration", "Core", "Coap", "test");
    private static Integer[] ports = {5683, 5684, 5685};
    private static String[] versions = {"v1", "v2", ""};

    public static void main(String[] args) throws InterruptedException {
        Logger root = (Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        logger = (Logger) LoggerFactory.getLogger(CoapClientApp.class);

        CoapClientApp client = new CoapClientApp();
        client.start();
    }

    public void start() throws InterruptedException {
        CoapClient client = new CoapClient();
        client.setEndpoint(new CoapEndpoint(createDtlsConnector(), NetworkConfig.getStandard()));
        for (String data : requestData) {
            int port = getRandom(ports);
            String version = getRandom(versions);

            client.setURI("localhost:" + port + "/sdk_token/" + version);
            client.post(new CoapPostHandler(), data, MediaTypeRegistry.TEXT_PLAIN);
            logger.info("[CLIENT] <> Message sent: \"" + data + "\" <> port: " + port + " <> version: " + version);

            Thread.sleep(1000);
        }
    }

    private DTLSConnector createDtlsConnector() {
        DTLSConnector dtlsConnector = null;
        try {
            // load key store
            KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream in = getClass().getClassLoader().getResourceAsStream(KEY_STORE_LOCATION);
            keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());

            // load trust store
            KeyStore trustStore = KeyStore.getInstance("JKS");
            InputStream inTrust = getClass().getClassLoader().getResourceAsStream(TRUST_STORE_LOCATION);
            trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());

            // You can load multiple certificates if needed
            Certificate[] trustedCertificates = new Certificate[1];
            trustedCertificates[0] = trustStore.getCertificate("root");

            DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(0));
            builder.setPskStore(new StaticPskStore("Client_identity", "secretPSK".getBytes()));
            builder.setIdentity((PrivateKey) keyStore.getKey("client", KEY_STORE_PASSWORD.toCharArray()),
                    keyStore.getCertificateChain("client"), true);
            builder.setTrustStore(trustedCertificates);
            dtlsConnector = new DTLSConnector(builder.build());
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Could not load the keystore");
            e.printStackTrace();
        }
        return dtlsConnector;
    }

    private <T> T getRandom(T[] arr) {
        return arr[new Random().nextInt(arr.length)];
    }

    private class CoapPostHandler implements CoapHandler {
        @Override
        public void onLoad(CoapResponse response) {
            logger.info("[CLIENT] <> Response received: \"" + new String(response.getPayload()) + "\"");
        }

        @Override
        public void onError() {
            logger.info("[CLIENT] <> Response error");
        }
    }


}
