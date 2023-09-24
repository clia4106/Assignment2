package invaders.engine;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import invaders.GameObject;
import invaders.entities.*;
import invaders.physics.BoxCollider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {

	private boolean gameOverFlag = false;

	private final Renderable gameOverPrompt;

	private final int windowWidth ;
	private final int windowHeight ;

	private int currentDeadEnemyCount = 0 ;

	private int currentEnemyCannonballCount = 0 ;

	private List<GameObject> gameobjects;
	private List<Renderable> renderables;
	private Player player;


	private JSONObject config;

	private boolean left;
	private boolean right;

	public GameEngine(String config){
		// read the config here
		try {
			JSONParser parser = new JSONParser();
			this.config = (JSONObject) parser.parse(new FileReader(config));
		}catch (Exception e){
			this.config = new JSONObject();
		}
		gameobjects = new ArrayList<GameObject>();
		renderables = new ArrayList<Renderable>();

		double playerLocationX = 300;
		double playerLocationY = 750;
		double playerMovedSpeed = 1;
		int playerLives = 3;
		Object x =getConfig("Player.position.x");
		if(x!= null){
			playerLocationX = Double.valueOf(x.toString());
		}
		Object y =getConfig("Player.position.y");
		if(y!= null){
			playerLocationY = Double.valueOf(y.toString());
		}
		Object speed =getConfig("Player.speed");
		if(y!= null){
			playerMovedSpeed = Double.valueOf(speed.toString());
		}
		Object lives =getConfig("Player.lives");
		if(y!= null){
			playerLives = Integer.valueOf(lives.toString());
		}
		Map<String,String> livesMap = new HashMap<>();
		for (int i = 0; i <= playerLives; i++) {
			livesMap.put(String.valueOf(i),"lives:"+i);
		}
		Object gameWindowX =getConfig("Game.size.x");
		this.windowWidth = gameWindowX == null ? 600 : Integer.valueOf(gameWindowX.toString());
		Object gameWindowY =getConfig("Game.size.y");
		this.windowHeight = gameWindowY==null? 800 :  Integer.valueOf(gameWindowY.toString());
		TextPrompt livesPrompt = new TextPrompt(livesMap,new Vector2D(0,10),2,windowWidth,60) ;
		renderables.add(livesPrompt);

		player = new Player(new Vector2D(playerLocationX, playerLocationY),playerLives,playerMovedSpeed,livesPrompt);
		renderables.add(player);
		gameobjects.add(player);

		JSONArray enemies = (JSONArray)getConfig("Enemies");
		if(enemies != null && enemies.size() > 0 ){
			for (int i = 0; i < enemies.size(); i++) {
				JSONObject enemyConfig = (JSONObject)enemies.get(i);
				JSONObject enemypositionConfig = (JSONObject) enemyConfig.get("position");
				Enemy alien = new Enemy(new Vector2D(Double.valueOf(enemypositionConfig.get("x").toString()), Double.valueOf(enemypositionConfig.get("y").toString()))
						,enemyConfig.get("projectile").toString());
				renderables.add(alien);
				gameobjects.add(alien);
			}
		}


		JSONArray bunkers = (JSONArray)getConfig("Bunkers");
		if(bunkers != null && bunkers.size() > 0 ){
			for (int i = 0; i < bunkers.size(); i++) {
				JSONObject bunkerConfig = (JSONObject)bunkers.get(i);
				JSONObject bunkerPositionConfig = (JSONObject) bunkerConfig.get("position");
				JSONObject bunkerSizeConfig = (JSONObject) bunkerConfig.get("size");
				Bunker bunker = new Bunker(new Vector2D(Double.valueOf(bunkerPositionConfig.get("x").toString()), Double.valueOf(bunkerPositionConfig.get("y").toString()))
						,Double.valueOf(bunkerSizeConfig.get("x").toString()),Double.valueOf(bunkerSizeConfig.get("y").toString()));
				renderables.add(bunker);
				gameobjects.add(bunker);
			}
		}


		Map<String,String> map = new HashMap<String,String>();
		map.put("gameOver","Game Over");
		gameOverPrompt = new TextPrompt(map,new Vector2D(0,(windowHeight-60)/2),1,windowWidth,60) ;


	}

	/**
	 * Updates the game/simulation
	 */
	public void update(int windowWidth, int windowHeight, List<EntityView> entityViews){
		List<Renderable> totalAdds = new ArrayList<>() ;
		if(this.gameOverFlag){
			if(renderables.indexOf(gameOverPrompt) ==-1){
				renderables.add(gameOverPrompt);
			}
			return;
		}
		for(GameObject go: gameobjects){
			EntityView current = null ;
			if(go instanceof Renderable){
				for (EntityView view : entityViews) {
					if (view.matchesEntity((Renderable)go)) {
						current = view;
						break;
					}
				}
			}
			if(current == null || !current.isMarkedForDelete()){
				List adds = go.update(this, entityViews);
				if(adds != null && adds.size() > 0){
					totalAdds.addAll(adds);
				}
			}
		}
		if( totalAdds.size() > 0 ){
			for (int i = 0; i < totalAdds.size(); i++) {
				Renderable added = totalAdds.get(i);
				renderables.add(added);
				gameobjects.add((GameObject) added);
			}
		}

		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= windowWidth) {
				ro.getPosition().setX(windowWidth-ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= windowHeight) {
				ro.getPosition().setY(windowHeight-ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}
	public List<GameObject> getGameObjects(){
		return gameobjects;
	}

	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean shootPressed(){
		return player.shoot(renderables,gameobjects);
	}

	public Object getConfig(String key){
		if(this.config != null){
			String[] keys = key.split("\\.");
			if(keys != null && keys.length > 0 ){
				Object obj =this.config;
				for (int i = 0; i < keys.length; i++) {
					if(obj instanceof JSONObject){
						obj = ((JSONObject) obj).get(keys[i]);
					}
				}
				return obj;
			}
		}
		return null ;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public int getCurrentDeadEnemyCount() {
		return currentDeadEnemyCount;
	}

	public void addCurrentDeadEnemyCount(){
		this.currentDeadEnemyCount++;
	}

	public int getCurrentEnemyCannonballCount() {
		return currentEnemyCannonballCount;
	}

	public void currentEnemyCannonballCountDown() {
		this.currentEnemyCannonballCount--;
	}
	public void currentEnemyCannonballCountUp() {
		this.currentEnemyCannonballCount++;
	}

	public void checkIsGameOver(Enemy enemy){
		boolean result = false;
		if(!this.player.isAlive()){
			result = true;
		}
		if(enemy != null){
			if(  enemy.getPosition().getY()>=750||new BoxCollider(enemy.getWidth(),enemy.getHeight(),enemy.getPosition()).isColliding(new BoxCollider(player.getHeight(),player.getWidth(),player.getPosition()))){
				result = true;
			}
		}
		boolean isFind = false ;
		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderable = renderables.get(i);
			if(renderable instanceof Enemy){
				isFind = true;
				break;
			}
		}
		if(!isFind){
			result = true;
		}
		this.gameOverFlag =  result;
	}

}
