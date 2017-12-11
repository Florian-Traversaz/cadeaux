package fr.kutussu.gricha.giftdistribution.Drawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fr.kutussu.gricha.giftdistribution.model.Player;
import fr.kutussu.gricha.giftdistribution.model.Rule;

/**
 * Created by flori on 20/11/2016.
 */

public class DrawingProcessor {
    private ArrayList<Rule> ruleList;
    private ArrayList<Player> playerArrayList;
    private HashMap<Player, Player> finalResult;
    private int tryNumber=0;

    public DrawingProcessor(ArrayList<Rule> ruleList, ArrayList<Player> playerArrayList) {
        this.ruleList = ruleList;
        this.playerArrayList = playerArrayList;
    }

    public void drawDistribution() throws Exception {
        System.out.println("New Draw");
        HashMap<Player, Player> resultMap = new HashMap<>();
        ArrayList<Player> supplierTempArrayList = (ArrayList<Player>) playerArrayList.clone();
        ArrayList<Player> receiverTempArrayList = (ArrayList<Player>) playerArrayList.clone();

        Integer numberOfTry = 0;
        while (supplierTempArrayList.size() != 0) {
            Collections.shuffle(supplierTempArrayList);
            Collections.shuffle(receiverTempArrayList);
            Player supplierPlayer = supplierTempArrayList.get(0);
            Boolean result = findAReceiver(supplierPlayer, receiverTempArrayList, resultMap);
            if (result) {
                supplierTempArrayList.remove(supplierPlayer);
            }
            numberOfTry++;
            if (numberOfTry == 100)
                break;
        }

        if ((supplierTempArrayList.size() != 0 || receiverTempArrayList.size() != 0) && resultMap.size() != this.playerArrayList.size()) {
            //There is no solution for the moment, we redraw
            tryNumber++;
            if(tryNumber==100){
                throw new Exception("Aucune combinaison trouvée, veuillez modifier vos règles");
            }
            drawDistribution();
        } else {
            finalResult = resultMap;
        }
    }

    public HashMap<Player, Player> startDraw() throws Exception {
        drawDistribution();
        return finalResult;
    }

    private Boolean findAReceiver(Player supplierPlayer, ArrayList<Player> receiverArrayList, HashMap<Player, Player> resultMap) {
        for (Player receiverPlayer : receiverArrayList) {
            if (isAuthorized(receiverPlayer, supplierPlayer)) {
                resultMap.put(supplierPlayer, receiverPlayer);
                System.out.println(resultMap);
                receiverArrayList.remove(receiverPlayer);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Boolean isAuthorized(Player receiverPlayer, Player supplierPlayer) {
        if (receiverPlayer != supplierPlayer) {
            for (Rule rule : ruleList) {
                if (
                        (rule.getPlayer1().equals(supplierPlayer) && rule.getPlayer2().equals(receiverPlayer))) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
