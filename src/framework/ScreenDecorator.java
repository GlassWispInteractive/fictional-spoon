package framework;

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
    protected void tick(int ticks) {
        decoratedScreen.tick(ticks);
    }
    
    @Override
    protected void render() {
        decoratedScreen.render();
    }
}
