package core.utilities;

import java.util.Properties;

import core.model.EmailData;

/**
 * @summary For handling token related to password reset
 * @deprecated
 * TODO To be removed from repo
 * @author Surendra.Shekhawat
 */
public class EmailReceiver {

//	private EmailData emailData;
//
//	/**
//	 * 
//	 * @param protocol
//	 * @param host
//	 * @param port
//	 * @return
//	 */
//	private Properties getServerProperties(String protocol, String host, String port) {
//		Properties properties = new Properties();
//
//		properties.put(String.format("mail.%s.host", protocol), host);
//		properties.put(String.format("mail.%s.port", protocol), port);
//
//		properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
//				"javax.net.ssl.SSLSocketFactory");
//		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
//		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));
//
//		return properties;
//	}
//
//	/**
//	 * 
//	 * @param protocol
//	 * @param host
//	 * @param port
//	 * @param userName
//	 * @param password
//	 * @return
//	 */
//	public EmailData downloadEmails(String protocol, String host, String port, String userName, String password) {
//		Properties properties = getServerProperties(protocol, host, port);
//		Session session = Session.getDefaultInstance(properties);
//
//		try {
//			Store store = session.getStore(protocol);
//			store.connect(userName, password);
//
//			Folder folderInbox = store.getFolder("INBOX");
//			folderInbox.open(Folder.READ_ONLY);
//
//			Flags seen = new Flags(Flags.Flag.SEEN);
//			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
//			Message messages[] = folderInbox.search(unseenFlagTerm);
//
//			for (int i = messages.length - 1; i > 0; i--) {
//				Message msg = messages[i];
//				Address[] fromAddress = msg.getFrom();
//				String from = fromAddress[0].toString();
//				String subject = msg.getSubject();
//				String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
//				String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
//				String sentDate = msg.getSentDate().toString();
//
//				String contentType = msg.getContentType();
//				String messageContent = "";
//
//				if (contentType.contains("text/plain") || contentType.contains("text/html")) {
//					try {
//						Object content = msg.getContent();
//						if (content != null) {
//							messageContent = content.toString();
//						}
//					} catch (Exception ex) {
//						messageContent = "[Error downloading content]";
//						ex.printStackTrace();
//					}
//				}
//
//				emailData = new EmailData();
//				emailData.setCc(ccList);
//				emailData.setFrom(from);
//				emailData.setTo(toList);
//				emailData.setSubject(subject);
//				emailData.setSentDate(sentDate);
//				emailData.setMessageContent(messageContent);
//				emailData.setMessageNumber(i + 1);
//			}
//
//			folderInbox.close(false);
//			store.close();
//		} catch (NoSuchProviderException ex) {
//			ex.printStackTrace();
//
//		} catch (MessagingException ex) {
//			logger.info("Could not connect to the message store");
//			ex.printStackTrace();
//		}
//		return emailData;
//	}
//
//	/**
//	 * 
//	 * @param address
//	 * @return
//	 */
//	private String parseAddresses(Address[] address) {
//		String listAddress = "";
//		if (address != null) {
//			for (int i = 0; i < address.length; i++) {
//				listAddress += address[i].toString() + ", ";
//			}
//		}
//		if (listAddress.length() > 1) {
//			listAddress = listAddress.substring(0, listAddress.length() - 2);
//		}
//		return listAddress;
//	}
}