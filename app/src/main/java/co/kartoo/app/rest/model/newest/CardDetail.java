package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 12/7/2016.
 */

public class CardDetail {

    Availablecards card;
    String minimumIncome;
    String cardFee;
    String monthlyRateFee;
    ArrayList<Perks> perks;
    ArrayList<Details> details;


    public ArrayList<Perks> getPerks() {
        return perks;
    }

    public void setPerks(ArrayList<Perks> perks) {
        this.perks = perks;
    }

    public ArrayList<Details> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Details> details) {
        this.details = details;
    }

    public Availablecards getCard() {
        return card;
    }

    public void setCard(Availablecards card) {
        this.card = card;
    }

    public String getMinimumIncome() {
        return minimumIncome;
    }

    public void setMinimumIncome(String minimumIncome) {
        this.minimumIncome = minimumIncome;
    }

    public String getCardFee() {
        return cardFee;
    }

    public void setCardFee(String cardFee) {
        this.cardFee = cardFee;
    }

    public String getMonthlyRateFee() {
        return monthlyRateFee;
    }

    public void setMonthlyRateFee(String monthlyRateFee) {
        this.monthlyRateFee = monthlyRateFee;
    }


}
