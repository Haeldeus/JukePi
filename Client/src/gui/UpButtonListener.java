package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;

import connection.Collector;

/**
 * The ActionListener for the Up-Button.
 * @author Haeldeus
 *
 */
public class UpButtonListener implements ActionListener{

	/**
	 * The Gaplist as a selectable List.
	 */
	private JList<String> gaplist;
	
	/**
	 * The Collector that will send the Message.
	 */
	private Collector c;
	
	/**
	 * The Label that displays responses.
	 */
	private JLabel fail;
	
	/**
	 * The Frame, that contains the Fail-Label.
	 */
	private JFrame frame;
	
	/**
	 * The Constructor for the ActionListener.
	 * @param gaplist	The Gaplist as a selectable List.
	 * @param c	The Collector, that will send the Message.
 	 * @param fail	The Label, that displays Responses.
	 * @param frame	The Frame, that contains the Fail-Label.
	 */
	public UpButtonListener(JList<String> gaplist, Collector c, JLabel fail, JFrame frame) {
		this.gaplist = gaplist;
		this.c = c;
		this.fail = fail;
		this.frame = frame;
	}
	
	/**
	 * Performs the Action
	 * @param arg0 Just a stub.
	 */
	public void actionPerformed(ActionEvent arg0) {
		int index = gaplist.getSelectedIndex();
		c.moveTrackUp(index, fail, frame);
		try{Thread.sleep(100);}catch(Exception e) {}
		gaplist.setSelectedIndex(index-1);
	}
}