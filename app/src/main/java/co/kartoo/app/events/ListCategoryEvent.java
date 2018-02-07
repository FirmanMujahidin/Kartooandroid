package co.kartoo.app.events;

import java.util.ArrayList;

import co.kartoo.app.rest.model.Category;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class ListCategoryEvent {
    ArrayList<Category> listCategory;

    public ListCategoryEvent(ArrayList<Category> listCategory) {
        this.listCategory = listCategory;
    }

    public ArrayList<Category> getListCategory() {
        return listCategory;
    }

    public void setListCategory(ArrayList<Category> listCategory) {
        this.listCategory = listCategory;
    }
}
