package sumoarena.algorithm;

import java.awt.geom.Point2D;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundStartInfo;
import valueobjects.Sphere;

public class GameProcessor2 {
	
	private RoundStartInfo roundInfo;

	public AccelerationVector getAccVector(PlayingInfo playingInfo) {
		Sphere me = findMe(playingInfo.getSpheres());
		
		int accX = 1;
		int accY = 1;
		double distanceToMiddle = Point2D.distance(0, 0, me.x, me.y);
		
		if(me.x > 0)
			accX = -1;
		
		if(me.y > 0)
			accY = -1;
		
		return new AccelerationVector(accX, accY);
	}

	private Sphere findMe(Sphere[] spheres) {
		return spheres[getRoundInfo().myIndex];
	}

	public RoundStartInfo getRoundInfo() {
		return roundInfo;
	}

	public void setRoundInfo(RoundStartInfo roundInfo) {
		this.roundInfo = roundInfo;
	}

}
