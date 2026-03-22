package com.example.quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final String FILENAME = "questions";

    TextView textView;
    EditText ed;
    Button btn;
    private String strwr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        ed = findViewById(R.id.ed);
        btn = findViewById(R.id.btn);



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
                //textView.setText(sB);
                Log.d("QUIZ_DEBUG", "File content: " + text);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("QUIZ_DEBUG", "Resource not found: " + FILENAME);
        }

    }


    public void write(View view) {
        strwr=ed.getText().toString();
        try {
            FileOutputStream fOS = openFileOutput(FILENAME,MODE_PRIVATE);
            OutputStreamWriter oSW = new OutputStreamWriter(fOS);
            BufferedWriter bW = new BufferedWriter(oSW);
            bW.write(strwr);
            bW.close();
            oSW.close();
            fOS.close();

            textView.setText(strwr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void reset(View view) {
        try {
            FileOutputStream fOS = openFileOutput(FILENAME,MODE_PRIVATE);
            OutputStreamWriter oSW = new OutputStreamWriter(fOS);
            BufferedWriter bW = new BufferedWriter(oSW);
            bW.write("");
            bW.close();
            oSW.close();
            fOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ed.setText("");
        textView.setText("");
    }

    public void read(View view) {
        try {
            FileInputStream fIS= openFileInput(FILENAME);
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            StringBuilder sB = new StringBuilder();
            String line = bR.readLine();
            while (line != null) {
                sB.append(line+'\n');
                line = bR.readLine();
            }
            bR.close();
            iSR.close();
            fIS.close();
            textView.setText(sB.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void push(View view) {

        write(view);
    }
}


