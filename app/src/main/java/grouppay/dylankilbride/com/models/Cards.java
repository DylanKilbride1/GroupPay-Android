package grouppay.dylankilbride.com.models;

public class Cards {

  private long cardId;
  private String cardNumber;
  private String expiryDate;
  private String cardholderName;
  private String cardType;

  public Cards(){

  }

  public Cards(long id, String cardNumber, String expiryDate, String cardholderName, String cardType) {
    this.cardId = id;
    this.cardNumber = cardNumber;
    this.expiryDate = expiryDate;
    this.cardholderName = cardholderName;
    this.cardType = cardType;
  }

  public long getCardId() {
    return cardId;
  }

  public String getCardNumber() {
    return cardNumber;
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

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
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
}
