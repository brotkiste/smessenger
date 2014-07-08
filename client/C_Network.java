package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Stellt einen Socket zur Verfügung
 * 
 * @author Matthias Kraus
 *
 */
public class C_Network {
	Socket s;
	BufferedWriter out;
	BufferedReader in;
	
	/**
	 * 
	 * @param ip Zieladresse
	 * @param port Zielport
	 */
	public C_Network(String ip, int port){
		try {
			s = new Socket(ip, port);
		
			out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println(ip + " konnte nicht gefunden werden!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Verschickt einen String
	 * @param s Nachricht
	 */
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
	
	
	/**
	 * Wartet auf einen String. Der String muss unbedingt mit einem Zeilenumbruch beendet werden!
	 * @return empfangener String
	 */
	public String stringEmpfangen(){
		try {
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "FEHLER!! C_Network.stringEmpfangen()";
	}
	
	/**
	 * sollte aufgerufen werden, wenn die Verbindung nicht mehr benötigt wird
	 */
	public void socketSchliessen(){
		try{
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
