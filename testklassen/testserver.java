package testklassen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class testserver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(5555);
			Socket s = server.accept();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String text = in.readLine();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			out.write(text.toUpperCase());
			out.newLine();
			out.flush();
			out.close();
			in.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
