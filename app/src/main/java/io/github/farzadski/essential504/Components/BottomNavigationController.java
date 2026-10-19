package io.github.farzadski.essential504.Components;

import android.view.View;
import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import io.github.farzadski.essential504.Components.Navigation.FavoritesNavigationStrategy;
import io.github.farzadski.essential504.Components.Navigation.FragmentNavigationContext;
import io.github.farzadski.essential504.Components.Navigation.HomeNavigationStrategy;
import io.github.farzadski.essential504.Components.Navigation.SearchNavigationStrategy;
import io.github.farzadski.essential504.R;
import java.util.Arrays;

public class BottomNavigationController {
  private final View root;
  private final FragmentNavigationContext navigationContext;

  private int selectedItemId = R.id.nav_home;

  public BottomNavigationController(FragmentActivity activity, View root) {
    this.root = root;
    navigationContext =
        new FragmentNavigationContext(
            activity,
            Arrays.asList(
                new HomeNavigationStrategy(),
                new SearchNavigationStrategy(),
                new FavoritesNavigationStrategy()));
    bindNavigation();
    select(R.id.nav_home);
  }

  private void bindNavigation() {
    bind(R.id.nav_home);
    bind(R.id.nav_search);
    bind(R.id.nav_favorites);
  }

  private void bind(@IdRes int itemId) {
    root.findViewById(itemId).setOnClickListener(view -> select(itemId));
  }

  public void select(@IdRes int itemId) {
    selectedItemId = itemId;
    setIndicator(R.id.indicator_home, itemId == R.id.nav_home);
    setIndicator(R.id.indicator_search, itemId == R.id.nav_search);
    setIndicator(R.id.indicator_favorites, itemId == R.id.nav_favorites);
    navigationContext.navigateTo(itemId);
  }

  private void setIndicator(int viewId, boolean selected) {
    root.findViewById(viewId).setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
  }

  public int getSelectedItemId() {
    return selectedItemId;
  }
}
