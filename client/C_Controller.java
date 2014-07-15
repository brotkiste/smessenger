package client;

public class C_Controller {

	C_Network net;
	C_Model model;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new C_Controller();
	}
	
	public  C_Controller(){
		this.net = new C_Network("localhost", 5555);
		this.model = new C_Model();
		
		
		net.stringSenden(C_Krypto.encryptString("Hallo Welt!", C_Krypto.lookup("matthiaskraus@live.de")));
		System.out.println(net.stringEmpfangen());
	}

}
