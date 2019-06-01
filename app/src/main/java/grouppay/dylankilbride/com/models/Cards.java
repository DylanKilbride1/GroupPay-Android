package grouppay.dylankilbride.com.models;

public class Cards {

  private String cardId;
  private String lastFour;
  private String expiryDate;
  private int expiryMonth;
  private int expiryYear;
  private String cardholderName;
  private String cardType;
  private boolean isPressed = false;

  public Cards(){
  }

  public Cards(String id, String lastFour, int expiryMonth, int expiryYear, String cardType) {
    this.cardId = id;
    this.lastFour = lastFour;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.cardType = cardType;
  }

  public Cards(String lastFour, int expiryMonth, int expiryYear, String cardType) {
    this.lastFour = lastFour;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.cardType = cardType;
  }

  public String getCardId() {
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

  public void setCardId(String id) {
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

  public void setPressedTrue(){
    isPressed = true;
  }

  public void setPressedFalse() { //what happens if this is called and isPressed already false?
    isPressed = false;
  }

  public boolean getIsPressedValue() {
    return isPressed;
  }

  public boolean isPressed() {
    return isPressed;
  }
}
