package co.kartoo.app.rest.model.newest;

import co.kartoo.app.rest.model.Category;

/**
 * Created by Luthfi Apriyanto on 10/3/2016.
 */

public class Interest {
    String id;
    int counter;
    Category category;
    UserProfile user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}