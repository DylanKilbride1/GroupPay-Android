package grouppay.dylankilbride.com.models;

public class StripeChargeReceipt {

  private Long amountPaid;
  private String failureCode;
  private String failureMessage;
  private String message;

  public StripeChargeReceipt(Long amountPaid, String failureCode, String failureMessage, String message) {
    this.amountPaid = amountPaid;
    this.failureCode = failureCode;
    this.failureMessage = failureMessage;
    this.message = message;
  }

  public StripeChargeReceipt() {
  }

  public Long getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(Long amountPaid) {
    this.amountPaid = amountPaid;
  }

  public String getFailureCode() {
    return failureCode;
  }

  public void setFailureCode(String failureCode) {
    this.failureCode = failureCode;
  }

  public String getMessage() {
    return message;
  }

  public String getFailureMessage() {
    return failureMessage;
  }

  public void setFailureMessage(String failureMessage) {
    this.failureMessage = failureMessage;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

