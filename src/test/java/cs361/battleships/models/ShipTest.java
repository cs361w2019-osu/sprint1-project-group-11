package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipTest {

    @Test
    public void testSize() {
        
        Ship ship1 = new Ship("BATTLESHIP");
        assertEquals(4, ship1.getOccupiedSquares().size());

        Ship ship2 = new Ship("DESTROYER");
        assertEquals(3, ship2.getOccupiedSquares().size());

        Ship ship3 = new Ship("MINESWEEPER");
        assertEquals(2, ship3.getOccupiedSquares().size());
        
        Ship ship4 = new Ship("SUBMARINE");
        assertEquals(5, ship3.getOccupiedSquares().size());
        

    }
}
