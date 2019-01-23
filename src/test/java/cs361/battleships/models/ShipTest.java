package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipTest {

    @Test
    public void testHealth() {

        Ship ship1 = new Ship("BATTLESHIP");
        assertEquals(4, s1.getHealth());

        Ship ship2 = new Ship("DESTROYER");
        assertEquals(3, s2.getHealth());

        Ship ship3 = new Ship("MINESWEEPER");
        assertEquals(2, s3.getHealth());

    }
}
