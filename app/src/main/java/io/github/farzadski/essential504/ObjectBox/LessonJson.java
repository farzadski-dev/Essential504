package io.github.farzadski.essential504.ObjectBox;

import java.util.List;

public class LessonJson {
  public long Id;
  public long LessonNumber;
  public List<WordJson> Words;

  public static class WordJson {
    public long Id;
    public long LessonId;
    public long LessonNumber;
    public String Word;
    public String Meaning;
  }
}
