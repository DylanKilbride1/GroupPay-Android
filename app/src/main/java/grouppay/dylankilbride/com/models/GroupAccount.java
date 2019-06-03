package grouppay.dylankilbride.com.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.acl.Group;
import java.util.List;

public class GroupAccount implements Serializable {

  private long groupAccountId;
  private long adminId;
  private GroupImage groupImage;
  private String accountName;
  private String accountDescription;
  private int numberOfMembers;
  private BigDecimal totalAmountOwed;
  private BigDecimal totalAmountPaid;
  private List<Transaction> paymentLog;
  private BigDecimal paymentProgress = new BigDecimal(0);

  public GroupAccount(long accountId, GroupImage groupImage, String accountName, String accountDescription, int numberOfMembers, BigDecimal totalAmountPaid, BigDecimal totalAmountOwed, List<Transaction> paymentLog) {
    this.groupAccountId = accountId;
    this.groupImage = groupImage;
    this.accountName = accountName;
    this.accountDescription = accountDescription;
    this.numberOfMembers = numberOfMembers;
    this.totalAmountPaid = totalAmountPaid;
    this.totalAmountOwed = totalAmountOwed;
    this.paymentLog = paymentLog;
  }

  /**For creating a basic account**/
  public GroupAccount(long adminId, String accountName, String accountDescription, BigDecimal totalAmountOwed){
    this.adminId = adminId;
    this.accountName = accountName;
    this.accountDescription = accountDescription;
    this.totalAmountOwed = totalAmountOwed;
  }

  public GroupAccount(long groupAccountId, String accountName, String accountDescription, int numberOfMembers, BigDecimal totalAmountOwed, BigDecimal totalAmountPaid, GroupImage groupImage) {
    this.groupAccountId = groupAccountId;
    this.accountName = accountName;
    this.accountDescription = accountDescription;
    this.numberOfMembers = numberOfMembers;
    this.totalAmountPaid = totalAmountPaid;
    this.totalAmountOwed = totalAmountOwed;
    this.groupImage = groupImage;
  }

  public GroupAccount(String accountName) {
    this.accountName = accountName;
  }

  public List<Transaction> getPaymentLog() {
    return paymentLog;
  }

  public void setPaymentLog(List<Transaction> paymentLog) {
    this.paymentLog = paymentLog;
  }

  public GroupImage getGroupImage() {
    return groupImage;
  }

  public void setGroupImage(GroupImage groupImage) {
    this.groupImage = groupImage;
  }

  public GroupAccount() {}

  public long getGroupAccountId() {
    return groupAccountId;
  }

  public long getAdminId() {
    return adminId;
  }

  public void setAdminId(long adminId) {
    this.adminId = adminId;
  }

  public String getAccountName() {
    return accountName;
  }

  public String getAccountDescription() {
    return accountDescription;
  }

  public int getNumberOfMembers() {
    return numberOfMembers;
  }

  public BigDecimal getTotalAmountOwed() {
    return totalAmountOwed;
  }

  public BigDecimal getTotalAmountPaid() {
    return totalAmountPaid;
  }

  public String getAccountFinanceString(){
    String amountPaid, amountOwed;
    if (totalAmountPaid.compareTo(BigDecimal.ZERO) == 0) {
      amountPaid = "0";
    } else {
      amountPaid = String.valueOf(totalAmountPaid.stripTrailingZeros().toPlainString());
    }
    if(totalAmountOwed.compareTo(BigDecimal.ZERO) == 0) {
      amountOwed = "0";
    } else {
      amountOwed = String.valueOf(totalAmountOwed.stripTrailingZeros().toPlainString());
    }
    return "€" + amountPaid + " of €" + amountOwed;
  }

  public String getNumberOfMembersString(){
    String noOfMembers = String.valueOf(numberOfMembers);
    return noOfMembers + " Members";
  }

  public void setGroupAccountId(long accountId) {
    this.groupAccountId = accountId;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public void setAccountDescription(String accountDescription) {
    this.accountDescription = accountDescription;
  }

  public void setNumberOfMembers(int numberOfMembers) {
    this.numberOfMembers = numberOfMembers;
  }

  public void setTotalAmountOwed(BigDecimal totalAmountOwed) {
    this.totalAmountOwed = totalAmountOwed;
  }

  public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
    this.totalAmountPaid = totalAmountPaid;
  }

  public String getPaymentProgress(BigDecimal paymentValue) {
    paymentProgress = paymentProgress.add(paymentValue);
    return String.valueOf(paymentProgress);
  }
}
