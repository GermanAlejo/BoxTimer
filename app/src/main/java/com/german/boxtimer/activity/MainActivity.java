package com.german.boxtimer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.german.boxtimer.R;
import com.german.boxtimer.TimerApp;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.AppViewModelFactory;
import com.german.boxtimer.viewModel.MainTimerViewModel;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainTimerViewModel mainTimerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.tag(TAG).i("Creating main view");
        // 1. Get the ViewModel
        mainTimerViewModel = getMainTimerViewModel();
        //Get main control views
        View roundControlView = findViewById(R.id.control_round);
        View restControlView = findViewById(R.id.control_rest);
        View numberControlView = findViewById(R.id.control_number_rounds);

        //Button to start timer
        Button startButton = findViewById(R.id.startButton);

        //Get view text to change
        TextView roundTimeText = findViewById(R.id.timeText);
        TextView restTimeText = findViewById(R.id.rest_time);
        TextView numRoundsText = findViewById(R.id.numRounds);

        // 2. Observe state: whenever the ViewModel's value changes, update the screen


        //Open timer view
        openNewView(startButton, RunningTimerActivity.class);

        // 3. Forward clicks: no logic here
        //set button listeners
        bindControl(roundControlView, roundTimeText, mainTimerViewModel.getRoundTime(),
                mainTimerViewModel::increaseRound, mainTimerViewModel::decreaseRound);
        bindControl(restControlView, restTimeText, mainTimerViewModel.getRestTime(),
                mainTimerViewModel::increaseRest,
                mainTimerViewModel::decreaseRest);
        bindControl(numberControlView, numRoundsText, mainTimerViewModel.getNumRounds(),
                mainTimerViewModel::increaseNumRounds, mainTimerViewModel::decreaseNumRounds);
    }

    public <T>void openNewView(Button view, Class<T> targetClass) {
        Timber.tag(TAG).i("Loading new view");
        view.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, targetClass);
            intent.putExtra("timerId", mainTimerViewModel.getTimerCurrentConfig().getUid());
            startActivity(intent);
        });
    }

    private void bindControl(View control, TextView textView, LiveData<Integer> dataValue, Runnable moreFunc, Runnable lessFunc) {
        control.findViewById(R.id.more_button).setOnClickListener(v -> moreFunc.run());
        control.findViewById(R.id.less_button).setOnClickListener(v -> lessFunc.run());

        dataValue.observe(this, number -> textView.setText(String.valueOf(number)));
    }

    private MainTimerViewModel getMainTimerViewModel() {
        TimerConfigRepository repo = ((TimerApp) getApplication()).getRepository();
        return new ViewModelProvider(this, new AppViewModelFactory(repo))
                .get(MainTimerViewModel.class);
    }
}
