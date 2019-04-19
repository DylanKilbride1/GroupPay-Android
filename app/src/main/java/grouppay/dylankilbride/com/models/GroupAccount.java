package grouppay.dylankilbride.com.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.acl.Group;
import java.util.List;

public class GroupAccount implements Serializable {

  private long groupAccountId;
  private long adminId;
  private String groupAccountPhoto;
  private String accountName;
  private String accountDescription;
  private int numberOfMembers;
  private BigDecimal totalAmountOwed;
  private BigDecimal totalAmountPaid;
  private List<Transaction> paymentLog;
  private BigDecimal paymentProgress = new BigDecimal(0);

  public GroupAccount(long accountId, String groupAccountPhoto, String accountName, String accountDescription, int numberOfMembers, BigDecimal totalAmountPaid, BigDecimal totalAmountOwed, List<Transaction> paymentLog) {
    this.groupAccountId = accountId;
    this.groupAccountPhoto = groupAccountPhoto;
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

  public GroupAccount(long groupAccountId, String accountName, String accountDescription, int numberOfMembers, BigDecimal totalAmountOwed, BigDecimal totalAmountPaid, String groupAccountPhoto) {
    this.groupAccountId = groupAccountId;
    this.accountName = accountName;
    this.accountDescription = accountDescription;
    this.numberOfMembers = numberOfMembers;
    this.totalAmountPaid = totalAmountPaid;
    this.totalAmountOwed = totalAmountOwed;
    this.groupAccountPhoto = groupAccountPhoto;
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

  public String getGroupAccountPhoto() {
    return groupAccountPhoto;
  }

  public void setgroupAccountPhoto(String groupAccountPhoto) {
    this.groupAccountPhoto = groupAccountPhoto;
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
    String amountPaid = String.valueOf(totalAmountPaid);
    String amountOwed = String.valueOf(totalAmountOwed);
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
