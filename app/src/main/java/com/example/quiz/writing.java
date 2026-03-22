package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class writing extends AppCompatActivity {

    EditText que, ans1, ans2, ans3, ans4, rnum;
    private final String FILENAME = "a";

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
