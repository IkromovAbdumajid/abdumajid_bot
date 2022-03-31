package dto;

public class CurrencyDto {
    private int id;
    private String Code;
    private String Ccy;
    private String CcyNm_RU;
    private String CcyNm_UZ;
    private String CcyNm_UZC;
    private String CcyNm_EN;
    private String Nominal;
    private String Rate;
    private String Diff;
    private String Date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return Code;
    }


    public String getCcy() {
        return Ccy;
    }

    public String getCcyNm_RU() {
        return CcyNm_RU;
    }



    public String getCcyNm_UZ() {
        return CcyNm_UZ;
    }

    public String getCcyNm_UZC() {
        return CcyNm_UZC;
    }

    public String getCcyNm_EN() {
        return CcyNm_EN;
    }

    public String getNominal() {
        return Nominal;
    }

    public String getRate() {
        return Rate;
    }

    public String getDiff() {
        return Diff;
    }

    public String getDate() {
        return Date;
    }

    public void setCcy(String ccy) {
        Ccy = ccy;
    }
}
