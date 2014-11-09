package server;

import javafx.application.Platform;


public class GapListLoader extends Thread {
	
	
	private YTJBServer server;
	private IdelViewer viewer;
	
	public GapListLoader( YTJBServer server, IdelViewer viewer) {
		this.server = server;
		this.viewer = viewer;
	}
	
	@Override
	public void run() {
		super.run();
		server.loadGapListFromFile();
		Platform.runLater(() -> viewer.gaplistReadOut(false));
	}
}
