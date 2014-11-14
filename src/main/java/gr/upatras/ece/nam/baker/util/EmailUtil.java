/**
 * Copyright 2014 University of Patras 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package gr.upatras.ece.nam.baker.util;

import gr.upatras.ece.nam.baker.impl.BakerJpaController;
import gr.upatras.ece.nam.baker.repo.BakerRepository;
import gr.upatras.ece.nam.baker.repo.BakerRepositoryAPIImpl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailUtil {

	private static final transient Log logger = LogFactory.getLog(EmailUtil.class.getName());

	public static void SendRegistrationActivationEmail(String email, String messageBody) {

		Properties props = new Properties();

		// Session session = Session.getDefaultInstance(props, null);

		props.setProperty("mail.transport.protocol", "smtp");
		if ((BakerRepository.getPropertyByName("mailhost").getValue()!=null)&&(!BakerRepository.getPropertyByName("mailhost").getValue().isEmpty()))
			props.setProperty("mail.host", BakerRepository.getPropertyByName("mailhost").getValue());
		if ((BakerRepository.getPropertyByName("mailuser").getValue()!=null)&&(!BakerRepository.getPropertyByName("mailuser").getValue().isEmpty()))
			props.setProperty("mail.user", BakerRepository.getPropertyByName("mailuser").getValue());
		if ((BakerRepository.getPropertyByName("mailpassword").getValue()!=null)&&(!BakerRepository.getPropertyByName("mailpassword").getValue().isEmpty()))
			props.setProperty("mail.password", BakerRepository.getPropertyByName("mailpassword").getValue());

		String adminemail = BakerRepository.getPropertyByName("adminEmail").getValue();
		String subj = BakerRepository.getPropertyByName("activationEmailSubject").getValue();
		logger.info("adminemail = " + adminemail);
		logger.info("subj = " + subj);

		Session mailSession = Session.getDefaultInstance(props, null);
		Transport transport;
		try {
			transport = mailSession.getTransport();

			MimeMessage msg = new MimeMessage(mailSession);
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress( adminemail , adminemail));
			msg.setSubject(subj);
			msg.setContent(messageBody, "text/html; charset=ISO-8859-1");
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));

			transport.connect();
			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));

			transport.close();

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
