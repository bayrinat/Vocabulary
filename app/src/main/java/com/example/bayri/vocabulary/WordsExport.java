package com.example.bayri.vocabulary;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bayri on 31.10.2015.
 */
public class WordsExport {
    public WordsExport() {
    }

    public boolean downloadFile() {
        // Create a URL for the desired page
        final URL url;
        try {
            url = new URL("https://wordpress.org/plugins/about/readme.txt");

            AsyncTask<Long, Object, Object> downloadFile =
                    new AsyncTask<Long, Object, Object>() {
                        @Override
                        protected Object doInBackground(Long... params) {
                            try {
                                return new BufferedReader(new InputStreamReader(url.openStream()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object result) {
                            String str;
                            BufferedReader reader = (BufferedReader) result;
                            try {
                                while ((str = reader.readLine()) != null) {
                                    int i = 0;
                                    // str is one line of text; readLine() strips the newline character(s)
                                }
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
            downloadFile.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
