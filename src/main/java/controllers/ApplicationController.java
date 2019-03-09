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
import java.util.Collections;
import java.util.Comparator;
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
        if (g.getisSubmerged() && !g.getShipType().equals("SUBMARINE")) {
            return Results.badRequest();
        }
        boolean result = game.placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical(), g.getisSubmerged());
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
            return Results.json().render(g.getGame().getOpponentsBoard().sonar(g.getGame(), g.getActionRow(), g.getActionColumn()));
        }
        else {
            return Results.json().render(0);
        }
    }

    public Result moveN(Context context, AttackGameAction g) {
        Game game = g.getGame();
        game.moveN();
        return Results.json().render(game);
    }

    public Result moveE(Context context, AttackGameAction g) {
        Game game = g.getGame();
        game.moveE();
        return Results.json().render(game);
    }

    public Result moveS(Context context, AttackGameAction g) {
        Game game = g.getGame();
        game.moveS();
        return Results.json().render(game);
    }

    public Result moveW(Context context, AttackGameAction g) {
        Game game = g.getGame();
        game.moveW();
        return Results.json().render(game);
    }






}
