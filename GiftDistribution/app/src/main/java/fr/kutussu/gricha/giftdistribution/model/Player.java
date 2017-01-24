package fr.kutussu.gricha.giftdistribution.model;

/**
 * Created by flori on 20/11/2016.
 */

public class Player {
    private String name;
    private String mail;
    private Gender gender;

    public Player(String name, String mail, Gender gender) {
        this.name = name;
        this.mail = mail;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        Player player = (Player) o;

        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (mail != null ? !mail.equals(player.mail) : player.mail != null) return false;
        return gender == player.gender;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
//        return "Player{" +
//                "name='" + name + '\'' +
//                ", mail='" + mail + '\'' +
//                ", gender=" + gender +
//                '}';

        return name;
    }
}
