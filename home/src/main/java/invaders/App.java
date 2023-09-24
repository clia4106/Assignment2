package invaders;

import javafx.application.Application;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;
import org.json.simple.JSONObject;

import java.util.Map;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GameEngine model = new GameEngine("src/main/resources/config.json");

        GameWindow window = new GameWindow(model, model.getWindowWidth(), model.getWindowHeight());

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}
