package core;

import java.util.ArrayList;

import javafx.scene.input.KeyEvent;

public class Events {
    private static ArrayList<String> input = new ArrayList<String>();;
    
    private Events() {
        throw new AssertionError("no objects of this class available");
    }
    
    public static void addCode(KeyEvent e) {
        String code = e.getCode().toString();
        if (!input.contains(code))
            input.add(code);
    }
    
    public static void removeCode(KeyEvent e) {
        String code = e.getCode().toString();
        // delete.add(code); // remove later
        input.remove(code);
    }
    
    public static void clear() {
        input.clear();
    }
    
    public static boolean isLeft() {
        return input.contains("LEFT") || input.contains("A");
    }
    
    public static boolean isRight() {
        return input.contains("RIGHT") || input.contains("D");
    }
    
    public static boolean isUp() {
        return input.contains("UP") || input.contains("W");
    }
    
    public static boolean isDown() {
        return input.contains("DOWN") || input.contains("S");
    }
    
    public static boolean isC() {
        return input.contains("C");
    }
    
    public static boolean isQ() {
        return input.contains("Q");
    }
    
    public static boolean isEnter() {
        return input.contains("ENTER");
    }
    
    public static boolean isESC() {
        return input.contains("ESCAPE");
    }
    
    public static boolean isOne() {
        return input.contains("DIGIT1") || input.contains("NUMPAD1");
    }
    
    public static boolean isTwo() {
        return input.contains("DIGIT2") || input.contains("NUMPAD2");
    }
    
    public static boolean isThree() {
        return input.contains("DIGIT3") || input.contains("NUMPAD3");
    }
    
    public static boolean isFour() {
        return input.contains("DIGIT4") || input.contains("NUMPAD4");
    }
    
    public static boolean isFive() {
        return input.contains("DIGIT5") || input.contains("NUMPAD5");
    }
}
