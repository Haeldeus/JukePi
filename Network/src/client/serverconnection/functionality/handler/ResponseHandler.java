package client.serverconnection.functionality.handler;

import client.listener.ResponseListener;
import client.serverconnection.functionality.ResponseController;

public class ResponseHandler implements Runnable {
	
	private ResponseController responses ;
	private String[] message;

	public ResponseHandler(ResponseController responses,String[] message ) {
		this.responses = responses;
		this.message = message;
	}
	
	@Override
	public void run() {
		int messageType = Integer.parseInt(message[1]);
		String[] arguments = new String[message.length - 2];
		for (int i = 2; i < message.length; i++){
			arguments[i - 2] = message[i];
		}
		ResponseListener currentResponseListener;
		synchronized(responses){
			currentResponseListener = responses.getResponseListener(messageType);
		}
		currentResponseListener.onResponse(arguments);
	}

}
