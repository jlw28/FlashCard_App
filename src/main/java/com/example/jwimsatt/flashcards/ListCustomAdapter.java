package com.example.jwimsatt.flashcards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapter class for listview layout
 */
public class ListCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private DBHelper db;


    public ListCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        db = new DBHelper(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Checks if sure you want to delete. If yes, deletes the set
                new AlertDialog.Builder(context)
                      .setMessage("Are you sure you want to delete this set?")
                      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              String name = list.get(position);
                              db.deleteFlashcardSet(name);

                              list.remove(position);
                              notifyDataSetChanged();    //updates listview
                          }
                      })
                      .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                          public void onClick(DialogInterface dialog, int i) {
                              // do nothing
                          }
                      })
                      .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                //notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goes to FlashCardSet and tells it to not hide save button
                String itemName = (String) list.get(position);

                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("setName", itemName);
                intent.putExtra("type", "edit");
                context.startActivity(intent);
            }
        });

        return view;
    }
}
