package com.example.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final String FILENAME = "questions";
    private final String PREFS_NAME = "QuizPrefs";
    private final String KEY_HIGH_SCORE = "high_score";

    RadioGroup radioGroup;
    RadioButton radio1, radio2, radio3, radio4;
    TextView quiz;
    TextView score_text;
    TextView high_score_text;
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
        high_score_text = findViewById(R.id.high_score_text); // Make sure this exists in XML
        next = findViewById(R.id.next);

        updateHighScoreDisplay();
        read();
        Push(null);
    }

    private void updateHighScoreDisplay() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(KEY_HIGH_SCORE, 0);
        if (high_score_text != null) {
            high_score_text.setText("High Score: " + highScore);
        }
    }

    private void checkAndSaveHighScore() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(KEY_HIGH_SCORE, 0);
        if (score > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_HIGH_SCORE, score);
            editor.apply();
            updateHighScoreDisplay();
        }
    }

    public static List<String> splitIntoLines(String input) {
        List<String> lines = new ArrayList<>();
        if (input == null || input.isEmpty()) return lines;
        String[] split = input.split("\\r?\\n");
        for (String line : split) {
            if (!line.trim().isEmpty()) lines.add(line);
        }
        return lines;
    }

    public void read() {
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
        }

        questionsList = splitIntoLines(Total);
        questionsList = removeDuplicates(questionsList);

        for (int i = 0; i < questionsList.size(); i++) {
            questions.add(new qustion(questionsList.get(i)));
        }
    }

    public void Push(View view) {
        radioGroup.setVisibility(View.VISIBLE);
        
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            qustion currentQ = questions.get(currentQuestionIndex);
            
            int correctId = -1;
            if (currentQ.getRan() == 1) correctId = R.id.an1;
            else if (currentQ.getRan() == 2) correctId = R.id.an2;
            else if (currentQ.getRan() == 3) correctId = R.id.an3;
            else if (currentQ.getRan() == 4) correctId = R.id.an4;

            if (selectedId == correctId) {
                score++;
                score_text.setText(String.valueOf(score));
            }
            radioGroup.clearCheck();
        }

        currentQuestionIndex++;
        if (currentQuestionIndex >= questions.size()) {
            radioGroup.setVisibility(View.INVISIBLE);
            quiz.setText("THE END. Final Score: " + score);
            checkAndSaveHighScore();
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
        currentQuestionIndex = -1;
        score = 0;
        score_text.setText("0");
        updateHighScoreDisplay();
        read();
        Push(null);
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
