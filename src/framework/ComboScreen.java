package framework;

import java.util.ArrayList;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import combat.Combo;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ComboScreen extends State {

    private static ComboScreen singleton;

    public static ComboScreen getComboScreen() {

	if (singleton == null) {
	    singleton = new ComboScreen();
	}

	return singleton;
    }

    private ComboScreen() {

	initBackgroundSouls();
    }

    @Override
    protected void tick(int ticks) {

	computeBackgroundSouls(ticks);

    }

    @Override
    protected void render() {

	// start from clean screen
	GraphicsContext gc = gcs.get(0);
	gc.clearRect(0, 0, Window.SIZE_X, Window.SIZE_Y);

	renderBackgroundSouls(gc);

	renderTextboxes(gc);

    }

    private void renderTextboxes(GraphicsContext gc) {

	// font settings
	gc.setFont(Window.bigFont);
	gc.setTextAlign(TextAlignment.CENTER);
	gc.setTextBaseline(VPos.BASELINE);
	// gc.setLineWidth(1);

	ArrayList<String> comboNames = new ArrayList<String>();
	comboNames.add("Combos:");
	for (Combo combo : Combo.getCombosInUse()) {
	    comboNames.add(combo.toString());
	}
	if (Combo.getCombosInUse().size() == 0) {
	    comboNames.clear();
	    comboNames.add("No Combos");
	}

	FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
	int textWidth = (int) fontLoader.computeStringWidth("", gc.getFont());
	int textHeight = (int) fontLoader.getFontMetrics(gc.getFont())
		.getLineHeight();

	// calc textLength
	for (int i = 0; i < comboNames.size(); i++) {
	    
	    if (i == 0) {
		gc.setFont(Window.hugeFont);
	    } else {
		gc.setFont(Window.bigFont);
	    }
	    if (textWidth < (int) fontLoader.computeStringWidth(
		    comboNames.get(i).toString(), gc.getFont())) {
		textWidth = (int) fontLoader.computeStringWidth(
			comboNames.get(i).toString(), gc.getFont());
	    }
	}

	int padding = 10;
	int width = textWidth + 2 * padding;
	int height = (int) (1.5 * textHeight);
	int rowY = (int) (Window.SIZE_Y * 0.2);
	int columnX = (int) (Window.SIZE_X /2 - width / 2 -  padding);
	
	gc.setStroke(Color.ORANGE);
	gc.strokeLine(0, rowY + padding, Window.SIZE_X, rowY + padding);

	for (int j = 0; j < Math.min(10, comboNames.size()); j++) {

	    if (j == 0) {
		gc.setFont(Window.hugeFont);
		
	    } else {
		gc.setFont(Window.bigFont);
	    }

	    gc.setFill(Color.ORANGE);
	    gc.fillText(comboNames.get(j).toString(), columnX + width / 2, rowY);

	    rowY += height;
	}
    }
}
