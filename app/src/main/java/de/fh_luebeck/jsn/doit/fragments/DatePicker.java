package de.fh_luebeck.jsn.doit.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 14.04.2017.
 */

public class DatePicker extends DialogFragment {


    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Date defaultDate;

    public DatePicker() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        Calendar c = Calendar.getInstance();
        c.setTime(defaultDate);

        return new DatePickerDialog(getActivity(), onDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }
}
