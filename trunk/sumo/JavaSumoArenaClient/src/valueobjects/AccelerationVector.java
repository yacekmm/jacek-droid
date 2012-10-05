package valueobjects;

public class AccelerationVector {
	
	/**
	 * The modification to be applied to the sphere velocity vector
	 * @param dVx
	 * @param dVy
	 */
	public AccelerationVector(int dVx, int dVy) {
		this.dVx = dVx;
		this.dVy = dVy;
	}
	
	/**
	 * The modification to apply on the horizontal component of the velocity vector
	 */
	private int dVx;
	
	/**
	 * The modification to apply on the vertical component of the velocity vector
	 */
	private int dVy;
	
	public int getdVx() {
		return dVx;
	}

	public void setdVx(int dVx) {
		this.dVx = dVx;
	}

	public int getdVy() {
		return dVy;
	}

	public void setdVy(int dVy) {
		this.dVy = dVy;
	}
}
