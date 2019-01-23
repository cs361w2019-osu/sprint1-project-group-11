package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String kind;
	@JsonProperty private int size;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}

	public Ship(String kind) {
		this();
		this.kind = kind;
		if (kind.equals("MINESWEEPER")) {
			size = 2;
		}
		else if (kind.equals("DESTROYER")) {
			size = 3;
		}
		else if (kind.equals("BATTLESHIP")) {
			size = 4;
		}
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public String getKind() {
		return kind;
	}

	public void place(int row, char column, boolean isVertical) {			// Soft placement of the ship to see what squares it will occupy (does not actually place it on the board)
		if (isVertical) {
			for (int i = 0; i < size; i++) {
				occupiedSquares.add(new Square(row + i, column));
			}
		}
		else {
			for (int i = 0; i < size; i++) {
				occupiedSquares.add(new Square(row, (char) (column + i)));
			}
		}
	}

	public Result hit(int x, char y) {
		Square targetLocation = new Square(x, y);
		Result hitResult = new Result(targetLocation);

		Square attackedLocation = getOccupiedSquares().stream().filter(square -> square.equals(targetLocation)).findFirst().get();

		if (attackedLocation.getHit()) {
			hitResult.setResult(AtackStatus.INVALID);
			return hitResult;
		}
		else {
			attackedLocation.hit();
			hitResult.setShip(this);

			if (hasSunk()) {
				hitResult.setResult(AtackStatus.SUNK);
				return hitResult;
			}

			hitResult.setResult(AtackStatus.HIT);
			return hitResult;
		}
	}


	public boolean hasSunk() {
		if (getOccupiedSquares().stream().allMatch(square -> square.getHit())) {
			return true;
		}
		else {
			return false;
		}
	}

}
