package com.german.boxtimer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.german.boxtimer.R;
import com.german.boxtimer.viewModel.RunningTimerViewModel;


public class MainActivity extends AppCompatActivity {

    private RunningTimerViewModel runningTimerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Get the ViewModel
        runningTimerViewModel = new ViewModelProvider(this).get(RunningTimerViewModel.class);
        View roundControlView = findViewById(R.id.control_round);
        View restControlView = findViewById(R.id.control_rest);
        View numberControlView = findViewById(R.id.control_number_rounds);

        ImageView plusRoundControl = roundControlView.findViewById(R.id.more_button);
        ImageView lessRoundControl = roundControlView.findViewById(R.id.less_button);

        TextView roundTimeText = findViewById(R.id.timeText);

        Button startButton = findViewById(R.id.startButton);

        // 2. Observe state: whenever the ViewModel's value changes, update the screen
        runningTimerViewModel.isRunning().observe(this, isRunning -> startButton.setEnabled(!isRunning));

        // 3. Forward clicks: no logic here
        startButton.setOnClickListener(v -> {
            //runningTimerViewModel.start();
            Intent intent = new Intent(MainActivity.this, RunningTimerActivity.class);
            startActivity(intent);
        });
        plusRoundControl.setOnClickListener(v -> runningTimerViewModel.increaseButton());
        lessRoundControl.setOnClickListener(v -> runningTimerViewModel.decreaseButton());
        runningTimerViewModel.getValButton().observe(this, number -> roundTimeText.setText(String.valueOf(number)));
    }

    public void openTimerView(View view) {

    }
}
