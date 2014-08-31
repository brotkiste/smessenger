package client;

import java.util.Vector;

public abstract class ServerAdapter {

	public abstract boolean openConnection();
	public abstract void closeConnection();
	
	public abstract void sendMessage(Msg msgout);
	public abstract Msg[] checkForMsg(Vector<C_Identity> identities);
}
