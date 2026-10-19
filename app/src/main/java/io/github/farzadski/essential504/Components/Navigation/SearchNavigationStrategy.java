
package io.github.farzadski.essential504.Components.Navigation;

import androidx.fragment.app.Fragment;
import io.github.farzadski.essential504.Fragments.SearchFragment;
import io.github.farzadski.essential504.R;

public class SearchNavigationStrategy implements IFragmentNavigationStrategy {

    @Override
    public int getNavigationItemId() {
        return R.id.nav_search;
    }

    @Override
    public String getTag() {
        return "search";
    }

    @Override
    public Fragment createFragment() {
        return new SearchFragment();
    }
}
