package framework;

import javafx.scene.canvas.GraphicsContext;

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

    }

}
