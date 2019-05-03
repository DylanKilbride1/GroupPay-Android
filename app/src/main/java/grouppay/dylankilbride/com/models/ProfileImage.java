package grouppay.dylankilbride.com.models;

public class ProfileImage {

  private long imageId;

  private String profileImageLocation;

  public ProfileImage(String profileImageLocation) {
    this.profileImageLocation = profileImageLocation;
  }

  public ProfileImage() {}

  public String getProfileImageLocation() {
    return profileImageLocation;
  }

  public void setProfileImageLocation(String profileImageLocation) {
    this.profileImageLocation = profileImageLocation;
  }

  public long getImageId() {
    return imageId;
  }

  public void setImageId(long imageId) {
    this.imageId = imageId;
  }
}
