package com.german.boxtimer.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.german.boxtimer.R;
import com.german.boxtimer.viewModel.RunningTimerViewModel;

public class RunningTimerActivity extends AppCompatActivity {

    RunningTimerViewModel runningTimerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_timer);
    }
}
