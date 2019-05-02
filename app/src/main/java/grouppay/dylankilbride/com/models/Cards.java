package grouppay.dylankilbride.com.models;

public class Cards {

  private long cardId;
  private String lastFour;
  private String expiryDate;
  private int expiryMonth;
  private int expiryYear;
  private String cardholderName;
  private String cardType;

  public Cards(){
  }

  public Cards(long id, String lastFour, String expiryDate, String cardholderName, String cardType) {
    this.cardId = id;
    this.lastFour = lastFour;
    this.expiryDate = expiryDate;
    this.cardholderName = cardholderName;
    this.cardType = cardType;
  }

  public Cards(String lastFour, int expiryMonth, int expiryYear, String cardType) {
    this.lastFour = lastFour;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.cardType = cardType;
  }

  public long getCardId() {
    return cardId;
  }

  public String getLastFour() {
    return lastFour;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public String getCardholderName() {
    return cardholderName;
  }

  public String getCardType() {
    return cardType;
  }

  public void setCardId(long id) {
    this.cardId = id;
  }

  public void setLastFour(String lastFour) {
    this.lastFour = lastFour;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public void setCardholderName(String cardholderName) {
    this.cardholderName = cardholderName;
  }

  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
  }
}
