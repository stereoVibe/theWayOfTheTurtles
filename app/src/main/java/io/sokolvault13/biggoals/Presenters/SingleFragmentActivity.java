package io.sokolvault13.biggoals.Presenters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import io.sokolvault13.biggoals.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    private Toolbar mToolBar;
    private int mMainLayout;
    private int mToolbarResource;
    private int mToolbarMenu;
    private int mToolbarLayout;
    private String mTag;
    private boolean mHomeAsUp = false;

    public final void assignResources(int mainLayout,
                                      int toolbarLayout,
                                      int toolbarResource,
                                      int toolbarMenu,
                                      String tag,
                                      boolean homeAsUp){
        this.mMainLayout = mainLayout;
        this.mToolbarLayout = toolbarLayout;
        this.mToolbarResource = toolbarResource;
        this.mToolbarMenu = toolbarMenu;
        this.mTag = tag;
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
                    .add(R.id.fragmentContainer, fragment, mTag)
                    .commit();
        }

        mToolBar = (Toolbar) findViewById(mToolbarResource);
        setSupportActionBar(mToolBar);

        if (mHomeAsUp){
            mToolBar.setNavigationIcon(R.drawable.ic_arrow_left);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

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
