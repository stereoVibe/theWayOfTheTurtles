package io.sokolvault13.biggoals;

import android.media.Image;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;

import io.sokolvault13.biggoals.db.HelperFactory;

public class BigGoalsActivity extends AppCompatActivity {

    private NumberProgressBar mProgressBar;
    private int mProgessStatus = 0;
    private Button mIncreaseButton;
    private Button mDecreaseButton;
    private ImageView mPlusButton;
    private TextView mTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.big_goals_list_toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (NumberProgressBar) findViewById(R.id.progressBar);
        mIncreaseButton = (Button) findViewById(R.id.increaseButton);
        mDecreaseButton = (Button) findViewById(R.id.decreaseButton);

        mIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.incrementProgressBy(1);
            }
        });

        mDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.incrementProgressBy(-1);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
        ActionBar actionBar = getSupportActionBar();

        LayoutInflater inflater = LayoutInflater.from(this);
        getMenuInflater().inflate(R.menu.action_bar, menu);

        View view = inflater.inflate(R.layout.action_bar_layout, null);

        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setCustomView(view);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        mPlusButton = (ImageView) findViewById(R.id.imageButton);
        mTextButton = (TextView) findViewById(R.id.textView);

        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BigGoalsActivity.this, "plusButton clicked", Toast.LENGTH_LONG).show();
            }
        });

        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BigGoalsActivity.this, "textButton clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HelperFactory.setHelper(getApplicationContext());
        mProgressBar.setProgress(10);
    }

    @Override
    protected void onStop() {
        HelperFactory.releaseHelper();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
