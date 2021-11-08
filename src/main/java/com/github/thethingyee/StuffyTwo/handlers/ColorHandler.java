package com.github.thethingyee.StuffyTwo.handlers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;

/**
 * ColorHandler is meant specifically for getting dominant colors.
 */
public class ColorHandler {

    /**
     * Color is for the RGB values and Integer is for the votes.
     */
    private final HashMap<Color, Integer> votes = new HashMap<>();
    private final HashMap<int[], Integer> votes2 = new HashMap<>();
    private final BufferedImage img;

    /**
     * Stores image into cache to be used with methods.
     *
     * @param img Input image to be used.
     */
    public ColorHandler(BufferedImage img) {
        this.img = img;
    }

    /**
     * Gets the dominant color of the stored image.
     * Uses the voting algorithm to see what is the most used color in the image.
     *
     * @return Returns the most dominant color of the cached image.
     */
    public Color getDominantColor() {
        int blackLevel = 15;
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                Color c = new Color(img.getRGB(x, y));
                if(c.getRed() < blackLevel && c.getGreen() < blackLevel && c.getBlue() < blackLevel) continue;
                int cVote = votes.containsKey(c) ? votes.get(c) + 1 : 1;
                votes.put(c, cVote);
            }
        }

        int max = Collections.max(votes.values());
        for(Color c : votes.keySet()) {
            if(votes.get(c) == max) {
                return c;
            }
        }

        return new Color(0, 0, 0);
    }

    /**
     * Gets the dominant color of the stored image.
     * Still uses the voting algorithm but it uses raster instead of a Color object.
     *
     * @return Returns the most dominant color of the cached image.
     */
    public int[] getDominantColorByRaster() {
        int[] pixel;
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                pixel = img.getRaster().getPixel(x, y, new int[3]);
                if(pixel[0] < 15 && pixel[1] < 15 && pixel[2] < 15) continue;
                int cVote = votes2.containsKey(pixel) ? votes2.get(pixel) + 1 : 1;
                votes2.put(pixel, cVote);
            }
        }

        int max = Collections.max(votes2.values());
        for(int[] c : votes2.keySet()) {
            if(votes2.get(c) == max) {
                return c;
            }
        }

        return new int[3];
    }
}
