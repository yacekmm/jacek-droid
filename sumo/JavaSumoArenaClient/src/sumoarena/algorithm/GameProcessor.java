package sumoarena.algorithm;

import java.awt.Point;
import java.awt.geom.Point2D;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundStartInfo;
import valueobjects.Sphere;

public class GameProcessor {
	
	private static final int MAX_DESIRED_LENGTH = 100;
	private double rescueThreshold = 0.65;
	private RoundStartInfo roundInfo;
	
	public AccelerationVector getAccVector(PlayingInfo playingInfo) {
		System.out.println("");
		Sphere me = findMe(playingInfo.getSpheres());
		Sphere enemy = findEnemy(playingInfo.getSpheres());
		
		Point pointToFollow = new Point(0,0);
		
		if(enemy!=null){
			pointToFollow = new Point(enemy.x, enemy.y);
			System.out.println("Chasing enemy!: " + enemy.index + ", " + pointToFollow + ", distance: " + Point2D.distance(pointToFollow.x, pointToFollow.y, me.x, me.y) + ", me: " + new Point(me.x, me.y));
		}

		double distanceFromMiddle = Point.distance(me.x, me.y, 0, 0);
		
		if(iAmTooCloseToEdge(playingInfo, distanceFromMiddle) && iAmApproachingToEdge(me)){
			System.out.println("----Trying to backout! Arena size: " + playingInfo.getArenaRadius());
			return reduceFallOffRisk(me, playingInfo);
		}else{
			AccelerationVector accVec = attackPoint(pointToFollow, me, playingInfo);
			if(iAmApproachingToEdge(me) && iWillFallOffAfterThisStep(me, accVec, playingInfo) ){
				System.out.println("\n\n!!! You are going to fall off!!! Backing off to (0,0)");
				accVec = attackPoint(new Point(0,0), me, playingInfo);
			}
			return accVec;
		}
	}

	private boolean iAmApproachingToEdge(Sphere me) {
		Point curPos = new Point(me.x, me.y);
		Point futurePos = new Point(me.x + me.vx, me.y + me.vy);
		Point center = new Point(0,0);
		
		double curDistanceToCenter = curPos.distance(center);
		double futureDistanceToCenter = futurePos.distance(center);
		
		return futureDistanceToCenter > curDistanceToCenter;
	}

	private boolean iAmTooCloseToEdge(PlayingInfo playingInfo,
			double distanceFromMiddle) {
		return distanceFromMiddle/playingInfo.getArenaRadius() > rescueThreshold;
	}
	
	private AccelerationVector reduceFallOffRisk(Sphere me, PlayingInfo playingInfo) {
		return attackPoint(new Point(0, 0), me, playingInfo);
	}

	private AccelerationVector attackPoint(Point pointToAttack, Sphere me, PlayingInfo playingInfo){
		
		System.out.println("Going to point:\t" + pointToAttack);
		
		AccelerationVector desiredAcc = new AccelerationVector(pointToAttack.x - me.x, pointToAttack.y - me.y);
		double desiredLength = MathHelper.getVectorLength(desiredAcc);
		System.out.println("Desired vector:\t" + desiredAcc.getdVx() + ", " + desiredAcc.getdVy() + "\tlength: " + desiredLength);

		//add desired - cur
		AccelerationVector accVec = MathHelper.sumVectors(desiredAcc, new AccelerationVector(-me.vx, -me.vy));
		System.out.println("calc X:Y:\t" + accVec.getdVx() + ":" + accVec.getdVy());
		
		accVec = normalize(accVec, desiredAcc, me);
		
		return accVec;
	}

	private AccelerationVector reduceDesiredAcc(Point pointToAttack,
			AccelerationVector desiredAcc, double desiredLength) {
		if(desiredLength > MAX_DESIRED_LENGTH && pointToAttack.x != 0 && pointToAttack.y != 0){
			System.out.println("@@@ reducing desired Length from: " + desiredLength);
			desiredAcc = MathHelper.scaleVectorToLength(desiredAcc, MAX_DESIRED_LENGTH);
		}
		return desiredAcc;
	}

	private boolean iWillFallOffAfterThisStep(Sphere me, AccelerationVector accVec, PlayingInfo playingInfo) {
		AccelerationVector resultVec = MathHelper.sumVectors(accVec, new AccelerationVector(me.vx, me.vy));
		Point resultPos = new Point(me.x + resultVec.getdVx(), me.y + resultVec.getdVy());
		double resultDistanceFromMiddle = resultPos.distance(new Point(0,0));
		System.out.println("CurPos:\t" + new Point(me.x,me.y) + "\tResultPos:\t" + resultPos + "\tresultDistance:\t" + resultDistanceFromMiddle + "\tArenaRadius:\t" + playingInfo.getArenaRadius());
		return resultDistanceFromMiddle > playingInfo.getArenaRadius();
	}

	private AccelerationVector normalize(AccelerationVector accVec, AccelerationVector desiredAccVec, Sphere me) {

		if(MathHelper.getVectorLength(accVec) > roundInfo.maxSpeedVariation ){
			accVec = MathHelper.scaleVectorToLength(accVec, roundInfo.maxSpeedVariation);
			double accVecLength = MathHelper.getVectorLength(accVec);
			
			AccelerationVector curVec = new AccelerationVector(me.vx, me.vy);
			double curVecLength = MathHelper.getVectorLength(curVec);

			//cosine theorem
			double cosAlpha = MathHelper.cosAngleBetweenVectors(accVec, desiredAccVec);
			double aFactor = 1;
			double bFactor = -1 * 2 * cosAlpha * curVecLength;
			double cFactor = curVecLength * curVecLength - accVecLength * accVecLength;
			System.out.println("\n\n\n");
			MathHelper.quadraticEquationSolver(aFactor, bFactor, cFactor);
    	}
		System.out.println("limited X:Y:\t" + accVec.getdVx() + ":" + accVec.getdVy() + "\tmaxSpeedVar: " + roundInfo.maxSpeedVariation);
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
