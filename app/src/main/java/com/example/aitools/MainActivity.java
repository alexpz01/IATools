package com.example.aitools;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button submit;
    private EditText textarea;
    private SeekBar temperatureBar;
    private TextView temperatureLevel;
    private TextView max_words;
    private FrameLayout deleteAll;
    private FrameLayout loadingIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.submit = findViewById(R.id.submit_button);
        this.textarea = findViewById(R.id.textarea);
        this.temperatureBar = findViewById(R.id.temperatureBar);
        this.temperatureLevel = findViewById(R.id.temperatureLevel);
        this.max_words = findViewById(R.id.maxWords);
        this.deleteAll = findViewById(R.id.delete_all_button);
        this.loadingIcon = findViewById(R.id.loadingIcon);

        showTextOnClick();
        updateTemperature();
        updateWords();
        deleteAll();
        animationButtons(submit);
        animationButtons(deleteAll);
    }

    //Send the API request and displays the response in the editText
    protected void showTextOnClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalText = textarea.getText().toString();
                startLoading();
                if (totalText.length() > 0) {
                    APIrequest request = new APIrequest(getApplicationContext(), textarea.getText().toString());
                    request.getAiResponse(new APIrequest.OnResponseListener() {

                        // Displays the response progressively to the user
                        @Override
                        public void onResponse(String text) {
                            endLoading();
                            TextWriter tw = new TextWriter(text, textarea);
                            tw.execute(1);
                        }
                    });
                }
            }
        });
    }

    // Update the temperature value of the api
    protected void updateTemperature() {
        this.temperatureBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float value = i/100.0f;
                Options.temperature = value;
                temperatureLevel.setText(Float.toString(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    //Update the maximum value of characters that the API response should have
    protected void updateWords() {
        max_words.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0 || charSequence.equals('0')) {
                    Options.max_tokens = Integer.parseInt(charSequence.toString());
                } else {
                    Options.max_tokens = 100;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    //Resets the text of the main editText
    protected void deleteAll() {
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textarea.setText("");
            }
        });
    }

    //Sets the animations for delete and submit buttons
    protected void animationButtons(View view) {
        Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_touch);
        Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_touch_up);
        animIn.setFillAfter(true);
        animOut.setFillAfter(true);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.startAnimation(animIn);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(animOut);
                }
                return false;
            }
        });
    }

    // Start the loading icon animation
    protected void startLoading() {
        loadingIcon.setVisibility(View.VISIBLE);
        Animation loadingAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading);
        loadingIcon.startAnimation(loadingAnimation);
    }


    // Stop the loading icon animation
    protected void endLoading() {
        loadingIcon.setVisibility(View.GONE);
        loadingIcon.clearAnimation();
    }
}