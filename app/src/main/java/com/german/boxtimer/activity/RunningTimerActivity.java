package com.german.boxtimer.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.german.boxtimer.R;
import com.german.boxtimer.TimerApp;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.AppViewModelFactory;
import com.german.boxtimer.viewModel.RunningTimerViewModel;
import timber.log.Timber;

public class RunningTimerActivity extends AppCompatActivity {

    private static final String TAG = "RunningTimerActivity";
    private RunningTimerViewModel runningTimerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_timer);
        Timber.tag(TAG).i("Creating running timer view");

        runningTimerViewModel = getRunningTimerViewModel();

        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.resetButton);
        TextView runningTimerText = findViewById(R.id.runningTimerText);

        startButton.setOnClickListener(v -> runningTimerViewModel.start());

        stopButton.setOnClickListener(v -> runningTimerViewModel.reset());

        //Change the state when running
        runningTimerViewModel.isRunning().observe(this, isRunning -> startButton.setEnabled(!isRunning));
        runningTimerViewModel.getDisplayTimeText().observe(this, runningTimerText::setText);
    }

    private RunningTimerViewModel getRunningTimerViewModel() {
        Timber.tag(TAG).i("Getting timer from main activity");
        long timerId = getIntent().getLongExtra("timerId", -1L);
        //Get the timer from db
        if(timerId == -1) {
            Timber.tag(TAG).e("Error - Invalid uid");
            throw new RuntimeException("Timer invalid");
        }

        TimerConfigRepository repo = ((TimerApp) getApplication()).getRepository();

        AppViewModelFactory vmFactory =  new AppViewModelFactory(repo, timerId);
        return new ViewModelProvider(this, vmFactory).get(RunningTimerViewModel.class);
    }
}
