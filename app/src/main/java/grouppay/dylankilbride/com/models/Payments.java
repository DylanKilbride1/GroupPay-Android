package grouppay.dylankilbride.com.models;

import java.math.BigDecimal;
import java.util.Date;

public class Payments {

  private User user;
  private BigDecimal amountPaid;
  private String paymentType;
  private Date paymentDateAndTime;

  public Payments(User user, BigDecimal amountPaid, Date paymentDateAndTime) {
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

  public Date getPaymentDateAndTime() {
    return paymentDateAndTime;
  }

  public void setPaymentDateAndTime(Date paymentDateAndTime) {
    this.paymentDateAndTime = paymentDateAndTime;
  }
}
