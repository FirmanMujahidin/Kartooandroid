package co.kartoo.app.rest.model.newest;

import java.io.Serializable;
import java.util.ArrayList;

import co.kartoo.app.rest.model.Bank;

/**
 * Created by Luthfi Apriyanto on 3/4/2016.
 */
public class Availablecards implements Serializable {
    String id;
    String card_Edition;
    String card_Type;
    String url_img;
    Bank bank;
    String minimumIncome;
    String cardFee;
    String monthlyRateFee;
    ArrayList<String> shortPerks;

    public ArrayList<String> getShortPerks() {
        return shortPerks;
    }

    public void setShortPerks(ArrayList<String> shortPerks) {
        this.shortPerks = shortPerks;
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


    public Availablecards () {
    }

    public Availablecards(String id, String card_Edition, String card_Type, String url_img, Bank bank) {
        this.id = id;
        this.card_Edition = card_Edition;
        this.bank = bank;
        this.card_Type = card_Type;
        this.url_img = url_img;
        this.bank = bank;

    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard_Edition() {
        return card_Edition;
    }

    public void setCard_Edition(String card_Edition) {
        this.card_Edition = card_Edition;
    }

    public String getCard_Type() {
        return card_Type;
    }

    public void setCard_Type(String card_Type) {
        this.card_Type = card_Type;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }



}
