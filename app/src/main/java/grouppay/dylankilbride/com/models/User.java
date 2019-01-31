package grouppay.dylankilbride.com.models;

public class User {

  private long id;
  //TODO Add photo field
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String password;
  private String mobileNumber;

  public User(long id, String firstName, String lastName, String emailAddress, String password, String mobileNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.password = password;
    this.mobileNumber = mobileNumber;
  }

  public User() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getPassword() {
    return password;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public String getInitials() {
    char firstNameInitial = firstName.charAt(0);
    char lastNameInitial = lastName.charAt(0);
    String fnInitial = Character.toString(firstNameInitial).toUpperCase();
    String lnInitial = Character.toString(lastNameInitial).toUpperCase();
    return new StringBuilder().append(fnInitial).append(lnInitial).toString();
  }
}