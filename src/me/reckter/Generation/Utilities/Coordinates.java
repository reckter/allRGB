package me.reckter.Generation.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/4/13
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Coordinates {
	private int x;
	private int y;
	private int z;

	/**
	 * This works in 3D as in 2D.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public Coordinates(int x, int y, int z) {
		this(x, y);
		this.z = z;
	}

	/**
	 * 2D Constructor
	 *
	 * @param x
	 * @param y
	 */
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns true if Coordinate are the same
	 *
	 * @param to
	 * @return
	 */
	public boolean equal(Coordinates to) {
		return this.equal(to.getX(), to.getY(), to.getZ());
	}

	/**
	 * Returns true if Coordinates are the same
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public boolean equal(int x, int y, int z) {
		return this.x == x && this.y == y && this.z == z;
	}

	/**
	 * Returns true if Coordinates are the same 2D)
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean equal(int x, int y) {
		return this.x == x && this.y == y;
	}

	/**
	 * Returns the distance to a Coordinate
	 *
	 * @param from
	 * @return
	 */
	public double getDistance(Coordinates from) {
		return Math.sqrt(this.getSquaredDistance(from));
	}

	/**
	 * Returns the squared distance to a Coordinate
	 *
	 * @param from
	 * @return
	 */
	public double getSquaredDistance(Coordinates from) {
		int disX, disY, disZ;
		disX = from.getX() - this.x;
		disY = from.getY() - this.y;
		disZ = from.getZ() - this.z;

		return disX * disX + disY * disY + disZ * disZ;
	}


	/**
	 * return this + to
	 *
	 * @param to
	 * @return
	 */
	public Coordinates add(Coordinates to) {
		return this.add(to.getX(), to.getY(), to.getZ());
	}

	/**
	 * returns this + (x,y,z)
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Coordinates add(int x, int y, int z) {
		return new Coordinates(this.x + x, this.y + y, this.z + z);

	}

	/**
	 * returns this + (x,y) (2D)
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public Coordinates add(int x, int y) {
		return new Coordinates(this.x + x, this.y + y);

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}


	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + z;
		return result;
	}
}
