package grouppay.dylankilbride.com.models;

public class Contact {

  private int testResourceId;
  private String firstName;
  private String lastName;
  private String contactEmail;
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

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
