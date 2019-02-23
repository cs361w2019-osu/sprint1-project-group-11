package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
		assertTrue(board.placeShip(new Ship("DESTROYER"), 4, 'E', true));
		assertTrue(board.attack(4, 'E').getResult() == AtackStatus.SUNK);
	}
	
}
