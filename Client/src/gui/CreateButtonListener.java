package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import connection.Collector;

/**
 * The ActionListener for the CreateButton.
 * @author Haeldeus.
 *
 */
public class CreateButtonListener implements ActionListener{

	/**
	 * The Collector that will send the Messages.
	 */
	private Collector c;
	
	/**
	 * The TextField, that contains the Name of the Gaplist.
	 */
	private JTextField tf;
	
	/**
	 * The Label, that will display possible Messages.
	 */
	private JLabel fail;
	
	/**
	 * The Frame of the Application.
	 */
	private JFrame frame;
	
	/**
	 * The Constructor for the ActionListener.
	 * @param c	The Collector that will send the Messages.
	 * @param tf	The TextField, that contains the Name for the List.
	 * @param fail	The Label, that will display possible Messages.
	 * @param frame The Frame, that contains the Fail-Label.
	 */
	public CreateButtonListener(Collector c, JTextField tf, JLabel fail, JFrame frame) {
		this.c = c;
		this.tf = tf;
		this.fail = fail;
		this.frame = frame;
	}

	/**
	 * Performs the Action.
	 * @param arg0 Just a stub.
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (tf.getText() != null && !tf.getText().equals("")) {
			String s = tf.getText();
			s = s.replaceAll("�", "ae");
			s = s.replaceAll("�", "ae");
			s = s.replaceAll("�", "ue");
			s = s.replaceAll("�", "ue");
			s = s.replaceAll("�", "oe");
			s = s.replaceAll("�", "oe");
			s = s.replaceAll("�", "ss");
			c.createGaplist(s, fail, frame);
		}
		else {
			fail.setText("Please insert a Name for the new Gaplist first");
			fail.setVerticalAlignment(JLabel.CENTER);
			fail.setHorizontalAlignment(JLabel.CENTER);
			fail.setVisible(true);
			new util.ShowLabelThread(fail, frame).start();
		}
	}
}
