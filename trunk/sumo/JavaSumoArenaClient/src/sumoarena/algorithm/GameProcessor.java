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
	
	private static final int MAX_SPEED = 7;
	private RoundStartInfo roundInfo;

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
//		return attackPoint(new Point(0, 0), me);
		return attackPoint(pointToFollow, me);
	}
	
	private AccelerationVector attackPoint(Point pointToStop, Sphere me){
		int accX = 0;
		int accY = 0;
		
		System.out.println("Going to point:\t" + pointToStop);
		
		int desiredVX = pointToStop.x - me.x;
		int desiredVY = pointToStop.y - me.y;
		System.out.println("Desired vector:\t" + desiredVX + ", " + desiredVY);
		
		double c = Math.sqrt(Math.pow(desiredVX, 2) + Math.pow(desiredVY, 2));
		double cosAlpha = Math.cos(desiredVX/c);
		System.out.println("Desired cos:\t" + cosAlpha);
		
		double alpha = Math.toDegrees(Math.acos(cosAlpha));
		System.out.println("Desired alpha:\t" + alpha);
		
		int minusCurX = -me.vx;
		int minusCurY = -me.vy;
		
		//add desired + minusCur
		System.out.println("calc X:Y:\t" + accX + ":" + accY);
		accX =  desiredVX + minusCurX;
		accY =  desiredVY + minusCurY;
		
		
		return normalize(new AccelerationVector(accX, accY));
	}

	private AccelerationVector normalize(AccelerationVector accelerationVector) {
		if(accelerationVector.getdVx() * accelerationVector.getdVx() 
    			+ accelerationVector.getdVy() * accelerationVector.getdVy() 
    			> roundInfo.maxSpeedVariation * roundInfo.maxSpeedVariation)
    	{
    		int accX = accelerationVector.getdVx();
    		int accY = accelerationVector.getdVy();
    		
    		double ratio = 1;
    		if(Math.abs(accX) >= Math.abs(accY) && Math.abs(accX) > MAX_SPEED){
    			ratio = Math.abs((double)MAX_SPEED / (double)accX);
    			accX = DoubleMath.roundToInt(Math.signum(accX) * MAX_SPEED, RoundingMode.HALF_UP);
    			accY = DoubleMath.roundToInt(accY * ratio, RoundingMode.HALF_UP);
    		}else if (Math.abs(accX) < Math.abs(accY) && Math.abs(accY) > MAX_SPEED){
    			ratio = Math.abs((double)MAX_SPEED / (double)accY);
    			accY = DoubleMath.roundToInt(Math.signum(accY) * MAX_SPEED, RoundingMode.HALF_UP);
    			accX = DoubleMath.roundToInt(accX * ratio, RoundingMode.HALF_UP);
    		}
    		
    		System.out.println("limited X:Y:\t" + accX + ":" + accY + "\t ratio: " + ratio);
    		
    		return new AccelerationVector(accX, accY);
    	}
		return accelerationVector;
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
