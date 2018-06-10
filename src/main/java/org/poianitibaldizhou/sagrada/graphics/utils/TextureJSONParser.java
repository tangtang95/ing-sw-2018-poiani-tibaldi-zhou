package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;


public class TextureJSONParser {

    private JSONObject root;

    private static final String ROOT_KEY = "frames";
    private static final String FRAME_KEY = "frame";

    private static final String META_KEY = "meta";
    private static final String SIZE_KEY = "size";

    public TextureJSONParser(String resourcePath) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        root = (JSONObject) parser.parse(new InputStreamReader(getClass().getClassLoader()
                .getResourceAsStream(resourcePath)));

    }

    public Rectangle2D getRectangleView(String imageUrl){
        JSONObject frames = (JSONObject) root.get(ROOT_KEY);
        JSONObject imageObject = (JSONObject) frames.get(imageUrl);
        JSONObject frameObject = (JSONObject) imageObject.get(FRAME_KEY);
        long x = (Long) frameObject.get("x");
        long y = (Long) frameObject.get("y");
        long width = (Long) frameObject.get("w");
        long height = (Long) frameObject.get("h");
        return new Rectangle2D(x, y, width, height);
    }

    public Point2D getImageSize() {
        JSONObject metaObject = (JSONObject) root.get(META_KEY);
        JSONObject imageSize = (JSONObject) metaObject.get(SIZE_KEY);
        long width = (Long) imageSize.get("w");
        long height = (Long) imageSize.get("h");
        return new Point2D(width, height);
    }
}
