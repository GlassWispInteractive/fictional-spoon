package framework;

public abstract class ScreenDecorator extends Screen {
	protected Screen decoratedScreen;

	public ScreenDecorator(Screen decoratedScreen) {
		this.decoratedScreen = decoratedScreen;
		group = decoratedScreen.group;
		scene = decoratedScreen.scene;
		layers = decoratedScreen.layers;
		gcs = decoratedScreen.gcs;
	}

	protected void tick(int ticks) {
		decoratedScreen.tick(ticks);
	}

	protected void render() {
		decoratedScreen.render();
	}
}
