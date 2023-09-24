package invaders.status.impl;

import invaders.physics.Vector2D;
import invaders.status.BunkerStatus;
import javafx.scene.image.Image;

public class BunkerStatusOne implements BunkerStatus {

    private final Image image ;

    public BunkerStatusOne(Vector2D position, int width, int height) {
        this.image = new Image(createBunker(width,height,1));
    }

    @Override
    public Image getImage() {
        return this.image;
    }
}
