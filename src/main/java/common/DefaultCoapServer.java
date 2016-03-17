package common;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

/**
 * Created by javac on 11.03.16.
 */
public class DefaultCoapServer extends CoapServer {

    private Resource defaultResource;

    public DefaultCoapServer(Resource defaultResource) {
        super();
        this.defaultResource = defaultResource;
        add(defaultResource);
    }

    @Override
    protected Resource createRoot() {
        return new CoapResource("") {
            @Override
            public Resource getChild(String name) {
                return defaultResource;
            }
        };
    }
}
