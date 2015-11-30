package com.estimote.examples.demos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.estimote.examples.demos.R;
import com.estimote.examples.demos.adapters.BeaconListAdapter;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Region;
import java.util.Collections;
import java.util.List;

/**
 * Displays list of found beacons sorted by RSSI.
 * Starts new activity with selected beacon if activity was provided.
 *
 * @author wiktor.gworek@estimote.com (Wiktor Gworek)
 */
public class SelectBook extends ActionBarActivity {
    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";
    private static final String TAG = SelectBook.class.getSimpleName();
    public ListView genreChoice,bookChoice;
    public int genre=0;
    public int book=0;
    public String bookString,genreString;
    public String[][] books= new String[][] {{"Sherlock Holmes","Detective Conan"},{"Twilight","Hunger Games"},{"Steve Jobs","Genghis Khan"},{"Justin Trudeau","Vladimir Putin"},{"World History","American History"}};
    public String[] genres= new String[] { "Mystery","Drama", "Biography","Autobiography","History"};

    //public Button genreButton;
    //public Button bookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_selection);
        // Configure device list.
        //genreButton =(Button) findViewById(R.id.gButton);
        //bookButton =(Button) findViewById(R.id.bButton);
        //bookButton.setVisibility(View.GONE);
        setUpGenre();
        setUpBook();
        selectGenre();
        selectBook();
    }

    public void setUpGenre()
    {
        /////////////////////////////////////////////////////////////////////CHARITY LIST VIEW////////////////////////////////////////////////////////////////
        // Get ListView object from xml
        genreChoice = (ListView) findViewById(R.id.genreChoices);
        // Defined Array values to show in ListView

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapterGenre = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, genres);

        // Assign adapter to ListView
        genreChoice.setAdapter(adapterGenre);
    }
    public void setUpBook()
    {
        bookChoice = (ListView) findViewById(R.id.bookChoices);

        ArrayAdapter<String> adapterBook = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, books[genre]);
        bookChoice.setAdapter(adapterBook);
    }
    public void selectGenre()
    {
        genreChoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                genreString = (String) genreChoice.getItemAtPosition(position);
                genre = position;
                setUpBook();
            }
        });
    }
    public void selectBook()
    {
        bookChoice.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int position, long id){
                bookString = (String) genreChoice.getItemAtPosition(position);
                book = position;
                Intent intent = new Intent("com.estimote.examples.demos.activities.PROXIMITYACTIVITY");
                intent.putExtra("Genre",""+genre);
                intent.putExtra("Name",bookString);
                intent.putExtra(SelectBook.EXTRAS_TARGET_ACTIVITY, FindBookActivity.class.getName());
                startActivity(intent);

            }
        });
    }
    @Override protected void onDestroy() {        super.onDestroy();    }
    @Override protected void onResume() {        super.onResume();    }
    @Override protected void onStop() {        super.onStop();    }

}

