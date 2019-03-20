package grouppay.dylankilbride.com.models;

public class Contact {

  private int testResourceId;
  private String firstName;
  private String lastName;
  private String fullname;
  private String contactEmail;
  private String contactPhoneNumber;
  private boolean isPressed = false;

  public Contact(int testResourceId, String firstName, String lastName, String contactEmail) {
    this.testResourceId = testResourceId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.contactEmail = contactEmail;
  }

  public Contact(String firstName, String lastName, String contactEmail) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.contactEmail = contactEmail;
  }

  public Contact(String fullname, String contactPhoneNumber) {
    this.fullname = fullname;
    this.contactPhoneNumber = contactPhoneNumber;
  }
  public Contact() {}

  public int getTestResourceId() {
    return testResourceId;
  }

  public void setTestResourceId(int testResourceId) {
    this.testResourceId = testResourceId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public void setPressedTrue(){
    isPressed = true;
  }

  public void setPressedFalse() {
    isPressed = false;
  }

  public boolean getIsPressedValue() {
    return isPressed;
  }

  public String getNamesAppended() {
    return firstName + " " + lastName;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getContactPhoneNumber() {
    return contactPhoneNumber;
  }

  public void setContactPhoneNumber(String contactPhoneNumber) {
    this.contactPhoneNumber = contactPhoneNumber;
  }
}
