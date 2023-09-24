package invaders.status.impl;

import invaders.physics.Vector2D;
import invaders.status.BunkerStatus;
import javafx.scene.image.Image;

public class BunkerStatusZero implements BunkerStatus {

    private final Image image ;

    public BunkerStatusZero(Vector2D position, int width, int height) {
        this.image = new Image(createBunker(width,height,0));
    }

    @Override
    public Image getImage() {
        return this.image;
    }
}
