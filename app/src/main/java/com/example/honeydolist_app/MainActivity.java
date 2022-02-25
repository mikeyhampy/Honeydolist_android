package com.example.honeydolist_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

/**User selects which list they want to work with */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** open your list */
    public void open_your_list(View view) {
        String thelist = "mylist";
        Intent your_list = new Intent(this, Openlist.class);
        your_list.putExtra("thelist", thelist);
        startActivity(your_list);
    }

    /** open their list */
    public void open_their_list(View view) {
        String thelist = "theirlist";
        Intent their_list = new Intent(this, Openlist.class);
        their_list.putExtra("thelist", thelist);
        startActivity(their_list);
    }
}