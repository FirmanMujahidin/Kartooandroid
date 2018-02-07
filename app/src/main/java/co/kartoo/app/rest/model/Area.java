package co.kartoo.app.rest.model;

import java.io.Serializable;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class Area implements Serializable {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
