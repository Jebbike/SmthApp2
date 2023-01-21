package melbet.malbet.hispone.quiz;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Question implements Serializable {
    final UUID id;
    String question;
    String image;
    Long responseTime;
    List<Answer> possibleAnswers;


    public Question(String question, String image, Long responseTime, List<Answer> answerList) {
        this.question = question;
        this.image = image;
        this.responseTime = responseTime;
        this.id = UUID.randomUUID();
        this.possibleAnswers = answerList;
    }

    public UUID getId() {
        return id;
    }

    public List<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", image='" + image + '\'' +
                ", responseTime=" + responseTime +
                ", possibleAnswers=" + possibleAnswers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        if (!question.equals(question1.question)) return false;
        return possibleAnswers.equals(question1.possibleAnswers);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + possibleAnswers.hashCode();
        return result;
    }

    public String getQuestion() {
        return question;
    }

    public String getImage() {
        return image;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public boolean answer(UUID answerId) throws NullPointerException {
        for (Answer ans : possibleAnswers) {
            if (ans.id.equals(answerId))
                return ans.isCorrect();
        }
        return false;
    }

}