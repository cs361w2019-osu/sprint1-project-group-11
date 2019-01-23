package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		if (ships.size() >= 3) {			// Check to see if we have already placed at least 3 ships
			return false;
		}
		else {
			for (int i = 0; i < ships.size(); i++) {			// Check to see if we have already placed a ship of same type
				if (ships.get(i).getKind().equals(ship.getKind())) {
					return false;
				}
			}

			Ship newShip = new Ship(ship.getKind());
			newShip.place(x, y, isVertical);

			List<Square> newShipOccupiedSquares = newShip.getOccupiedSquares();
			for (int i = 0; i < newShipOccupiedSquares.size(); i++) {				// Check to see if ship goes out of bounds from placing at desired location
				int newRow = newShipOccupiedSquares.get(i).getRow();
				char newColumn = newShipOccupiedSquares.get(i).getColumn();
				if (newRow > 10 || newRow < 1 || newColumn > 'J' || newColumn < 'A') {
					return false;
				}
			}

			for (int i = 0; i < ships.size(); i++) {									// Check to see if ship overlaps with any other previously placed ships
				for (int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
					for (int z = 0; z < newShip.getOccupiedSquares().size(); z++) {
						if (ships.get(i).getOccupiedSquares().get(j).getRow() == newShip.getOccupiedSquares().get(z).getRow() && ships.get(i).getOccupiedSquares().get(j).getColumn() == newShip.getOccupiedSquares().get(z).getColumn()) {
							return false;
						}
					}
				}
			}

			ships.add(newShip);			// Officially adds the ship to the board
			return true;
		}
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Square attackSquare = new Square(x, y);
		Result attackResult = new Result(attackSquare);
		
		if (x > 10 || x < 1 || y > 'J' || y < 'A') {
			attackResult.setResult(AtackStatus.INVALID);
			attacks.add(attackResult);
			return attackResult;
		}

		for (int i = 0; i < attacks.size(); i++) {
			if (attacks.get(i).getLocation().equals(attackSquare)) {
				attackResult.setResult((AtackStatus.INVALID));
				attacks.add(attackResult);
				return attackResult;
			}
		}

		List<Ship> targetShips = new ArrayList<>();

		for (int i = 0; i < ships.size(); i++) {
			for (int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
				if (ships.get(i).getOccupiedSquares().get(j).equals(attackSquare)) {
					targetShips.add(ships.get(i));
				}
			}
		}

		if (targetShips.size() == 0) {
			attackResult.setResult(AtackStatus.MISS);
			attacks.add(attackResult);
			return attackResult;
		}
		else {
			Ship targetShip = targetShips.get(0);

			Result hitResult = targetShip.hit(attackSquare.getRow(), attackSquare.getColumn());

			if (ships.stream().allMatch(ship -> ship.hasSunk())) {
				hitResult.setResult(AtackStatus.SURRENDER);
			}

			attacks.add(hitResult);
			return hitResult;
		}
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}
}
