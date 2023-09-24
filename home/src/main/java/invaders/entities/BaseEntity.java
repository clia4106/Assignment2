package invaders.entities;

import invaders.engine.GameEngine;
import invaders.physics.BoxCollider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;

public class BaseEntity implements Renderable {

    private final List<String>  collidingEntities;
    private final Vector2D position;
    private final double width;
    private final double height;
    private final Image image;

    public BaseEntity(String[] collidingEntities, Vector2D position, double width, double height, Image image) {
        this.collidingEntities = Arrays.asList(collidingEntities);
        this.position = position;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }


    @Override
    public void dealColliding(GameEngine model, List<EntityView> entityViews,Renderable renderable) {

    }

    public Renderable checkIsColliding(List<Renderable> renderables) {
        if(collidingEntities != null){
            BoxCollider boxCollider = new BoxCollider(this.getWidth(), this.getHeight(), this.getPosition());
            for (int i = 0; i < renderables.size(); i++) {
                Renderable renderable = renderables.get(i);
                if(collidingEntities.indexOf(renderable.getClass().getName()) == -1){
                    continue;
                }
                boolean isColliding = boxCollider.isColliding(new BoxCollider(renderable.getWidth(), renderable.getHeight(), renderable.getPosition()));
                if(isColliding){
                    return renderable;
                }
            }
        }
        return null;
    }
    public EntityView findEntityViewByRenderable(List<EntityView> entityViews){
        EntityView result = null;

        for (EntityView view : entityViews) {
            if (view.matchesEntity(this)) {
                result = view;
            }
        }
        if (result == null) {
            EntityView entityView = new EntityViewImpl(this);
            entityViews.add(entityView);
            result = entityView;
        }
        return result;
    }

}
