package co.kartoo.app.rest.model.newest;

/**
 * Created by Luthfi Apriyanto on 12/19/2016.
 */

public class PopupReviewObject {
    String id;
    String feedbackContent;
    String feedbackCheckList;
    String oStype;

    public PopupReviewObject() {

    }

    public PopupReviewObject(String id, String feedbackContent, String feedbackCheckList, String oStype) {
        this.id = id;
        this.feedbackContent = feedbackContent;
        this.feedbackCheckList = feedbackCheckList;
        this.oStype = oStype;

    }
}