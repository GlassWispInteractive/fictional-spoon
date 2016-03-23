package framework;

public abstract class ScreenDecorator extends Screen {
	protected Screen decoratedScreen;
	
	public ScreenDecorator(Screen decoratedScreen) {
		this.decoratedScreen = decoratedScreen;
	}
	
	protected void tick(int ticks) {
		decoratedScreen.tick(ticks);
		System.out.println("screen decorator tick");
	}

	protected void render() {
		decoratedScreen.render();
	}
}
