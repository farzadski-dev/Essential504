package io.github.farzadski.essential504.ObjectBox.Entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Lesson {
  @Id(assignable = true)
  public long Id;

  public long LessonNumber;
  public ToMany<Word> Words;
  public int _Status = 0;

  public long getId() {
    return Id;
  }

  public void setId(long id) {
    Id = id;
  }

  public long getLessonNumber() {
    return LessonNumber;
  }

  public void setLessonNumber(long lessonNumber) {
    LessonNumber = lessonNumber;
  }

  public ToMany<Word> getWords() {
    return Words;
  }

  public void setWords(ToMany<Word> words) {
    Words = words;
  }

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

  public int getCompletionPercentage() {
    if (Words == null || Words.isEmpty()) {
      return 0;
    }

    int completedWords = 0;

    for (Word word : Words) {
      if (word.getStatus() == Status.DONE) {
        completedWords++;
      }
    }

    return Math.round((completedWords * 100f) / Words.size());
  }
}
