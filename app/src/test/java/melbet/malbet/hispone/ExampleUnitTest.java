package melbet.malbet.hispone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.xmlpull.v1.XmlPullParserException;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import melbet.malbet.hispone.plug.NewsParser;

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
    public void testParseNews() {
        String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<content>\n" +
                "    <news header=\"aaaa\" image_src=\"123123\">\n" +
                "        wef;ekrmg e\n" +
                "        rgmkermg er\n" +
                "        m elrkmg lekpe\n" +
                "    </news>\n" +
                "    <news header=\"bbb\" image_src=\"123\" ></news>\n" +
                "</content>\n";

        try {
            System.out.println(NewsParser.parse(new ByteArrayInputStream(s.getBytes())));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void randomDashOfString() {
        String loremIpsum = "lorem ipsum";

        System.out.println(PlugActivity.PlugActivityUtils.randomDash(loremIpsum, loremIpsum));
        assertEquals(loremIpsum, PlugActivity.PlugActivityUtils.randomDash(loremIpsum, ""));
        assertEquals(loremIpsum.concat("1"), PlugActivity.PlugActivityUtils.randomDash(loremIpsum, "1"));

        System.out.println(PlugActivity.PlugActivityUtils.randomDash(loremIpsum, "12"));
        System.out.println(PlugActivity.PlugActivityUtils.randomDash(loremIpsum, "12"));

    }
}