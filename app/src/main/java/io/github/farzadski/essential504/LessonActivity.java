package io.github.farzadski.essential504;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.github.farzadski.essential504.Adapters.WordAdapter;
import io.github.farzadski.essential504.Components.MyCustomBtnBack;
import io.github.farzadski.essential504.Components.MyCustomTextViewExtraBoldFont;
import io.github.farzadski.essential504.ObjectBox.Entities.Lesson;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import io.github.farzadski.essential504.ObjectBox.ObjectBoxStore;
import io.objectbox.relation.ToMany;

public class LessonActivity extends AppCompatActivity {
  private static final String EXTRA_LESSON_ID = "LESSON_ID";
  private static final String EXTRA_LESSON_COLOR = "LESSON_COLOR";

  private int color;
  private Lesson lesson;
  private ToMany<Word> words;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_lesson);
    setInputs();
    initViews();
  }

  private void setInputs() {
    color = getIntent().getIntExtra(EXTRA_LESSON_COLOR, 0);
    lesson =
        ObjectBoxStore.get()
            .boxFor(Lesson.class)
            .get(getIntent().getLongExtra(EXTRA_LESSON_ID, 0L));
    words = lesson.getWords();
  }

  private void initViews() {
    setWords();
    setBackBtn();
    setLessonNumber();
  }

  private void setLessonNumber() {
    MyCustomTextViewExtraBoldFont txt = findViewById(R.id.txt_lesson_number);
    txt.setText(String.valueOf(lesson.getLessonNumber()));
  }

  private void setWords() {
    RecyclerView list = findViewById(R.id.recycler_words);
    list.setAdapter(new WordAdapter(words, color));
  }

  private void setBackBtn() {
    FrameLayout container = findViewById(R.id.layout_for_btn_back);
    container.addView(new MyCustomBtnBack(this));
  }

  @Override
  protected void onResume() {
    super.onResume();
    setInputs();
    initViews();
  }
}
