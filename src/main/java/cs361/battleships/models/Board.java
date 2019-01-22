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
				if (ships.get(i).getKind() == ship.getKind()) {
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
				Set<Square> newShipSquares = Set.copyOf(ship.getOccupiedSquares());
				Set<Square> existingShipSquares = Set.copyOf((ships.get(i).getOccupiedSquares()));
				if (Sets.intersection(newShipSquares, existingShipSquares).size() != 0) {
					return false;
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
		Result attackResult = new Result(new Square(x, y));

		if (x > 10 || x < 1 || y > 'J' || y < 'A') {			// Check to see if attack is out of bounds
			attackResult.setResult(AtackStatus.INVALID);
			attacks.add(attackResult);
			return attackResult;
		}

		for (int i = 0; i < attacks.size(); i++) {				// Check to see if we have already made this attack before
			if ((attacks.get(i).getLocation().getColumn() == y) && (attacks.get(i).getLocation().getRow() == x)) {
				attackResult.setResult(AtackStatus.INVALID);
				attacks.add(attackResult);
				return attackResult;
			}
		}

		List<Ship> targetShips = new ArrayList<>();

		for (int i = 0; i < ships.size(); i++) {				// Grabs any ship that is at desired attack location
			List<Square> thisShipSquares = ships.get(i).getOccupiedSquares();
			for (int j = 0; j < thisShipSquares.size(); j++) {
				if (thisShipSquares.get(j).getRow() == x || thisShipSquares.get(j).getColumn() == y) {
					targetShips.add(ships.get(i));
				}
			}
		}

		if (targetShips.size() == 0) {						// Check to see if there is a ship at desired attack location
			attackResult.setResult((AtackStatus.MISS));
			attacks.add(attackResult);
			return attackResult;
		}

		Ship hitShip = targetShips.get(0);

		Result hitResult = hitShip.hit(x, y);				// Perform the attack on the ship (de-occupies the square)

		if (hitResult.getResult() == AtackStatus.SUNK) {	// If the ship was sunk as a result of the attack, then check if all other ships are also sunk
			if (ships.stream().allMatch(ship -> ship.getOccupiedSquares().size() == 0)) {		// If all other ships are sunk as well, then signal surrender
				attackResult.setResult(AtackStatus.SURRENDER);
			}
		}
		else {
			attackResult.setResult(AtackStatus.HIT);
		}

		attacks.add(attackResult);
		return attackResult;
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
