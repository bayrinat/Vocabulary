package com.example.bayri.vocabulary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    public static final String CATEGORY_ID_START = "category_word_id_start";
    public static final String FILENAME = "words.csv";

    private DatabaseHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DatabaseHelper(getBaseContext());

        if(mHelper.getCategories().size() == 0)
            mHelper.insertCategory("All");

        // fill categories spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, mHelper.getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setAdapter(adapter);

        Button addButton = (Button) findViewById(R.id.launchButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent swapActivityIntent =
                        new Intent(MainActivity.this, SwapActivity.class);
                swapActivityIntent.putExtra(CATEGORY_ID_START,
                        categorySpinner.getSelectedItemPosition());
                startActivity(swapActivityIntent);
            }
        });

        Button downloadButton = (Button) findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordsExport wordsExport = new WordsExport();
                wordsExport.execute();
            }
        });

        Button addCategoryButton = (Button) findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);
                mHelper.isExsistCategory(editText.getText().toString());
                editText.getText().clear();
            }
        });
    }

    public class WordsExport extends AsyncTask<Long, Object, Object> {

        @Override
        protected Object doInBackground(Long... params) {
            InputStream  istream = getResources().openRawResource(R.raw.words);
            if(istream == null)
                return null;
            try {
                int index = 0;
                ContentValues cv = new ContentValues();
                StringBuilder buffer = new StringBuilder();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int b;
                while( (b = istream.read())!= -1 ) {
                    baos.write( b );
                }
                // TODO: remake stupid convert
                String s = baos.toString();

                for(int i = 0; i < s.length(); i++) {
                    char ch = s.charAt(i);
                    if(ch == '\n')
                        continue;

                    if(ch == '#' || ch == '\r') {
                        String str = buffer.toString();
                        buffer.setLength(0);
                        switch (++index){
                            case 1:
                                cv.put(DatabaseHelper.COLUMN_ENGLISH, str);
                                break;
                            case 2:
                                cv.put(DatabaseHelper.COLUMN_RUSSIAN, str);
                                break;
                            case 3:
                                cv.put(DatabaseHelper.COLUMN_CATEGORY, str);
                                mHelper.insertFromFile(cv);
                                cv.clear();
                                index = 0;
                        }
                    } else {
                        buffer.append(ch);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
        }
    }
}
