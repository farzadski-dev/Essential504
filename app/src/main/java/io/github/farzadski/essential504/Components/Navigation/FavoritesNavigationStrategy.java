
package io.github.farzadski.essential504.Components.Navigation;

import androidx.fragment.app.Fragment;
import io.github.farzadski.essential504.Fragments.FavoritesFragment;
import io.github.farzadski.essential504.R;

public class FavoritesNavigationStrategy implements IFragmentNavigationStrategy {

    @Override
    public int getNavigationItemId() {
        return R.id.nav_favorites;
    }

    @Override
    public String getTag() {
        return "favorites";
    }

    @Override
    public Fragment createFragment() {
        return new FavoritesFragment();
    }
}
