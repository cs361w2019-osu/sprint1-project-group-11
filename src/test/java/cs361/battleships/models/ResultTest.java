package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ResultTest {

    @Test
    public void testResultSet(){
        Result res = new Result();

        Ship testShip = new Ship("Destroyer");
        res.setShip(testShip);
        assertEquals(res.getShip(), testShip);

        Square testSquare = new Square(4, 'F');
        res.setLocation(testSquare);
        assertEquals(res.getLocation(), testSquare);
    }
}
