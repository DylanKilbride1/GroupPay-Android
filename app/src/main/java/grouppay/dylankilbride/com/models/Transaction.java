package grouppay.dylankilbride.com.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction {

  private BigDecimal amountPaid;
  private String paymentType;
  private String paymentDateAndTime;
  private User user;

  public Transaction(User user, BigDecimal amountPaid, String paymentDateAndTime) {
    this.user = user;
    this.amountPaid = amountPaid;
    this.paymentDateAndTime = paymentDateAndTime;
  }
  
  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public BigDecimal getAmountPaid() {
    return amountPaid;
  }

  public String getAmountPaidStr() {
    String amountPaidStr = String.valueOf(getAmountPaid());
    return amountPaidStr;
  }

  public void setAmountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
  }

  public String getFormattedPaymentDateAndTime(Calendar paymentDateTime) {
    paymentDateTime.add(Calendar.DATE, 1);
    SimpleDateFormat format = new SimpleDateFormat("dd MMM, H:mm");
    return format.format(paymentDateTime.getTime());
  }

  public String getPaymentDateAndTime() {
    return paymentDateAndTime;
  }

  public void setPaymentDateAndTime(String paymentDateAndTime) {
    this.paymentDateAndTime = paymentDateAndTime;
  }

  public void convertStringToCalendar(String paymentDateAndTime) {

  }
}
