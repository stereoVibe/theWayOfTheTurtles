package io.sokolvault13.biggoals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.daimajia.numberprogressbar.NumberProgressBar;

import io.sokolvault13.biggoals.db.HelperFactory;

public class BigGoalsActivity extends AppCompatActivity {

    private NumberProgressBar mProgressBar;
    private int mProgessStatus = 0;
    private Button mIncreaseButton;
    private Button mDecreaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_goals);

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

}
