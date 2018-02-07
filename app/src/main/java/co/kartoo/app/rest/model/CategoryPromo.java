package co.kartoo.app.rest.model;

import java.io.Serializable;

/**
 * Created by MartinOenang on 11/5/2015.
 */
public class CategoryPromo implements Serializable{
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
