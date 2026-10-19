package io.github.farzadski.essential504.ObjectBox.Entities;

public enum Status {
  NOT_DONE(0, "Not Done"),
  DONE(1, "Done");

  private final int code;
  private final String message;

  Status(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
