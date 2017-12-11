package fr.kutussu.gricha.giftdistribution.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by flori on 20/11/2016.
 */

public class Player implements Parcelable {
    private Long id;
    private String name;
    private String mail;
    private Gender gender;
    private Long projectId;

    public Player(String name, String mail, Gender gender) {
        this.name = name;
        this.mail = mail;
        this.gender = gender;
    }

    public Player(Long id, String name, String mail, Gender gender) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.gender = gender;
    }

    public Player(String name, String mail, Gender gender, Long projectId) {
        this.name = name;
        this.mail = mail;
        this.gender = gender;
        this.projectId = projectId;
    }

    public Player(Long id, String name, String mail, Gender gender, Long projectId) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.gender = gender;
        this.projectId = projectId;
    }

    protected Player(Parcel in) {
        id = in.readLong();
        name = in.readString();
        mail = in.readString();
        gender = Gender.valueOf(in.readString());
        projectId = in.readLong();

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(mail);
        dest.writeString(gender.name());
        dest.writeLong(projectId);
    }
    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);

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

        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
