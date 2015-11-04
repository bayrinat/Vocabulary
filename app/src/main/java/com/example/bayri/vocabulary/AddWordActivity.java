package com.example.bayri.vocabulary;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddWordActivity extends AppCompatActivity {
    public static final String ENGLISH_WORD = "english_word";
    public static final String RUSSIAN_WORD = "russian_word";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        Bundle extras = getIntent().getExtras();

        Button addButton = (Button) findViewById(R.id.addWordButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText englishEditText = (EditText) findViewById(R.id.englishEditText);
                EditText russianEditText = (EditText) findViewById(R.id.russianEditText);
                Intent mainIntent =
                        new Intent(AddWordActivity.this, MainActivity.class);
                mainIntent.putExtra(ENGLISH_WORD, englishEditText.getText().toString());
                mainIntent.putExtra(RUSSIAN_WORD, russianEditText.getText().toString());
                setResult(Activity.RESULT_OK, mainIntent);
                onBackPressed();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(Activity.RESULT_CANCELED, null);
    }
}
