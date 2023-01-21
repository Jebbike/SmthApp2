package melbet.malbet.hispone.quiz;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class QuizParser {
    public static List<Quiz> read(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return read(parser);
        } finally {
            in.close();
        }
    }

    private static List<Quiz> read(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Quiz> list = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            list.add(readQuiz(parser));
        }

        return list;
    }

    private static Quiz readQuiz(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (!parser.getName().equals("quiz")) {
            skip(parser);
            return null;
        }

        String title = parser.getAttributeValue(null, "title");
        List<Question> questions = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            questions.add(readQuestion(parser));
        }

        return new Quiz(title, questions);
    }

    private static Question readQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (!parser.getName().equals("question")) {
            skip(parser);
            return null;
        }

        String image = parser.getAttributeValue(null, "image");
        String text = parser.getAttributeValue(null, "text");
        Long time = Long.parseLong(parser.getAttributeValue(null, "time"));

        List<Answer> answers = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            parser.next();
            answers.add(readAnswer(parser));
        }

        return new Question(text, image, time, answers);
    }

    private static Answer readAnswer(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (!parser.getName().equals("answer")) {
            skip(parser);
            return null;
        }

        String text = parser.getAttributeValue(null, "text");
        boolean correct = Boolean.parseBoolean(parser.getAttributeValue(null, "correct"));

        return new Answer(text, correct);
    }


    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
