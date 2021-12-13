/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 */
package de.terrestris.shogun.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Helper class for mailing
 */
public class Mail {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(Mail.class);

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

			LOGGER.info("Sent an eMail with subject '" + subject + "' to '" + to + "'. The sender is '" + from + "'.");

		} catch (Exception sendException) {
			throw new MailSendException("Error while sending an email: "
					+ sendException.getMessage(), sendException);
		}
	}

}
