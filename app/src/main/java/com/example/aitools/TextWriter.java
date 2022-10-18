package com.example.aitools;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

// This class is used to progressively display a text in an editText
public class TextWriter extends AsyncTask<Integer, Character, Boolean> {

    private EditText et;

    public String finalText = "";
    public String text = "";


    public TextWriter(String text, EditText editText) {
        this.finalText = text;
        this.et = editText;
    }

    // Disables the ability to write to the editText for the duration of the process
    @Override
    protected void onPreExecute() {
        et.setFocusable(false);
        super.onPreExecute();
    }

    // Sends a progress update with one character of the text every 60 milliseconds
    @Override
    protected Boolean doInBackground(Integer... integers) {
        for (int i = 0;i < finalText.length();i++) {
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(finalText.charAt(i));
        }
        return null;
    }

    // Change the color of the text and its background and then write the character it receives to editText
    @Override
    protected void onProgressUpdate(Character... values) {
        SpannableString newString = new SpannableString(values[0].toString());
        newString.setSpan(new BackgroundColorSpan(Color.parseColor("#b8ffd7")), 0, newString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        newString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, newString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.append(newString);
        super.onProgressUpdate(values);
    }

    // Re-enables the ability to write to the editText
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        et.setFocusableInTouchMode(true);
        super.onPostExecute(aBoolean);
    }





}
