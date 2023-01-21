package melbet.malbet.hispone.quiz;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Quiz implements Serializable {
    final UUID uuid;
    String title;
    List<Question> questions;
    int score = 0;
    int questionIndex = -1;

    public Quiz(String title, List<Question> questions) {
        this.title = title;
        this.questions = questions;
        this.uuid = UUID.randomUUID();
    }

    public int getQuestionCount(){
        return questions.size();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + questions.hashCode();
        return result;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean hasQuestions() {
        System.out.println("idx = "+questionIndex);
        return questionIndex < questions.size() - 1;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public Question getCurrentQuestion() {
        if (questions.isEmpty())
            return null;

        if (questionIndex == -1) {
            questionIndex = 0;
        }

        return questions.get(questionIndex);
    }

    public int getScore() {
        return score;
    }

    public boolean answer(UUID questionId, UUID answerId) throws NullPointerException {
        for (Question q : questions) {
            if (q.id.equals(questionId)) {
                if (q.answer(answerId)) {
                    score += 1;
                    return true;
                }

                return false;
            }
        }
        return false;
    }

    public boolean nextQuestion() {
        if (questionIndex < questions.size() - 1) {
            questionIndex += 1;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", questions=" + questions +
                ", score=" + score +
                ", questionIndex=" + questionIndex +
                '}';
    }
}
