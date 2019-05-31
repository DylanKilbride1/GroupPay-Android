package grouppay.dylankilbride.com.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction {

  private BigDecimal amountPaid;
  private String paymentType;
  private String paymentDateAndTime;
  private long userId;
  private String transactionOwner;
  private String groupName;

  public Transaction(String transactionOwner, BigDecimal amountPaid, String paymentDateAndTime) {
    this.userId = userId;
    this.amountPaid = amountPaid;
    this.paymentDateAndTime = paymentDateAndTime;
    this.transactionOwner = transactionOwner;
  }

  public Transaction(BigDecimal amountPaid, String paymentDateAndTime, String groupName, String transactionOwner) {
    this.userId = userId;
    this.amountPaid = amountPaid;
    this.paymentDateAndTime = paymentDateAndTime;
    this.groupName = groupName;
    this.transactionOwner = transactionOwner;
  }
  
  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public BigDecimal getAmountPaid() {
    return amountPaid;
  }

  public String getAmountPaidStr() {
    String amountPaidStr = "€" + String.valueOf(getAmountPaid());
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

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getTransactionOwner() {
    return transactionOwner;
  }

  public void setTransactionOwner(String transactionOwner) {
    this.transactionOwner = transactionOwner;
  }

  public String getServerPaymentType(String paymentType){
    if(paymentType.equals("Incoming")) {
      return "Paid In";
    } else {
      return "Paid Out";
    }
  }

  public String getInitials() {
    String[] names = transactionOwner.split(" ");
    char firstNameInitial = names[0].charAt(0);
    char lastNameInitial = names[1].charAt(0);
    String fnInitial = Character.toString(firstNameInitial).toUpperCase();
    String lnInitial = Character.toString(lastNameInitial).toUpperCase();
    return new StringBuilder().append(fnInitial).append(lnInitial).toString();
  }

  public String returnFundsDepositedToGroup() {
    return "Deposited to: " + groupName;
  }
}