package xyz.devcmb.treeTumblers.util;

import net.kyori.adventure.text.Component;

public class Utilities {
    public static Component formatTime(int time){
        int minutes = time / 60;
        int seconds = time % 60;
        return Component.text(String.format("%02d:%02d", minutes, seconds));
    }
}
