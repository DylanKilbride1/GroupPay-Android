package grouppay.dylankilbride.com.models;

import java.util.Comparator;
import java.util.Objects;

public class User {

  private long id;
  private String profileUrl;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String password;
  private String mobileNumber;
  private boolean isPressed = false;
  private ProfileImage profileImage;
  private String deviceToken;
  private String isUserVerified = "false";

  public User(long id, String firstName, String lastName, String emailAddress, String password, String mobileNumber, String profileUrl) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.password = password;
    this.mobileNumber = mobileNumber;
    this.profileUrl = profileUrl;
  }

  public User(long id, String firstName, String lastName, String emailAddress, String mobileNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.mobileNumber = mobileNumber;
  }

  public User(String firstName, String lastName, String mobileNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobileNumber = mobileNumber;
  }

  public User(String firstName, String lastName, String emailAddress, String mobileNumber, ProfileImage profileImage) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.mobileNumber = mobileNumber;
    this.profileImage = profileImage;
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

  public void setPressedTrue(){
    isPressed = true;
  }

  public void setPressedFalse() { //what happens if this is called and isPressed already false?
    isPressed = false;
  }

  public boolean getIsPressedValue() {
    return isPressed;
  }

  public boolean isPressed() {
    return isPressed;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public ProfileImage getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(ProfileImage profileImage) {
    this.profileImage = profileImage;
  }

  public String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
  }

  public String getIsUserVerified() {
    return isUserVerified;
  }

  public void setIsUserVerified(String isUserVerified) {
    this.isUserVerified = isUserVerified;
  }

  public static Comparator<User> nameComparator = new Comparator<User>() {
    @Override
    public int compare(User u1, User u2) {
      return (int) (u1.getFirstName().compareTo(u2.getFirstName()));
    }
  };

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id == user.id &&
        isPressed == user.isPressed &&
        Objects.equals(profileUrl, user.profileUrl) &&
        Objects.equals(firstName, user.firstName) &&
        Objects.equals(lastName, user.lastName) &&
        Objects.equals(emailAddress, user.emailAddress) &&
        Objects.equals(password, user.password) &&
        Objects.equals(mobileNumber, user.mobileNumber) &&
        Objects.equals(profileImage, user.profileImage) &&
        Objects.equals(deviceToken, user.deviceToken) &&
        Objects.equals(isUserVerified, user.isUserVerified);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, profileUrl, firstName, lastName, emailAddress, password, mobileNumber, isPressed, profileImage, deviceToken, isUserVerified);
  }
}