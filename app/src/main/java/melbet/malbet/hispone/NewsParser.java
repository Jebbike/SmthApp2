package melbet.malbet.hispone;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import melbet.malbet.hispone.plug.Info;

public class NewsParser {
    public static List<Info> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private static List<Info> read(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Info> list = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            list.add(readNews(parser));
        }

        return list;
    }

    private static Info readNews(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (!parser.getName().equals("news")) {
            skip(parser);
            return null;
        }

        String header = parser.getAttributeValue(null, "header");
        String text = "";
        String img = parser.getAttributeValue(null, "image_src");

        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
        }
        parser.next();

        return new Info(header, text, img);
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
