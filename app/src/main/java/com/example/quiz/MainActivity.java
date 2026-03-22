package com.example.quiz;

import android.content.Intent;
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
    TextView score_text;
    Button next;
    String Total;

    List<String> questionsList = new ArrayList<>();
    List<qustion> questions = new ArrayList<>();
    int currentQuestionIndex = -1;
    int score = 0;

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
        score_text = findViewById(R.id.score_text);
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

        Log.d("QUIZ_DEBUG", "Questions: " + questions.toString());


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


    public void read() {
        try {
            FileInputStream fIS = openFileInput(FILENAME);
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            StringBuilder sB = new StringBuilder();
            String line = bR.readLine();
            while (line != null) {
                sB.append(line + '\n');
                line = bR.readLine();
            }
            bR.close();
            iSR.close();
            fIS.close();

            Total += sB.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        qustion q;
        questionsList = splitIntoLines(Total);
        for (int i = 0; i < questionsList.size()-1; i++) {
            q = new qustion(questionsList.get(i), i);
            questions.add(q);
        }
    }


    public void Push(View view) {
        radioGroup.setVisibility(View.VISIBLE);
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.an1) {
                score++;
                score_text.setText(String.valueOf(score));
            }
            radioGroup.clearCheck();
        }

        // 2. Move to the next question
        currentQuestionIndex++;
        if (currentQuestionIndex >= questions.size()) {
            radioGroup.setVisibility(View.INVISIBLE);
            quiz.setText("THE END");
        } else {

            qustion current = questions.get(currentQuestionIndex);

            quiz.setText(current.getQu());
            radio1.setText(current.getAn1());
            radio2.setText(current.getAn2());
            radio3.setText(current.getAn3());
            radio4.setText(current.getAn4());
        }
    }



    public void add(View view) {
        Intent shaw = new Intent(this, writing.class);
        startActivity(shaw);
    }
}
