package controllers;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ModifyUserController implements Initializable{

	Stage primaryStage = new Stage();
	
	String staff = "DELTA";
	String adm = "OMEGA";
	
	String username, status, firstnameOld, lastnameOld, roleOld, statusOld;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	public void backToMenu() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu.fxml"));
		Parent root = loader.load();
		
		MenuController controller= loader.getController();
		controller.displayUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Modify User");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@FXML
	private TextField tfUsernameMD, tfFirstnameMD, tfLastnameMD, tfRoleMD, tfStatusMD;
	@FXML
	private Button bBackMU;
	
	public void modifyUser() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
	
			System.out.println("Connected to the database accountdb!");
			
			String oldValues = "SELECT firstname, lastname, role, status FROM accountdb.account WHERE username=" + "\"" + tfUsernameMD.getText() + "\"";		
			String update = "UPDATE accountdb.account SET firstname=?, lastname=?, role=?, status=? WHERE username=" + "\"" + tfUsernameMD.getText() + "\"";
		
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(oldValues);
			if (rs.next()) {
				firstnameOld = rs.getString(1); 
				lastnameOld = rs.getString(2);
				roleOld = rs.getString(3);
				statusOld = rs.getString(4); 
			}
			rs.close();
			
			if ( !tfFirstnameMD.getText().equals("")  ) firstnameOld = tfFirstnameMD.getText();
			if ( !tfLastnameMD.getText().equals("") ) lastnameOld = tfLastnameMD.getText();
			if ( !tfRoleMD.getText().equals("")) roleOld = tfRoleMD.getText();
			if ( !tfStatusMD.getText().equals("")) statusOld = tfStatusMD.getText();

			if (tfRoleMD.getText().equals(staff))
				tfRoleMD.setText("Staff");
			else if (tfRoleMD.getText().equals(adm))
				tfRoleMD.setText("Adm");
			else
				tfRoleMD.setText("Operator");
			
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, firstnameOld);
			statement.setString(2, lastnameOld);
			statement.setString(3, roleOld);
			statement.setString(4, statusOld);
			
			Boolean state;
			
			String check = "SELECT * FROM accountdb.account WHERE username=" + "\"" + tfUsernameMD.getText() + "\"";
			ResultSet rs1 = stmt.executeQuery(check);
			if (rs1.next()) {
				state = true;
			} else {
				state = false;
			}
			rs1.close();
			stmt.close();
			
			if( tfUsernameMD.getText().equals(null)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Username missing!");
				alert.setContentText("Username must be completed to modify his profile.");
				alert.showAndWait();
			}
			else if (state.equals(true)){
				int rows = statement.executeUpdate();
				if (rows > 0) {
					System.out.println("A row has been inserted.");
				}
			}
			
			tfUsernameMD.setText("");
			tfFirstnameMD.setText("");
			tfLastnameMD.setText("");
			tfRoleMD.setText("");
			tfStatusMD.setText("");
			
			statement.close();
			connection.close();
				
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	}
	
	public void deleteUser() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
	
			System.out.println("Connected to the database accountdb!");
			
			String delete = "DELETE FROM accountdb.account WHERE username=" + "\"" + tfUsernameMD.getText() + "\"";
			
			PreparedStatement statement = connection.prepareStatement(delete);
			if( tfUsernameMD.getText().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Username missing!");
				alert.setContentText("Complete username field to delete the user inserted.");
				alert.showAndWait();
			}
			else {
				int rows = statement.executeUpdate();
				if( rows > 0 ) System.out.println("A row has been deleted.");
			}
			
			System.out.println("Connection closed!");
			
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
