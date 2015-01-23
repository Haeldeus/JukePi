package client.serverconnection.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import messages.MessageType;
import client.ServerAddress;
import client.listener.NotificationListener;
import client.listener.ResponseListener;
import client.serverconnection.ServerConnectionNotifier;
import client.serverconnection.ServerConnection;
import client.serverconnection.functionality.LowLevelServerConnection;
import client.serverconnection.functionality.YTJBLowLevelServerConnection;

public class YTJBServerConnection implements ServerConnection, ServerConnectionNotifier {
	
	private List<NotificationListener> notificationListener;
	private LowLevelServerConnection serverConnection;
	private boolean connected = false;
	private int checkIntervall = 0;
	
	public YTJBServerConnection() {
		this(0);
	}
	
	public YTJBServerConnection(int checkIntervall) {
		notificationListener = new ArrayList<NotificationListener>();
		connected = false;
		this.checkIntervall = checkIntervall;
	}

	@Override
	public void onNotify(int notifyType,String[] args) {
		switch(notifyType){
		case MessageType.PAUSERESUMENOTIFY: for(NotificationListener l: notificationListener) l.onPauseResumeNotify(Boolean.parseBoolean(args[0]));
			break;
		case MessageType.NEXTTRACKNOTIFY:for(NotificationListener l: notificationListener) l.onNextTrackNotify(args[0],args[1],Boolean.parseBoolean(args[2]));
			break;
		case MessageType.GAPLISTUPDATEDNOTIFY:for(NotificationListener l: notificationListener) l.onGapListUpdatedNotify(args);
			break;
		case MessageType.WISHLISTUPDATEDNOTIFY:for(NotificationListener l: notificationListener) l.onWishListUpdatedNotify(args);
			break;
		case MessageType.GAPLISTCOUNTCHANGEDNOTIFY:for(NotificationListener l: notificationListener) l.onGapListCountChangedNotify(args);
			break;
		case MessageType.GAPLISTCHANGEDNOTIFY:for(NotificationListener l: notificationListener) l.onGapListChangedNotify(args[0]);
			break;
		case MessageType.DISCONNECT: for(NotificationListener l: notificationListener) l.onDisconnect();
										connected = false;
		}

	}

	@Override
	public void addNotificationListener(NotificationListener listener) {
		this.notificationListener.add(listener);

	}

	@Override
	public void removeNotificationListener(NotificationListener listener) {
		this.notificationListener.remove(listener);

	}

	@Override
	public void addToList(ResponseListener response, String url,
			boolean toWishList, boolean toBack) {
		if (toWishList && toBack)
			this.serverConnection.sendMessage(response, MessageType.YOUTUBE, url);
		else if (!toWishList && toBack)
			this.serverConnection.sendMessage(response, MessageType.GAPYOUTUBE, url);
		else if (toWishList && !toBack)
			this.serverConnection.sendMessage(response, MessageType.BEGINNINGYOUTUBE, url);
		else if (!toWishList && !toBack)
			this.serverConnection.sendMessage(response, MessageType.GAPBEGINNINGYOUTUBE, url);	
	}

	@Override
	public void deleteFromList(ResponseListener response, int index) {
		this.serverConnection.sendMessage(response, MessageType.DELETEFROMGAPLIST, ""+index);
	}

