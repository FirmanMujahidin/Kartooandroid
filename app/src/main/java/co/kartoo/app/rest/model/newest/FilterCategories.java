package co.kartoo.app.rest.model.newest;

import java.io.Serializable;

/**
 * Created by Luthfi Apriyanto on 3/2/2017.
 */

public class FilterCategories implements Serializable{
    int icon;
    String label;

    public FilterCategories(int icon, String label) {
        this.icon = icon;
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
