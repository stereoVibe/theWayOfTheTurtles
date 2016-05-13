package io.sokolvault13.biggoals;

import android.support.v4.app.Fragment;

public class SubGoalsListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SubGoalsListFragment();
    }

}