	@Override
	public void getCurrentTrackTitle(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETCURRENTTRACK);
		
	}

	@Override
	public void getGapList(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETGAPLIST);
	}

	@Override
	public void getWishList(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETWISHLIST);
	}

	@Override
	public void skip(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.SKIP);
		
	}

	@Override
	public void pauseResume(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.PAUSERESUME);
	}

	@Override
	public void switchToGapList(ResponseListener response, String name) {
		this.serverConnection.sendMessage(response, MessageType.LOADGAPLIST,name);
		
	}

	@Override
	public void deleteGapList(ResponseListener response, String name) {
		this.serverConnection.sendMessage(response, MessageType.DELETEGAPLIST,name);
	}

	@Override
	public void getAvailableGapLists(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETAVAILABLEGAPLISTS);
		
	}

	@Override
	public void saveGapList(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GAPLISTSAVETOFILE);
		
	}

	@Override
	public void getCurrentGapListName(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETCURRENTGAPLISTNAME);
	}

	@Override
	public void getTitleFromGapList(ResponseListener response, String name) {
		this.serverConnection.sendMessage(response, MessageType.GETTITLEFROMGAPLIST,name);
	}

	@Override
	public void getCurrentPlaybackStatus(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETCURRENTPLAYBACKSTATUS);
	}

	@Override
	public void setGapListTrackUp(ResponseListener response, int index) {
		this.serverConnection.sendMessage(response, MessageType.GAPLISTTRACKUP,""+index);
	}

	@Override
	public void setGapListTrackDown(ResponseListener response, int index) {
		this.serverConnection.sendMessage(response, MessageType.GAPLISTTRACKDOWN,""+index);
	}

	@Override
	public void getNextVideoURL(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETNEXTVIDEOURL);
	}

	@Override
	public void notifyPlayerFinished(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.PLAYERFINISHED);
	}

	@Override
	public boolean close() {
		if(serverConnection.close()){
			connected = false;
			return true;
		}else{
			connected = true;
			return false;
		}
	}

	@Override
	public void setMeAsPlayer() {
		this.serverConnection.sendMessage(MessageType.SETMEASPLAYER);
	}

	@Override
	public boolean connect(String ipAddress, int port) {
		this.serverConnection = new YTJBLowLevelServerConnection(this,ipAddress,port,checkIntervall);
		if (serverConnection.connect()){
			connected = true;
			return true;
		}else{
			connected = false;
			return false;			
		}
	}

	@Override
	public void getLoadGapListStatus(ResponseListener response) {
		this.serverConnection.sendMessage(response,MessageType.GETLOADGAPLISTSTATUS);
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public ServerAddress waitForUDPConnect() {
	    // Netzwerk-Gruppe
	    String NETWORK_GROUP = "230.0.0.1";
	    // Netzwerk-Gruppen Port
	    int NETWORK_GROUP_PORT = 4447;
	   
	    // Nachrichten-Codierung
	    String TEXT_ENCODING = "UTF8";
	   
	    MulticastSocket socket;
	 
	      // Gruppe anlegen
	      try {
			socket = new MulticastSocket(NETWORK_GROUP_PORT);
		      InetAddress socketAddress = InetAddress.getByName(NETWORK_GROUP);

		      socket.joinGroup(socketAddress);
	   
	     
	      byte[] bytes = new byte[65536];
	      DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
	     
	      while(true){
	        // Warten auf Nachricht
	  	    try {
		        socket.receive(packet);
		        String message = new String(packet.getData(),0,packet.getLength(), TEXT_ENCODING);
		        socket.leaveGroup(socketAddress);
		        socket.close();
		        String[] values = message.split(MessageType.SEPERATOR);
		        return new ServerAddress(values[0],Integer.parseInt(values[1]));
		    } catch (IOException e) {
			      e.printStackTrace();
			    }
	      }   
		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
	    return null;
	}

	@Override
	public String getIPAddress() {
		return this.serverConnection.getIPAddress();
	}

	@Override
	public int getPort() {
		return this.serverConnection.getPort();
	}

	@Override
	public void getCurrentPlayerCount(ResponseListener response) {
		this.serverConnection.sendMessage(response,MessageType.GETCURRENTPLAYERCOUNT);
	}

	@Override
	public void getCurrentClientCount(ResponseListener response) {
		this.serverConnection.sendMessage(response, MessageType.GETCURRENTCLIENTCOUNT);
	}

	@Override
	public boolean connect(ServerAddress serverAddress) {
		return connect(serverAddress.getIPAddress(),serverAddress.getPort());
	}

}