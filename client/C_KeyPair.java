package client;

import java.io.Serializable;

public class C_KeyPair implements Serializable{
	
	private C_SecKey secretKey;
	private C_PubKey pubKey;
	

	public C_KeyPair(C_SecKey sec, C_PubKey pub) {
		this.secretKey = sec;
		this.pubKey = pub;
	}


	public C_PubKey getPubKey() {
		// TODO Auto-generated method stub
		return pubKey;
	}

}
