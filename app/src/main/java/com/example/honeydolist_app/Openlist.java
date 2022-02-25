package com.example.honeydolist_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Openlist extends AppCompatActivity {
    /** class will display the list of choice and allow edits to be made to the database */

    // global variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView YourListContent;
    private TextView TheTitle;
    private ArrayList<String> KEYS = new ArrayList();
    private String THE_LIST = "";
    String LIST_BOX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openlist);

        //enable back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get title name and database location from LIST_BOX
        TheTitle = (TextView) findViewById(R.id.ListTitle);
        LIST_BOX = getIntent().getStringExtra("thelist");
        if (LIST_BOX.contains("their")) {
            TheTitle.setText("Their Honeydo List");
        } else if (LIST_BOX.contains("my")) {
            TheTitle.setText("My Honeydo List");
        }

        // declare database and call listener function to get changes
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/michaelhampton/" + LIST_BOX);
        YourListContent = (TextView) findViewById(R.id.ListOfItems);
        getdata();
    }

    /** Back button method */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /** database listener, listens for changes and displays in TextBox for the user */
    private void getdata() {


        this.databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // clear display data to be pulled again
                THE_LIST = "";
                KEYS.clear();

                // get all key names from the database
                Iterator var2 = snapshot.getChildren().iterator();
                while(var2.hasNext()) {
                    DataSnapshot postSnapshot = (DataSnapshot)var2.next();
                    KEYS.add(postSnapshot.getKey());
                }

                // iterate through and but all the keys into a list
                int count = 0;
                for (String Key: KEYS) {
                    ++count;
                    THE_LIST += (count + ". " + Key + "\n");
                }
                YourListContent.setText(THE_LIST);
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Openlist.this, "Fail to get data.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /** add items to the database */
    public void enter_words(View view) {

        try {
            // get entered word back
            EditText new_word = (EditText)this.findViewById(R.id.WordAdd);
            String new_word_key = new_word.getText().toString();

            // push new word and clear TextBox
            this.databaseReference.child(new_word_key).setValue("false");
            new_word.setText("");
            this.KEYS.clear();

        } catch (Exception var4) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show();
        }

    }

    /** remove item from the database */
    public void remove_words(View view) {

        try {
            // get number listen to be deleted
            EditText number_to_delete = (EditText)this.findViewById(R.id.NumberRemove);
            int word_key = Integer.parseInt(number_to_delete.getText().toString()) - 1;

            // check for invalid number
            if (word_key < 0 || word_key >= this.KEYS.size()) {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show();
            } else {
                // get word key from the array KEYS
                String word_key_to_remove = (String) this.KEYS.get(word_key);

                // remove word and clear text display
                this.databaseReference.child(word_key_to_remove).getRef().removeValue();
                number_to_delete.setText("");
            }
        } catch (Exception var5) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show();
        }

    }
}