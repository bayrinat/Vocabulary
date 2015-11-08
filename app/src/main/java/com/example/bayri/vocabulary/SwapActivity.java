package com.example.bayri.vocabulary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SwapActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DatabaseHelper mHelper;
    private int mRecordsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new DatabaseHelper(getBaseContext());

        mRecordsCount = mHelper.getRecordsCount();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewWord =
                        new Intent(SwapActivity.this, AddWordActivity.class);
                ArrayList<String> categories = mHelper.getCategories();
                addNewWord.putExtra(AddWordActivity.CATEGORIES_LIST, categories);
                startActivityForResult(addNewWord, 0); // start Activity
            }
        });
    }

    @Override
    public void onDestroy() {
        mHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK ) {
            Bundle bundle = data.getExtras();
            String englishWord = bundle.getString(AddWordActivity.ENGLISH_WORD);
            String russianWord = bundle.getString(AddWordActivity.RUSSIAN_WORD);
            int categoryId = bundle.getInt(AddWordActivity.CATEGORY_ID);
            Record record = new Record(englishWord, russianWord);
            record.setCategory(categoryId);
            mHelper.insertRecord(record);
            mRecordsCount = mHelper.getRecordsCount();
            mViewPager.getAdapter().notifyDataSetChanged();

            String output = englishWord + "#" + russianWord + "#" +
                    mHelper.getCategory(categoryId) + "\r\n";
            WriteToFile(output);
        }
    }

    private void WriteToFile(String raw){
        try {
            FileOutputStream fos = openFileOutput(MainActivity.FILENAME, Context.MODE_PRIVATE);
            fos.write(raw.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int newPosition = position + 1;
            DatabaseHelper.RecordCursor cursor = queryRuns(newPosition);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(newPosition, cursor);
        }

        @Override
        public int getCount() {
            return mRecordsCount;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_CURSOR = "current_cursor";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, DatabaseHelper.RecordCursor cursor) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_CURSOR, cursor);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_swap, container, false);

            // make translate visible
            LinearLayout linearLayoutRussian = (LinearLayout) rootView.findViewById(R.id.LinearLayoutRussian);
            linearLayoutRussian.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TextView russianTextView = (TextView) rootView.findViewById(R.id.russianTextView);
                    russianTextView.setVisibility(View.VISIBLE);
                }
            });

            // set as known word
            CheckBox knowCheckBox = (CheckBox) rootView.findViewById(R.id.knowCheckBox);
            knowCheckBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((CheckBox)v).isChecked();

                }
            });

            // get cursor
            DatabaseHelper.RecordCursor cursor = (DatabaseHelper.RecordCursor) getArguments().
                    getSerializable(ARG_CURSOR);
            assert cursor != null;
            Record record = cursor.getRecord();
            if(record != null) {
                TextView englishTextView = (TextView) rootView.findViewById(R.id.englishTextView);
                englishTextView.setText(record.getEnglish());
                TextView russianTextView = (TextView) rootView.findViewById(R.id.russianTextView);
                russianTextView.setText(record.getRussian());
                // make russian invisible
                russianTextView.setVisibility(View.INVISIBLE);
            }
            return rootView;
        }
    }

    /**
     *
     */
    public DatabaseHelper.RecordCursor queryRuns(int id) {
        return mHelper.queryRecord(id);
    }
}
