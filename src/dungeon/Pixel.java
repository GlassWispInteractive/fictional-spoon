package dungeon;

class Pixel {
	private int value;
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	private int x, y;
	
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
		this.value = 0;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x - the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y - the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean dead_end() {
		return false;
	}
}
