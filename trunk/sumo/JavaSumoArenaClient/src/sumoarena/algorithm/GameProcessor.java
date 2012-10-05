package sumoarena.algorithm;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.math.RoundingMode;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundStartInfo;
import valueobjects.Sphere;

import com.google.common.math.DoubleMath;

public class GameProcessor {
	
	private static final int MAX_SPEED = 15;
	private static final double RESCUE_THRESHOLD = 0.65;
	private RoundStartInfo roundInfo;
	
	private boolean shouldIRescue = false;

	public AccelerationVector getAccVector(PlayingInfo playingInfo) {
		System.out.println("");
		Sphere me = findMe(playingInfo.getSpheres());
		Sphere enemy = findEnemy(playingInfo.getSpheres());
		
		Point pointToFollow = new Point(100,100);
		
		if(enemy!=null){
			pointToFollow.x = enemy.x;
			pointToFollow.y = enemy.y;
			System.out.println("Chasing enemy!: " + enemy.index + ", " + pointToFollow + ", distance: " + Point2D.distance(pointToFollow.x, pointToFollow.y, me.x, me.y) + ", me: " + new Point(me.x, me.y));
		}

		double distanceFromMiddle = Point.distance(me.x, me.y, 0, 0);
		shouldIRescue = distanceFromMiddle/playingInfo.getArenaRadius() > RESCUE_THRESHOLD;
		
		if(shouldIRescue){
			return reduceFallOffRisk(me);
		}else
			return attackPoint(pointToFollow, me);
	}
	
	private AccelerationVector reduceFallOffRisk(Sphere me) {
		System.out.println("----Trying to backout!");
		return attackPoint(new Point(0, 0), me);
	}

	private AccelerationVector attackPoint(Point pointToStop, Sphere me){
		int accX = 0;
		int accY = 0;
		
		System.out.println("Going to point:\t" + pointToStop);
		
		int desiredVX = pointToStop.x - me.x;
		int desiredVY = pointToStop.y - me.y;
		System.out.println("Desired vector:\t" + desiredVX + ", " + desiredVY);
		
		int minusCurX = -me.vx;
		int minusCurY = -me.vy;
		
		//add desired + minusCur
		System.out.println("calc X:Y:\t" + accX + ":" + accY);
		accX =  desiredVX + minusCurX;
		accY =  desiredVY + minusCurY;
		
		
		return normalize(new AccelerationVector(accX, accY), new AccelerationVector(desiredVX, desiredVY), me);
	}

	private AccelerationVector normalize(AccelerationVector accVec, AccelerationVector desiredAccVec, Sphere me) {
		double ratio = 1;

		if(accVec.getdVx() * accVec.getdVx() 
    			+ accVec.getdVy() * accVec.getdVy() 
    			> roundInfo.maxSpeedVariation * roundInfo.maxSpeedVariation)
    	{
    		int accX = accVec.getdVx();
    		int accY = accVec.getdVy();
    		
    		if(Math.abs(accX) >= Math.abs(accY) && Math.abs(accX) > MAX_SPEED){
    			ratio = Math.abs((double)MAX_SPEED / (double)accX);
    			accX = DoubleMath.roundToInt(Math.signum(accX) * MAX_SPEED, RoundingMode.HALF_UP);
    			accY = DoubleMath.roundToInt(accY * ratio, RoundingMode.HALF_UP);
    		}else if (Math.abs(accX) < Math.abs(accY) && Math.abs(accY) > MAX_SPEED){
    			ratio = Math.abs((double)MAX_SPEED / (double)accY);
    			accY = DoubleMath.roundToInt(Math.signum(accY) * MAX_SPEED, RoundingMode.HALF_UP);
    			accX = DoubleMath.roundToInt(accX * ratio, RoundingMode.HALF_UP);
    		}
    		
    		accVec.setdVx(accX);
    		accVec.setdVy(accY);
    	}
		System.out.println("limited X:Y:\t" + accVec.getdVx() + ":" + accVec.getdVy() + "\t ratio: " + ratio + "\tmaxSpeedVar: " + roundInfo.maxSpeedVariation);
		return accVec;
	}

	private Sphere findEnemy(Sphere[] spheres) {
		for (Sphere sphere : spheres) {
			if(sphere.index != getRoundInfo().myIndex && sphere.inArena && sphere.team != getRoundInfo().myTeamIndex)
				return sphere;
		}
		return null;
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
