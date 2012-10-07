package sumoarena.algorithm;

import java.awt.Point;
import java.awt.geom.Point2D;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundStartInfo;
import valueobjects.Sphere;

public class GameProcessor {
	
	private static final double RESCUE_THRESHOLD = 0.65;
	private RoundStartInfo roundInfo;
	
	private boolean shouldIRescue = false;

	public AccelerationVector getAccVector(PlayingInfo playingInfo) {
		System.out.println("");
		Sphere me = findMe(playingInfo.getSpheres());
		Sphere enemy = findEnemy(playingInfo.getSpheres());
		
		Point pointToFollow = new Point(50,50);
		
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
		AccelerationVector desiredAcc = new AccelerationVector(desiredVX, desiredVY);
		System.out.println("Desired vector:\t" + desiredAcc.getdVx() + ", " + desiredAcc.getdVy() + "\tlength: " + MathHelper.getVectorLength(desiredAcc));
//		desiredAcc = MathHelper.scaleVectorToLength(desiredAcc, roundInfo.maxSpeedVariation);
//		System.out.println("Scaled desired:\t" + desiredAcc.getdVx() + ", " + desiredAcc.getdVy() + "\tlength: " + MathHelper.getVectorLength(desiredAcc));
		
		//add desired - cur
		accX =  desiredAcc.getdVx() - me.vx;
		accY =  desiredAcc.getdVy() - me.vy;
		System.out.println("calc X:Y:\t" + accX + ":" + accY);
		
		return normalize(new AccelerationVector(accX, accY), new AccelerationVector(desiredVX, desiredVY));
	}



	private AccelerationVector normalize(AccelerationVector accVec, AccelerationVector desiredAccVec) {
		double ratio = 1;

		if(MathHelper.getVectorLength(accVec) > roundInfo.maxSpeedVariation ){
			accVec = MathHelper.scaleVectorToLength(accVec, roundInfo.maxSpeedVariation);
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
