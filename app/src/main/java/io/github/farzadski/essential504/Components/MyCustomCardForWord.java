package io.github.farzadski.essential504.Components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.github.farzadski.essential504.ObjectBox.Entities.Status;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import io.github.farzadski.essential504.R;
import io.github.farzadski.essential504.WordActivity;

public class MyCustomCardForWord extends ConstraintLayout {

  private static final String EXTRA_WORD_ID = "WORD_ID";
  private static final String EXTRA_LESSON_COLOR = "LESSON_COLOR";
  private final int lessonColor;
  private MyCustomSquircleCardView lessonColorView;
  private MyCustomTextViewExtraBoldFont txtWord;
  private AppCompatImageView imgIsDone;
  private AppCompatImageView imgGo;
  private MyCustomTextViewMediumFont txtMeaning;

  public MyCustomCardForWord(@NonNull Context context) {
    this(context, null, 0);
  }

  public MyCustomCardForWord(@NonNull Context context, Word word, int lessonColor) {
    super(context);
    this.lessonColor = lessonColor;
    inflateLayout(context);
    cacheViews();
    applyLessonColor(lessonColor);
    setWord(word);
  }

  public void setWord(Word word) {
    if (word == null) {
      clear();
      return;
    }
    bindWord(word);
  }

  private void inflateLayout(Context context) {
    LayoutInflater.from(context).inflate(R.layout.my_custom_card_for_word, this, true);
  }

  private void cacheViews() {
    lessonColorView = findViewById(R.id._lesson_color);
    txtWord = findViewById(R.id.txt_word);
    imgIsDone = findViewById(R.id.img_is_done);
    imgGo = findViewById(R.id.img_go);
    txtMeaning = findViewById(R.id.txt_meaning);
  }

  private void applyLessonColor(int color) {
    lessonColorView.setCardBackgroundColor(color);
  }

  private void clear() {
    txtWord.setText("");
    txtMeaning.setText("");
    imgIsDone.setVisibility(GONE);
  }

  private void bindWord(Word word) {
    txtWord.setText(word.Word);
    txtMeaning.setText(word.Meaning);
    bindDoneIcon(word);

    imgGo.setOnClickListener(
        v -> {
          Intent i = new Intent(super.getContext(), WordActivity.class);
          i.putExtra(EXTRA_WORD_ID, word.Id);
          i.putExtra(EXTRA_LESSON_COLOR, lessonColor);
          super.getContext().startActivity(i);
        });
  }

  private void bindDoneIcon(Word word) {
    imgIsDone.setVisibility(isDone(word) ? VISIBLE : GONE);
  }

  private boolean isDone(Word word) {
    return Status.DONE.equals(word.getStatus());
  }

  public void setSearchMode() {
    txtMeaning.setVisibility(GONE);
    imgGo.setVisibility(GONE);
  }

  public void applySearchWord(Word word, int color) {
    applyLessonColor(color);
    setSearchMode();
    setWord(word);
  }

  public void setImgGoVisibility(int visibility) {
    imgGo.setVisibility(visibility);
  }

  public void setDoneIcon(Word word) {
    imgIsDone.setVisibility(isDone(word) ? VISIBLE : GONE);
  }
}
