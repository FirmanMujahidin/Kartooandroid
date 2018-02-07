package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 4/5/2016.
 */
public class Notifications {
    String platform;
    String handle;
    ArrayList tags;

    public Notifications(){
    }

    public Notifications(String platform, String handle, ArrayList tags) {
        this.platform = platform;
        this.handle = handle;
        this.tags = tags;
    }
}
