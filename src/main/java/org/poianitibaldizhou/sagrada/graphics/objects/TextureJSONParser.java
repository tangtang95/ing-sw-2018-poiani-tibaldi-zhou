package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.geometry.Rectangle2D;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;


public class TextureJSONParser {

    private JSONObject frames;

    private static final String ROOT_KEY = "frames";
    private static final String FRAME_KEY = "frame";

    public TextureJSONParser(String resourcePath) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(new InputStreamReader(getClass().getClassLoader()
                .getResourceAsStream(resourcePath)));
        frames = (JSONObject) root.get(ROOT_KEY);
    }

    public Rectangle2D getRectangleView(String imageUrl){
        JSONObject imageObject = (JSONObject) frames.get(imageUrl);
        JSONObject frameObject = (JSONObject) imageObject.get(FRAME_KEY);
        long x = (Long) frameObject.get("x");
        long y = (Long) frameObject.get("y");
        long width = (Long) frameObject.get("w");
        long height = (Long) frameObject.get("h");
        return new Rectangle2D(x, y, width, height);
    }

}
