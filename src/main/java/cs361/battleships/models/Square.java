package cs361.battleships.models;

@SuppressWarnings("unused")
public class Square {

	private int row;
	private char column;
	private boolean hit;
	private boolean isCaptain = false;
	private boolean submerged = false;

	public Square() {
		hit = false;
	}

	public Square(int row, char column) {
		this();
		this.row = row;
		this.column = column;
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void hit() {
		hit = true;
	}

	public boolean getHit() {
		return hit;
	}

	public void setCaptain() {
		isCaptain = true;
	}

	public boolean getisCaptain() {
		return isCaptain;
	}

	public void setSubmerged() {
		submerged = true;
	}

	public boolean getSubmerged() {
		return submerged;
	}

	public boolean equals(Square other) {
		if (other.getRow() == this.row && other.getColumn() == this.column) {
			return true;
		}
		else {
			return false;
		}
	}

}
