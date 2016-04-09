package framework;

import java.util.ArrayList;

import javafx.scene.input.KeyEvent;

public class EventControl {
    private static EventControl instance;
    private ArrayList<String> input;
    // private ArrayList<String> delete;
    
    private EventControl() {
        input = new ArrayList<String>();
        // delete = new ArrayList<String>();
    }
    
    public static EventControl getEvents() {
        if (instance == null) {
            instance = new EventControl();
        }
        
        return instance;
    }
    
    public void addCode(KeyEvent e) {
        String code = e.getCode().toString();
        if (!input.contains(code))
            input.add(code);
    }
    
    public void removeCode(KeyEvent e) {
        String code = e.getCode().toString();
        // delete.add(code); // remove later
        input.remove(code);
    }
    
    public void clear() {
        input.clear();
    }
    
    public boolean isLeft() {
        return input.contains("LEFT") || input.contains("A");
    }
    
    public boolean isRight() {
        return input.contains("RIGHT") || input.contains("D");
    }
    
    public boolean isUp() {
        return input.contains("UP") || input.contains("W");
    }
    
    public boolean isDown() {
        return input.contains("DOWN") || input.contains("S");
    }
    
    public boolean isC() {
        return input.contains("C");
    }
    
    public boolean isQ() {
        return input.contains("Q");
    }
    
    public boolean isEnter() {
        return input.contains("ENTER");
    }
    
    public boolean isESC() {
        return input.contains("ESCAPE");
    }
    
    public boolean isOne() {
        return input.contains("DIGIT1") || input.contains("NUMPAD1");
    }
    
    public boolean isTwo() {
        return input.contains("DIGIT2") || input.contains("NUMPAD2");
    }
    
    public boolean isThree() {
        return input.contains("DIGIT3") || input.contains("NUMPAD3");
    }
    
    public boolean isFour() {
        return input.contains("DIGIT4") || input.contains("NUMPAD4");
    }
    
    public boolean isFive() {
        return input.contains("DIGIT5") || input.contains("NUMPAD5");
    }
}
