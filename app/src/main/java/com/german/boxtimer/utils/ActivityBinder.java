package com.german.boxtimer.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.german.boxtimer.R;
import com.german.boxtimer.entity.timer.TimeSpan;
import timber.log.Timber;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Class to abstract common bindings and listeners to the Activity classes
 */
public class ActivityBinder {
    private static final String TAG = "ActivityBinder";
    private final Activity activity;
    private final LifecycleOwner lifecycleOwner;

    public ActivityBinder(Activity activity, LifecycleOwner lifecycleOwner) {
        this.activity = activity;
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * Interface used to abstract method setTimer as a BiConsumer
     */
    private interface OnTimePicked {
        //This abstracts the setTimer(min, sec)
        void onPicked(int minutes, int seconds);
    }

    /**
     * Function to bind control of the 2 buttons more/less to increment decrement time
     *
     * @param control view holding the buttons
     * @param textView Text element holding the text with the time value
     * @param dataValue LiveData of the element to update
     * @param moreFunc function to increment the time
     * @param lessFunc function to decrement the time
     */
    public void bindControl(View control, TextView textView, LiveData<String> dataValue, Runnable moreFunc, Runnable lessFunc) {
        control.findViewById(R.id.more_button).setOnClickListener(v -> moreFunc.run());
        control.findViewById(R.id.less_button).setOnClickListener(v -> lessFunc.run());

        dataValue.observe(lifecycleOwner, textView::setText);
    }

    /**
     * Abstracted function to open a view from a button with an untermined number of variables passed <br>
     * uses setOnClickListener
     *
     * @param view Button to set the event on
     * @param targetClass class implementing the view
     * @param updater function that updates needed values in the viewModel if needed
     * @param valueMap map representing the name as key as the name to set up and the value as the actual value to put
     * @param <T> Class type of the target
     */
    public <T> void openRunningTimerView(Button view, Class<T> targetClass, Runnable updater,
                                         Map<String , Supplier<Serializable>> valueMap) {
        Timber.tag(TAG).i("Loading new view to: %s", targetClass.getName());
        view.setOnClickListener(v -> {
            updater.run();
            Intent intent = new Intent(activity.getApplicationContext(), targetClass);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            valueMap.forEach((name, serializableSupplier) -> intent.putExtra(name, serializableSupplier.get()));
            activity.getApplicationContext().startActivity(intent);
        });
    }

    /**
     * Function to bind the timer picker to a listener event
     *
     * @param titleText text to show in the picker
     * @param timeLabel element to listen to click
     * @param getTimerValue function to return the value from the viewModel
     * @param timerSet function to set the new value using minutes and seconds
     */
    public void bindPickerControl(String titleText, TextView timeLabel, Supplier<TimeSpan> getTimerValue,
                                  BiConsumer<Integer, Integer> timerSet) {
        timeLabel.setOnClickListener(view -> {
            TimeSpan timer = getTimerValue.get();
            if (timer == null) {
                Timber.tag(TAG).e("Timer not available");
                throw new RuntimeException("Round timer not found");
            }
            showTimePicker(
                    titleText,
                    timer.getMinutes(),
                    timer.getSeconds(),
                    timerSet::accept
            );
        });
    }

    /**
     * Method to build the picker for interactive text
     *
     * @param title text to display
     * @param minutes new minutes value
     * @param seconds new seconds value
     * @param callback this is the timer (later TimeSpan) set method abstracted
     */
    private void showTimePicker(String title, int minutes, int seconds, OnTimePicked callback) {
        View content = activity.getLayoutInflater().inflate(R.layout.dialog_timer_picker, null);
        NumberPicker minutesPicker = content.findViewById(R.id.picker_minutes);
        NumberPicker secondsPicker = content.findViewById(R.id.picker_seconds);

        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(99);
        minutesPicker.setValue(minutes);

        // Steps of 5 seconds: the picker works with indexes 0..11, displayedValues shows 00..55
        String[] secondLabels = new String[12];
        for (int i = 0; i < 12; i++)
            secondLabels[i] = String.format(
                    Locale.getDefault(), UtilConstants.TimeConstants.PICKER_FORMAT,
                    i * UtilConstants.TimeConstants.MAX_SECONDS_CHANGE
            );
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(11);
        secondsPicker.setDisplayedValues(secondLabels);
        secondsPicker.setValue(seconds / 5);

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(content)
                .setPositiveButton(android.R.string.ok, (d, which) ->
                        callback.onPicked(
                                minutesPicker.getValue(),
                                secondsPicker.getValue() * UtilConstants.TimeConstants.MAX_SECONDS_CHANGE)
                )
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

}
