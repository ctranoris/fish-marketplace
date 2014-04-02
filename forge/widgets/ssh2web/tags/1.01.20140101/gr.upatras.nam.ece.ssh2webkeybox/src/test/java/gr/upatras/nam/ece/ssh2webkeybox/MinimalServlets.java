package gr.upatras.nam.ece.ssh2webkeybox;

import java.lang.management.ManagementFactory;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
 
public class MinimalServlets {
 
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("C:/Users/ctranoris/wsJunoEE/gr.upatras.nam.ece.ssh2webkeybox/target/keybox-2.02.02");
        server.setHandler(webapp);
        server.start();
        server.join();
    }
}