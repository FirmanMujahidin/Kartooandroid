package co.kartoo.app.events;

import co.kartoo.app.rest.model.CategoryHeader;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class TrendingFinishedEventCategory {
    CategoryHeader listPromo;

    public TrendingFinishedEventCategory(CategoryHeader listPromo) {
        this.listPromo = listPromo;
    }

    public CategoryHeader getListPromo() {
        return listPromo;
    }

    public void setListPromo(CategoryHeader listPromo) {
        this.listPromo = listPromo;
    }
}
