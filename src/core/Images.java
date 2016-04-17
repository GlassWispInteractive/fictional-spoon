package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javafx.scene.image.Image;

public class Images {
    private static HashMap<String, Image> images = new HashMap<>();
    
    static {
    	try {
            Files.walk(Paths.get("res/images")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String[] s = filePath.toString().split("\\\\");
                    String name = s[s.length - 1];
                    name = name.substring(0, name.lastIndexOf("."));
                    
                    images.put(name, new Image(filePath.toUri().toString()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Image get(String key) {
        if (!images.containsKey(key)) {
            throw new AssertionError("image does not exist");
        }
        
        return images.get(key);
    }
}
