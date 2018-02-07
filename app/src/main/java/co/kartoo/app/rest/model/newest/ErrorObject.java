package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 12/19/2016.
 */

public class ErrorObject {
    String promotionID;
    String suggestCheckList;
    String suggestContent;

    public ErrorObject() {

    }

    public ErrorObject(String promotionID, String suggestCheckList, String suggestContent) {
        this.promotionID = promotionID;
        this.suggestCheckList = suggestCheckList;
        this.suggestContent = suggestContent;

    }
}