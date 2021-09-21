package controllers;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuController implements Initializable{

	Stage primaryStage = new Stage();
	
	@FXML 
	private Label lLoginUsername;
	@FXML
	private TextField tfCheckStatus;
	String status;
	
	public void displayUsername(String username, String status) {
		lLoginUsername.setFont(Font.font(16));
		lLoginUsername.setText(username);
		this.status = status;
	}
	
	public void goToDownloadDB() throws Exception {
		if( status.equals("Operator")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Access restricted!");
			alert.setContentText("Operators don't have access!");
			alert.showAndWait();
		}
		else {
		
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DownloadDB.fxml"));
			Parent root = loader.load();
			
			DownloadDBController controller= loader.getController();
			controller.getUsername(lLoginUsername.getText(), status);
			
			Scene scene = new Scene(root);
			primaryStage.setTitle("Download Database");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();	
			Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
		}
	}
	@FXML
	Button bModifyUsers;
	
	@FXML
	private void goToAddCall() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AddCall.fxml"));
		Parent root = loader.load();
		
		AddCallController controller= loader.getController();
		controller.getUsername(lLoginUsername.getText(), status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Add Call");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@FXML
	private void goToPrograms() throws Exception{
		if( status.equals("Operator") || status.equals("Operator")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Access restricted!");
			alert.setContentText("Only Adm role has access to this section!");
			alert.showAndWait();
		}
		else {
		
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ModifyUser.fxml"));
			Parent root = loader.load();
			
			ModifyUserController controller= loader.getController();
			controller.getUsername(lLoginUsername.getText(), status);
			
			Scene scene = new Scene(root);
			primaryStage.setTitle("Modify User");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();	
			Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
		}
	}
	
	@FXML
	private void goToSchedule() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Schedule.fxml"));
		Parent root = loader.load();
		
		ScheduleController controller= loader.getController();
		controller.getUsername(lLoginUsername.getText(), status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Schedule");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	 
	@FXML
	private void goToRegistration() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Registration.fxml"));
		Parent root = loader.load();
		
		RegistrationController controller = loader.getController();
		controller.getUsername(lLoginUsername.getText(), status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Registration");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	

	@FXML
	private void goToAdministration() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Administration.fxml"));
		Parent root = loader.load();
		
		AdministrationController controller = loader.getController();
		controller.getUsername(lLoginUsername.getText(), status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Administration");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	public void logout() throws Exception{	
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}	

	@FXML
	private Button bLogout;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bLogout.setCancelButton(true);
	}
}
