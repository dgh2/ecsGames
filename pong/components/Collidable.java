package game1.components;

import ecs.Component;
import java.util.UUID;

public class Collidable implements Component {
    private UUID collidedWith = null;
    public Collidable() {}

    public UUID getCollidedWith() {
        return collidedWith;
    }

    public void setCollidedWith(UUID collidedWith) {
        this.collidedWith = collidedWith;
    }
}
