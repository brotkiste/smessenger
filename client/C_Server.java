package client;

import java.io.Serializable;

public class C_Server implements Serializable{
	
	private static final int JAVASERVER = 0;
	
	private int type;
	private transient ServerAdapter sAdapter;

	public C_Server(String[] serverParams){
		//Geht davon aus, dass es nur den JavaServer gibt
		if(serverParams.length==2){
			this.type = JAVASERVER;
		}
		
		this.initialize(serverParams);
	}
	
	public void initialize(String[] serverParams){
		switch (type) {
		case JAVASERVER:
			sAdapter = new JavaServerAdapter(serverParams[0], serverParams[1]);
			break;

		default:
			break;
		}
	}


	public void sendTextMsg(C_Identitiy absender, C_Contact empfaenger,
			String msg, boolean encrypted) {
		String msgout;
		String absenderout;
		
		String timestamp = System.currentTimeMillis()+"";
		String encTimestamp = C_Krypto.encryptString(timestamp, empfaenger.getKeyObject());
		
		if(encrypted){
			msgout = C_Krypto.encryptString(msg, empfaenger.getKeyObject());
			 absenderout = C_Krypto.encryptString(absender.toString(), empfaenger.getKeyObject());
		}else{
			msgout = msg;
			absenderout = absender.toString();	
		}
		
		sAdapter.sendMessage(new TextMsg(absenderout, msgout, timestamp, encTimestamp, encrypted));
		
	}

}
