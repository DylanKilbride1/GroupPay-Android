package grouppay.dylankilbride.com.models;

import android.media.Image;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class GroupAccount {

  private long accountId;
  private String accountName;
  private String accountDescription;
  private int numberOfMembers;
  private BigDecimal totalAmountOwed;
  private BigDecimal totalAmountPaid;
  //Map<Users, Payments> memberPayments;
 // List<Users> groupMembers;

  public GroupAccount(long accountId, String accountName, String accountDescription, int numberOfMembers, BigDecimal totalAmountPaid, BigDecimal totalAmountOwed) {
    this.accountId = accountId;
    this.accountName = accountName;
    this.accountDescription = accountDescription;
    this.numberOfMembers = numberOfMembers;
    this.totalAmountOwed = totalAmountOwed;
    this.totalAmountPaid = totalAmountPaid;
  }

  public GroupAccount() {}

  public long getAccountId() {
    return accountId;
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
    //DecimalFormat decimalFormat = new DecimalFormat("#.##");
    String amountPaid = String.valueOf(totalAmountPaid);
    String amountOwed = String.valueOf(totalAmountOwed);
    return "€" + amountPaid + " of €" + amountOwed;
  }

  public String getNumberOfMembersString(){
    String noOfMembers = String.valueOf(numberOfMembers);
    return noOfMembers + " Members";
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
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
}
