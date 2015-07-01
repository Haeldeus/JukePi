package windows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import client.listener.DebugNotificationListener;
import client.serverconnection.ServerConnection;

/**
 * The Window, that will display the Debug-Messages
 * @author Haeldeus
 * @version 1.0
 */
public class DebugWindow extends Window implements DebugNotificationListener{

	/**
	 * The Frame, this Class will be working on.
	 */
	private JFrame frame;
	
	/**
	 * The Debug-Messages as an {@link ArrayList}.
	 */
	private ArrayList<String> messages;
	
	/**
	 * The TextArea for the Messages.
	 * @see JTextArea
	 */
	private JTextArea txtDebugs;
	
	/**
	 * Determines, if the Window should record the Messages and print them on the Frame or not.
	 */
	private boolean recording;
	
	private JScrollPane scrollPane;
	
	private JLabel playerCount;
	
	private JLabel clientCount;
	
	private StringBuilder buffer;
	
	/**
	 * The Constructor for the Window.
	 * @since 1.0
	 */
	public DebugWindow(ServerConnection serverConnection) {
		messages = new ArrayList<String>();
		txtDebugs = new JTextArea();
		playerCount = new JLabel(""+serverConnection.getCurrentPlayerCount());
		clientCount = new JLabel(""+serverConnection.getCurrentClientCount());
		recording = true;
		((DefaultCaret) txtDebugs.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		buffer = new StringBuilder("");
	}
	
	@Override
	public void showFail(String text) {
		
	}

	@Override
	public void show() {
		createFrame();
	}

	@Override
	public void close() {
		if (frame != null)
			frame.setVisible(false);
	}

	@Override
	public void onClientCountChangedNotify(int newClientCount) {
		clientCount.setText(""+newClientCount);
	}

	@Override
	public void onPlayerCountChangedNotify(int newPlayerCount) {
		playerCount.setText(""+newPlayerCount);
	}

	@Override
	public synchronized void onNewOutput(String output) {
		buffer.append(output+"\n");
		if (recording){ 
			addNewMessage();
		}
	}
	
	/**
	 * Adds a new Message to {@link #messages}, if {@link #recording} is {@code true}.
	 * @param message	The Message, that might be added.
	 * @since 1.0
	 */
	private synchronized void addNewMessage() {	
		if (buffer.length() > 0){
			txtDebugs.append(buffer.toString());
			buffer.setLength(0);
			if (txtDebugs.getLineCount() > 200){
				int diff = txtDebugs.getLineCount() - 200;
				try {
					txtDebugs.replaceRange("", 0, txtDebugs.getLineEndOffset(diff - 1));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Creates the Frame for the DebugWindow.
	 * @since 1.0
	 */
	private void createFrame() {
		frame = new JFrame();
		frame.setSize(new Dimension(800, 400));
		frame.setTitle("Debugging Tool");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container pane = new Container();
		pane.setLayout(new BorderLayout());
		
		scrollPane = new JScrollPane(txtDebugs);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setToolTipText("Stops recording Debug Data");
		
		pane.add(scrollPane, BorderLayout.CENTER);
		pane.add(btnStop, BorderLayout.SOUTH);
		
		Container clientCounts = new Container();
		clientCounts.setLayout(new GridLayout(1,2));
		
		Container left = new Container();
		left.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		left.add(new JLabel("Connected clients: "));
		left.add(clientCount);
		
		Container right = new Container();
		right.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		right.add(new JLabel("Connected player: "));
		right.add(playerCount);
		
		clientCounts.add(left);
		clientCounts.add(right);
		
		pane.add(clientCounts,BorderLayout.NORTH);
		
		frame.setContentPane(pane);
		
		btnStop.addActionListener((ActionEvent ae) -> 	{	if (recording) {
																recording = false;
																btnStop.setText("Start");
																btnStop.setToolTipText("Starts recording Debug Data");
															}
															else {
																recording = true;
																btnStop.setText("Stop");
																btnStop.setToolTipText("Stops recording Debug Data");
																addNewMessage();
															}
														});
		
		frame.setVisible(true);
	}
}