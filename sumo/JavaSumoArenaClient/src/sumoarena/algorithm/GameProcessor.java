package sumoarena.algorithm;

import java.awt.Point;
import java.math.RoundingMode;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundStartInfo;
import valueobjects.Sphere;

import com.google.common.math.DoubleMath;

public class GameProcessor {

	private static final double RESCUE_THRESHOLD_ON = 0.6;
	private static final double RESCUE_THRESHOLD_OFF = RESCUE_THRESHOLD_ON;
	private static final int MAX_NORMALIZATION_TRIES = 10;
	private static final int MAX_DESIRED_SPEED = 90;
	private static final Point ARENA_CENTER = new Point(0,0);
	private static final double PREDICTION_FACTOR = 1;
	
	private RoundStartInfo roundInfo;
	private PlayingInfo playingInfo;
	private int normalizationCounter;
	private Sphere me;
	Point curPos;
	AccelerationVector curVec;
	private boolean rescueModeOn = false;
	
	private void resetVariables(PlayingInfo _playingInfo) {
		this.playingInfo = _playingInfo;
		normalizationCounter = 0;
		me = findMe(playingInfo.getSpheres());
		curPos = new Point(me.x, me.y);
		curVec = new AccelerationVector(me.vx, me.vy);
	}

	public AccelerationVector getAccVector(PlayingInfo _playingInfo) {
		
		System.out.println("");
		resetVariables(_playingInfo);

		Sphere enemy = findEnemy(playingInfo.getSpheres());

		Point pointToFollow = ARENA_CENTER;

		if(enemy!=null){
			pointToFollow = new Point(enemy.x, enemy.y);
			pointToFollow = new Point(	enemy.x + DoubleMath.roundToInt(enemy.vx * PREDICTION_FACTOR, RoundingMode.DOWN), 
										enemy.y + DoubleMath.roundToInt(enemy.vy * PREDICTION_FACTOR, RoundingMode.DOWN));
			System.out.println("Chasing enemy!: " + enemy.index + ", " + pointToFollow + ", distance: " +  curPos.distance(pointToFollow) + ", me: " + curPos);
		}

		double distanceFromMiddle = curPos.distance(ARENA_CENTER);

		if(rescueModeOn){
			rescueModeOn = iAmTooCloseToEdge(distanceFromMiddle, RESCUE_THRESHOLD_OFF);
		}
		
		if(iAmTooCloseToEdge(distanceFromMiddle, RESCUE_THRESHOLD_ON) && iAmApproachingToEdge(me) || rescueModeOn ){
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
		
//		double desVecLength = MathHelper.getVectorLength(desiredAcc) - 1;
//		if(desVecLength < roundInfo.maxSpeedVariation){
//			System.out.println("\n\nFUCK! desVecLen:\t" + desVecLength + "\n");
//		}

		return accVec;
	}

	private AccelerationVector normalizeSpeed(AccelerationVector accVec, AccelerationVector desiredAccVec, Sphere me, Point pointToAttack, PlayingInfo playingInfo) {

		if(MathHelper.getVectorLength(accVec) > roundInfo.maxSpeedVariation ){
			accVec = MathHelper.scaleVectorToLength(accVec, roundInfo.maxSpeedVariation);

			double targetDesVecLenth = getTargetDesVecLength(desiredAccVec, accVec);
			if(targetDesVecLenth > 0 && normalizationCounter < MAX_NORMALIZATION_TRIES){
				normalizationCounter++;
				int targetDesVecLenthInt = DoubleMath.roundToInt(targetDesVecLenth, RoundingMode.DOWN) - 1;
				System.out.println("***scaling desiredVector to length:\t" + targetDesVecLenthInt);
				accVec = attackPoint(pointToAttack, me, playingInfo, targetDesVecLenthInt);
			}
		}
		System.out.println("limited X:Y:\t" + accVec.getdVx() + ":" + accVec.getdVy() + "\tmaxSpeedVar: " + roundInfo.maxSpeedVariation);
		return accVec;
	}

	private double getTargetDesVecLength(AccelerationVector desiredAccVec,
			AccelerationVector accVec) {
		
		double curVecLength = MathHelper.getVectorLength(curVec);
		double accVecLength = MathHelper.getVectorLength(accVec);
		
		//cosine theorem
		double cosAlpha = MathHelper.cosAngleBetweenVectors(curVec, desiredAccVec);
		double aFactor = 1;
		double bFactor = -1 * 2 * cosAlpha * curVecLength;
		double cFactor = curVecLength * curVecLength - accVecLength * accVecLength;
		return MathHelper.quadraticEquationSolver(aFactor, bFactor, cFactor);
	}

	private boolean iAmApproachingToEdge(Sphere me) {
		Point futurePos = new Point(me.x + me.vx, me.y + me.vy);
		return futurePos.distance(ARENA_CENTER) > curPos.distance(ARENA_CENTER);
	}

	private boolean iAmTooCloseToEdge(double distanceFromMiddle, double rescueThreshold) {
		return distanceFromMiddle/playingInfo.getArenaRadius() > rescueThreshold;
	}

	private AccelerationVector reduceFallOffRisk(Sphere me, PlayingInfo playingInfo) {
		System.out.println("----Trying to backout! Arena size: " + playingInfo.getArenaRadius());
		rescueModeOn = true;
		return attackPoint(ARENA_CENTER, me, playingInfo, MAX_DESIRED_SPEED);
	}

	private Sphere findEnemy(Sphere[] spheres) {
		for (Sphere sphere : spheres) {
			if(sphere.index != roundInfo.myIndex && sphere.inArena && sphere.team != playingInfo.getSpheres()[roundInfo.myIndex].team){
				System.out.println("^^^^^attacking sphere: team:\t" + sphere.team + ", index:\t" + sphere.index + ", my team:\t" + roundInfo.myTeamIndex + ", myIndex:\t" + roundInfo.myIndex);
				return sphere;
			}
		}
		return null;
	}

	private Sphere findMe(Sphere[] spheres) {
		return spheres[roundInfo.myIndex];
	}

	public void setRoundInfo(RoundStartInfo roundStartInfo) {
		this.roundInfo = roundStartInfo;
		// TODO Auto-generated method stub
		
	}
}
