
package io.github.farzadski.essential504.Components.Navigation;

import androidx.fragment.app.Fragment;
import io.github.farzadski.essential504.Fragments.HomeFragment;
import io.github.farzadski.essential504.R;

public class HomeNavigationStrategy implements IFragmentNavigationStrategy {

    @Override
    public int getNavigationItemId() {
        return R.id.nav_home;
    }

    @Override
    public String getTag() {
        return "home";
    }

    @Override
    public Fragment createFragment() {
        return new HomeFragment();
    }
}
