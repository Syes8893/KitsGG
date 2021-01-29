package me.syes.kits.utils;

public class TimeUtils {
	
    public static String format(double data) {
        final int minutes = (int)(data / 60.0);
        final int seconds = (int)(data % 60.0);
        final String str = String.format("%01d:%02d", minutes, seconds);
        return str;
    }
    
}
