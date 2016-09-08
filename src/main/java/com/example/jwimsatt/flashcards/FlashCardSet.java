package com.example.jwimsatt.flashcards;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FlashCardSet extends AppCompatActivity {
    int count = 0;
    String setName;
    private DBHelper db;
    private ArrayList<Card> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card_set);

        try{
            Intent intent = getIntent();
            db = new DBHelper(this);
            setName = intent.getStringExtra("setName");

            TextView number = (TextView) findViewById(R.id.number);
            TextView side1 = (TextView) findViewById(R.id.side1_text);
            TextView side2 = (TextView) findViewById(R.id.side2_text);
            TextView title = (TextView) findViewById(R.id.titleView);
            title.setText(setName);

            set = db.getFlashcardSet(setName);
            Card card = set.get(0);
            side1.setText(card.getSide1());
            side2.setText(card.getSide2());
            number.setText(Integer.toString(card.getNumber()));

        }catch(NullPointerException e){
            Context context = getApplicationContext();
            CharSequence text = "Error with flashcard set";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash_card_set, menu);
        return true;
    }

    //Returns to ViewSets activity
    public void buttonClick_back_to_views(View view){
        Intent intent = new Intent(this, ViewSets.class);
        startActivity(intent);
    }

    //reveals side 2
    public void click_side1(View view){
        LinearLayout answer = (LinearLayout) findViewById(R.id.side2);
        answer.setVisibility(view.VISIBLE);
    }

    //toggles visibility of side 2 card
    public void click_side2(View view){
        LinearLayout answer = (LinearLayout) findViewById(R.id.side2);
        if(answer.getVisibility() == view.INVISIBLE){
            answer.setVisibility(view.VISIBLE);
        }else{
            answer.setVisibility(view.INVISIBLE);
        }

    }

    //Shows previous flashcard
    public void buttonClick_back_one(View view){
        count--;
        LinearLayout answer = (LinearLayout) findViewById(R.id.side2);
        answer.setVisibility(view.INVISIBLE);

        TextView side1 = (TextView) findViewById(R.id.side1_text);
        TextView side2 = (TextView) findViewById(R.id.side2_text);
        TextView number = (TextView) findViewById(R.id.number);

        if(count >= 0){
            Card card = set.get(count);
            side1.setText(card.getSide1());
            side2.setText(card.getSide2());
            number.setText(Integer.toString(card.getNumber()));
        }

        if(count <= 0){
            count = 0;

            ImageButton back = (ImageButton) findViewById(R.id.back_arrow);
            back.setVisibility(View.INVISIBLE);
        }
    }

    //Shows next flashcard
    public void buttonClick_next_one(View view){
        count++;

        LinearLayout answer = (LinearLayout) findViewById(R.id.side2);
        answer.setVisibility(view.INVISIBLE);

        ImageButton back = (ImageButton) findViewById(R.id.back_arrow);
        back.setVisibility(View.VISIBLE);

        TextView side1 = (TextView) findViewById(R.id.side1_text);
        TextView side2 = (TextView) findViewById(R.id.side2_text);
        TextView number = (TextView) findViewById(R.id.number);

        if(count < set.size()){
            Card card = set.get(count);
            side1.setText(card.getSide1());
            side2.setText(card.getSide2());
            number.setText(Integer.toString(card.getNumber()));
        }

        if(count >= set.size()){
            count = set.size() - 1;
        }

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
