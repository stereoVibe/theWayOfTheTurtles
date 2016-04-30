package io.sokolvault13.biggoals;

import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import io.sokolvault13.biggoals.db.HelperFactory;

/**
 * Created by Vault on 26/04/16.
 */
public class BigGoalsActivity2 extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Window window = getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        HelperFactory.setHelper(getApplicationContext());
        return new BigGoalsListFragment();
    }
}
