package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class JavaServerAdapter extends ServerAdapter {
	
	private String ip;
	private int port;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

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

}
