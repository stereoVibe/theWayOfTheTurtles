package io.sokolvault13.biggoals;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    private Toolbar mToolBar;
    private int mMainLayout;
    private int mToolbarResource;
    private int mToolbarMenu;
    private int mToolbarLayout;
    private boolean mHomeAsUp = false;

    public final void assignResources(int mainLayout,
                                      int toolbarLayout,
                                      int toolbarResource,
                                      int toolbarMenu,
                                      boolean homeAsUp){
        this.mMainLayout = mainLayout;
        this.mToolbarLayout = toolbarLayout;
        this.mToolbarResource = toolbarResource;
        this.mToolbarMenu = toolbarMenu;
        this.mHomeAsUp = homeAsUp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mMainLayout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        mToolBar = (Toolbar) findViewById(mToolbarResource);
        setSupportActionBar(mToolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        ActionBar actionBar = getSupportActionBar();

        LayoutInflater inflater = LayoutInflater.from(this);
        getMenuInflater().inflate(mToolbarMenu, menu);

        View view = inflater.inflate(mToolbarLayout, null);

        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);

            if (mHomeAsUp){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

//            actionBar.setHomeButtonEnabled(true);
            actionBar.setCustomView(view);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
