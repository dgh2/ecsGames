package pong2.components;

import ecs.Component;
import pong2.components.renderables.abstracts.RenderableObject;

import java.awt.Graphics;

public class Renderable implements Component, Comparable<Renderable> {
    private Double zIndex;
    private RenderableObject renderableObject;
    private Class<? extends RenderableObject> objectClass;

    public <T extends RenderableObject> Renderable(T renderableObject) {
        this(renderableObject, 0);
    }

    public <T extends RenderableObject> Renderable(T renderableObject, double zIndex) {
        this.renderableObject = renderableObject;
        this.objectClass = renderableObject.getClass();
        this.zIndex = zIndex;
    }

    public Double getZIndex() {
        return zIndex;
    }

    public void setZIndex(Double zIndex) {
        this.zIndex = zIndex;
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

    @Override
    public int compareTo(Renderable o) {
        return getZIndex().compareTo(o.getZIndex());
    }
}
