package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.Iterator;

public class JavaServerAdapter extends ServerAdapter {
	
	private String ip;
	private int port;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	//~
	private C_PubKey pubKey;

	public JavaServerAdapter(String ip, String port) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = Integer.parseInt(port);
	}

	@Override
	public boolean openConnection() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket(ip, port);
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub
		try {
			out.flush();
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendMessage(Msg msgout) {
		try {
			
			out.writeObject(msgout);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Msg[] checkForMsg(Vector<C_Identity> identities) {
		Msg[] result = null;
		Cmd response = null;
		try {
			out.writeObject(new Cmd(Protokoll.GETMESSAGESTAMPS));
			response = (Cmd)getResponse();
			if(response.getCmdCode()==Protokoll.MESSAGESTAMPS){
				response.getData();
				//TODO Übergabeformat für die Stamps definieren!!
			}else{
				System.out.println("checkForMsg(...): unerwartete Serverantwort");
				return null;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}
	

	private Object getResponse(){
		try {
			return in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
