package sumoarena.algorithm;

import java.math.RoundingMode;

import com.google.common.math.DoubleMath;

import valueobjects.AccelerationVector;

public class MathHelper {
	public static double pythagoras(Integer side1, Integer side2, Integer hypotenuse){
		
		if(hypotenuse == null && side1 != null && side2 != null){
			return Math.sqrt(side1 * side1 + side2 * side2);
		}else if(side1 == null && hypotenuse != null && side2 != null){
			return Math.sqrt(hypotenuse * hypotenuse - side2 * side2);
		}else if(side2 == null && hypotenuse != null && side1 != null){
			return Math.sqrt(hypotenuse * hypotenuse - side1 * side1);
		}else throw new IllegalArgumentException("Exactly one argument has to be null");
	}
	
	public static AccelerationVector scaleVectorToLength(AccelerationVector vector, int desiredLength) {
		double currentLength = MathHelper.pythagoras(vector.getdVx(), vector.getdVy(), null);
		double resultX = vector.getdVx() / currentLength * desiredLength;
		double resultY = vector.getdVy() / currentLength * desiredLength;
		vector.setdVx(DoubleMath.roundToInt(resultX, RoundingMode.DOWN));
		vector.setdVy(DoubleMath.roundToInt(resultY, RoundingMode.DOWN));
		return vector;
	}

	public static double getVectorLength(AccelerationVector vector) {
		return MathHelper.pythagoras(vector.getdVx(), vector.getdVy(), null);
	}
}
