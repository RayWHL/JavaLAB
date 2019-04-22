package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*程序入口*/
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Read file fxml and draw interface.
        	J2MySQL aCon = new J2MySQL();
        	aCon.connect2MySQL();
        	FXMLLoader loader = new FXMLLoader(
      	          getClass().getResource("Choose.fxml")
      	        );
        	AnchorPane root = new AnchorPane();
        	Scene myScene = new Scene(root);
      	    myScene.setRoot((Parent) loader.load());
            primaryStage.setScene(myScene);
            primaryStage.setTitle("选择登入类型");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}