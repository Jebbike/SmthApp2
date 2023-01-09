package melbet.malbet.hispone;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
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