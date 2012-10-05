package sumoarena.algorithm;

public class MathHelper {
	public static Double pythagoras(Integer side1, Integer side2, Integer hypotenuse){
		
		if(hypotenuse == null && side1 != null && side2 != null){
			return Math.sqrt(side1 * side1 + side2 * side2);
		}else if(side1 == null && hypotenuse != null && side2 != null){
			return Math.sqrt(hypotenuse * hypotenuse - side2 * side2);
		}else if(side2 == null && hypotenuse != null && side1 != null){
			return Math.sqrt(hypotenuse * hypotenuse - side1 * side1);
		}else throw new IllegalArgumentException("Exactly one argument has to be null");
	}
}
