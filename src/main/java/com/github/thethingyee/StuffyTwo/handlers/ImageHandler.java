package com.github.thethingyee.StuffyTwo.handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageHandler {

    public static BufferedImage getYoutubeThumbnail(String vidId) {
        try {
            URL thumbnailURL = new URL(getYouTubeThumbnailURL(vidId));
            return ImageIO.read(thumbnailURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getYouTubeThumbnailURL(String vidId) {
        return String.format("https://i.ytimg.com/vi/%s/mqdefault.jpg", vidId);
    }
}
