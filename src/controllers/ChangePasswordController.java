package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ChangePasswordController implements Initializable{

	Stage primaryStage = new Stage();
	
	String username, status;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	public void backToAdministration() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Administration.fxml"));
		Parent root = loader.load();
		
		AdministrationController controller= loader.getController();
		controller.getUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Administration");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@FXML
	private TextField tfOldPassword, tfNewPassword, tfRetypePassword;
	@FXML
	private void changePassword() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			if (tfOldPassword.getText().equals("") || tfNewPassword.getText().equals("") || tfRetypePassword.getText().equals("")) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Data missing");
				alert.setContentText("All 3 fields must be filled");
				alert.showAndWait();
				alert.close();
			}
			else if( !tfNewPassword.getText().equals(tfRetypePassword.getText())){ 
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Data missing");
				alert.setContentText("New password and re-type password must be the same");
				alert.showAndWait();
				alert.close();
			}
			else {
				String update = "UPDATE accountdb.account SET password=? WHERE username=" + "\"" + username + "\"";
				
				PreparedStatement  statement = connection.prepareStatement(update);
				statement.setString(1, tfNewPassword.getText());
				
				int rows = statement.executeUpdate();
				if (rows > 0) {
					System.out.println("A row has been updated.");
				}
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
