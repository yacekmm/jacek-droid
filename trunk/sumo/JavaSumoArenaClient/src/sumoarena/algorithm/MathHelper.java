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
	
	public static double angleBetweenVectors(AccelerationVector vec1, AccelerationVector vec2){
		double cosAlpha = MathHelper.cosAngleBetweenVectors(vec1, vec2);
		return Math.toDegrees(Math.acos(cosAlpha));
	}
	
	public static AccelerationVector sumVectors(AccelerationVector vec1, AccelerationVector vec2){
		return new AccelerationVector(vec1.getdVx() + vec2.getdVx(), vec1.getdVy() + vec2.getdVy());
	}

	public static double cosAngleBetweenVectors(AccelerationVector vec1, AccelerationVector vec2) {
		//V1.x * V2.x + V1.y * V2.y = L1 * L2 * cos alfa
		double numerator = vec1.getdVx() * vec2.getdVx() + vec1.getdVy() * vec2.getdVy();
		double denominator = MathHelper.getVectorLength(vec1) * MathHelper.getVectorLength(vec2);
		double cosAlpha = numerator / denominator;
		return cosAlpha;
	}
	
	public static double quadraticEquationSolver(double a, double b, double c){
		double discr, root1, root2;

	     // Solve the discriminant (SQRT (b^2 - 4ac)
	     discr = Math.sqrt((b * b) - (4 * a * c));
//	     System.out.println("Discriminant = " + discr);

	     if(Double.isNaN(discr)){
//	        System.out.println("Equation has no roots");
	        return -1;
	     }
	     
	     if(discr > 0){
	        root1 = (-b + discr)/2 * a;
	        root2 = (-b - discr)/2 * a;
//	        System.out.println("\n\nEquation has 2 roots:\t" + root1 + "\t" + root2);
	        return Math.max(root1, root2);
	      }

	      if(discr == 0){
	        root1 = (-b + discr)/2 * a;
//	        System.out.println("Equation has 1 root:\t" + root1);
	        return root1;
	      }

	       if(discr < 0){
//	         System.out.println("Equation has imaginary roots");
	         return -1;
	       }
	       
	       return -1;
	}
}
