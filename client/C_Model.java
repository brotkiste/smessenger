package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ListIterator;
import java.util.Vector;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;

public class C_Model {
	private String contactsfile;
	private String identitiesfile;
	
	
	private Vector<C_Contact>  		contacts;
	private Vector<C_Identity> 	identities;
	private Vector<C_Server> 		servers;
	
	public C_Model(String contactsfile, String identitiesfile){
		this.contactsfile	= contactsfile;
		this.identitiesfile	= identitiesfile;
		this.contacts		= (Vector<C_Contact>) loadObject(contactsfile);
		this.identities		= (Vector<C_Identity>) loadObject(identitiesfile);
	}
	
	public C_Identity addIdentity(String name, String email, String telnr, String passwd){
		C_Krypto.genKeyPair(name, telnr, email, passwd);
		return null;
	}
	
	public C_Identity addIdentitiy(String name, String email, String telnr, String passwd, C_KeyPair keyPair){
		C_Identity result = new C_Identity(name, email, telnr, passwd, keyPair);
		identities.add(result);
		return result;
		
	}

	
	public C_Contact addContact(String name, String email, String telnr, String suchcode, String pubKeyFile, String[] serverParams){
		C_Contact result = null;
		C_PubKey pubKey = null;
		C_Server server = null;
		
		if(!pubKeyFile.equals("")){
			try {
				
				pubKey = C_Krypto.importPublicKey(new FileInputStream(pubKeyFile));
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(pubKey==null&&!suchcode.equals("")){
			pubKey = C_Krypto.lookup(suchcode);
		}
		
		if(pubKey==null&&!telnr.equals("")){
			pubKey = C_Krypto.lookup(telnr);
		}
		
		if(pubKey==null&&!email.equals("")){
			pubKey = C_Krypto.lookup(email);
		}
		
		if(pubKey==null&&!name.equals("")){
			pubKey = C_Krypto.lookup(name);
		}
		
		if(pubKey==null){
			System.out.println("Kein public Key verfügbar!");
			return null;
		}
		
		server = new C_Server(serverParams);
		
		if(server==null){
			System.out.println("Der Server konnte nicht gefunden werden!");
			return null;
		}
		
		result = new C_Contact(name, email, telnr, suchcode, pubKey, server);
		
		contacts.add(result);
		saveObject(contacts, contactsfile);
		return result;
	}
	
	
	public void saveObject(Object o, String filename){
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
		
			out.writeObject(o);
		
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object loadObject(String filename){
		Object result = null;
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
			
			result = in.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Datei konnten nicht geladen werden!");
		}
			
		return result;
	}
	
	public int checkForMsg(){
		int result = 0;
		
		ListIterator<C_Contact> iterator = contacts.listIterator();
		while(iterator.hasNext()){
			iterator.next().
			//TODO
		}
		
		return result;
	}
	
}
