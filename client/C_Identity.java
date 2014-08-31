package client;

import java.io.Serializable;

/**
 * Speichert die Informationen einer Identität
 * @author matthias
 *
 */
public class C_Identity implements Serializable{
	
	private String name;
	private String email;
	private String telnr;
	private String passwd;
	private C_KeyPair keyPair;

	/**
	 * Erzeugt eine neue Identität
	 * @param name
	 * @param email
	 * @param telnr
	 * @param passwd	//TODO Es sollte später möglich sein, kein Passwort für den PrivateKey zu speichern und den User bei Bedarf danach zu fragen
	 * @param keyPair
	 */
	public C_Identity(String name, String email, String telnr, String passwd,
			C_KeyPair keyPair) {
		this.name 		= name;
		this.email 		= email;
		this.telnr 		= telnr;
		this.keyPair	= keyPair; 
		this.passwd 	= passwd;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getTelnr(){
		return telnr;
	}
	
	public String getPasswd(){
		return passwd;
	}
	
	public String getPubKeyString(){
		return keyPair.getPubKey().getKeyString();
	}
	
	@Override
	/**
	 * Erzeugt einen String, in dem die Informationen der Identität gespeichert sind, die nötig sind, um auf eine Nachricht zu antworten
	 * KEIN PRIVATE KEY !!
	 */
	public String toString() {
		return "[C_Identity={{Name=\""+this.name+"\"},{Email=\""+this.email+"\"},{Telnr=\""+this.telnr+"\"},{PubKey=\""+this.getPubKeyString()+"\"}}]";
	}

}
