package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class C_Network {
	Socket s;
	BufferedWriter out;
	
	public C_Network(String ip, int port){
		try {
			s = new Socket(ip, port);
		
			out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println(ip + " konnte nicht gefunden werden!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stringSenden(String s){
		try {
			out.write(s);
		
		out.newLine();
		out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
