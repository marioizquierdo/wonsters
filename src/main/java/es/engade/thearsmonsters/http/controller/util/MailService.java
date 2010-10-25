package es.engade.thearsmonsters.http.controller.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MailService {

	private static final String FROM = "diegodl84@gmail.com";
	private static final String FROM_NAME = "Wonsters Admin";

	public static boolean sendMail(String to, String toName, String subject,
			String message) throws InternalErrorException, MessagingException {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM, FROM_NAME));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to,
					toName));
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);

		} catch (UnsupportedEncodingException e) {
			throw new InternalErrorException(e);
		}

		return true;
	}

	public static boolean sendRegistrationMessage(String to, String login)
			throws InternalErrorException, MessagingException {

		// TODO: LOCALIZE
		String registrarionSubject = "Registro en Wonsters";
		String registrarionMessage = "Hola "
				+ login
				+ "!\n\n"
				+ "Te has registrado satisfactoriamente en Wonsters.\n"
				+ "Recuerda que por el momento el juego está en versión Alpha, "
				+ "por lo que deberás ser comprensible con las actualizaciones y "
				+ "cambios de parámetros. Asímismo, te agradeceremos que nos "
				+ "reportes cualquier error que se produzca. Para poder localizarlo "
				+ "fácil, es esencial que intentes reproducir el error para conocer "
				+ "las condiciones en que se produce.";

		return sendMail(to, login, registrarionSubject, registrarionMessage);

	}

	// TODO: Depurar
	// public static boolean sendMailMIME(String to, String toName,
	// String subject, String message, File f) throws InternalErrorException,
	// MessagingException {
	//
	// Properties props = new Properties();
	// Session session = Session.getDefaultInstance(props, null);
	//
	// String htmlBody =
	// "<body> HOLA! <a href=\"http://google.es\">Link</a><br/>"
	// +
	// "<img title=\"Wonsters\" src=\"/images/thearsmonsters_logo.png\"/><br/> "
	// +
	// "<img title=\"Wonsters\" src=\"http://localhost:8888/images/thearsmonsters_logo.png\"/></body>";
	// if (f.exists()) {
	// System.out.println("ENCONTRADO!");
	// } else {
	// System.out.println("NO ENCONTRADO!");
	// }
	// byte[] attachmentData;
	//
	// try {
	// attachmentData = getBytesFromFile(f);
	//
	// MimeMultipart mp = new MimeMultipart();
	//
	// MimeBodyPart htmlPart = new MimeBodyPart();
	// htmlPart.setContent(htmlBody, "text/html");
	// mp.addBodyPart(htmlPart);
	//
	// MimeBodyPart attachment = new MimeBodyPart();
	// attachment.setFileName("manual.pdf");
	// attachment.setContent(attachmentData, "text/html");
	// mp.addBodyPart(attachment);
	//
	// Message msg = new MimeMessage(session);
	// msg.setFrom(new InternetAddress(FROM, FROM_NAME));
	// msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to,
	// toName));
	// msg.setSubject(subject);
	// msg.setText(message);
	// msg.setContent(mp);
	//
	// Transport.send(msg);
	// } catch (UnsupportedEncodingException e) {
	// throw new InternalErrorException(e);
	// } catch (IOException e) {
	// throw new InternalErrorException(e);
	// }
	//
	// return true;
	//
	// }

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
}
