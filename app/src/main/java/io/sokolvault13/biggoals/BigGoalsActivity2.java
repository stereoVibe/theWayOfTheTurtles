package io.sokolvault13.biggoals;

import android.support.v4.app.Fragment;

/**
 * Created by Vault on 26/04/16.
 */
public class BigGoalsActivity2 extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new BigGoalsListFragment();
    }
}
