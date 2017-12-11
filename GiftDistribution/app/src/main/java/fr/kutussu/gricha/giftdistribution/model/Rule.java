package fr.kutussu.gricha.giftdistribution.model;

import android.os.Parcel;
import android.os.Parcelable;

import fr.kutussu.gricha.giftdistribution.model.Player;

/**
 * Created by flori on 20/11/2016.
 */
public class Rule implements Parcelable {

    Long id;
    Player player1;
    Player player2;
    Long project_id;

    public Rule(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Rule(Long id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }


    protected Rule(Parcel in) {
        player1 = in.readParcelable(Player.class.getClassLoader());
        player2 = in.readParcelable(Player.class.getClassLoader());
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    @Override
    public String toString() {
        return player1 +" n'offre pas à " + player2;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(player1, flags);
        dest.writeParcelable(player2, flags);
    }
}

