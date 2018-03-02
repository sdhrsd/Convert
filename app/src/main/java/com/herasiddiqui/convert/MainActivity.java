package com.herasiddiqui.convert;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    String amountINR = "";
    String amountUSD = "";
    String backgroundINR = "";
    String backgroundUSD = "";
    Boolean inForeGround = true;
    Double convertedValue = 0.0;
    private EditText editTextINR;
    private EditText editTextUSD;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    Activity activity;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity","Created");

        editTextINR = findViewById(R.id.editTextINR);
        editTextUSD = findViewById(R.id.editTextUSD);

            editTextINR.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0 && editTextINR.hasFocus() && inForeGround) {
                            Log.i("Activity","INR Amount Edited");
                            editTextUSD.setText("");
                    }
                }

                @Override
                    public void afterTextChanged(Editable s) {

                }
            });

            editTextUSD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0 && editTextUSD.hasFocus() && inForeGround) {
                        Log.i("Activity","USD Amount Edited");
                        editTextINR.setText("");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            configurationDetection();
        }

    /**************************** method for convert button ***************************************/

    public void convertClicked(View button) {
        hideKeyboard();
        amountINR = String.valueOf(editTextINR.getText());
        amountUSD = String.valueOf(editTextUSD.getText());
        /*
        To convert USD to INR
         */
        if(editTextUSD.hasFocus() && !amountUSD.isEmpty() ) {
            if (amountUSD.equals(".")) {
                Toast.makeText(this, "Enter a valid USD Amount", Toast.LENGTH_SHORT).show();
            } else {
                convertedValue = Double.parseDouble(amountUSD) * 63.89;
                editTextINR.setText(String.valueOf(df2.format(convertedValue)));
                Log.i("Activity","USD Amount Converted to INR Amount");
            }
        }
        /*
        To convert INR to USD
         */
        else if(!amountINR.isEmpty() && editTextINR.hasFocus() ) {
            if (amountINR.equals(".")) {
                Toast.makeText(this, "Enter a valid INR Amount", Toast.LENGTH_SHORT).show();
            } else {
                convertedValue = Double.parseDouble(amountINR) * 0.016;
                editTextUSD.setText(String.valueOf(df2.format(convertedValue)));
                Log.i("Activity","INR Amount Converted to USD Amount");
            }
        }
        else {
            Toast.makeText(this, "Enter an amount to convert", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState ) {
        super.onSaveInstanceState(outState);
        Log.i("Activity", "onSaveInstanceState");
        backgroundUSD = String.valueOf(editTextUSD.getText());
        outState.putString("savedUSD",backgroundUSD);
        backgroundINR = String.valueOf(editTextINR.getText());
        outState.putString("savedINR",backgroundINR);
        inForeGround = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity", "onResume");
        inForeGround = false;
        editTextINR = findViewById(R.id.editTextINR);
        editTextUSD = findViewById(R.id.editTextUSD);
        editTextUSD.setText(String.valueOf(backgroundUSD));
        editTextINR.setText(String.valueOf(backgroundINR));
        inForeGround = true;
        configurationDetection();

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Activity", "onRestoreInstanceState");
        backgroundUSD = savedInstanceState.getString("savedUSD");
        backgroundINR = savedInstanceState.getString("savedINR");
    }

    protected void configurationDetection() {
        activity = MainActivity.this;
        value = activity.getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            if(editTextUSD.getText().length() == 0 && editTextINR.getText().length() == 0) {
                editTextUSD.requestFocus();
            }
        }
        if (value == Configuration.ORIENTATION_LANDSCAPE) {
            if(editTextUSD.getText().length() == 0 && editTextINR.getText().length() == 0) {
                editTextINR.requestFocus();
            }
        }
    }
}

//Detecting Configuration Help
//https://www.android-examples.com/find-get-current-screen-orientation-in-android-programmatically/