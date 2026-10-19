package io.github.farzadski.essential504;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import io.github.farzadski.essential504.Components.MyCustomBtnBack;
import io.github.farzadski.essential504.Components.MyCustomCardForWord;
import io.github.farzadski.essential504.Components.MyCustomTextViewExtraBoldFont;
import io.github.farzadski.essential504.ObjectBox.Entities.Status;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import io.github.farzadski.essential504.ObjectBox.ObjectBoxStore;
import io.objectbox.Box;

public class WordActivity extends AppCompatActivity {
  private static final String EXTRA_WORD_ID = "WORD_ID";
  private static final String EXTRA_LESSON_COLOR = "LESSON_COLOR";

  private Word word;
  private MyCustomCardForWord card;
  private MyCustomTextViewExtraBoldFont txtBtnDone;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupWindow();
    loadWord();
    setupViews();
  }

  private void setupWindow() {
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_word);
  }

  private void setupViews() {
    setupBackBtn();
    setupWordCard();
    setupDoneButton();
  }

  private void setupBackBtn() {
    FrameLayout container = findViewById(R.id.layout_for_btn_back);
    container.addView(new MyCustomBtnBack(this));
  }

  private void setupWordCard() {
    int color = getIntent().getIntExtra(EXTRA_LESSON_COLOR, 0);
    card = new MyCustomCardForWord(this, word, color);
    card.setImgGoVisibility(View.GONE);
    ((FrameLayout) findViewById(R.id.layout_word)).addView(card);
  }

  private void setupDoneButton() {
    txtBtnDone = findViewById(R.id.txt_btn_done);
    renderDoneLabel();
    findViewById(R.id.btn_done).setOnClickListener(v -> toggleDone());
  }

  private void loadWord() {
    long id = getIntent().getLongExtra(EXTRA_WORD_ID, 0L);
    word = wordBox().get(id);
  }

  private Box<Word> wordBox() {
    return ObjectBoxStore.get().boxFor(Word.class);
  }

  private void toggleDone() {
    word.setStatus(isDone() ? Status.NOT_DONE : Status.DONE);
    wordBox().put(word);
    card.setDoneIcon(word);
    renderDoneLabel();
  }

  private void renderDoneLabel() {
    txtBtnDone.setText(isDone() ? R.string.un_done : R.string.done);
  }

  private boolean isDone() {
    return Status.DONE.equals(word.getStatus());
  }
}
