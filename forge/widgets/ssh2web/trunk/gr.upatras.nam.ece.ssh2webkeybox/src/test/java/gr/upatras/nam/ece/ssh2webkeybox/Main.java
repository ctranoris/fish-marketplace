//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package gr.upatras.nam.ece.ssh2webkeybox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jasper.servlet.JspServlet;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.JavaUtilLog;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.webapp.WebAppContext;


/**
 * 
 * C:\tranoris\programs\jetty-distribution-9.1.3.v20140225>"C:\Program Files\Java\jdk1.7.0_40\bin\java.exe" -jar start.jar
 * Example of using JSP's with embedded jetty and not requiring
 * all of the overhead of a WebAppContext
 */
public class Main
{
    // Resource path pointing to where the WEBROOT is
    private static final String WEBROOT_INDEX = "/"; //"/webroot/";

    public static void main(String[] args) throws Exception
    {
        int port = 8080;
        LoggingUtil.config();
        Log.setLog(new JavaUtilLog());

        Main main = new Main(port);
        main.start();
        main.waitForInterrupt();
    }

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private int port;
    private Server server;
    private URI serverURI;

    public Main(int port)
    {
        this.port = port;
    }

    public URI getServerURI()
    {
        return serverURI;
    }

    public void start() throws Exception
    {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
        if (indexUri == null)
        {
            throw new FileNotFoundException("Unable to find resource " + WEBROOT_INDEX);
        }

        // Points to wherever /webroot/ (the resource) is
        URI baseUri = indexUri.toURI();

        // Establish Scratch directory for the servlet context (used by JSP compilation)
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(),"embedded-jetty-jsp");

        if (!scratchDir.exists())
        {
            if (!scratchDir.mkdirs())
            {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }

        // Set JSP to use Standard JavaC always
        System.setProperty("org.apache.jasper.compiler.disablejsr199","false");

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("C:/Users/ctranoris/wsJunoEE/gr.upatras.nam.ece.ssh2webkeybox/target/ssh2web-1.01");
        
        ServletHandler handler = createServletHandler();
        webapp.setServletHandler(handler);
        webapp.setErrorHandler(createErrorHandler());
        
        server.setHandler(webapp);
        
//        // Setup the basic application "context" for this application at "/"
//        // This is also known as the handler tree (in jetty speak)
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        context.setAttribute("javax.servlet.context.tempdir",scratchDir);
//        context.setResourceBase(baseUri.toASCIIString());
//        server.setHandler(context);
        
        // Add Application Servlets
        //context.addServlet(DateServlet.class,"/date/");

        // Set Classloader of Context to be sane (needed for JSTL)
        // JSP requires a non-System classloader, this simply wraps the
        // embedded System classloader in a way that makes it suitable
        // for JSP to use
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        webapp.setClassLoader(jspClassLoader);

        // Add JSP Servlet (must be named "jsp")
        ServletHolder holderJsp = new ServletHolder("jsp",JspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel","DEBUG");
        holderJsp.setInitParameter("fork","false");
        holderJsp.setInitParameter("xpoweredBy","false");
        holderJsp.setInitParameter("compilerTargetVM","1.7");
        holderJsp.setInitParameter("compilerSourceVM","1.7");
        holderJsp.setInitParameter("keepgenerated","true");
        webapp.addServlet(holderJsp,"*.jsp");
        webapp.addServlet(holderJsp,"*.jspf");
        webapp.addServlet(holderJsp,"*.jspx");

//        // Add Example of mapping jsp to path spec
//        ServletHolder holderAltMapping = new ServletHolder("foo.jsp", JspServlet.class);
//        holderAltMapping.setForcedPath("/test/foo/foo.jsp");
//        context.addServlet(holderAltMapping,"/test/foo/");
//
//        // Add Default Servlet (must be named "default")
//        ServletHolder holderDefault = new ServletHolder("default",DefaultServlet.class);
//        LOG.info("Base URI: " + baseUri);
//        holderDefault.setInitParameter("resourceBase",baseUri.toASCIIString());
//        holderDefault.setInitParameter("dirAllowed","true");
//        context.addServlet(holderDefault,"/");

        // Start Server
        server.start();

        // Show server state
        if (LOG.isLoggable(Level.FINE))
        {
            LOG.fine(server.dump());
        }

        // Establish the Server URI
        String scheme = "http";
        for (ConnectionFactory connectFactory : connector.getConnectionFactories())
        {
            if (connectFactory.getProtocol().equals("SSL-http"))
            {
                scheme = "https";
            }
        }
        String host = connector.getHost();
        if (host == null)
        {
            host = "localhost";
        }
        int port = connector.getLocalPort();
        serverURI = new URI(String.format("%s://%s:%d/",scheme,host,port));
        LOG.info("Server URI: " + serverURI);
    }

    private ErrorHandler createErrorHandler() {
        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(500, "/error.html");
        return errorHandler;
      }

      private ServletHandler createServletHandler() {
        ServletHandler servletHandler = new ServletHandler();

       
        FilterHolder strutsFilterHolder = createStrutsFilterHolder();
        servletHandler.addFilter(strutsFilterHolder, createFilterMapping("/*", strutsFilterHolder));    
        servletHandler.addFilter(strutsFilterHolder, createFilterMapping("*.action", strutsFilterHolder));    

        return servletHandler;
      }
      
      private FilterHolder createStrutsFilterHolder() {
    	    FilterHolder filterHolder
    	        = new FilterHolder(StrutsPrepareAndExecuteFilter.class);
    	    filterHolder.setName("struts2");
    	    filterHolder.setInitParameter("struts.devMode", "true");
    	    filterHolder.setInitParameter("struts.convention.exclude.parentClassLoader", "false");
    	    return filterHolder;
    	  }

    	  private FilterMapping createFilterMapping(
    	      String pathSpec, FilterHolder filterHolder) {
    	    FilterMapping filterMapping = new FilterMapping();
    	    filterMapping.setPathSpec(pathSpec);
    	    filterMapping.setFilterName(filterHolder.getName());
    	    return filterMapping;
    	  }

      
    public void stop() throws Exception
    {
        server.stop();
    }

    /**
     * Cause server to keep running until it receives a Interrupt.
     * <p>
     * Interrupt Signal, or SIGINT (Unix Signal), is typically seen as a result of a kill -TERM {pid} or Ctrl+C
     */
    public void waitForInterrupt() throws InterruptedException
    {
        server.join();
    }
}
