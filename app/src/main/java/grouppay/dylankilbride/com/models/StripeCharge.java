package grouppay.dylankilbride.com.models;

public class StripeCharge {

  private String tokenId;
  private double amountForGroup;
  private double amountInclFees;
  private String userId;
  private String groupAccountId;

  public StripeCharge(String tokenId, double amountForGroup, double amountInclFees, String userId, String groupAccountId) {
    this.tokenId = tokenId;
    this.amountForGroup = amountForGroup;
    this.amountInclFees = amountInclFees;
    this.userId = userId;
    this.groupAccountId = groupAccountId;
  }

  public StripeCharge(String tokenId, String userId) {
    this.tokenId = tokenId;
    this.userId = userId;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public double getAmountForGroup() {
    return amountForGroup;
  }

  public void setAmountForGroup(double amountForGroup) {
    this.amountForGroup = amountForGroup;
  }

  public double getAmountInclFees() {
    return amountInclFees;
  }

  public void setAmountInclFees(double amountInclFees) {
    this.amountInclFees = amountInclFees;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getGroupAccountId() {
    return groupAccountId;
  }

  public void setGroupAccountId(String groupAccountId) {
    this.groupAccountId = groupAccountId;
  }
}
