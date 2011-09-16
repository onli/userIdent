package userIdent;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import audio.SoundPreparer;

public class Dialogue {
	public JDialog showActivityBar(String title, JFrame frame) {
		final JDialog dlg = new JDialog(frame, title, true);
		JProgressBar dpb = new JProgressBar(0, 500);
	    dpb.setVisible(true);
	    dlg.add(BorderLayout.CENTER, dpb);
	    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    dlg.setSize(300, 75);
	    dlg.setLocationRelativeTo(frame);
	    dpb.setIndeterminate(true);
		Thread t = new Thread(new Runnable() {
	      public void run() {
	        dlg.setVisible(true);
	      }
	    });
	    t.start();
	    return dlg;
	}
	
	public File soundDialog(JFrame frame, boolean train) {
		Object[] options = {"Kinect",
							"Record",
					        	"Select"};
								int n = JOptionPane.showOptionDialog(frame,
								"Record via kinect, microphone or select a .wav?",
								"Sound Source Selection",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
		File file = null;
		if (n == 2) {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
			}
		} else if (n==0){
			SoundPreparer sp = new SoundPreparer();
			JDialog dlg = this.showActivityBar("Recording Audio", frame);
			file = sp.recordKinect(train); 
			dlg.dispose();
		} else if (n==1){
			SoundPreparer sp = new SoundPreparer();
			JDialog dlg = this.showActivityBar("Recording Audio", frame);
			file = sp.record(train); 
			dlg.dispose();
		} else {
			
		}
		return file;
	}
}
