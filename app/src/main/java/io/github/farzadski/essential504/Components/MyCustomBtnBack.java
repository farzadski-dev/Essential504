package io.github.farzadski.essential504.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import io.github.farzadski.essential504.R;

public class MyCustomBtnBack extends RelativeLayout {

  public MyCustomBtnBack(Context context) {
    super(context);
    init(context);
  }

  public MyCustomBtnBack(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MyCustomBtnBack(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.custom_btn_back, this, true);
    RelativeLayout btn_back = view.findViewById(R.id.btn_back);

    btn_back.setOnClickListener(
        v -> {
          AppCompatActivity ap = (AppCompatActivity) context;
          ap.finish();
        });
  }
}
