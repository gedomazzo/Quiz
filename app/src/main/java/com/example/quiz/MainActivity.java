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

/** 
* @author        benjamin rogachevsky 
* @version       1.0
* @since           1/7/26
* Main activity that handles the quiz gameplay, question loading, and high score management.
*/
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

    /** 
    * Initializes the activity, sets up the UI components, and loads the initial quiz state.
    * <p>
    * 
    * @param savedInstanceState bundle containing the activity's previously saved state
    * @return void
    */
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

    /** 
    * Retrieves the high score from SharedPreferences and updates the corresponding TextView.
    * <p>
    * 
    * @param 
    * @return void
    */
    private void updateHighScoreDisplay() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(KEY_HIGH_SCORE, 0);
        if (high_score_text != null) {
            high_score_text.setText("High Score: " + highScore);
        }
    }

    /** 
    * Compares the current score with the high score and saves it if the current score is higher.
    * <p>
    * 
    * @param 
    * @return void
    */
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

    /** 
    * Splits a single string into a list of lines, filtering out empty lines.
    * <p>
    * 
    * @param input the raw string content to be split
    * @return list of non-empty strings representing each line
    */
    public static List<String> splitIntoLines(String input) {
        List<String> lines = new ArrayList<>();
        if (input == null || input.isEmpty()) return lines;
        String[] split = input.split("\\r?\\n");
        for (String line : split) {
            if (!line.trim().isEmpty()) lines.add(line);
        }
        return lines;
    }

    /** 
    * Reads question data from both raw resources and internal storage to populate the quiz list.
    * <p>
    * 
    * @param 
    * @return void
    */
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

    /** 
    * Handles the logic for checking the current answer and displaying the next question or finishing the quiz.
    * <p>
    * 
    * @param view the view that was clicked to trigger the next question
    * @return void
    */
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

    /** 
    * Opens the activity for adding new questions.
    * <p>
    * 
    * @param view the view that was clicked to trigger adding a question
    * @return void
    */
    public void add(View view) {
        Intent shaw = new Intent(this, writing.class);
        startActivity(shaw);
    }

    /** 
    * Refreshes the quiz state and high score when returning to the activity.
    * <p>
    * 
    * @param 
    * @return void
    */
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

    /** 
    * Removes duplicate elements from a list while maintaining order.
    * <p>
    * 
    * @param list the list containing potentially duplicate elements
    * @return a new list with duplicates removed
    */
    public static <T> List<T> removeDuplicates(List<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        return new ArrayList<>(set);
    }

    /** 
    * Inflates the options menu for the activity.
    * <p>
    * 
    * @param menu the options menu in which items are placed
    * @return true if the menu was successfully created
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /** 
    * Handles the action when a menu item is selected.
    * <p>
    * 
    * @param item the menu item that was selected
    * @return true if the action was handled successfully
    */
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
