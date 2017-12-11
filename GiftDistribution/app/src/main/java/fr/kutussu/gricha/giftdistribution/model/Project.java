package fr.kutussu.gricha.giftdistribution.model;

/**
 * Created by A555917 on 26/01/2017.
 */

public class Project {
    private Integer id;
    private String name;

    public Project(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
