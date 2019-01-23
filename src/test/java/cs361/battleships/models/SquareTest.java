package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SquareTest {

    @Test
    public void testSquareSetAndGet() {
    
        Square square1 = new Square();
        square.setRow(5);
        assertEquals(5, square1.getRow());
        
        square1.setColumn('H');
        assertEquals('H', square1.getColumn());
    }

}
