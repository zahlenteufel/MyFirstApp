package com.example.gas.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String WEIGHT;

    private EditText weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightText = findViewById(R.id.weightText);
        Button enterButton = findViewById(R.id.enterButton);
        disableButtonIffInputEmpty(weightText, enterButton);
        makeDoneButtonOnKeyboardEnterWeight();
    }

    private void disableButtonIffInputEmpty(EditText weightText, final Button button) {
        // Initially disable button.
        button.setEnabled(false);

        weightText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button.setEnabled(s.length() > 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing.
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });
    }

    private void makeDoneButtonOnKeyboardEnterWeight() {
        weightText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (weightText.getText().length() > 0) {
                        sendMessage(v);
                    }
                    handled = true;
                }
                return handled;
            }
        });

    }

    /**
     * Called when the user taps the Enter button or Done button in the keyboard while writing
     * the weight.
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, Graph.class);
        intent.putExtra(WEIGHT, weightText.getText().toString());
        startActivity(intent);
    }
}

