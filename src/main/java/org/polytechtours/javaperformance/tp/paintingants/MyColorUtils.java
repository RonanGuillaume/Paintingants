package org.polytechtours.javaperformance.tp.paintingants;

import java.awt.*;

public class MyColorUtils {
    public static int getRed(int couleur){
        return (couleur & 0xFF0000) >> 16;
    }

    public static int getGreen(int couleur){
        return (couleur & 0x00FF00) >> 8;
    }

    public static int getBlue(int couleur){
        return (couleur & 0x0000FF);
    }

    public static int createIntColorFromRGB(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }

    public static Color createCreateColorFromInt(int couleur){
        return new Color(getRed(couleur), getGreen(couleur), getBlue(couleur));
    }
}
