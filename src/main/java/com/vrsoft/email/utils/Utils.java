package com.vrsoft.email.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.vrsoft.email.EmailTransactionConfiguration;

public class Utils {

	private static Logger log = Logger.getLogger(Utils.class);

	private Utils() {
		// All static methods no public constructor required
	}

	/***
	 * For Email Without Attachment
	 * 
	 * @param email_id -
	 * @param subject  -
	 * @param message  -
	 * @param config   - Configuration Object.
	 * @return - Response Json Recieved by Mailgun API.
	 *
	 */
	public static Response sendEmail_MailGun(final String email_id, final String subject, final String message,
			final String test_email,final EmailTransactionConfiguration config) {

		Client client = JerseyClientBuilder.newClient();
		client.register(HttpAuthenticationFeature.basicBuilder().credentials("api", config.getApi_key()).build());

		WebTarget target = client.target(config.getApi_url()).path(config.getApi_domain()).path("messages");

		Form formData = new Form();
		formData.param("from", config.getApi_from());
		formData.param("to", email_id.trim());
		formData.param("subject", subject.trim());
		formData.param("text", message);
		
		if (test_email.trim().equals("1")) {
			formData.param("o:testmode", "true");
		}

		return target.request().post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
	}

	/***
	 * For Emails with Attachment
	 * 
	 * @param email_id   - Email Id to Be sent
	 * @param subject    - Subject
	 * @param message    - Email Body
	 * @param attachment - Attachment File name
	 * @param config     - Configuration Object.
	 * @return - Response Json Recieved by Mailgun API
	 */
	public static Response sendEmail_MailGun(final String email_id, final String subject, final String message,
			final String test_email, final String attachment, final EmailTransactionConfiguration config) {

		Client client = JerseyClientBuilder.newClient();
		client.register(HttpAuthenticationFeature.basicBuilder().credentials("api", config.getApi_key()).build());
		client.register(MultiPartFeature.class);
		WebTarget target = client.target(config.getApi_url()).path(config.getApi_domain()).path("messages");

		FormDataMultiPart formData = new FormDataMultiPart();
		formData.field("from", config.getApi_from());
		formData.field("to", email_id.trim());
		formData.field("subject", subject.trim());
		formData.field("text", message);

		if (test_email.trim().equals("1")) {
			formData.field("o:testmode", "true");
		}

		String[] mul_attachments = attachment.split(",");

		if (mul_attachments.length > 1) {
			for (int i = 0; i < mul_attachments.length; i++) {
				File mul_attach = new File(mul_attachments[i]);
				if (!mul_attach.exists()) {
					try {
						formData.close();
						client.close();
					} catch (IOException e) {
						log.error("File Not Found", e);
					}
					return Response.serverError().entity("Attachment File not found").build();
				}

				formData.bodyPart(new FileDataBodyPart("attachment" + "[" + i + "]", mul_attach,
						MediaType.APPLICATION_OCTET_STREAM_TYPE));

			}

		}

		else {
			File attach = new File(attachment);
			if (!attach.exists()) {
				try {
					formData.close();
					client.close();
				} catch (IOException e) {
					log.error("File Not Found", e);
				}
				return Response.serverError().entity("Attachment File not found").build();
			}

			formData.bodyPart(new FileDataBodyPart("attachment", attach, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		}
		return target.request().post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA_TYPE));
	}

