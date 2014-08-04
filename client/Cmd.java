package client;

import java.io.Serializable;

public class Cmd implements Serializable{
	
	private int cmdCode;
	private Object data;
	
	public Cmd(int cmdCode){
		this.cmdCode = cmdCode;
	}
	
	public int getCmdCode(){
		return this.cmdCode;
	}
	
	public Object getData(){
		return this.data;
	}

}
