package client;

public abstract class ServerAdapter {

	public abstract boolean openConnection();
	public abstract void closeConnection();
	
	public abstract void sendMessage(Msg msgout);
}
