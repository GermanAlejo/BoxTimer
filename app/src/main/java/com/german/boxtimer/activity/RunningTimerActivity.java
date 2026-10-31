package com.german.boxtimer.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.german.boxtimer.R;
import com.german.boxtimer.TimerApp;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.AppViewModelFactory;
import com.german.boxtimer.viewModel.RunningTimerViewModel;

public class RunningTimerActivity extends AppCompatActivity {

    private RunningTimerViewModel runningTimerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_timer);

        runningTimerViewModel = getRunningTimerViewModel();

        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.resetButton);
        TextView timeText = findViewById(R.id.timeText);

        startButton.setOnClickListener(v -> runningTimerViewModel.start());

        stopButton.setOnClickListener(v -> runningTimerViewModel.reset());

        //Change the state when running
        runningTimerViewModel.isRunning().observe(this, isRunning -> startButton.setEnabled(!isRunning));
    }

    private RunningTimerViewModel getRunningTimerViewModel() {
        long timerId = getIntent().getLongExtra("timerId", -1L);
        //Get the timer from db
        if(timerId == -1) {
            //Log here
            throw new RuntimeException("Timer invalid");
        }

        TimerConfigRepository repo = ((TimerApp) getApplication()).getRepository();

        AppViewModelFactory vmFactory =  new AppViewModelFactory(repo, timerId);
        return vmFactory.create(RunningTimerViewModel.class);
    }
}
