package de.fh_luebeck.jsn.doit.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 03.07.2017.
 */

public class TimePicker extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener onDateSetListener;
    private Date defaultDate;

    public TimePicker() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        Calendar c = Calendar.getInstance();
        c.setTime(defaultDate);

        return new TimePickerDialog(getActivity(), onDateSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
    }

    public void setOnDateSetListener(TimePickerDialog.OnTimeSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }
}
