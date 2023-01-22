package bilyoner.vionter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bilyoner.vionter.quiz.Answer;
import bilyoner.vionter.quiz.AnswerListAdapter;
import bilyoner.vionter.quiz.Question;
import bilyoner.vionter.quiz.Quiz;


public class PlugActivity extends AppCompatActivity {

    Quiz quiz;
    List<Answer> currentAnswers = new ArrayList<>();
    AnswerListAdapter adapter;

    GridView answersGrid;
    Button nextQuestionBtn;
    TextView questionTimerTextView;
    CountDownTimer questionTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quiz = (Quiz) getIntent().getSerializableExtra(QuizChooseActivity.QUIZ_EXTRA);
        if (quiz == null)
            return;


        startQuiz(quiz);
    }

    void startQuiz(Quiz quiz) {
        setContentView(R.layout.center_text_view);
        TextView textView = findViewById(R.id.center_text_view_content);

        CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(String.valueOf((int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                setContentView(R.layout.plug_layout);

                adapter = new AnswerListAdapter(PlugActivity.this, currentAnswers);
                answersGrid = findViewById(R.id.answerButtons);
                nextQuestionBtn = findViewById(R.id.plug_next);
                answersGrid.setAdapter(adapter);
                questionTimerTextView = findViewById(R.id.time);

                nextQuestionBtn.setOnClickListener(x -> {
                    if (quiz.hasQuestions()) {
                        quiz.nextQuestion();
                        startQuestion(quiz.getCurrentQuestion());
                    } else {
                        Intent intent = new Intent(PlugActivity.this, QuizChooseActivity.class);
                        startActivity(intent);
                    }
                });


                answersGrid.setOnItemClickListener((parent, v, position, id) -> {
                    Answer answer = (Answer) v.getTag(R.string.TAG_ANSWER_ID);
                    quiz.answer(quiz.getCurrentQuestion().getId(), answer.getId());
                    saveScore(quiz);
                    closeGame();
                });

                startQuestion(quiz.getCurrentQuestion());
            }
        };
        countDownTimer.start();
    }

    public void openGame() {
        nextQuestionBtn.setEnabled(false);
        answersGrid.setEnabled(true);

        for (int i = 0; i < answersGrid.getChildCount(); i++) {
            View child = answersGrid.getChildAt(i);

            child.setBackgroundColor(Color.WHITE);
            ((TextView) child.findViewById(R.id.grid_item_title))
                    .setTextColor(getApplicationContext().getResources().getColor(R.color.green));
        }
    }

    public void closeGame() {
        questionTimer.cancel();
        nextQuestionBtn.setEnabled(true);
        answersGrid.setEnabled(false);

        for (int i = 0; i < answersGrid.getChildCount(); i++) {
            View child = answersGrid.getChildAt(i);
            ((TextView) child.findViewById(R.id.grid_item_title)).setTextColor(Color.WHITE);

            Answer ans = (Answer) child.getTag(R.string.TAG_ANSWER_ID);
            int color = Color.RED;
            if (ans.isCorrect())
                color = getApplicationContext().getResources().getColor(R.color.green);

            child.setBackgroundColor(color);
        }

        if (!quiz.hasQuestions()) {
            nextQuestionBtn.setText(R.string.finish_game);
        }

        ImageView image = findViewById(R.id.item_icon);
        Drawable drawableFromAssets = getDrawableFromAssets(quiz.getCurrentQuestion().getImage());

        image.setImageDrawable(drawableFromAssets);
    }


    public Bitmap blurredImage(Question question) {
        Drawable drawableFromAssets = getDrawableFromAssets(question.getImage());
        if (drawableFromAssets == null)
            return null;


        Bitmap bitmap = ((BitmapDrawable) drawableFromAssets).getBitmap();
        return blur(bitmap);
    }

    public void startQuestion(Question question) {
        currentAnswers.clear();
        currentAnswers.addAll(question.getPossibleAnswers());
        adapter.notifyDataSetChanged();
        openGame();

        TextView header = findViewById(R.id.fowier);
        ImageView image = findViewById(R.id.item_icon);


        image.setImageBitmap(blurredImage(question));


        String text = this.getApplicationContext().getResources().getText(R.string.header_placeholder).toString();
        text = text
                .replace("%a", String.valueOf(quiz.getQuestionIndex() + 1))
                .replace("%b", String.valueOf(quiz.getQuestions().size()));

        header.setText(text);


        questionTimer = new CountDownTimer(question.getResponseTime() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                questionTimerTextView.setText(String.valueOf((int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                closeGame();
            }
        };
        questionTimer.start();
    }

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public Drawable getDrawableFromAssets(String fileName) {
        try {
            InputStream ims = getApplicationContext().getAssets().open(fileName);
            return Drawable.createFromStream(ims, null);
        } catch (Exception ex) {
            Log.d("PlugActivity", "Error: ", ex);
            return null;
        }
    }

    public void saveScore(Quiz quiz) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String quizPrefKey = QuizChooseActivity.getQuizPrefKey(quiz);

        if (pref.getInt(quizPrefKey, 0) < quiz.getScore()) {
            pref.edit().putInt(quizPrefKey, quiz.getScore()).apply();
            Log.d("QuizLog", "saved score = " + quiz.getScore());
        }
    }

    @Override
    public void onBackPressed() {
    }
}