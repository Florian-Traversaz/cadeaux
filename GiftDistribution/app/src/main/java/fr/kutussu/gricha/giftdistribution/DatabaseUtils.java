package fr.kutussu.gricha.giftdistribution;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.Assert;

import fr.kutussu.gricha.giftdistribution.model.Gender;
import fr.kutussu.gricha.giftdistribution.model.Player;
import fr.kutussu.gricha.giftdistribution.model.Project;
import fr.kutussu.gricha.giftdistribution.model.Rule;

/**
 * Created by a555917 on 31/01/2017.
 */

public class DatabaseUtils {

    public static Player addPlayer(SQLiteDatabase database, Player player) {
        ContentValues values = new ContentValues();

        //  values.put(KEY_ID, information.getId());
        values.put("name", player.getName());
        values.put("mail", player.getMail());
        values.put("gender", player.getGender().name());
        values.put("project_id", player.getProjectId());


        player.setId(database.insert("users", null, values));
        return player;
    }

    public static Boolean deletePlayer(SQLiteDatabase database, Player player) {

        return database.delete("users", "id=" + player.getId(), null) > 0;

    }

    public static boolean deleteProjectAndItsUsers(SQLiteDatabase database, Project project) {
        ContentValues values = new ContentValues();
        int nbProjectDeleted = database.delete("projects", "id=" + project.getId(), null);
        Assert.assertEquals(nbProjectDeleted, 1);
        int nbUsersDeleted = database.delete("users", "project_id=" + project.getId(), null);

        return true;
    }

    public static boolean checkRuleAlreadyExists(SQLiteDatabase database, Rule rule, Long projectId) {
        Cursor resultSet = database.rawQuery("Select * from rules where project_id = '" + projectId + "' and player1_id = " + rule.getPlayer1().getId() + " and player2_id = " + rule.getPlayer2().getId() + ";", null);
        resultSet.moveToFirst();
        if (resultSet.getCount() != 0) {
            return true;
        }
        return false;
    }

    public static Rule addRule(SQLiteDatabase database, Rule rule, Long projectId) {
        ContentValues values = new ContentValues();

        //  values.put(KEY_ID, information.getId());
        values.put("player1_id", rule.getPlayer1().getId());
        values.put("player2_id", rule.getPlayer2().getId());
        values.put("project_id", projectId);


        rule.setId(database.insert("rules", null, values));
        return rule;
    }

    public static Boolean deleteRule(SQLiteDatabase database, Rule rule) {

        return database.delete("rules", "id=" + rule.getId(), null) > 0;

    }


}
