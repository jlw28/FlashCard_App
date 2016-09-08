package com.example.jwimsatt.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewSets extends AppCompatActivity {
    private ListView list;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sets);

        //set list variable to ListView object
        list = (ListView)findViewById(R.id.list);
        db = new DBHelper(this);
        //Array holding Flashcard set names to show in ListView
       /* String[] set_names = new String[]{"Colors",
                                        "Computational Geometry",
                                        "Oddities"};
       */
        try {
            ArrayList<String> set_names = null;
            set_names = db.getAllTitles();
            //Create adapter: context, layout for the row, id of TextView, array of data
            ListCustomAdapter adapter = new ListCustomAdapter(set_names, this);
            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.activity_list_item, android.R.id.text1, set_names);*/

            //Assign adapter to ListView
            list.setAdapter(adapter);

            //Onclick event listener
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //int itemPostion = position;
                    String itemName = (String) list.getItemAtPosition(position);


                    Intent intent = new Intent(ViewSets.this, FlashCardSet.class);
                    intent.putExtra("setName", itemName);
                    //intent.putExtra("type", "view");
                    startActivity(intent);

                }
            });
        }catch (Exception e){
            String name = "none";
            Intent intent = new Intent(ViewSets.this, FlashCardSet.class);
            intent.putExtra("setName", name);
            startActivity(intent);
        }
    }

    //Returns to main activity
    public void buttonClick_back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_sets, menu);
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
}
