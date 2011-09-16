package body;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;



public class Body {
	private static Body instance = new Body();
	private Map<String, Node> nodes = new HashMap<String, Node>();
	private Map<String, Double> lengths = new HashMap<String, Double>();
	
	public static Body getInstance() {
		return instance;
	}
	
	private Body() { }
	
	/**
	 * Set a joint to this body-model if not already set
	 * @param Node node
	 */
	public void setNode(Node node) {
		nodes.put(node.name, node);
		this.log(node);
	}
	
	private boolean isStored(Node node) {
		return nodes.containsKey(node.name);
	}
	
	private boolean isStored(String name) {
		return nodes.containsKey(name);
	}
	
	private Double setLength(String id, String nodeName1, String nodeName2) {
		if (isStored(nodeName1) && isStored(nodeName2)) {
			Calc calc = new Calc();
			lengths.put(id, calc.calcDistance(nodes.get(nodeName1), nodes.get(nodeName2)));
			return lengths.get(id);
		} else {
			return 0.0;
		}
	}
	
	
	/**
	 * Calculate the length of the upper right arm if the the necessary node r_elbow and r_shoulder are set
	 * @return Double the length of the upper right arm OR 0.0
	 */
	public Double getRUpperArmLength() {
		return this.setLength("rUpperArm", "r_elbow", "r_shoulder");
	}
	
	/**
	 * Calculate the length of the lower right arm if the the necessary node r_elbow and r_hand are set
	 * @return Double the length of the upper right arm OR 0.0
	 */
	public Double getRLowerArmLength() {
		return this.setLength("rLowerArm", "r_elbow", "r_hand");
	}
	
	public Double getRUpperLegLength() {
		return this.setLength("rUpperLeg", "r_hip", "r_knee");
	}
	
	public Double getRLowerLegLength() {
		return this.setLength("rLowerLeg", "r_foot", "r_knee");
	}
	/**
	 * Calculate the length of the upper right arm if the the necessary node r_elbow and r_shoulder are set
	 * @return Double the length of the upper right arm OR 0.0
	 */
	public Double getLUpperArmLength() {
		return this.setLength("lUpperArm", "l_elbow", "l_shoulder");
	}
	
	/**
	 * Calculate the length of the lower right arm if the the necessary node r_elbow and r_hand are set
	 * @return Double the length of the upper right arm OR 0.0
	 */
	public Double getLLowerArmLength() {
		return this.setLength("lLowerArm", "l_elbow", "l_hand");
	}
	
	public Double getLUpperLegLength() {
		return this.setLength("lUpperLeg", "l_hip", "l_knee");
	}
	
	public Double getLLowerLegLength() {
		return this.setLength("lLowerLeg", "l_foot", "l_knee");
	}
	public Double getRTorsoHipLength() {
		return this.setLength("rTorsoHip", "r_hip", "torso");
	}
	public Double getLTorsoHipLength() {
		return this.setLength("lTorsoHip", "l_hip", "torso");
	}
	
	public Double getTorsoNeckLength() {
		return this.setLength("torsoNeck", "torso", "neck");
	}
	public Double getNeckHeadLength() {
		return this.setLength("neckHead", "neck", "head");
	}
	
	private void log(Node node) {
		try {
			FileWriter fstream = new FileWriter("/home/onli/nodes.log", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.append(node.name +" " +node.x+" "+node.y+" "+node.z +"\n");
			out.close();
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void logLengths() {
		this.getRLowerArmLength();
		this.getRLowerLegLength();
		this.getRTorsoHipLength();
		this.getRUpperArmLength();
		this.getRUpperLegLength();
		this.getLLowerArmLength();
		this.getLLowerLegLength();
		this.getLTorsoHipLength();
		this.getLUpperArmLength();
		this.getLUpperLegLength();
		this.getTorsoNeckLength();
		this.getNeckHeadLength();
		try {
			FileWriter fstream = new FileWriter("/home/onli/nodes.log", true);
			BufferedWriter out = new BufferedWriter(fstream);
			for(Map.Entry<String,Double> entry: this.lengths.entrySet()){
					if (entry.getValue() > 0.0) {
						out.append(entry.getKey() +" "+ entry.getValue() +"\n");
					}
			}
			out.close();
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
}
