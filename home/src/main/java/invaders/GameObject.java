package invaders;

import invaders.engine.GameEngine;
import invaders.entities.EntityView;
import invaders.rendering.Renderable;

import java.util.List;

// contains basic methods that all GameObjects must implement
public interface GameObject {

    public List<Renderable> update(GameEngine model, List<EntityView> entityViews);
}
