package io.github.farzadski.essential504.App;

import android.app.Application;
import io.github.farzadski.essential504.ObjectBox.DatabaseSeeder;
import io.github.farzadski.essential504.ObjectBox.ObjectBoxStore;

public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    ObjectBoxStore.init(this);
    DatabaseSeeder.seed(this);
  }
}
