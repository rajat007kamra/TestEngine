package core.model;

/**
 * @summary Model for reading emails e.g. APPROVERS, PERMISSIONS, RESET PASSWORD etc.
 * @author Surendra.Shekhawat
 */
public class EmailData {

	private int messageNumber;
	private String from;
	private String to;
	private String cc;
	private String subject;
	private String sentDate;
	private String messageContent;

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

}