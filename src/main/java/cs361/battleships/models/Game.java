package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical, boolean isSubmerged) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical, isSubmerged);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical(), isSubmerged);
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }

    public Board getOpponentsBoard() {
        return opponentsBoard;
    }

    public List<Ship> otherShips() {
        return opponentsBoard.getShips();
    }

    private char randCol() {
        return (char) ('A' + new Random().nextInt(10));
    }

    private int randRow() {
        return new Random().nextInt(10) + 1;
    }

    private boolean randVertical() {
        return new Random().nextBoolean();
    }

    public List<Ship> moveN() {
        if (opponentsBoard.getShips().stream().filter(ship -> ship.hasSunk()).collect(Collectors.toList()).size() >= 2) {
            return playersBoard.moveNorth();
        }
        else {
            return playersBoard.getShips();
        }
    }

    public List<Ship> moveE() {
        if (opponentsBoard.getShips().stream().filter(ship -> ship.hasSunk()).collect(Collectors.toList()).size() >= 2) {
            return playersBoard.moveEast();
        }
        else {
            return playersBoard.getShips();
        }
    }

    public List<Ship> moveS() {
        if (opponentsBoard.getShips().stream().filter(ship -> ship.hasSunk()).collect(Collectors.toList()).size() >= 2) {
            return playersBoard.moveSouth();
        }
        else {
            return playersBoard.getShips();
        }
    }

    public List<Ship> moveW() {
        if (opponentsBoard.getShips().stream().filter(ship -> ship.hasSunk()).collect(Collectors.toList()).size() >= 2) {
            return playersBoard.moveWest();
        }
        else {
            return playersBoard.getShips();
        }
    }
}