	/***
	 * Sending SMTP Email Without Mailgun.
	 * 
	 * @param email_id  - Email Id to be used
	 * @param subject   - Email Subject
	 * @param body      - Content of the Email
	 * @param file_name - Attached File Name
	 * @param cc        - If copy of email is to be forwarded.
	 * @param config    - Configuration Object
	 * @return -
	 */
	public static String sendEmail_SMTP(final String email_id, final String subject, final String body,
			final String file_name, final String cc, final EmailTransactionConfiguration config) {

		String[] ids = cc.split(",");

		Session session = Session.getInstance(getEmailProperties(config), new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(config.getUsername(), config.getPassword());
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(config.getFrom_email()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_id));

			if (ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].length() > 0) {
						message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ids[i].trim()));
					}
				}
			}

			message.setSubject(subject);

			if (file_name.trim().length() > 1) {
				if (new File(file_name).exists()) {
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setText(body);
					messageBodyPart.setContent(body, "text/html");

					BodyPart attachmentPart = new MimeBodyPart();
					DataSource source = new FileDataSource(file_name);
					attachmentPart.setDataHandler(new DataHandler(source));
					String AttachmentName = source.getName();
					attachmentPart.setFileName(AttachmentName);

					Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart);
					multipart.addBodyPart(attachmentPart);
					message.setContent(multipart);

				}

				else {
					return "File Not Found";
				}
			}

			else {
				Multipart mp = new MimeMultipart();
				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(body, "text/html");
				mp.addBodyPart(htmlPart);
				message.setContent(mp);
			}

			Transport.send(message);

		} catch (AddressException e) {
			log.error("Email Address Is incorrect", e);
			return "Email Address Is incorrect";
		} catch (MessagingException e) {
			log.error("Issues While Sending Email", e);
			return "Issues while Sending Email";
		}
		return "Email Sent";
	}

	/***
	 * 
	 * @param config - Configuration Object.
	 * @return - Properties Object
	 */
	private static Properties getEmailProperties(final EmailTransactionConfiguration config) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", config.getSmtp_host());
		props.put("mail.smtp.port", config.getSmtp_port());
		return props;
	}

	/***
	 * 
	 * @param config - Configuration Object.
	 * @return - Status Of MailGun API.
	 */
	public static boolean getMailStatus(final EmailTransactionConfiguration config) {

		try {

			HttpResponse<JsonNode> accountMonitorsJson = Unirest.post(config.getApi_check_url())
					.queryString("noJsonCallback", 1).asJson();

			JSONObject jsonObject = accountMonitorsJson.getBody().getObject();
			String status = jsonObject.getJSONObject("monitors").getJSONArray("monitor").getJSONObject(0)
					.getString("status");

			if (("2").equals(status)) {
				return true;
			}

		} catch (UnirestException e) {
			log.error("Error while parsing", e);
		}

		return false;
	}

	/***
	 * 
	 * @param email   - Email Id to be Checked
	 * @param subject - Subject to be checked
	 * @param message - Message to be checked
	 * @return - Error String.
	 */
	public static String checkParam(final String email, final String subject, final String message) {

		if (email == null) {
			return "Email Id Not Specified";
		}

		if (subject == null) {
			return "Subject Not Specified";
		}

		if (message == null) {
			return "Email Body Not Specified";
		}

		return "";
	}

	/**
	 * 
	 * @param emailid
	 * @param config  - Configuration Object.
	 * @return - Json of Valid Email Address.
	 */
	private static String getValidate(final String emailid, final EmailTransactionConfiguration config) {

		Client client = JerseyClientBuilder.newClient();

		client.register(HttpAuthenticationFeature.basicBuilder().credentials("api", config.getApi_val_key()).build());

		WebTarget target = client.target(config.getApi_url()).path("address").path("validate").queryParam("address",
				emailid);

		return target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(String.class);
	}

	/***
	 * 
	 * @param emailid - Email id to be checked.
	 * @param config- Configuration Object.
	 * @return - true if Email is Valid, false Otherwise.
	 */
	public static boolean checkValidEmail(final String emailid, final EmailTransactionConfiguration config) {
		JSONObject jsonObject = new JSONObject(getValidate(emailid, config));
		return (boolean) jsonObject.get("is_valid");
	}

	public static String validateEmail(final String email_Id) {
		if (email_Id == null || ("").equals(email_Id) || !isValidEmailPattern(email_Id)) {
			return "";
		}

		return email_Id.split(",")[0].trim();
	}

	private static boolean isValidEmailPattern(final String emailId) {
		return Pattern.matches("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})", emailId);
	}

}
