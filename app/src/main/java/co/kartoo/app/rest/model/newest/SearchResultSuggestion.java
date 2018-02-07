package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 2/1/2017.
 */

public class SearchResultSuggestion {
    String header;
    ArrayList<SearchRecent> resultList;

    public SearchResultSuggestion(String header, ArrayList<SearchRecent> resultList) {
        this.header = header;
        this.resultList = resultList;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<SearchRecent> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<SearchRecent> resultList) {
        this.resultList = resultList;
    }
}
