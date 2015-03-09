package connection;


import javax.swing.JFrame;

import server.Server;
import server.ServerFactory;
import windows.LogIn;
import windows.MainWindow;
import windows.Window;
import client.ServerConnectionFactory;
import client.listener.DefaultNotificationListener;
import client.listener.GapListNotificationListener;
import client.listener.PauseResumeNotificationListener;
import client.serverconnection.ServerConnection;
import client.serverconnection.Song;

/**
 * The Collector, that will start the Client. Also provides all necessary information for each Frame, to work properly.
 * @author Haeldeus
 * @version 1.0
 */
public class Collector implements DefaultNotificationListener, PauseResumeNotificationListener, GapListNotificationListener{

	/**
	 * Time in ms, when the wrapper should check the connectivity of the server, if no response 
	 * arrived.
	*/
	private static final int CONNECTIONCHECKINTERVALL = 15000;
	
	/**
	 * The wrapper, that will send the Messages.
	 * @see ServerConnection
	 */
	private ServerConnection wrapper;
	
	/**
	 * The Login-Screen, that will be shown, when starting the Client.
	 * @see LogIn
	 */
	private LogIn loginScreen;
	
	/**
	 * The Main-Screen, that will be shown after logging in to a Server.
	 * @see MainWindow
	 */
	private MainWindow mainScreen;
	
	/**
	 * The possible Server, that runs locally. Will be null if connecting to another Server, 
	 * but will be an instance of Server, if the User clicks the "Create own Server"-Button.
	 * @see Server
	 */
	private Server localServer;
	
	/**
	 * The Screen, that is currently visible.
	 * @see JFrame
	 */
	private JFrame visibleScreen;
	
	/**
	 * The Gaplist, which is played on the Server.
	 * @see Song
	 */
	private Song[] gaplist;
	
	/**
	 * The Wishlist of Songs, that will be played.
	 * @see Song
	 */
	private Song[] wishlist;
	
	/**
	 * The Constructor for the Collector. Creates a new Connection to the Server and 
	 * instantiates a new Login-Screen.
	 * @since 1.0
	 */
	public Collector() {
		wrapper = ServerConnectionFactory.createServerConnection(CONNECTIONCHECKINTERVALL);
		visibleScreen = new JFrame();
		loginScreen = new LogIn(this, visibleScreen);
	}
	
	@Override
	public void onPauseResumeNotify(boolean isPlaying) {
		mainScreen.pauseResume(isPlaying);
	}

	@Override
	public void onGapListCountChangedNotify(String[] gapLists) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGapListChangedNotify(String gapListName) {
		mainScreen.gaplistChanged(gapListName);
	}

	@Override
	public void onGapListUpdatedNotify(Song[] title) {
		mainScreen.setGaplist(title);
	}

	@Override
	public void onWishListUpdatedNotify(Song[] title) {
		mainScreen.setWishlist(title);
	}

	@Override
	public void onNextTrackNotify(String title, String url, boolean isVideo) {
		mainScreen.setNextTrack(title);
	}

	@Override
	public void onDisconnect() {
		disconnect();
	}

	/**
	 * Connects to the given IP and Port. Also hides the Login-Screen and opens the 
	 * Main-Screen.
	 * @param ip	The IP, the Client will connect to.
	 * @param port	The Port, the Client will connect to.
	 * @return	true, if the Connection was established, false else (e.g. wrong data).
	 * @since 1.0
	 */
	public boolean connect(String ip, String port) {
		showFail(loginScreen, "Pending IP, please wait!");
		int iport = -1;
		try {
			iport = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			return false;
		}
		wrapper.addDefaultNotificationListener(this);
		wrapper.addGapListNotificationListener(this);
		wrapper.addPauseResumeNotificationListener(this);
		
		if (wrapper.connect(ip, iport)) {
			loginScreen.close();
			mainScreen = new MainWindow(this, visibleScreen, wrapper, gaplist, wishlist);
			mainScreen.show();
			mainScreen.setIpAndPort(ip, iport);
			return true;
		}
		else {
			showFail(loginScreen, "Incorrect Server information. Please try another IP-Address.");
			return false;
		}
	}
	
	/**
	 * Disconnects from the Server and displays the Login-Screen.
	 * @since 1.0
	 */
	public void disconnect() {
		mainScreen.close();
		wrapper.close();
		if (localServer != null)
			localServer.shutDown();
		visibleScreen.getContentPane().removeAll();
		loginScreen.show();
	}
	
	/**
	 * Shows the Fail-Label with the given text on the given window.
	 * @param window	The window to display the Message on.
	 * @param text	The text to be shown.
	 * @since 1.0
	 */
	public void showFail(Window window,String text) {
		window.showFail(text);
	}
	
	/**
	 * Creates a local Server with the given Port.
	 * @param port	The Port, the Server will have.
	 * @since 1.0
	 */
	public void createLocalServer(String port){
		int iport = -1;
		try {
			iport = Integer.parseInt(port);
			localServer = ServerFactory.createServer(iport);
			localServer.startUp();
			this.connect("localhost", ""+port);
		} catch (NumberFormatException nfe) {
			showFail(loginScreen, "Please insert a real number at the Port-Field.");
		}
		
	}
	
	/**
	 * Starts the Client.
	 * @since 1.0
	 */
	public void startUp() {
		this.loginScreen.show();
	}

	/**
	 * Calls the showFail-Method, when the UDP-Connect failed.
	 * @param string	The Message to be shown.
	 * @since 1.0
	 */
	public void showUDPFail(String string) {
		showFail(loginScreen, string);
	}
	
	/**
	 * Sets the given lists as new Wish- and Gaplist.
	 * @param wishlist	The new Wishlist.
	 * @param gaplist	The new Gaplist.
	 * @since 1.0
	 */
	public void setLists(Song[] wishlist, Song[] gaplist) {
		this.wishlist = wishlist;
		this.gaplist = gaplist;
	}
}