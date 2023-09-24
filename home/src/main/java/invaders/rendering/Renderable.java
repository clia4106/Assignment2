package invaders.rendering;

import invaders.GameObject;
import invaders.entities.EntityView;
import invaders.physics.Vector2D;
import invaders.engine.GameEngine;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Represents something that can be rendered
 */
public interface Renderable {

    public Image getImage();

    public double getWidth();
    public double getHeight();

    public Vector2D getPosition();

    public Layer getLayer();

    public void dealColliding(GameEngine model, List<EntityView> entityViews,Renderable renderable);


    /**
     * The set of available layers
     */
    public static enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }
}
