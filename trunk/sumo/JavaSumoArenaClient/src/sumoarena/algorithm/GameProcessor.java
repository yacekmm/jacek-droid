package sumoarena.algorithm;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.math.RoundingMode;

import com.google.common.math.DoubleMath;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundStartInfo;
import valueobjects.Sphere;

public class GameProcessor {

	private static final double RESCUE_THRESHOLD = 0.60;
	private static final int MAX_NORMALIZATION_TRIES = 10;
	private static final int MAX_DESIRED_SPEED = 100;
	
	private RoundStartInfo roundInfo;
	private PlayingInfo playingInfo;
	private int normalizationCounter;
	
	private void resetVariables(PlayingInfo _playingInfo) {
		this.playingInfo = _playingInfo;
		normalizationCounter = 0;
	}

	public AccelerationVector getAccVector(PlayingInfo _playingInfo) {
		resetVariables(_playingInfo);
		
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
			return reduceFallOffRisk(me, playingInfo);
		}else{
			return attackPoint(pointToFollow, me, playingInfo, MAX_DESIRED_SPEED);
		}
	}

	private AccelerationVector attackPoint(Point pointToAttack, Sphere me, PlayingInfo playingInfo, int targetDesVecLenth){
		System.out.println("Going to point:\t" + pointToAttack);

		AccelerationVector desiredAcc = new AccelerationVector(pointToAttack.x - me.x, pointToAttack.y - me.y);
		if(targetDesVecLenth > 0){
			desiredAcc = MathHelper.scaleVectorToLength(desiredAcc, targetDesVecLenth);
		}
		System.out.println("Desired vector:\t" + desiredAcc.getdVx() + ", " + desiredAcc.getdVy() + "\tlength: " + MathHelper.getVectorLength(desiredAcc));

		//accVec = desired - cur
		AccelerationVector accVec = MathHelper.sumVectors(desiredAcc, new AccelerationVector(-me.vx, -me.vy));
		System.out.println("calc X:Y:\t" + accVec.getdVx() + ":" + accVec.getdVy());

		accVec = normalizeSpeed(accVec, desiredAcc, me, pointToAttack, playingInfo);

		return accVec;
	}

	private AccelerationVector normalizeSpeed(AccelerationVector accVec, AccelerationVector desiredAccVec, Sphere me, Point pointToAttack, PlayingInfo playingInfo) {

		if(MathHelper.getVectorLength(accVec) > roundInfo.maxSpeedVariation ){
			accVec = MathHelper.scaleVectorToLength(accVec, roundInfo.maxSpeedVariation);
			double accVecLength = MathHelper.getVectorLength(accVec);

			AccelerationVector curVec = new AccelerationVector(me.vx, me.vy);
			double curVecLength = MathHelper.getVectorLength(curVec);

			double targetDesVecLenth = getTargetDesVecLength(desiredAccVec, accVecLength, curVec, curVecLength);
			if(targetDesVecLenth > 0 && normalizationCounter < MAX_NORMALIZATION_TRIES){
				normalizationCounter++;
				int targetDesVecLenth2 = DoubleMath.roundToInt(targetDesVecLenth, RoundingMode.DOWN) - 1;
				System.out.println("***scaling desiredVector to length:\t" + targetDesVecLenth2);
				accVec = attackPoint(pointToAttack, me, playingInfo, targetDesVecLenth2);
			}
		}
		System.out.println("limited X:Y:\t" + accVec.getdVx() + ":" + accVec.getdVy() + "\tmaxSpeedVar: " + roundInfo.maxSpeedVariation);
		return accVec;
	}

	private double getTargetDesVecLength(AccelerationVector desiredAccVec,
			double accVecLength, AccelerationVector curVec, double curVecLength) {
		//cosine theorem
		double cosAlpha = MathHelper.cosAngleBetweenVectors(curVec, desiredAccVec);
		double aFactor = 1;
		double bFactor = -1 * 2 * cosAlpha * curVecLength;
		double cFactor = curVecLength * curVecLength - accVecLength * accVecLength;
		System.out.println("\n\n\n");
		return MathHelper.quadraticEquationSolver(aFactor, bFactor, cFactor);
	}

	private boolean iAmApproachingToEdge(Sphere me) {
		Point curPos = new Point(me.x, me.y);
		Point futurePos = new Point(me.x + me.vx, me.y + me.vy);
		Point center = new Point(0,0);

		return futurePos.distance(center) > curPos.distance(center);
	}

	private boolean iAmTooCloseToEdge(PlayingInfo playingInfo,
			double distanceFromMiddle) {
		return distanceFromMiddle/playingInfo.getArenaRadius() > RESCUE_THRESHOLD;
	}

	private AccelerationVector reduceFallOffRisk(Sphere me, PlayingInfo playingInfo) {
		System.out.println("----Trying to backout! Arena size: " + playingInfo.getArenaRadius());
		return attackPoint(new Point(0, 0), me, playingInfo, MAX_DESIRED_SPEED*2);
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
