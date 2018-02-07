package co.kartoo.app.rest.model.newest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 3/17/2016.
 */
public class Cities implements Serializable{
    ArrayList<City> cities;

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }
}
