package com.example.jwimsatt.flashcards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class NewActivity extends AppCompatActivity {
    private ArrayList<Card> input = new ArrayList<Card>();
    private String title;
    private DBHelper db;
    private int currentNumber;
    private String type = " ";
    EditText side1;
    EditText side2;
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        currentNumber = 0;
        db = new DBHelper(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        title = intent.getStringExtra("setName");

        side1 = (EditText) findViewById(R.id.side1_text);
        side2 = (EditText) findViewById(R.id.side2_text);
        number = (TextView) findViewById(R.id.number);


        if(type.equals("edit")){
            getSet();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    //Moves back to main activity(if started new set) or viewSets activity(if edit)
    public void buttonClick_cancel(View view){
        if(type.equals("new")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, ViewSets.class);
            startActivity(intent);
        }
    }

    //Saves set to database
    public void buttonClick_save(View view){

        EditText titlename = (EditText) findViewById(R.id.title);
        title = titlename.getText().toString().trim();
        //Checks if there's a title
        if(title.matches("")){
            new AlertDialog.Builder(this)
                    .setMessage("Please enter title")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        //Checks if current card had input and adds to list before saving
        String one = side1.getText().toString().trim();
        String two = side2.getText().toString().trim();
        Log.d("Save Current", Integer.toString(currentNumber));
        if(!one.matches("") && !two.matches("")) {
            Card card;
            if(currentNumber >= input.size()) {
                card = new Card();
            }else{
                card = input.get(currentNumber);
            }
            card.setSide1(one);
            card.setSide2(two);
            card.setNumber(Integer.parseInt(number.getText().toString()));
            input.add(card);

        }
        //Checks database to make sure title isn't already in use
        if(type.equals("new")) {
            if (db.checkTitle(title) == true) {
                new AlertDialog.Builder(this)
                        .setMessage("Title is already used. Select different name.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else {
                for (int i = 0; i < input.size(); i++) {
                    Card card = input.get(i);
                    db.insertFlashcard(title, card.getSide1(), card.getSide2(), card.getNumber());
                }
                Toast.makeText(getBaseContext(), "Flashcard set saved", Toast.LENGTH_LONG).show();

                buttonClick_cancel(view);
            }
        }else{
            for (int i = 0; i < input.size(); i++) {

                Card card = input.get(i);

                //Checks if new cards were added to set
                if(card.getId() != 0) {
                    db.updateFlashcard(card.getId(), title, card.getSide1(), card.getSide2(), card.getNumber());
                }else{
                    db.insertFlashcard(title, card.getSide1(), card.getSide2(), card.getNumber());
                }
            }
            Toast.makeText(getBaseContext(), "Flashcard set updated", Toast.LENGTH_LONG).show();

            buttonClick_cancel(view);
        }
    }

    //Goes back one card
    public void buttonClick_back(View view){

        if(!input.isEmpty()){
            //Log.d("Back input", Integer.toString(input.size()));
            //Log.d("Back current number", Integer.toString(currentNumber) );
            Card current = new Card();
            if(currentNumber < input.size()){
                current = input.get(currentNumber);
            }
            //Update current card before showing previous
            String one = side1.getText().toString().trim();
            String two = side2.getText().toString().trim();

            if(TextUtils.isEmpty(one) && TextUtils.isEmpty(two) && currentNumber < input.size()) {
                new AlertDialog.Builder(this)
                        .setMessage("Please enter values for both Side 1 and Side 2")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;

            }else{

                //Log.d("Back has inputs", one);
                current.setSide1(one);
                current.setSide2(two);
                current.setNumber(Integer.parseInt(number.getText().toString()));

                if(currentNumber >= input.size()){
                    input.add(current);
                }
            }

            //Shows previous card
            currentNumber = currentNumber-1;

            if(currentNumber < 0){
                currentNumber = 0;
            }

            current = input.get(currentNumber);
            side1.setText(current.getSide1());
            side2.setText(current.getSide2());
            number.setText(Integer.toString(current.getNumber()));

            if(currentNumber == 0){
                ImageButton back = (ImageButton) findViewById(R.id.back_btn);
                back.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Adds new card to set
    public void buttonClick_next(View view){
        ImageButton back = (ImageButton) findViewById(R.id.back_btn);
        back.setVisibility(View.VISIBLE);

        String num = Integer.toString(input.size());
        Log.d("Input size", num);
        Log.d("Initial current", Integer.toString(currentNumber));

        //Updates existing card and move to next card
        if( (!input.isEmpty()) && (currentNumber < input.size()) ){
            //Updates current card before moving to the next
            Card card = input.get(currentNumber);
            card.setSide1(side1.getText().toString());
            card.setSide2(side2.getText().toString());

            //Shows next card
            currentNumber = currentNumber + 1;
            if(currentNumber < input.size()){
                card = input.get(currentNumber);
                side1.setText(card.getSide1());
                side2.setText(card.getSide2());
                number.setText(Integer.toString(card.getNumber()));

                //Log.d("next side 1", card.getSide1());
                //Log.d("top update current", Integer.toString(currentNumber));
            } else {
                side1.setText(" ");
                side2.setText(" ");
                //side1.setHint("Side 1");  can't get it to work after initial
                //side2.setHint("Side 2");
                number.setText(Integer.toString(currentNumber + 1));
            }
            //Log.d("current top", Integer.toString(currentNumber));
            return;
        }

        if(input.isEmpty() || currentNumber == input.size()){
            String one = side1.getText().toString().trim();
            String two = side2.getText().toString().trim();
            int numCurrent = Integer.parseInt(number.getText().toString());

            //Log.d("Edittext side1", one);

            //Checks if current card has inputs
            if(TextUtils.isEmpty(one) && TextUtils.isEmpty(two)) {
                new AlertDialog.Builder(this)
                        .setMessage("Please enter values for both Side 1 and Side 2")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return;
            }else{
                //Log.d("Current", Integer.toString(currentNumber));
                Card card = new Card();
                card.setSide1(one);
                card.setSide2(two);

                //Log.d("Current card added", one);
                card.setNumber(numCurrent);
                input.add(card);

                currentNumber = currentNumber + 1;

            }

            //Log.d("Input size update", Integer.toString(input.size()));
            //Log.d("New current", Integer.toString(currentNumber));

            //If last card, clear input
            if (currentNumber == input.size() ) {
                side1.setText(" ");
                side2.setText(" ");
                //side1.setHint("Side 1");  can't get it to work after initial
                //side2.setHint("Side 2");
                number.setText(Integer.toString(currentNumber + 1));
            }

        }

    }

    //gets selected set to be edited
    public void getSet(){
        try{
            EditText titleView = (EditText) findViewById(R.id.title);
            titleView.setText(title);

            input = db.getFlashcardSet(title);
            Card card = input.get(currentNumber);
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
