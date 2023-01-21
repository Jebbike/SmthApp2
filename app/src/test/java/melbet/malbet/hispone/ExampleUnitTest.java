package melbet.malbet.hispone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.xmlpull.v1.XmlPullParserException;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import melbet.malbet.hispone.quiz.QuizParser;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testParseQuiz() {
        String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<content>\n" +
                "    <quiz title=\"lkmer\">\n" +
                "        <question image=\"ddddd\" text=\"aqwer\" time=\"10\">\n" +
                "            <answer correct=\"true\" text=\"123\"/>\n" +
                "            <answer correct=\"false\" text=\"1234\"/>\n" +
                "            <answer correct=\"false\" text=\"12345\"/>\n" +
                "        </question>\n" +
                "\n" +
                "        <question image=\"fffff\" text=\"asdf\" time=\"10\">\n" +
                "            <answer correct=\"true\" text=\"123f\"/>\n" +
                "            <answer correct=\"false\" text=\"1234f\"/>\n" +
                "            <answer correct=\"false\" text=\"12345f\"/>\n" +
                "        </question>\n" +
                "    </quiz>\n" +
                "\n" +
                "    <quiz title=\"lkmer2\">\n" +
                "        <question image=\"23\" text=\"sggg\" time=\"10\">\n" +
                "            <answer correct=\"true\" text=\"123\"/>\n" +
                "            <answer correct=\"false\" text=\"1234\"/>\n" +
                "            <answer correct=\"false\" text=\"12345\"/>\n" +
                "        </question>\n" +
                "    </quiz>\n" +
                "</content>";

        try {
            System.out.println(QuizParser.read(new ByteArrayInputStream(s.getBytes())));

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}