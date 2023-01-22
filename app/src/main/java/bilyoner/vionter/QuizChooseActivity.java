package bilyoner.vionter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bilyoner.vionter.quiz.Quiz;
import bilyoner.vionter.quiz.QuizParser;


public class QuizChooseActivity extends AppCompatActivity {
    public static String QUIZ_EXTRA = "QUIZ_EXTRA";

    List<Quiz> quizzes = new ArrayList<>();
    List<String> quizListItems = new ArrayList<>();
    ArrayAdapter<String> quizListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_choose);

        quizListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, quizListItems);

        ListView list = findViewById(R.id.quiz_list);
        list.setAdapter(quizListAdapter);
        list.setOnItemClickListener((p, v, position, id) -> onQuizChosen(quizzes.get(position)));

        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadList();
    }

    void onQuizChosen(Quiz quiz) {
        Intent intent = new Intent(QuizChooseActivity.this, PlugActivity.class);
        intent.putExtra(QUIZ_EXTRA, quiz);
        startActivity(intent);
    }

    void loadList() {
        this.quizzes = parseQuiz();
        this.quizListItems.clear();
        this.quizListItems.addAll(formatQuizzes(quizzes));
        this.quizListAdapter.notifyDataSetChanged();
    }

    private List<String> formatQuizzes(List<Quiz> quizzes) {
        List<String> items = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        for (Quiz quiz : quizzes) {
            String s = String.format("%s/%s %s",
                    getRecord(prefs, quiz),
                    quiz.getQuestionCount(),
                    quiz.getTitle()
            );
            items.add(s);
        }

        return items;
    }

    private List<Quiz> parseQuiz() {
        try {
            return QuizParser.read(getAssets().open("quiz_list.xml"));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int getRecord(SharedPreferences pref, Quiz quiz) {
        String quizPrefKey = QuizChooseActivity.getQuizPrefKey(quiz);

        return pref.getInt(quizPrefKey, 0);
    }

    @Override
    public void onBackPressed() {
    }

    public static String getQuizPrefKey(Quiz quiz) {
        return "QUIZ_SCORE_KEY_" + quiz.getTitle();
    }
}