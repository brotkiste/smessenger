package client;

import org.bouncycastle.openpgp.PGPPublicKey;

public class C_Contact {
	private String name;
	private String email;
	
	PGPPublicKey pubKey;
	
	/**
	 * Des ganze muss noch durchdacht werden
	 */
	public void sendMsg(String msg, boolean encrypted){
		String msgout;
		
		if(encrypted){
			msgout = C_Krypto.encryptString(msg, pubKey);
		}else{
			msgout = msg;
		}
	}
}
