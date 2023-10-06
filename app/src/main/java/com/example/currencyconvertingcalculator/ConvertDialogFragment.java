package com.example.currencyconvertingcalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class ConvertDialogFragment extends DialogFragment {
    private RadioGroup fromRecyclerRadioGroup;
    private RadioGroup toRecyclerRadioGroup;
    private final String TAG = this.getClass().getSimpleName();
    private String from = "";
    private String to = "";

    public interface OnInputListener {
        void sendInput(String from, String to);
    }
    public OnInputListener mOnInputListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.convert_dialog,container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {   //make dialog fragment window rounded with drawable
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        fromRecyclerRadioGroup = v.findViewById(R.id.fromRecycler);
        toRecyclerRadioGroup = v.findViewById(R.id.toRecycler);
        assert getArguments() != null;
        for ( String this_currency_option: getArguments().getStringArray("currencies")) {  //set currency options to listviews
            RadioButton rb = new RadioButton(this.getContext());
            rb.setText(this_currency_option);
            rb.setTextColor(getResources().getColor(R.color.white));
            fromRecyclerRadioGroup.addView(rb);
            rb = new RadioButton(this.getContext());
            rb.setText(this_currency_option);
            rb.setTextColor(getResources().getColor(R.color.white));
            toRecyclerRadioGroup.addView(rb);
        }

        fromRecyclerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  //if both base and target currencies selected send data to Main Activity
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedFrom = v.findViewById(fromRecyclerRadioGroup.getCheckedRadioButtonId());
                from = checkedFrom.getText().toString();
                if(!to.isEmpty()){

                    mOnInputListener.sendInput(from, to);
                    Objects.requireNonNull(getDialog()).dismiss();

                }
            }
        });

        toRecyclerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedTo = v.findViewById(toRecyclerRadioGroup.getCheckedRadioButtonId());
                to = checkedTo.getText().toString();
                if(!from.isEmpty()){

                    mOnInputListener.sendInput(from, to);
                    Objects.requireNonNull(getDialog()).dismiss();

                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context)  //on Attach get listener from activity
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}



