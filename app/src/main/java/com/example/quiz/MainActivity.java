package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final String FILENAME = "questions";


    RadioGroup radioGroup;
    RadioButton radio1, radio2, radio3, radio4;
    TextView quiz;
    TextView score_text;
    Button next;
    String Total = "";

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

        // Initial read
        read();
        
        // Show first question
        Push(null);
    }


    public static List<String> splitIntoLines(String input) {
        List<String> lines = new ArrayList<>();

        if (input == null || input.isEmpty()) {
            return lines;
        }

        String[] split = input.split("\\r?\\n");

        for (String line : split) {
            if (!line.trim().isEmpty()) {
                lines.add(line);
            }
        }

        return lines;
    }


    public void read() {
        // 1. Clear everything to prevent duplicates
        Total = "";
        questions.clear();
        questionsList.clear();


        int resourcedID = this.getResources().getIdentifier(FILENAME, "raw", this.getPackageName());
        if (resourcedID != 0) {
            try (InputStream iS = this.getResources().openRawResource(resourcedID);
                 BufferedReader br = new BufferedReader(new InputStreamReader(iS))) {
                StringBuilder sB = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sB.append(line).append('\n');
                }
                Total = sB.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileInputStream fIS = openFileInput("a");
            BufferedReader bR = new BufferedReader(new InputStreamReader(fIS));
            StringBuilder sB = new StringBuilder();
            String line;
            while ((line = bR.readLine()) != null) {
                sB.append(line).append('\n');
            }
            bR.close();
            Total += sB.toString();
        } catch (IOException e) {
            // It's okay if user1 doesn't exist yet
        }

        // 4. Populate questions list (don't use size()-1, use full size)
        questionsList = splitIntoLines(Total);
        questionsList = removeDuplicates(questionsList);

        qustion q;
        for (int i = 0; i < questionsList.size(); i++) {
            q = new qustion(questionsList.get(i));
            questions.add(q);
        }
    }


    public void Push(View view) {
        radioGroup.setVisibility(View.VISIBLE);
        
        // 1. Check answer of the question currently on screen
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

    @Override
    protected void onResume() {
        super.onResume();
        // Reset state so when we come back we start from the beginning with updated data
        currentQuestionIndex = -1;
        score = 0;
        score_text.setText("0");
        read();
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        return new ArrayList<>(set);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String temp = item.getTitle().toString();
        if (temp.equals("Credits")) {
            Intent kuku = new Intent(this, MainActivityName.class);
            startActivity(kuku);
        }

        return super.onOptionsItemSelected(item);
    }

}
