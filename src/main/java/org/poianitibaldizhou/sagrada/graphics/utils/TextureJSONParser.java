package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * OVERVIEW: Read the json of the sprite sheets 
 */
public class TextureJSONParser {

    private JSONObject root;

    private static final String ROOT_KEY = "frames";
    private static final String FRAME_KEY = "frame";

    private static final String META_KEY = "meta";
    private static final String SIZE_KEY = "size";

    /**
     * Constructor.
     * Create a TextureJSONParser to read the json of the sprite sheets
     *
     * @param resourcePath the resource path of the json
     * @throws ParseException if there are parse errors
     * @throws IOException if there are input/output errors
     */
    public TextureJSONParser(String resourcePath) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        root = (JSONObject) parser.parse(new InputStreamReader(getClass().getClassLoader()
                .getResourceAsStream(resourcePath)));

    }

    /**
     * @param imageKey the name of the file image
     * @return the rectangle2D containing the position of the image, its width and its height
     */
    public Rectangle2D getRectangleView(String imageKey){
        JSONObject frames = (JSONObject) root.get(ROOT_KEY);
        JSONObject imageObject = (JSONObject) frames.get(imageKey);
        JSONObject frameObject = (JSONObject) imageObject.get(FRAME_KEY);
        long x = (Long) frameObject.get("x");
        long y = (Long) frameObject.get("y");
        long width = (Long) frameObject.get("w");
        long height = (Long) frameObject.get("h");
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * @return a point2D containing the width and height of the entire sprite sheet
     */
    public Point2D getImageSize() {
        JSONObject metaObject = (JSONObject) root.get(META_KEY);
        JSONObject imageSize = (JSONObject) metaObject.get(SIZE_KEY);
        long width = (Long) imageSize.get("w");
        long height = (Long) imageSize.get("h");
        return new Point2D(width, height);
    }
}
