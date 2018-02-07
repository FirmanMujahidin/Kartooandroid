package co.kartoo.app.search;

import java.util.ArrayList;
import co.kartoo.app.rest.model.newest.SearchResultSuggestion;

public class RecentEvent {

    ArrayList<SearchResultSuggestion> listDetail;

    public RecentEvent(ArrayList<SearchResultSuggestion> listDetail) {
        this.listDetail = listDetail;
    }

    public ArrayList<SearchResultSuggestion> getRecentList() {
        return listDetail;
    }

    public void setListPromo(ArrayList<SearchResultSuggestion> listDetail) {
        this.listDetail = listDetail;
    }
}
