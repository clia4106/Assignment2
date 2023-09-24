package invaders.status.impl;

import invaders.physics.Vector2D;
import invaders.status.BunkerStatus;
import javafx.scene.image.Image;

public class BunkerStatusTwo implements BunkerStatus {

    private final Image image ;

    public BunkerStatusTwo(Vector2D position, int width, int height) {
        this.image = new Image(createBunker(width,height,2));
    }

    @Override
    public Image getImage() {
        return this.image;
    }
}
