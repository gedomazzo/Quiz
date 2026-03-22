package com.example.quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String FILENAME = "questions";


    RadioGroup radioGroup;
    RadioButton radio1, radio2, radio3, radio4;
    TextView quiz;
    Button next;
    String Total;

    List<String> questionsList = new ArrayList<>();
    int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.group);

        radio1 = findViewById(R.id.an1);
        radio2 = findViewById(R.id.an2);
        radio3 = findViewById(R.id.an3);
        radio4 = findViewById(R.id.an4);

        quiz = findViewById(R.id.quiz);
        next = findViewById(R.id.next);




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
                Total = text;
                Log.d("QUIZ_DEBUG", "File content: " + text);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("QUIZ_DEBUG", "Resource not found: " + FILENAME);
        }


        read();


        questionsList = splitIntoLines(Total);

        qustion q = new qustion(questionsList.get(0), 1);

        Log.d("system_debugg", "Questions: " + questionsList.toString());
        Log.d("system_debugg", "Questions: " + q.toString());

    }



    public static List<String> splitIntoLines(String input) {
        List<String> lines = new ArrayList<>();

        if (input == null || input.isEmpty()) {
            return lines;
        }


        String[] split = input.split("\\r?\\n");

        for (String line : split) {
            lines.add(line);
        }

        return lines;
    }



    public void read(){
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

            Total += sB.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Push(View view) {


    }
}


