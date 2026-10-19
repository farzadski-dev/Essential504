package io.github.farzadski.essential504.ObjectBox;

import android.content.Context;
import io.github.farzadski.essential504.ObjectBox.Entities.MyObjectBox;
import io.objectbox.BoxStore;

public class ObjectBoxStore {
  private static BoxStore store;

  public static void init(Context context) {
    store = MyObjectBox.builder().androidContext(context).build();
  }

  public static BoxStore get() {
    return store;
  }
}
