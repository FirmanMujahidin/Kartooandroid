package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 5/10/2016.
 */
public class Malls {
    ArrayList<MallDetail> malls;
    int maxPage;
    int currentPage;


    public ArrayList<MallDetail> getMalls() {
        return malls;
    }

    public void setMalls(ArrayList<MallDetail> malls) {
        this.malls = malls;
    }


    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
