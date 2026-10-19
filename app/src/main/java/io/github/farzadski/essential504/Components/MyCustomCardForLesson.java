package io.github.farzadski.essential504.Components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import io.github.farzadski.essential504.LessonActivity;
import io.github.farzadski.essential504.ObjectBox.Entities.Lesson;
import io.github.farzadski.essential504.R;
import io.github.farzadski.essential504.RandomColor;
import java.text.MessageFormat;
import java.util.Objects;

public class MyCustomCardForLesson extends MyCustomSquircleCardView {
  private static final String EXTRA_LESSON_ID = "LESSON_ID";
  private static final String EXTRA_LESSON_COLOR = "LESSON_COLOR";
  private static final String NOT_COMPLETED = "\nCompleted";
  private int color;

  private Lesson lesson;

  public MyCustomCardForLesson(Context context) {
    super(context);
    initialize(context);
  }

  public MyCustomCardForLesson(Context context, Lesson lesson) {
    super(context);
    this.lesson = Objects.requireNonNull(lesson, "lesson cannot be null");
    initialize(context);
  }

  private void initialize(Context context) {
    LayoutInflater.from(context).inflate(R.layout.my_custom_card_for_lesson, this, true);
    MyCustomSquircleCardView root = findViewById(R.id.root);
    MyCustomTextViewMediumFont lessonNumber = findViewById(R.id.txt_lesson_number);
    MyCustomTextViewExtraBoldFont lessonStatus = findViewById(R.id.txt_lesson_status);
    MyCustomSquircleCardView goButton = findViewById(R.id.btn_go);
    color = RandomColor.get("40", "50", "60");
    root.setCardBackgroundColor(color);
    bindLesson(lessonNumber, lessonStatus);
    setupClickListener(goButton);
  }

  private void bindLesson(
      MyCustomTextViewMediumFont lessonNumber, MyCustomTextViewExtraBoldFont lessonStatus) {

    if (lesson == null) {
      return;
    }

    lessonNumber.setText(MessageFormat.format("{0}", lesson.getLessonNumber()));

    int completionPercentage = lesson.getCompletionPercentage();

    if (completionPercentage == 100) {
      lessonStatus.setText(R.string.done);
    } else {
      lessonStatus.setText(String.valueOf(completionPercentage).concat("%").concat(NOT_COMPLETED));
    }
  }

  private void setupClickListener(View goButton) {
    goButton.setOnClickListener(v -> openLesson());
  }

  private void openLesson() {
    if (lesson == null) {
      return;
    }

    Context context = getContext();
    Intent intent = new Intent(context, LessonActivity.class);
    intent.putExtra(EXTRA_LESSON_ID, lesson.getId());
    intent.putExtra(EXTRA_LESSON_COLOR, color);
    context.startActivity(intent);
  }

  public Lesson getLesson() {
    return lesson;
  }

  public void setLesson(Lesson lesson) {
    this.lesson = Objects.requireNonNull(lesson, "lesson cannot be null");
    MyCustomTextViewMediumFont lessonNumber = findViewById(R.id.txt_lesson_number);
    MyCustomTextViewExtraBoldFont lessonStatus = findViewById(R.id.txt_lesson_status);
    bindLesson(lessonNumber, lessonStatus);
  }
}
