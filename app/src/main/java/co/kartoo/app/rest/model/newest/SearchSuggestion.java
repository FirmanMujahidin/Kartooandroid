package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 2/1/2017.
 */

public class SearchSuggestion {

    ArrayList<SearchResultSuggestion> searchResults;

    public ArrayList<SearchResultSuggestion> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<SearchResultSuggestion> searchResults) {
        this.searchResults = searchResults;
    }
}
