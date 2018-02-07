package co.kartoo.app.rest.model.newest;

/**
 * Created by Luthfi Apriyanto on 1/18/2017.
 */

public class AnswerOTP {
    String verificationCode;
    String verificationID;

    public AnswerOTP(){
    }

    public AnswerOTP(String verificationCode, String verificationID) {
        this.verificationCode = verificationCode;
        this.verificationID = verificationID;

    }
}
