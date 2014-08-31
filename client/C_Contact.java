package client;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.bouncycastle.openpgp.PGPPublicKey;

/**
 * Speichert Kontaktinformationen
 * @author matthias
 *
 */
public class C_Contact implements Serializable {
	private String name;
	private String email;
	private String telnr;
	private String suchcode;
	
	private C_PubKey pubKey;
	private C_Server server;
	
	/**
	 * Erzeugt einen neuen Kontakt
	 * @param name		Name des Kontakts
	 * @param email		Email-Adresse
	 * @param telnr		z.b. Telefonnummer (--> Handyapp??), kann aber auch alles andere sein
	 * @param suchcode	??
	 * @param pubKey	Öffentlicher Schlüssel des Kontakts, mit dem Nachrichten an diesen verschlüsselt werden
	 * @param server	Server, über den dieser Kontakt erreicht werden kann
	 */
	public C_Contact(String name, String email, String telnr,
			String suchcode, C_PubKey pubKey, C_Server server) {
		this.name = name;
		this.email = email;
		this.telnr = telnr;
		this.suchcode = suchcode;
		
		this.pubKey = pubKey;
		this.server = server;
		
	}

	/**
	 * Sendet eine Textnachricht an diesen Kontakt
	 * @param absender	Absender, an den der Kontakt antworten soll
	 * @param msg		Nachricht
	 * @param encrypted	ob die Nachricht verschlüsselt werden soll
	 */
	public void sendTextMsg(C_Identity absender, String msg, boolean encrypted){
		server.sendTextMsg(absender, this, msg, encrypted);
	}
	

	public PGPPublicKey getKeyObject() {
		return this.pubKey.getKeyObject();
	}
}
