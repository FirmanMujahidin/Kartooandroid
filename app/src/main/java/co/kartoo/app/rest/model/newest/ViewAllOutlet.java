package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 3/11/2016.
 */
public class ViewAllOutlet  {
    int currentPage;
    int maxPage;
    ArrayList<Availableoutlets> outletDTOs;

    public ArrayList<Availableoutlets> getOutletDTOs() {
        return outletDTOs;
    }

    public void setOutletDTOs(ArrayList<Availableoutlets> outletDTOs) {
        this.outletDTOs = outletDTOs;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }


}
