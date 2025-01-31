//David Wang and Mark Vaysiberg
package songlib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.ListController;

public class SongLib extends Application {

	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Window.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		ListController listController= loader.getController();
		listController.start();
		listController.start(primaryStage);
		Scene scene = new Scene(root, 625, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
