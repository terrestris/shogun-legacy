package de.terrestris.shogun.util;

import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Helper class for mailing
 */
public class Mail {
	
	/**
	 * Sends an email with the given parameters
	 * 
	 * @param host host of mailserver
	 * @param to receipient's email address
	 * @param from the sender name
	 * @param subject the subject of the mail
	 * @param msgText the message text
	 * 
	 * @throws MailSendException
	 */
	public static void send(String host, Integer port, String to, String from, String subject,
			String msgText) throws MailSendException {
		
		try {
			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setHost(host);
			sender.setPort(port);

			SimpleMailMessage msg = new SimpleMailMessage();
			
			msg.setTo(to);
			msg.setFrom(from);
			msg.setSubject(subject);
			msg.setText(msgText);

			sender.send(msg);

		} catch (Exception sendException) {
			throw new MailSendException("Error while sending an email: "
					+ sendException.getMessage(), sendException);
		}
	}

}
