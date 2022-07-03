package com.github.thethingyee.StuffyTwo.handlers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * ColorHandler is meant specifically for getting dominant colors.
 */
public class ColorHandler {

    /**
     * Color is for the RGB values and Integer is for the votes.
     */
    private final HashMap<Color, Integer> votes = new HashMap<>();

    // Source: https://stackoverflow.com/a/63350967
    private final TreeMap<int[], Integer> votes2 = new TreeMap<>(Arrays::compare);
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
        int ignoreBlackLevel = 15;
        for(int y = 0; y < img.getHeight(); y++) {
            for(int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                if(c.getRed() < ignoreBlackLevel && c.getGreen() < ignoreBlackLevel && c.getBlue() < ignoreBlackLevel) continue;
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
        int ignoreBlackLevel = 15;
        int[] pixel;
        for(int y = 0; y < img.getHeight(); y++) {
            for(int x = 0; x < img.getWidth(); x++) {
                pixel = img.getRaster().getPixel(x, y, new int[3]);
                if(pixel[0] < ignoreBlackLevel && pixel[1] < ignoreBlackLevel && pixel[2] < ignoreBlackLevel) continue;
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
