package io.github.farzadski.essential504.Components.Navigation;

import android.util.SparseArray;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import io.github.farzadski.essential504.R;
import java.util.List;

public class FragmentNavigationContext {
  private final FragmentActivity activity;
  private final SparseArray<IFragmentNavigationStrategy> strategies = new SparseArray<>();

  public FragmentNavigationContext(
      FragmentActivity activity, List<IFragmentNavigationStrategy> strategies) {
    this.activity = activity;

    for (IFragmentNavigationStrategy strategy : strategies) {
      this.strategies.put(strategy.getNavigationItemId(), strategy);
    }
  }

  public void navigateTo(@IdRes int itemId) {
    IFragmentNavigationStrategy strategy = strategies.get(itemId);

    if (strategy == null) {
      return;
    }

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    Fragment current = fragmentManager.findFragmentById(R.id.fragment_container);

    if (current != null && strategy.getTag().equals(current.getTag())) {
      return;
    }

    Fragment existing = fragmentManager.findFragmentByTag(strategy.getTag());
    Fragment destination = existing != null ? existing : strategy.createFragment();
    fragmentManager
        .beginTransaction()
        .setReorderingAllowed(true)
        .replace(R.id.fragment_container, destination, strategy.getTag())
        .commit();
  }
}
