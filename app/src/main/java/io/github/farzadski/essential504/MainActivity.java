package io.github.farzadski.essential504;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import io.github.farzadski.essential504.Components.BottomNavigationController;
import io.github.farzadski.essential504.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);

    View bottomNavigation = findViewById(R.id.bottom_navigation);
    new BottomNavigationController(this, bottomNavigation);

    if (savedInstanceState == null) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_container, new HomeFragment(), "home")
          .commit();
    }
  }
}
