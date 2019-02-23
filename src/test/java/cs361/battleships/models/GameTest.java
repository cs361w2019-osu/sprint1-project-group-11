package cs361.battleships.models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GameTest {

    Game testgame = new Game();
    Game gametwo = new Game();


    @Test
    public void testBadBlaceShip(){ //
        Ship ship1 = new Ship("BATTLESHIP");
        assertEquals(false,testgame.placeShip(ship1, 15, 'L', true));
    }

    @Test
    public void testGoodPlaceShip(){
        Ship ship1 = new Ship("BATTLESHIP");
        assertEquals(true, gametwo.placeShip(ship1, 7, 'C', true));
    }

    @Test
    public void testBadAttack(){
        Game game = new Game();
        assertFalse(game.attack(12, 'A'));
        assertFalse(game.attack(6,'L'));
    }

    @Test
    public void testGoodAttack(){
        Game game = new Game();
        assertTrue(game.attack(7, 'B'));
    }


    @Test
    public void testAttackOnShip() {
        // setup a new game & add a ship
        Game g = new Game();
        Ship s = new Ship("DESTROYER");
        assertTrue(g.placeShip(s, 9, 'J', false));
        assertTrue(g.attack(9, 'J'));
    }
}
