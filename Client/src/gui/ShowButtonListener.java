package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JList;

import util.ShowLabelThread;
import connection.Collector;

/**
 * The ActionListener, that will perform Actions for the ShowButton.
 * @author Haeldeus
 *
 */
public class ShowButtonListener implements ActionListener{

	/**
	 * The Collector, that will send the Messages.
	 */
	private Collector c;
	
	/**
	 * The Gaplists, as a selectable List.
	 */
	private JList<String> gaplists;
	
	/**
	 * The Label, that will display possible Messages.
	 */
	private JLabel fail;
	
	/**
	 * The Constructor for the ActionListener.
	 * @param c	The Collector, that will send the Messages.
	 * @param gaplists	The Gaplists as a selectable List.
	 * @param fail	The Label that will display possible Messages.
	 */
	public ShowButtonListener(Collector c, JList<String> gaplists, JLabel fail) {
		this.c = c;
		this.gaplists = gaplists;
		this.fail = fail;
	}

	/**
	 * Performs the Action.
	 * @param arg0 Just a stub.
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (gaplists.getSelectedIndex() > -1) {
			fail.setText("Pending Server.");
			fail.setVerticalAlignment(JLabel.CENTER);
			fail.setHorizontalAlignment(JLabel.CENTER);
			fail.setVisible(true);
			new ShowLabelThread(fail, null).start();
			c.fillContentModel(gaplists.getSelectedValue(), fail);
		}
		else {
			fail.setText("Please select a Gaplist first.");
			fail.setVerticalAlignment(JLabel.CENTER);
			fail.setHorizontalAlignment(JLabel.CENTER);
			fail.setVisible(true);
			new ShowLabelThread(fail, null).start();
		}
	}
}
