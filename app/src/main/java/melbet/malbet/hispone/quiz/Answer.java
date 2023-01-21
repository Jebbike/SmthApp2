package melbet.malbet.hispone.quiz;

import java.io.Serializable;
import java.util.UUID;

public class Answer implements Serializable {
    final UUID id;
    String answer;
    boolean correct;

    public Answer(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;

        this.id = UUID.randomUUID();
    }


    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                ", correct=" + correct +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer1 = (Answer) o;

        if (correct != answer1.correct) return false;
        return answer.equals(answer1.answer);
    }

    @Override
    public int hashCode() {
        int result = answer.hashCode();
        result = 31 * result + (correct ? 1 : 0);
        return result;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getAnswer() {
        return answer;
    }

    public UUID getId() {
        return id;
    }
}