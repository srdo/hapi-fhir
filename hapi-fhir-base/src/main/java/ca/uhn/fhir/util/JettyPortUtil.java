
package ca.uhn.fhir.util;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class JettyPortUtil {

    /**
     * Gets the local port for the given server. The server must be started.
     */
    public static int getPortForStartedServer(Server server) {
        assert server.isStarted();
        Connector[] connectors = server.getConnectors();
        assert connectors.length == 1;
        return ((ServerConnector) (connectors[0])).getLocalPort();
    }
    
}
