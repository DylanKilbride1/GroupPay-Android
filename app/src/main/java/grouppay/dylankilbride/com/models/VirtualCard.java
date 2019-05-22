package grouppay.dylankilbride.com.models;

public class VirtualCard {

  private String pan;
  private String cardExpiry;
  private String country;
  private String cvv;
  private String issuer;

  public VirtualCard(String pan, String cardExpiry, String country, String cvv, String issuer) {
    this.pan = pan;
    this.cardExpiry = cardExpiry;
    this.country = country;
    this.cvv = cvv;
    this.issuer = issuer;
  }

  public VirtualCard() {

  }

  public String getPan() {
    return pan;
  }

  public void setPan(String pan) {
    this.pan = pan;
  }

  public String getCardExpiry() {
    return cardExpiry;
  }

  public void setCardExpiry(String cardExpiry) {
    this.cardExpiry = cardExpiry;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }
}
