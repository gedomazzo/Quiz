package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/** 
* @author        benjamin rogachevsky 
* @version       1.0
* @since           1/7/26
* Activity that displays credits or application name information. 
*/
public class MainActivityName extends AppCompatActivity {

    /** 
    * Initializes the activity layout.
    * <p>
    * 
    * @param savedInstanceState bundle containing the activity's previously saved state
    * @return void
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_name);
    }

    /** 
    * Inflates the menu resource into the existing menu.
    * <p>
    * 
    * @param menu the options menu in which items are placed
    * @return true for the menu to be displayed
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.name_menu, menu);
        return true;
    }

    /** 
    * Handles the action when a menu item is selected.
    * <p>
    * 
    * @param item the menu item that was selected
    * @return boolean return false to allow normal menu processing to proceed, true to consume it here
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String temp = item.getTitle().toString();
        if (temp.equals("back")) {
            Intent kuku = new Intent(this, MainActivity.class);
            startActivity(kuku);
        }

        return super.onOptionsItemSelected(item);
    }
}
