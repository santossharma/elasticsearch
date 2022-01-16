package com.baro.elasticsearch.document;

public class Passport {
    private Long ppsId;
    private String passportNumber;

    public Long getPpsId() {
        return ppsId;
    }

    public void setPpsId(Long ppsId) {
        this.ppsId = ppsId;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
