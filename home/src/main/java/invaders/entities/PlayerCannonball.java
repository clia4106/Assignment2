package invaders.entities;

import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

public class PlayerCannonball extends BaseEntity implements Cannonball {

    public PlayerCannonball(Vector2D position) {
        super(new String[]{NormalCannonball.class.getName(),LaserCannonball.class.getName(),Enemy.class.getName()}, position, 4, 10, new Image(new File("src/main/resources/playerCannonball.png").toURI().toString(), 4, 10, true, true));
    }

    @Override
    public List<Renderable> update(GameEngine model, List<EntityView> entityViews) {
        this.up();
        if(this.getPosition().getY() <= 0){
            EntityView thisView = findEntityViewByRenderable(entityViews);
            thisView.markForDelete();
            model.getRenderables().remove(this);
        }
        Renderable renderable = checkIsColliding(model.getRenderables());
        if(renderable != null){
            this.dealColliding(model,entityViews,renderable);
            renderable.dealColliding(model,entityViews,this);
        }
        return null;
    }

    @Override
    public int getSpeed() {
        return baseSpeed*3/2;
    }

    @Override
    public void up() {
        this.getPosition().setY(this.getPosition().getY() - getSpeed());
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        return;
    }

    @Override
    public void right() {
        return;
    }

    public void dealColliding(GameEngine model, List<EntityView> entityViews,Renderable renderable) {
        EntityView entityView = findEntityViewByRenderable(entityViews);
        entityView.markForDelete();
        model.getRenderables().remove(this);
    }
}
