package body;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.vecmath.Point3d;

public class Calc {
	private Double rLowerArmBarrier = 0.15904698052769;
	private Double lLowerLegBarrier = 0.22741618130241666666;
	private Double lUpperArmBarrier = 0.14820139414772333333;
	
	
	public Double calcDistance(Node node1, Node node2) {
		Point3d p1 = new Point3d(node1.x, node1.y, node1.z);
		Point3d p2 = new Point3d(node2.x, node2.y, node2.z);
		return p1.distance(p2);
	}
	
	
	public Double getMedian(LinkedList<Double> list) {
		java.util.Collections.sort(list);
		if ((list.size() % 2) == 0) {
			return list.get(list.size() / 2);
		} else {
			return list.get((list.size() + 1)  / 2);
		}
	}
	
	/**
	 * Get the distance from the barrier between the two defined groups of bodysizes
	 * @param rLowerArmsMedian
	 * @param lLowerLegsMedian
	 * @param lUpperArmsMedian
	 * @return
	 */
	public Map<String, Double> getGroupDistance(Double rLowerArmsMedian, Double lLowerLegsMedian, Double lUpperArmsMedian) {
		Map<String, Double> distances = new HashMap<String, Double>();
		distances.put("rLowerArm", this.rLowerArmBarrier - rLowerArmsMedian);
		distances.put("lLowerLeg",  this.lLowerLegBarrier - lLowerLegsMedian);
		distances.put("lUpperArm", this.lUpperArmBarrier - lUpperArmsMedian);
		return distances;
	}
}
