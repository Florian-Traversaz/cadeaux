package fr.kutussu.gricha.giftdistribution.Drawer;

import fr.kutussu.gricha.giftdistribution.model.Player;

/**
 * Created by flori on 20/11/2016.
 */
public class Rule {

    Player player1;
    Player player2;

    public Rule(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    @Override
    public String toString() {
        return "Rule " +
                player1 +
                " and " + player2 +
                " can't afford any gifts";
    }
}

