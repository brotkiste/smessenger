package client;

import java.io.Serializable;

import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;

public class C_SecKey implements Serializable{
	
	private transient PGPSecretKey keyObject;
	private String keyString;

	public C_SecKey(PGPSecretKey secretKey, String keyString) {
		this.keyObject = secretKey;
		this.keyString = keyString;
	}

}
