package game.control;

public abstract class ScreenDecorator extends Screen {
    protected Screen decoratedScreen;
    
    public ScreenDecorator(Screen decoratedScreen) {
        this.decoratedScreen = decoratedScreen;
        group = decoratedScreen.group;
        scene = decoratedScreen.scene;
        nodes = decoratedScreen.nodes;
        gcs = decoratedScreen.gcs;
    }
    
    @Override
    public void tick(int ticks) {
        decoratedScreen.tick(ticks);
    }
    
    @Override
    public void render() {
        decoratedScreen.render();
    }
}
