package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javafx.scene.media.AudioClip;

public class Audio {
    private static HashMap<String, AudioClip> audio = new HashMap<>();
    
    static {
    	try {
            Files.walk(Paths.get("res/images")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String[] s = filePath.toString().split("\\\\");
                    String name = s[s.length - 1];
                    name = name.substring(0, name.lastIndexOf("."));
                    
                    audio.put(name, new AudioClip(filePath.toUri().toString()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void get(String key) {
        if (!audio.containsKey(key)) {
            throw new AssertionError("image does not exist");
        }
        
        audio.get(key).play();
    }
}
