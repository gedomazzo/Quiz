package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/** 
* @author        benjamin rogachevsky 
* @version       1.0
* @since           1/7/26
* Activity responsible for capturing user input to create new quiz questions and saving them to internal storage.
*/
public class writing extends AppCompatActivity {

    EditText que, ans1, ans2, ans3, ans4, rnum;
    private final String FILENAME = "a";

    /** 
    * Initializes the activity layout and links UI components.
    * <p>
    * 
    * @param savedInstanceState bundle containing the activity's previously saved state
    * @return void
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        que = findViewById(R.id.que);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        rnum = findViewById(R.id.rnum);
    }

    /** 
    * Validates that all fields are filled, saves the data, and returns to the previous screen.
    * <p>
    * 
    * @param view the view that was clicked to trigger this action
    * @return void
    */
    public void back(View view) {
        if (!que.getText().toString().isEmpty() &&
            !ans1.getText().toString().isEmpty() &&
            !ans2.getText().toString().isEmpty() &&
            !ans3.getText().toString().isEmpty() &
            !ans4.getText().toString().isEmpty() &&
            !rnum.getText().toString().isEmpty()) {


            write(view);
            finish();
        }
    }

    /** 
    * Formats the question and answers into a single line and appends it to the questions file.
    * <p>
    * 
    * @param view the view that was clicked to trigger the write operation
    * @return void
    */
    public void write(View view) {
        // 1. Get the text RIGHT NOW when the button is pressed, not in onCreate
        String strwr = que.getText().toString() + "|" + 
                       ans1.getText().toString() + "|" + 
                       ans2.getText().toString() + "|" + 
                       ans3.getText().toString() + "|" + 
                       ans4.getText().toString() + "|" +
                       rnum.getText().toString() + "\n"; // Added newline

        try {
            FileOutputStream fOS = openFileOutput(FILENAME, MODE_APPEND);
            OutputStreamWriter oSW = new OutputStreamWriter(fOS);
            BufferedWriter bW = new BufferedWriter(oSW);
            
            bW.write(strwr);
            
            bW.close();
            oSW.close();
            fOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
