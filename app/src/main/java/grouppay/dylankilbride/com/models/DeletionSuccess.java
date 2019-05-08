package grouppay.dylankilbride.com.models;

public class DeletionSuccess {

  private boolean participantDelete;
  private boolean groupDelete;

  public DeletionSuccess(boolean participantDelete, boolean groupDelete) {
    this.participantDelete = participantDelete;
    this.groupDelete = groupDelete;
  }

  public DeletionSuccess() {}

  public boolean isParticipantDelete() {
    return participantDelete;
  }

  public void setParticipantDelete(boolean participantDelete) {
    this.participantDelete = participantDelete;
  }

  public boolean isGroupDelete() {
    return groupDelete;
  }

  public void setGroupDelete(boolean groupDelete) {
    this.groupDelete = groupDelete;
  }
}
