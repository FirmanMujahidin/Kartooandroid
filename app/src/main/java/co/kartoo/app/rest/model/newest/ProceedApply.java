package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

public class ProceedApply {
    String userID;
    String cardID;
    String applicationID;
    String currentStatus;
    String employment;
    String position;
    String income;
    String officeAddress;
    String officePhoneNumber;
    String emergencyContactNumber;
    String preferredTimeOfCall;
    String url_Img_KTP;
    String url_Img_NPW;
    String url_Img_IncomeStatement;
    String ownershipStatus;
    String familyReferenceName;
    String familyReferenceRelationship;
    String familyReferenceContactNumber;
    String billingAddress;
    String maritalStatus;
    String jenisPekerjaan;
    String industry;
    String department;
    String statusPekerjaan;
    String lamaBekerja;
    String companyName;

    public ProceedApply(){
    }

    public ProceedApply(String userID, String cardID, String applicationID, String currentStatus, String employment, String position,
                        String income, String officeAddress, String officePhoneNumber, String emergencyContactNumber, String preferredTimeOfCall,
                        String url_Img_KTP, String url_Img_NPW, String url_Img_IncomeStatement, String ownershipStatus, String familyReferenceName,
                        String familyReferenceRelationship, String familyReferenceContactNumber, String billingAddress, String maritalStatus,
                        String jenisPekerjaan, String industry, String department, String statusPekerjaan, String lamaBekerja, String companyName)
    {
        this.userID = userID;
        this.cardID = cardID;
        this.applicationID = applicationID;
        this.currentStatus = currentStatus;
        this.employment = employment;
        this.position = position;
        this.income = income;
        this.officeAddress = officeAddress;
        this.officePhoneNumber = officePhoneNumber;
        this.emergencyContactNumber = emergencyContactNumber;
        this.preferredTimeOfCall = preferredTimeOfCall;
        this.url_Img_KTP = url_Img_KTP;
        this.url_Img_NPW = url_Img_NPW;
        this.url_Img_IncomeStatement = url_Img_IncomeStatement;
        this.ownershipStatus = ownershipStatus;
        this.familyReferenceName = familyReferenceName;
        this.familyReferenceRelationship = familyReferenceRelationship;
        this.familyReferenceContactNumber = familyReferenceContactNumber;
        this.billingAddress = billingAddress;
        this.maritalStatus = maritalStatus;
        this.jenisPekerjaan = jenisPekerjaan;
        this.industry = industry;
        this.department = department;
        this.statusPekerjaan = statusPekerjaan;
        this.lamaBekerja = lamaBekerja;
        this.companyName = companyName;
    }
}
