package co.kartoo.app.events;

import co.kartoo.app.rest.model.newest.SearchPromotionHeader;

/**
 * Created by EricLaptop001 on 4/12/2017.
 */

public class TrendingFinishedEventFilterResult {
    SearchPromotionHeader listPromo;

    public TrendingFinishedEventFilterResult(SearchPromotionHeader listPromo) {
        this.listPromo = listPromo;
    }

    public SearchPromotionHeader getListPromo() {
        return listPromo;
    }

    public void setListPromo(SearchPromotionHeader listPromo) {
        this.listPromo = listPromo;
    }
}
