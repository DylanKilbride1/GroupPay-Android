package grouppay.dylankilbride.com.models;

public class ImageUploadResponse {

  private String success;
  private String fileUrl;

  public ImageUploadResponse(String success, String fileUrl) {
    this.success = success;
    this.fileUrl = fileUrl;
  }

  public ImageUploadResponse(String success) {
    this.success = success;
  }

  public String getSuccess() {
    return success;
  }

  public String getFileUrl() {
    return fileUrl;
  }
}
