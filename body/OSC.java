package body;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JDialog;

import userIdent.DemoGui;
import userIdent.Gui;
import userIdent.Logger;
import userIdent.Options;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;


public class OSC{
	public LinkedList<Double> rLowerArms = new LinkedList<Double>();
	public LinkedList<Double> lLowerLegs = new LinkedList<Double>();
	public LinkedList<Double> lUpperArms = new LinkedList<Double>();
	
	private VideoProfile videoProfile = null;
	private OSCPortIn receiver = null;
	private Process child;
	
	public OSC() {
		//The GUI should handly this, but as there is no fitting event, cant (yet):
		//Starting an activity-indicator because starting osceleton may take some time	
		try {
			receiver = new OSCPortIn(7110);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OSCListener listener = new OSCListener() {
			int count = 0; 
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				if (Options.demo) {
					DemoGui.getInstance().poseSetActive();
				}
				if (count < 3000) { 
					Node node = new Node(message); 
					Body.getInstance().setNode(node);
					count++;
					if (count%15==0) {
						rLowerArms.add(Body.getInstance().getRLowerArmLength());
						lLowerLegs.add(Body.getInstance().getLLowerLegLength());
						lUpperArms.add(Body.getInstance().getLUpperArmLength());
						System.out.println("calculating");
					}
				} else {
					Calc calc = new Calc();
					Double rLowerArmsMedian = calc.getMedian(rLowerArms);
					Double lLowerLegsMedian = calc.getMedian(lLowerLegs);
					Double lUpperArmsMedian = calc.getMedian(lUpperArms);
					
					Logger log = new Logger();
					log.log("rLowerArmMedian: " +rLowerArmsMedian +"\n" +
							"lUpperArmMedian: " +lUpperArmsMedian +"\n" +
							"lLowerLegMedian: "+ lLowerLegsMedian +"\n");
					
					Map<String, Double> distances = calc.getGroupDistance(rLowerArmsMedian, lLowerLegsMedian, lUpperArmsMedian);
					VideoProfile tempProfile = new VideoProfile();
					tempProfile.lLowerLeg = this.getGroup(distances.get("lLowerLeg"));
					tempProfile.rLowerArm = this.getGroup(distances.get("rLowerArm"));
					tempProfile.lUpperArm = this.getGroup(distances.get("lUpperArm"));
					videoProfile = tempProfile;
					System.out.println("done");
					child.destroy();
				}
			}
			
			private String getGroup(Double distance) {
				if (Math.abs(distance) < 0.01 ) {
					return "C";
				} else {
					if (distance > 0) {
						return "B";
					} else {
						return "A";
					}
				}
			}
		};
		receiver.addListener("/joint", listener);
		receiver.startListening();
		
		String command = "./osceleton -w";
		Logger logger = new Logger();
		try {
			this.child = Runtime.getRuntime().exec(command);
			InputStream in = child.getInputStream();
		    int c;
		    while ((c = in.read()) != -1 && (videoProfile = this.getProfile()) == null) {
		       logger.log((char)c);
		    }
		    in.close();	
		} catch (IOException e) { }
		finally {
			receiver.stopListening();
			receiver.close();
		}
	}
	
	
	public VideoProfile getProfile() {
		return this.videoProfile;
	}
}
