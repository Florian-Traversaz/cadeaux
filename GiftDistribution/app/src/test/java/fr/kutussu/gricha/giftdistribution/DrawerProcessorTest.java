package fr.kutussu.gricha.giftdistribution;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import fr.kutussu.gricha.giftdistribution.Drawer.DrawingProcessor;
import fr.kutussu.gricha.giftdistribution.model.Rule;
import fr.kutussu.gricha.giftdistribution.model.Gender;
import fr.kutussu.gricha.giftdistribution.model.Player;
import fr.kutussu.gricha.giftdistribution.model.PlayerList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by flori on 20/11/2016.
 */
public class DrawerProcessorTest {
    @Test
    public void DrawerProcessTest() throws Exception {
        PlayerList playerList = new PlayerList();
        ArrayList<Player> inputPlayerArrayList = playerList.getPlayerList();

        Player player1 = new Player("Florian", "mail1", Gender.MAN);
        Player player2 = new Player("Emma", "mail2", Gender.WOMAN);
        Player player3 = new Player("Gaspard", "mail3", Gender.MAN);
        Player player4 = new Player("Nico", "mail4", Gender.MAN);
        Player player5 = new Player("Alex", "mail5", Gender.MAN);
        inputPlayerArrayList.add(player1);
        inputPlayerArrayList.add(player2);
        inputPlayerArrayList.add(player3);
        inputPlayerArrayList.add(player4);
        inputPlayerArrayList.add(player5);
        ArrayList<Rule> rulesList = new ArrayList<>();
        rulesList.add(new Rule(player1,player2));
        rulesList.add(new Rule(player1,player3));
        rulesList.add(new Rule(player1,player4));
        System.out.println(rulesList);
        DrawingProcessor processor = new DrawingProcessor(rulesList,inputPlayerArrayList);
        HashMap<Player,Player> result = processor.startDraw();


        assertNotNull(result);
        System.out.println("Result :" + result);
        assertEquals(result.size(),inputPlayerArrayList.size());
    }
}
