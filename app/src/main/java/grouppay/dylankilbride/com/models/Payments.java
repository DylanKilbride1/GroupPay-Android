package grouppay.dylankilbride.com.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Payments {

  private User user;
  private BigDecimal amountPaid;
  private String paymentType;
  private Calendar paymentDateAndTime;

  public Payments(User user, BigDecimal amountPaid, Calendar paymentDateAndTime) {
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

  public String getPaymentDateAndTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, 1);
    SimpleDateFormat format = new SimpleDateFormat("dd MMM, H:mm");
    return format.format(calendar.getTime());
  }

  public void setPaymentDateAndTime(Calendar paymentDateAndTime) {
    this.paymentDateAndTime = paymentDateAndTime;
  }
}
