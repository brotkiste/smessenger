package client;

import java.io.Serializable;
import java.util.Vector;

public class C_Server implements Serializable{
	
	private static final int JAVASERVER = 0;
	
	private int type;
	private String[] serverParams;
	private transient ServerAdapter sAdapter;

	public C_Server(String[] serverParams){
		//Geht davon aus, dass es nur den JavaServer gibt
		this.serverParams = serverParams;
		if(this.serverParams.length==2){
			this.type = JAVASERVER;
		}
		
		this.initialize();
	}
	
	public void initialize(){
		if(sAdapter==null){
			switch (type) {
			case JAVASERVER:
				sAdapter = new JavaServerAdapter(this.serverParams[0], this.serverParams[1]);
				break;
				
			default:
				break;
			}
		}
	}


	public void sendTextMsg(C_Identity absender, C_Contact empfaenger,
			String msg, boolean encrypted) {
		
		this.initialize();
		
		
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
		
		sAdapter.openConnection();
		sAdapter.sendMessage(new TextMsg(absenderout, msgout, timestamp, encTimestamp, encrypted));
		sAdapter.closeConnection();
	}

	public int checkForMsg(Vector<C_Identity> identities) {
		
		this.initialize();
		
		sAdapter.openConnection();
		sAdapter.checkForMsg(identities);
		sAdapter.closeConnection();
		return 0;
	}

}
