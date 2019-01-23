package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String kind;
	@JsonProperty private int size;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind)
	{
		this();
		this.kind = kind;
		if (kind == "Minesweeper")
		{
			size = 2;
		}
		else if ( kind == "Destroyer")
		{
			size = 3;
		}
		else if (kind == "Battleship")
		{
			size = 4;
		}
	}

	public List<Square> getOccupiedSquares() 
	{
		return occupiedSquares;
	}
	
	public String getKind() {
		return kind;
	}
	
	public String getSize() {
		return size;	
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

	public Result hit(int x, char y) {							// Hits the ship with an attack and de-occupies the square at the attack location
		Result hitResult = new Result(new Square(x, y));

		occupiedSquares = occupiedSquares.stream().filter(square -> square.getRow() != x && square.getColumn() != y).collect(Collectors.toList());		// Removes square that was attacked

		if (occupiedSquares.size() == 0) {			// If the ship no longer occupies any squares, then it was sunk
			hitResult.setResult(AtackStatus.SUNK);
		}
		else {
			hitResult.setResult(AtackStatus.HIT);
		}

		return hitResult;
	}
}
