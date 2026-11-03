package com.german.boxtimer.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.german.boxtimer.R;
import com.german.boxtimer.TimerApp;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.AppViewModelFactory;
import com.german.boxtimer.utils.TimeConstants;
import com.german.boxtimer.viewModel.RunningTimerViewModel;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RunningTimerActivity extends AppCompatActivity {

    private static final String TAG = "RunningTimerActivity";
    private RunningTimerViewModel runningTimerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_timer);
        Timber.tag(TAG).i("Creating running timer view");

        runningTimerViewModel = getRunningTimerViewModel();

        //control Buttons
        Button startButton = findViewById(R.id.startButton);
        Button resetButton = findViewById(R.id.resetButton);
        Button pauseButton = findViewById(R.id.pauseButton);

        //timer texts
        TextView runningTimerText = findViewById(R.id.runningTimerText);
        TextView numRoundsTimerText = findViewById(R.id.roundTimerText);
        TextView restTimerText = findViewById(R.id.rest_timer_left);
        TextView totalTimeText = findViewById(R.id.total_time);
        LinearLayout runningTimeContainer = findViewById(R.id.running_timer_container);
        LinearLayout restTimeContainer = findViewById(R.id.rest_time_container);

        //Button listeners
        resetButton.setOnClickListener(v -> {
            //buttons
            startButton.setVisibility(VISIBLE);
            pauseButton.setVisibility(GONE);
            //text
            restTimeContainer.setVisibility(GONE);
            runningTimeContainer.setVisibility(VISIBLE);

            //Reset data
            runningTimerViewModel.reset();
        });
        startButton.setOnClickListener(v -> {
            startButton.setVisibility(GONE);
            pauseButton.setVisibility(VISIBLE);
            runningTimerViewModel.start();
        });
        pauseButton.setOnClickListener(v -> {
            startButton.setVisibility(VISIBLE);
            pauseButton.setVisibility(GONE);
            runningTimerViewModel.pause();
        });

        //Change the state when running
        runningTimerViewModel.isRunning().observe(this, isRunning -> startButton.setEnabled(!isRunning));
        runningTimerViewModel.isRunning().observe(this, pauseButton::setEnabled);
        runningTimerViewModel.getTotalTimeText().observe(this, totalTimeText::setText);
        runningTimerViewModel.getDisplayTimeText().observe(this, runningTimerText::setText);
        runningTimerViewModel.getNumRoundsLeftText().observe(this, numRoundsTimerText::setText);
        runningTimerViewModel.getRestTimeLeftText().observe(this, restTimerText::setText);
        //Observe rest
        runningTimerViewModel.isResting().observe(this, isResting -> {
            if (isResting) {
                restTimeContainer.setVisibility(VISIBLE);
                runningTimeContainer.setVisibility(GONE);
            } else {
                restTimeContainer.setVisibility(GONE);
                runningTimeContainer.setVisibility(VISIBLE);
            }
        });
        //ending events
        runningTimerViewModel.isEnded().observe(this, isEnded -> {
            if (isEnded) {
                startButton.setVisibility(VISIBLE);
                startButton.setEnabled(false);
                pauseButton.setVisibility(GONE);
                runningTimeContainer.setVisibility(VISIBLE);
                restTimeContainer.setVisibility(GONE);
            }
        });
    }

    private RunningTimerViewModel getRunningTimerViewModel() {
        Timber.tag(TAG).i("Getting timer from main activity");
        long timerId = getIntent().getLongExtra(TimeConstants.DEFAULT_TIMER_ID, -1L);
        //Get the timer from db
        if (timerId == -1) {
            Timber.tag(TAG).e("Error - Invalid uid");
            throw new RuntimeException("Timer invalid");
        }

        TimerConfigRepository repo = ((TimerApp) getApplication()).getRepository();

        AppViewModelFactory vmFactory = new AppViewModelFactory(repo, timerId);
        return new ViewModelProvider(this, vmFactory).get(RunningTimerViewModel.class);
    }
}
