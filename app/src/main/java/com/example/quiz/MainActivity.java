package com.example.quiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private final String FILENAME = "questions";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);


        // Use the FILENAME directly as the resource name
        int resourcedID = this.getResources().getIdentifier(FILENAME, "raw", this.getPackageName());

        if (resourcedID != 0) {
            try (InputStream iS = this.getResources().openRawResource(resourcedID);
                 InputStreamReader isr = new InputStreamReader(iS);
                 BufferedReader br = new BufferedReader(isr)) {

                StringBuilder sB = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sB.append(line).append('\n');
                }
                
                String text = sB.toString();
                textView.setText(sB);
                Log.d("QUIZ_DEBUG", "File content: " + text);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("QUIZ_DEBUG", "Resource not found: " + FILENAME);
        }





    }
}
