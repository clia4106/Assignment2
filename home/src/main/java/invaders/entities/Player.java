package invaders.entities;

import invaders.GameObject;
import invaders.engine.GameEngine;
import invaders.factory.CannonballFactory;
import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Player extends BaseEntity implements Moveable, Damagable , GameObject {

    TextPrompt livesPrompt;

    private int lives = 3;

    Date lastShootTime = null ;

    private double moveSpeed;

    public Player(Vector2D position, int lives, double moveSpeed, TextPrompt livesPrompt){
        super(new String[]{NormalCannonball.class.getName(),LaserCannonball.class.getName()}, position, 25, 30,new Image(new File("src/main/resources/player.png").toURI().toString(), 25, 30, true, true));
        this.moveSpeed = moveSpeed;
        this.lives = lives;
        this.livesPrompt = livesPrompt;
    }

    @Override
    public void takeDamage(double amount) {
        this.lives -= amount;
        this.livesPrompt.setImageKey(String.valueOf(this.lives));
    }

    @Override
    public double getHealth() {
        return this.lives;
    }

    @Override
    public boolean isAlive() {
        return this.lives > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.getPosition().setX(this.getPosition().getX() - moveSpeed);
    }

    @Override
    public void right() {
        this.getPosition().setX(this.getPosition().getX() + moveSpeed);
    }

    public boolean shoot(List<Renderable> renderables, List<GameObject> gameobjects){
        boolean isShoot = false;
        if(lastShootTime == null || (new Date().getTime() - lastShootTime.getTime() > 1000)){
            lastShootTime = new Date();
            isShoot = true;
            Cannonball cannonball = CannonballFactory.createCannonball("player", this.getPosition().getX() + (this.getWidth() / 2), this.getPosition().getY() - 10);
            renderables.add(cannonball);
            gameobjects.add(cannonball);
        }
        return isShoot;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public List<Renderable> update(GameEngine model, List<EntityView> entityViews) {
        if(model.isLeft()){
            this.left();
        }
        if(model.isRight()){
            this.right();
        }
        Renderable renderable = checkIsColliding(model.getRenderables());
        if(renderable != null){
            this.dealColliding(model,entityViews,renderable);
            renderable.dealColliding(model,entityViews,this);
        }

        return null;
    }

    @Override
    public void dealColliding(GameEngine model, List<EntityView> entityViews,Renderable renderable) {
        this.takeDamage(1);
    }
}
