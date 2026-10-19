package io.github.farzadski.essential504.Components.Navigation;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

public interface IFragmentNavigationStrategy {
  @IdRes
  int getNavigationItemId();

  String getTag();

  Fragment createFragment();
}
