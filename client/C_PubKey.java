package client;

import java.io.Serializable;

import org.bouncycastle.openpgp.PGPPublicKey;

public class C_PubKey implements Serializable{
	
	private transient PGPPublicKey keyObject;
	private String keyString;

	public C_PubKey(PGPPublicKey keyObject, String keyString){
		this.keyObject = keyObject;
		this.keyString = keyString;
	}

	public PGPPublicKey getKeyObject() {
		if(keyObject==null){
			this.keyObject = C_Krypto.readPublicKeyString(keyString);
		}
		return this.keyObject;
	}
	
	public String getKeyString(){
		return this.keyString;
	}

	
	
}
