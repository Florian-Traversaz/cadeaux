package fr.kutussu.gricha.giftdistribution.model;

import java.util.ArrayList;

/**
 * Created by flori on 20/11/2016.
 */

public class PlayerList {

    ArrayList<Player> playerList;

    public PlayerList() {
        playerList = new ArrayList<Player>();
    }

    public PlayerList(ArrayList<Player> playerList) {
        playerList = playerList;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(Player player){
        playerList.add(player);
    }
    @Override
    public String toString() {
        String result = "PlayerList{";
        for (Player player : playerList) {
            result+=player.toString().concat(",");
        }
        return result;
    }
}
