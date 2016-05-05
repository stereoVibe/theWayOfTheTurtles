package io.sokolvault13.biggoals;

import android.support.v4.app.Fragment;

/**
 * Created by Vault on 04/05/16.
 */
public class BigGoalsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BigGoalsFragment();
    }

}
