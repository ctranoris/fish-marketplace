package com.keybox.manage.socket;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.keybox.common.util.AuthUtil;
import com.keybox.manage.action.SecureShellAction;
import com.keybox.manage.model.SchSession;
import com.keybox.manage.model.SessionOutput;
import com.keybox.manage.model.UserSchSessions;
import com.keybox.manage.task.SecureShellTask;
import com.keybox.manage.util.DBUtils;
import com.keybox.manage.util.SessionOutputUtil;

@ServerEndpoint(value = "/logviewer.ws", configurator = GetHttpSessionConfigurator.class)
@SuppressWarnings("unchecked")
public class LogViewerWS {
	private HttpSession httpSession;
	private Session session;
	private Long sessionId = null;
	private Boolean stopAllThreads=false;

	private class logvileinfo {
		String fileSlug = "myfileSlug";
		String fileName = "myfileName";
		String fileCommand = "fileCommand";
		String channel = "stdout";
		String value = "myfilvalue";
	}
	

	private class logvileinfoTask implements Runnable {

		logvileinfo mylf;
		
		public logvileinfoTask(String filename, String fileCommand){
			 mylf = new logvileinfo();
			 mylf.fileName = filename;
			 mylf.fileSlug = mylf.fileName.replace('.', '-') ;
			 mylf.fileCommand = fileCommand; 
		}
		
		@Override
		public void run() {
	         
	         
			Runtime r = Runtime.getRuntime();
			Process p;
			
			
			try {
				//p = r.exec("tail -f /var/log/apache2/access.log");
				p = r.exec(mylf.fileCommand);

				System.out.println("logvileinfoTask started");
				Scanner s = new Scanner(p.getInputStream());
				while (s.hasNextLine()) {
					if (stopAllThreads)
						break;
					String line = s.nextLine();
					
					
					// Do whatever you want with the output.
					mylf.value = line;
					String json = new Gson().toJson(mylf);

					session.getBasicRemote().sendText(json);
				}

			p.destroy();	
			System.out.println("logvileinfoTask stopped");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * sends output to socket
	 */
	private void sendOutput() {
		System.out.println("LogViewerWS sendOutput");
		Runnable run=new logvileinfoTask("access.log", "tail -f /var/log/apache2/access.log");
		Thread logvileinfoTaskthread = new Thread(run);
        logvileinfoTaskthread.start();
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		run=new logvileinfoTask("startKeyBox.sh", "cat /home/forgebox/KeyBox-jetty-v2.02_02/startKeyBox.sh");
		logvileinfoTaskthread = new Thread(run);
        logvileinfoTaskthread.start();
        

        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		run=new logvileinfoTask("ifconfig", "ifconfig");
		logvileinfoTaskthread = new Thread(run);
        logvileinfoTaskthread.start();
        

        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		run=new logvileinfoTask("lscommand", "ls -la");
		logvileinfoTaskthread = new Thread(run);
        logvileinfoTaskthread.start();

	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {

		System.out.println("LogViewerWS onOpen");
		stopAllThreads=false;
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		// this.sessionId = AuthUtil.getSessionId(httpSession);
		this.session = session;
		
		
		
		sendOutput();
//		try {
//			logvileinfo logf = new logvileinfo();
//			String json = new Gson().toJson(logf);
//			this.session.getBasicRemote().sendText(json);
//			sendOutput();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("LogViewerWS onMessage");

		if (session.isOpen()) {

			if (StringUtils.isNotEmpty(message)) {

			} else {
				this.sendOutput();
			}
		}

	}

	@OnClose
	public void onClose() {

		System.out.println("LogViewerWS onClose");
		stopAllThreads=true;

	}

}
