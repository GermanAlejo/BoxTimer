package com.german.boxtimer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.german.boxtimer.R;
import com.german.boxtimer.TimerApp;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.ActivityBinder;
import com.german.boxtimer.utils.AppViewModelFactory;
import com.german.boxtimer.utils.UtilConstants;
import com.german.boxtimer.viewModel.MainTimerViewModel;
import timber.log.Timber;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


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
        // get the activity binder
        ActivityBinder binder = new ActivityBinder(this, this);

        //Get main control views
        final View roundControlView = findViewById(R.id.control_round);
        final View restControlView = findViewById(R.id.control_rest);
        final View numberControlView = findViewById(R.id.control_number_rounds);

        //Button to start timer
        final Button startButton = findViewById(R.id.start_button);

        //Get view text to change
        final TextView totalTimeText = findViewById(R.id.total_time);
        final TextView roundTimeText = findViewById(R.id.time_text);
        final TextView restTimeText = findViewById(R.id.rest_time);
        final TextView numRoundsText = findViewById(R.id.num_rounds);

        // 2. Observe state: whenever the ViewModel's value changes, update the screen
        mainTimerViewModel.getTotalTimeText().observe(this, totalTimeText::setText);

        //Open timer view
        binder.openRunningTimerView(startButton, RunningTimerActivity.class,
                mainTimerViewModel::updateTimerData, getRunningActivityUI());

        // 3. Forward clicks: no logic here
        //set button listeners
        binder.bindControl(roundControlView, roundTimeText, mainTimerViewModel.getRoundTime(),
                mainTimerViewModel::increaseRound, mainTimerViewModel::decreaseRound);
        binder.bindControl(restControlView, restTimeText, mainTimerViewModel.getRestTime(),
                mainTimerViewModel::increaseRest,
                mainTimerViewModel::decreaseRest);
        binder.bindControl(numberControlView, numRoundsText, mainTimerViewModel.getNumRounds(),
                mainTimerViewModel::increaseNumRounds, mainTimerViewModel::decreaseNumRounds);

        //get texts from layut
        final TextView roundTitle = findViewById(R.id.round_time_text);
        final TextView restTitle = findViewById(R.id.rest_time_text);

        //Set popUps for time set
        binder.bindPickerControl(roundTitle.getText().toString(),
                roundTimeText,
                () -> mainTimerViewModel.getRoundTimeValue().getValue(),
                mainTimerViewModel::setRoundTime);
        binder.bindPickerControl(restTitle.getText().toString(),
                restTimeText,
                () -> mainTimerViewModel.getRestTimeValue().getValue(),
                mainTimerViewModel::setRestTime);
    }

    private MainTimerViewModel getMainTimerViewModel() {
        TimerConfigRepository repo = ((TimerApp) getApplication()).getRepository();
        return new ViewModelProvider(this, new AppViewModelFactory(repo))
                .get(MainTimerViewModel.class);
    }

    private Map<String, Supplier<Serializable>> getRunningActivityUI() {
        Map<String, Supplier<Serializable>> valueMap = new HashMap<>();
        valueMap.put(UtilConstants.TimeConstants.DEFAULT_TIMER_ID,
                () -> mainTimerViewModel.getTimerCurrentConfig().getUid());
        return valueMap;
    }
}
