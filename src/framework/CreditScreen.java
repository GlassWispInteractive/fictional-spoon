package framework;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class CreditScreen extends State{

    private static CreditScreen singleton;

    public static CreditScreen getCreditScreen() {

	if (singleton == null) {
	    singleton = new CreditScreen();
	}

	return singleton;
    }

    private CreditScreen() {
	
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
	
	// font settings
	gc.setFont(Window.HUGE_FONT);
	gc.setTextAlign(TextAlignment.CENTER);
	gc.setTextBaseline(VPos.BASELINE);
	// gc.setLineWidth(1);

	//Title
	gc.setFill(Color.RED);
	gc.fillText(Window.TITLE, Window.SIZE_X / 2, Window.SIZE_Y / 2);
	
    }

}
