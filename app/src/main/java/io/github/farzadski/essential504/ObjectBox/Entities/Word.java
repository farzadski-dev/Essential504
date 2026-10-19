package io.github.farzadski.essential504.ObjectBox.Entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Word {
  @Id(assignable = true)
  public long Id;

  public long LessonId;
  public long LessonNumber;
  public String Word;
  public String Meaning;

  public int _Status = 0;

  public Status getStatus() {
    for (Status status : Status.values()) {
      if (status.getCode() == _Status) {
        return status;
      }
    }

    return Status.NOT_DONE;
  }

  public void setStatus(Status status) {
    if (status == null) {
      throw new IllegalArgumentException("Status cannot be null");
    }

    _Status = status.getCode();
  }
}
