package body;

public class VideoProfile {
	public String rLowerArm;
	public String lLowerLeg;
	public String lUpperArm;
	
	public boolean equals(VideoProfile otherProfile) {
		if (! otherProfile.rLowerArm.equals(this.rLowerArm)) {
			return false;
		}
		if (! otherProfile.lLowerLeg.equals(this.lLowerLeg)) {
			return false;
		}
		if (! otherProfile.lUpperArm.equals(this.lUpperArm)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * See if two profiles are equal, but treat C as "Dont Care"
	 * @param other
	 * @return
	 */
	public boolean fits(VideoProfile other) {
		if (this.rLowerArm.equals(other.rLowerArm) || this.rLowerArm.equals("C") || other.rLowerArm.equals("C")) {
			if (this.lLowerLeg.equals(other.lLowerLeg) || this.lLowerLeg.equals("C") || other.lLowerLeg.equals("C")) {
				if (this.lUpperArm.equals(other.lUpperArm) || this.lUpperArm.equals("C") || other.lUpperArm.equals("C")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String print() {
		return "BodyProfile:\n" +
		"rLowerArm " + this.rLowerArm + "\n"+
		"lUpperArm " + this.lUpperArm + "\n"+
		"lLowerLeg " + this.lLowerLeg + "\n";
	}

}
