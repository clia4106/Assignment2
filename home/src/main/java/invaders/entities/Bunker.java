package invaders.entities;

import invaders.GameObject;
import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.status.BunkerStatus;
import invaders.status.impl.BunkerStatusOne;
import invaders.status.impl.BunkerStatusTwo;
import invaders.status.impl.BunkerStatusZero;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Bunker extends BaseEntity implements GameObject {

    List<BunkerStatus> allStatus;
    private BunkerStatus status;
    public Bunker(Vector2D position, double width, double height) {
        super(new String[]{}, position, width, height, null);
        allStatus = new ArrayList<BunkerStatus>();
        allStatus.add(new BunkerStatusZero(position,(int)width,(int)height));
        allStatus.add(new BunkerStatusOne(position,(int)width,(int)height));
        allStatus.add(new BunkerStatusTwo(position,(int)width,(int)height));
        this.status = allStatus.get(0);
    }

    @Override
    public Image getImage() {
        return status.getImage();
    }

    @Override
    public List<Renderable> update(GameEngine model, List<EntityView> entityViews) {

        return null;
    }

    @Override
    public void dealColliding(GameEngine model, List<EntityView> entityViews,Renderable renderable) {
        if(allStatus.indexOf(this.status) >= allStatus.size()-1){
            EntityView entityView = findEntityViewByRenderable(entityViews);
            entityView.markForDelete();
            model.getRenderables().remove(this);
        }else{
            this.status = allStatus.get(allStatus.indexOf(this.status)+1);
        }
    }


}
