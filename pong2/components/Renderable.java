package pong2.components;

import ecs.Component;
import pong2.components.renderables.abstracts.RenderableObject;

import java.awt.Graphics;

public class Renderable implements Component {
    private RenderableObject renderableObject;
    private Class<? extends RenderableObject> objectClass;

    public <T extends RenderableObject> Renderable(T renderableObject) {
        this.renderableObject = renderableObject;
        this.objectClass = renderableObject.getClass();
    }

    public RenderableObject getObject() {
        return renderableObject;
    }

    @SuppressWarnings("unchecked")
    public <T extends RenderableObject> Class<T> getObjectClass() {
        return (Class<T>) objectClass;
    }

    public <T extends RenderableObject> void set(T renderableObject) {
        this.renderableObject = renderableObject;
        this.objectClass = renderableObject.getClass();
    }

    public void draw(Graphics graphics) {
        renderableObject.draw(graphics);
    }

    @Override
    public String toString() {
        return renderableObject.toString();
    }
}
