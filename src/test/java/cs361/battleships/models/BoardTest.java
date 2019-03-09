package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));

    }

    @Test
    public void testAttackBounds() {
        Board board = new Board();
        assertTrue(board.attack(15, 'C').getResult() == AtackStatus.INVALID);
    }

    @Test
    public void testTwoShipsSameLoc() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 4, 'E', true));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 4, 'E', true));
    }


    @Test
    public void testCaptainQuarters() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 4, 'E', true));
        assertTrue(board.attack(4, 'E').getResult() == AtackStatus.SUNK);
    }


    @Test
    public void testAttackSunk()
    {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 4, 'E', true);
        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);
        board.attack(4, 'E');
        assertTrue(board.attack(5, 'E').getResult() == AtackStatus.SUNK);
    }
	
	@Test
	 public void testMoveNorth()
	 {
		Board board = new Board ();
		board.placeShip(new Ship('MINESWEEPER'), 4, 'E', true);
		moveNorth();
		assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getColumn() == 'D');
	 }
	 
	@Test
	 public void testMoveSouth()
	 {
		Board board = new Board ();
		board.placeShip(new Ship('MINESWEEPER'), 4, 'E', true);
		moveSouth();
		assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getColumn() == 'F');
	 }


	@Test
	 public void testMoveEast()
	 {
		Board board = new Board ();
		board.placeShip(new Ship('MINESWEEPER'), 4, 'E', true);
		moveEast();
		assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getRow() == 5);
	 }
	
	@Test
	 public void testMoveWest()
	 {
		Board board = new Board ();
		board.placeShip(new Ship('MINESWEEPER'), 4, 'E', true);
		moveWest();
		assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getRow() == 3);
	 }

    @Test
    public void testSonar() {

        Game g = new Game();
        g.placeShip(new Ship("MINESWEEPER"), 4,'E', true);
        assertTrue(g.getOpponentsBoard().sonar(g, 10, 'E').size() == 0);

    }


}
