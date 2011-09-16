package body;
import com.illposed.osc.OSCMessage;


public class Node {
	public String name;
	public Float x;
	public Float y;
	public Float z;
	
	/**
	 * Set the joint as a node
	 * @param OSCMessage message Contains as arguments the name and the coordinates of the joint 
	 */
	public Node (OSCMessage message) {
		Object[] args = message.getArguments();
		this.name = (String)args[0];
		this.x = (Float)args[2];
		this.y = (Float)args[3];
		this.z = (Float)args[4];
	}
}
