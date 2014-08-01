package client;

public abstract class Msg {
	
	private String absender;
	private String msg;
	private String timestamp;
	private String encTimestamp;
	private boolean encrypted;
	
	public Msg(String absender, String msg, String timestamp, String encTimestamp, boolean encypted){
		this.absender	= absender;
		this.msg		= msg;
		this.timestamp	= timestamp;
		this.encTimestamp = encTimestamp;
		this.encrypted	= encypted;
	}

	public String getAbsender() {
		return absender;
	}

	public String getMsg() {
		return msg;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getEncTimestamp() {
		return encTimestamp;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

}
