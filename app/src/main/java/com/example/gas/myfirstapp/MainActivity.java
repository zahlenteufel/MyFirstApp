package com.example.gas.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static String WEIGHT;

    private EditText weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightText = (EditText) findViewById(R.id.weightText);
        Button enterButton = (Button) findViewById(R.id.enterButton);
        disableButtonIffInputEmpty(weightText, enterButton);
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

    /**
     * Called when the user taps the Send button.
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, Graph.class);
        intent.putExtra(WEIGHT, weightText.getText().toString());
        startActivity(intent);
    }
}

