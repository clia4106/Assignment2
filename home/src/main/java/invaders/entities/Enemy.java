package invaders.entities;

import invaders.GameObject;
import invaders.engine.GameEngine;
import invaders.factory.CannonballFactory;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends BaseEntity implements GameObject, Moveable {

    private Random random = new Random();

    private String projectile;

    private boolean isRightMoved = true;

    private int speed = 1;

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        this.getPosition().setY(this.getPosition().getY() + 10);
    }

    @Override
    public void left() {
        this.getPosition().setX(this.getPosition().getX() - speed);
    }

    @Override
    public void right() {
        this.getPosition().setX(this.getPosition().getX() + speed);
    }

    public Enemy(Vector2D position,String projectile) {
        super(new String[]{PlayerCannonball.class.getName(),Bunker.class.getName()}, position, 24, 24, new Image(new File("src/main/resources/c.png").toURI().toString(), 24, 24, true, true));
        this.projectile = projectile;
    }

    @Override
    public List<Renderable> update(GameEngine model, List<EntityView> entityViews) {
        List<Renderable> adds = new ArrayList<>();
        this.speed = model.getCurrentDeadEnemyCount()+1;
        if(this.isRightMoved){
            this.right();
        }else{
            this.left();
        }
        if(this.getPosition().getX() <=0|| this.getPosition().getX() >= model.getWindowWidth()-this.getWidth()){
            this.down();
            this.isRightMoved = !this.isRightMoved;
        }

        if(model.getCurrentEnemyCannonballCount() <3 && random.nextInt(100) == 66){
            adds.add(CannonballFactory.createCannonball(this.projectile,this.getPosition().getX() + this.getWidth()/2,this.getPosition().getY()+this.getHeight()));
            model.currentEnemyCannonballCountUp();
        }
        Renderable renderable = checkIsColliding(model.getRenderables());
        if(renderable != null){
            this.dealColliding(model,entityViews,renderable);
            renderable.dealColliding(model,entityViews,this);
        }
        model.checkIsGameOver(this);
        return adds;
    }

    @Override
    public void dealColliding(GameEngine model, List<EntityView> entityViews,Renderable renderable) {
        if(renderable instanceof PlayerCannonball){
            EntityView entityView = findEntityViewByRenderable(entityViews);
            entityView.markForDelete();
            model.getRenderables().remove(this);
            model.addCurrentDeadEnemyCount();
        }
    }

}
