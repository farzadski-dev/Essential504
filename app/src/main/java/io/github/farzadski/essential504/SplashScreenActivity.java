package io.github.farzadski.essential504;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import io.github.farzadski.essential504.Components.MyCustomSquircleCardView;

public class SplashScreenActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_splash_screen);

    MyCustomSquircleCardView btn_go = findViewById(R.id.btn_go);

    btn_go.setOnClickListener(
        v -> {
          SplashScreenActivity.this.finish();
          this.startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        });
  }
}
