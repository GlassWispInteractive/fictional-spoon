package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Audio {
	private static HashMap<String, Media> audio = new HashMap<>();

	static {
		try {
			Files.walk(Paths.get("res/audio")).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					String[] s = filePath.toString().split("\\\\");
					String name = s[s.length - 1];
					name = name.substring(0, name.lastIndexOf("."));

					audio.put(name, new Media(filePath.toUri().toString()));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void play(String key) {
        if (!audio.containsKey(key)) {
            throw new AssertionError("audio does not exist");
        }
        
		MediaPlayer player = new MediaPlayer(audio.get(key));
		player.setAutoPlay(true);
    }
}
