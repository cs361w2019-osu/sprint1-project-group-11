package controllers;

import com.google.inject.Singleton;
import cs361.battleships.models.Game;
import cs361.battleships.models.Ship;
import cs361.battleships.models.Square;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import org.flywaydb.core.internal.util.logging.console.ConsoleLog;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ApplicationController {

    public Result index() {
        return Results.html();
    }

    public Result newGame() {
        Game g = new Game();
        return Results.json().render(g);
    }

    public Result placeShip(Context context, PlacementGameAction g) {
        Game game = g.getGame();
        Ship ship = new Ship(g.getShipType());
        boolean result = game.placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public Result attack(Context context, AttackGameAction g) {
        Game game = g.getGame();
        boolean result = game.attack(g.getActionRow(), g.getActionColumn());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public Result sonar(Context context, AttackGameAction g) {

        if (g.getGame().otherShips().stream().anyMatch(ship -> ship.hasSunk())) {

            List<Square> occupiedSquares = new ArrayList<>();
            List<Square> sonarSquares = new ArrayList<>();
            List<Square> matchedSquares = new ArrayList<>();

            int targetRow = g.getActionRow();
            char targetCol = g.getActionColumn();

            for (int i = 0; i < g.getGame().otherShips().size(); i++) {
                for (int j = 0; j < g.getGame().otherShips().get(i).getOccupiedSquares().size(); j++) {
                    occupiedSquares.add(g.getGame().otherShips().get(i).getOccupiedSquares().get(j));
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

            return Results.json().render(matchedSquares);
        }
        else {
            return Results.json().render(0);
        }

    }
}
