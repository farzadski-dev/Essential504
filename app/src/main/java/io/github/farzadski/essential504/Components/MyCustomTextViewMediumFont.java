package io.github.farzadski.essential504.Components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class MyCustomTextViewMediumFont extends AppCompatTextView {
  private static final String DEFAULT_FONT = "fonts/montserrat_medium.ttf";

  public MyCustomTextViewMediumFont(Context context) {
    super(context);
    init(context);
  }

  public MyCustomTextViewMediumFont(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MyCustomTextViewMediumFont(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    Typeface face = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT);
    this.setTypeface(face);
  }
}
