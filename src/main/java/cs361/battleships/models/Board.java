package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;

import java.util.*;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private int moveCmdsAvailable = 2;


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
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical, boolean isSubmerged) {
		if (ships.size() >= 4) {			// Check to see if we have already placed at least 4 ships
			return false;
		}
		else {
			for (int i = 0; i < ships.size(); i++) {			// Check to see if we have already placed a ship of same type
				if (ships.get(i).getKind().equals(ship.getKind())) {
					return false;
				}
			}

			Ship newShip = new Ship(ship.getKind());
			newShip.place(x, y, isVertical, isSubmerged);

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
						if (ships.get(i).getOccupiedSquares().get(j).equals(newShip.getOccupiedSquares().get(z)) && ships.get(i).getOccupiedSquares().get(j).getSubmerged() == newShip.getOccupiedSquares().get(z).getSubmerged()) {
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

		boolean spaceLaserActive = false;

		if (ships.stream().anyMatch(ship -> ship.hasSunk())) {
			spaceLaserActive = true;
		}

		if (x > 10 || x < 1 || y > 'J' || y < 'A') {
			attackResult.setResult(AtackStatus.INVALID);
			attacks.add(attackResult);
			return attackResult;
		}


		List<Square> captainSquares = new ArrayList<>();

		for (int i = 0; i < ships.size(); i++) {
			for (int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
				if (ships.get(i).getOccupiedSquares().get(j).getisCaptain()) {
					captainSquares.add(ships.get(i).getOccupiedSquares().get(j));
				}
			}
		}

		List<Ship> targetShips = new ArrayList<>();

		for (int i = 0; i < ships.size(); i++) {
			for (int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
				if (ships.get(i).getOccupiedSquares().get(j).equals(attackSquare) && (spaceLaserActive || !ships.get(i).getOccupiedSquares().get(j).getSubmerged())) {
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

			Result hitResult = new Result();

			for (int z = 0; z < targetShips.size(); z++) {
				Ship targetShip = targetShips.get(z);

				hitResult = targetShip.hit(attackSquare.getRow(), attackSquare.getColumn());

				if (hitResult.getResult() == AtackStatus.SUNK) {
					for (int j = 0; j < targetShip.getOccupiedSquares().size(); j++) {
						for (int i = 0; i < attacks.size(); i++) {
							if (attacks.get(i).getLocation().equals(targetShip.getOccupiedSquares().get(j))) {
								attacks.remove(i);
								i--;
							}
						}
						Square newSquare = new Square(targetShip.getOccupiedSquares().get(j).getRow(), targetShip.getOccupiedSquares().get(j).getColumn());
						Result newAttack = new Result(newSquare);
						newAttack.setResult(AtackStatus.HIT);
						attacks.add(newAttack);
					}
				}
				else if (hitResult.getResult() == AtackStatus.HIT) {
					for (int i = 0; i < attacks.size(); i++) {
						if (attacks.get(i).getLocation().equals(attackSquare)) {
							if (attacks.get(i).getResult() == AtackStatus.MISS) {
								attacks.remove(i);
							}
						}
					}
				}
			}

			if (ships.stream().allMatch(ship -> ship.hasSunk())) {
				hitResult.setResult(AtackStatus.SURRENDER);
			}

			attacks.add(hitResult);
			return hitResult;
		}
	}

	public List<Square> sonar(Game g, int x, char y) {

			List<Square> occupiedSquares = new ArrayList<>();
			List<Square> sonarSquares = new ArrayList<>();
			List<Square> matchedSquares = new ArrayList<>();

			int targetRow = x;
			char targetCol = y;

			for (int i = 0; i < g.otherShips().size(); i++) {
				for (int j = 0; j < g.otherShips().get(i).getOccupiedSquares().size(); j++) {
					occupiedSquares.add(g.otherShips().get(i).getOccupiedSquares().get(j));
				}
			}

			sonarSquares.add(new Square(targetRow - 2, targetCol));
			sonarSquares.add(new Square(targetRow - 1, targetCol));
			sonarSquares.add(new Square(targetRow, targetCol));
			sonarSquares.add(new Square(targetRow + 1, targetCol));
			sonarSquares.add(new Square(targetRow + 2, targetCol));

			sonarSquares.add(new Square(targetRow - 1, (char) ((int) targetCol + 1)));
			sonarSquares.add(new Square(targetRow, (char) ((int) targetCol + 1)));
			sonarSquares.add(new Square(targetRow + 1, (char) ((int) targetCol + 1)));
			sonarSquares.add(new Square(targetRow, (char) ((int) targetCol + 2)));

			sonarSquares.add(new Square(targetRow - 1, (char) ((int) targetCol - 1)));
			sonarSquares.add(new Square(targetRow, (char) ((int) targetCol - 1)));
			sonarSquares.add(new Square(targetRow + 1, (char) ((int) targetCol - 1)));
			sonarSquares.add(new Square(targetRow, (char) ((int) targetCol - 2)));

			for (int i = 0; i < occupiedSquares.size(); i++) {
				for (int j = 0; j < sonarSquares.size(); j++) {
					if (occupiedSquares.get(i).equals(sonarSquares.get(j))) {
						matchedSquares.add(occupiedSquares.get(i));
					}
				}
			}

			return matchedSquares;

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

	public List<Ship> moveNorth() {

		List<Ship> playerShips = ships;

		if (moveCmdsAvailable > 0) {
			Collections.sort(playerShips, new Comparator<Ship>() {
				@Override
				public int compare(Ship s1, Ship s2) {
					int s1Pos = s1.getOccupiedSquares().get(0).getRow();
					int s2Pos = s2.getOccupiedSquares().get(0).getRow();

					for (int i = 0; i < s1.getOccupiedSquares().size(); i++) {
						if (s1.getOccupiedSquares().get(i).getRow() < s1Pos) {
							s1Pos = s1.getOccupiedSquares().get(i).getRow();
						}
					}

					for (int i = 0; i < s2.getOccupiedSquares().size(); i++) {
						if (s2.getOccupiedSquares().get(i).getRow() < s2Pos) {
							s2Pos = s2.getOccupiedSquares().get(i).getRow();
						}
					}

					return s1Pos - s2Pos;
				}
			});

			for (int i = 0; i < playerShips.size(); i++) {
				boolean canMove = true;

				for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) { // Checks if ship will be off board if moved
					if (playerShips.get(i).getOccupiedSquares().get(j).getRow() - 1 <= 0) {
						canMove = false;
					}
				}

				for (int j = 0; j < i; j++) { // Checks if current ship will overlap with any previous ships
					for (int k = 0; k < playerShips.get(j).getOccupiedSquares().size(); k++) {
						for (int z = 0; z < playerShips.get(i).getOccupiedSquares().size(); z++) {
							if (playerShips.get(j).getOccupiedSquares().get(k).getRow() == playerShips.get(i).getOccupiedSquares().get(z).getRow() - 1) {       // Do they have same row after the move?
								if (playerShips.get(i).getOccupiedSquares().get(z).getColumn() == playerShips.get(j).getOccupiedSquares().get(k).getColumn()) { // Do they have same column?
									if (playerShips.get(i).getOccupiedSquares().get(z).getSubmerged() == playerShips.get(j).getOccupiedSquares().get(k).getSubmerged()) {   // Do they both have same submerge value?
										canMove = false;
									}
								}
							}
						}
					}
				}

				if (canMove) {
					for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) {
						playerShips.get(i).getOccupiedSquares().get(j).setRow(playerShips.get(i).getOccupiedSquares().get(j).getRow() - 1);
					}
				}

			}

			moveCmdsAvailable = moveCmdsAvailable - 1;
		}

		return playerShips;
	}

	public List<Ship> moveEast() {

		List<Ship> playerShips = ships;

		if (moveCmdsAvailable > 0) {
			Collections.sort(playerShips, new Comparator<Ship>() {
				@Override
				public int compare(Ship s1, Ship s2) {
					int s1Pos = (int) (s1.getOccupiedSquares().get(0).getColumn());
					int s2Pos = (int) (s2.getOccupiedSquares().get(0).getColumn());

					for (int i = 0; i < s1.getOccupiedSquares().size(); i++) {
						if ((int) (s1.getOccupiedSquares().get(i).getColumn()) < s1Pos) {
							s1Pos = (int) (s1.getOccupiedSquares().get(i).getColumn());
						}
					}

					for (int i = 0; i < s2.getOccupiedSquares().size(); i++) {
						if ((int) (s2.getOccupiedSquares().get(i).getColumn()) < s2Pos) {
							s2Pos = (int) (s2.getOccupiedSquares().get(i).getColumn());
						}
					}

					return s2Pos - s1Pos;
				}
			});

			for (int i = 0; i < playerShips.size(); i++) {

				boolean canMove = true;

				for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) { // Checks if ship will be off board if moved
					if ((int) (playerShips.get(i).getOccupiedSquares().get(j).getColumn()) + 1 > 74) {
						canMove = false;
					}
				}

				for (int j = 0; j < i; j++) { // Checks if current ship will overlap with any previous ships
					for (int k = 0; k < playerShips.get(j).getOccupiedSquares().size(); k++) {
						for (int z = 0; z < playerShips.get(i).getOccupiedSquares().size(); z++) {
							if ((int) (playerShips.get(j).getOccupiedSquares().get(k).getColumn()) == (int) (playerShips.get(i).getOccupiedSquares().get(z).getColumn()) + 1) {       // Do they have same column after the move?
								if (playerShips.get(i).getOccupiedSquares().get(z).getRow() == playerShips.get(j).getOccupiedSquares().get(k).getRow()) { // Do they have same row?
									if (playerShips.get(i).getOccupiedSquares().get(z).getSubmerged() == playerShips.get(j).getOccupiedSquares().get(k).getSubmerged()) {   // Do they both have same submerge value?
										canMove = false;
									}
								}
							}
						}
					}
				}

				if (canMove) {
					for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) {
						playerShips.get(i).getOccupiedSquares().get(j).setColumn((char) ((int) (playerShips.get(i).getOccupiedSquares().get(j).getColumn()) + 1));
					}
				}

			}

			moveCmdsAvailable = moveCmdsAvailable - 1;
		}

		return playerShips;
	}

	public List<Ship> moveSouth() {

		List<Ship> playerShips = ships;

		if (moveCmdsAvailable > 0) {
			Collections.sort(playerShips, new Comparator<Ship>() {
				@Override
				public int compare(Ship s1, Ship s2) {
					int s1Pos = s1.getOccupiedSquares().get(0).getRow();
					int s2Pos = s2.getOccupiedSquares().get(0).getRow();

					for (int i = 0; i < s1.getOccupiedSquares().size(); i++) {
						if (s1.getOccupiedSquares().get(i).getRow() < s1Pos) {
							s1Pos = s1.getOccupiedSquares().get(i).getRow();
						}
					}

					for (int i = 0; i < s2.getOccupiedSquares().size(); i++) {
						if (s2.getOccupiedSquares().get(i).getRow() < s2Pos) {
							s2Pos = s2.getOccupiedSquares().get(i).getRow();
						}
					}

					return s2Pos - s1Pos;
				}
			});

			for (int i = 0; i < playerShips.size(); i++) {
				boolean canMove = true;

				for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) { // Checks if ship will be off board if moved
					if (playerShips.get(i).getOccupiedSquares().get(j).getRow() + 1 > 10) {
						canMove = false;
					}
				}

				for (int j = 0; j < i; j++) { // Checks if current ship will overlap with any previous ships
					for (int k = 0; k < playerShips.get(j).getOccupiedSquares().size(); k++) {
						for (int z = 0; z < playerShips.get(i).getOccupiedSquares().size(); z++) {
							if (playerShips.get(j).getOccupiedSquares().get(k).getRow() == playerShips.get(i).getOccupiedSquares().get(z).getRow() + 1) {       // Do they have same row after the move?
								if (playerShips.get(i).getOccupiedSquares().get(z).getColumn() == playerShips.get(j).getOccupiedSquares().get(k).getColumn()) { // Do they have same column?
									if (playerShips.get(i).getOccupiedSquares().get(z).getSubmerged() == playerShips.get(j).getOccupiedSquares().get(k).getSubmerged()) {   // Do they both have same submerge value?
										canMove = false;
									}
								}
							}
						}
					}
				}

				if (canMove) {
					for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) {
						playerShips.get(i).getOccupiedSquares().get(j).setRow(playerShips.get(i).getOccupiedSquares().get(j).getRow() + 1);
					}
				}

			}

			moveCmdsAvailable = moveCmdsAvailable - 1;
		}

		return playerShips;
	}

	public List<Ship> moveWest() {

		List<Ship> playerShips = ships;

		if (moveCmdsAvailable > 0) {
			Collections.sort(playerShips, new Comparator<Ship>() {
				@Override
				public int compare(Ship s1, Ship s2) {
					int s1Pos = (int) (s1.getOccupiedSquares().get(0).getColumn());
					int s2Pos = (int) (s2.getOccupiedSquares().get(0).getColumn());

					for (int i = 0; i < s1.getOccupiedSquares().size(); i++) {
						if ((int) (s1.getOccupiedSquares().get(i).getColumn()) < s1Pos) {
							s1Pos = (int) (s1.getOccupiedSquares().get(i).getColumn());
						}
					}

					for (int i = 0; i < s2.getOccupiedSquares().size(); i++) {
						if ((int) (s2.getOccupiedSquares().get(i).getColumn()) < s2Pos) {
							s2Pos = (int) (s2.getOccupiedSquares().get(i).getColumn());
						}
					}

					return s1Pos - s2Pos;
				}
			});

			for (int i = 0; i < playerShips.size(); i++) {

				boolean canMove = true;

				for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) { // Checks if ship will be off board if moved
					if ((int) (playerShips.get(i).getOccupiedSquares().get(j).getColumn()) - 1 < 65) {
						canMove = false;
					}
				}

				for (int j = 0; j < i; j++) { // Checks if current ship will overlap with any previous ships
					for (int k = 0; k < playerShips.get(j).getOccupiedSquares().size(); k++) {
						for (int z = 0; z < playerShips.get(i).getOccupiedSquares().size(); z++) {
							if ((int) (playerShips.get(j).getOccupiedSquares().get(k).getColumn()) == (int) (playerShips.get(i).getOccupiedSquares().get(z).getColumn()) - 1) {       // Do they have same column after the move?
								if (playerShips.get(i).getOccupiedSquares().get(z).getRow() == playerShips.get(j).getOccupiedSquares().get(k).getRow()) { // Do they have same row?
									if (playerShips.get(i).getOccupiedSquares().get(z).getSubmerged() == playerShips.get(j).getOccupiedSquares().get(k).getSubmerged()) {   // Do they both have same submerge value?
										canMove = false;
									}
								}
							}
						}
					}
				}

				if (canMove) {
					for (int j = 0; j < playerShips.get(i).getOccupiedSquares().size(); j++) {
						playerShips.get(i).getOccupiedSquares().get(j).setColumn((char) ((int) (playerShips.get(i).getOccupiedSquares().get(j).getColumn()) - 1));
					}
				}

			}

			moveCmdsAvailable = moveCmdsAvailable - 1;
		}

		return playerShips;
	}









}
