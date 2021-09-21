package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DownloadDBController implements Initializable{

	Stage primaryStage = new Stage();
	
	String username, status;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	public void getBackToMenu() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu.fxml"));
		Parent root = loader.load();
		
		MenuController controller= loader.getController();
		controller.displayUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Menu");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}
