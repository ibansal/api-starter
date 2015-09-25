package com.companyname.projectname.dto;

/**
 * Created by vijay.rawat01 on 7/6/15.
 */
public class AccountInfoDto {
    private String id;
    private String name;
    private String reward = "0";
    private String tds = "0";
    private String loan = "0";
    private String cash = "0";
    private String outstanding = "0";
    private String availableBalance = "0";
    private String accountType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "AccountInfoDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", reward='" + reward + '\'' +
                ", tds='" + tds + '\'' +
                ", loan='" + loan + '\'' +
                ", cash='" + cash + '\'' +
                ", outstanding='" + outstanding + '\'' +
                ", availableBalance='" + availableBalance + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountInfoDto that = (AccountInfoDto) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
