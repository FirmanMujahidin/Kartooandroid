package co.kartoo.app.rest.model;

import java.io.Serializable;

/**
 * Created by MartinOenang on 10/16/2015.
 */
public class Card implements Serializable{
    String id;
    String idBank;
    Bank bank;
    String dateCreated;
    String cardType;
    String cardEdition;
    String urlImg;

    public Card() {
    }

    public Card(String id, String idBank, Bank bank, String dateCreated, String cardType, String cardEdition, String urlImg) {
        this.id = id;
        this.idBank = idBank;
        this.bank = bank;
        this.dateCreated = dateCreated;
        this.cardType = cardType;
        this.cardEdition = cardEdition;
        this.urlImg = urlImg;
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

    public String getIdBank() {
        return idBank;
    }

    public void setIdBank(String idBank) {
        this.idBank = idBank;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardEdition() {
        return cardEdition;
    }

    public void setCardEdition(String cardEdition) {
        this.cardEdition = cardEdition;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
