package controllers;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController implements Initializable{

	Stage primaryStage = new Stage();
		
	String username, status;
	
		public void goRegister() throws Exception {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Register.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Counter Call");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
		}

		@FXML 
		private Button bLogin = new Button();
		
		@Override
		public void initialize(URL url, ResourceBundle resourceBundle) {
			bLogin.setDefaultButton(true);
		}
		
		@FXML
		private GridPane gpGridPane = new GridPane();
		@FXML
		private Text tUsername = new Text();
		@FXML
		private TextField tfUserName;
		@FXML
		private PasswordField pfLPassword;
		@FXML
		private void checkUserAndPass() throws Exception{
		 	try {
		 		ConnectionClass connectionClass = new ConnectionClass();
		 		Connection connection = connectionClass.getConnection();
				Statement stmt = connection.createStatement();
				Boolean state;
				String checkUsername = "SELECT * FROM account WHERE username="+ "\"" + tfUserName.getText() + "\"";
				String checkPassword = "SELECT * FROM account WHERE password="+ "\"" + pfLPassword.getText() + "\"";
				String select =  "SELECT firstname, username, role FROM account WHERE username="+ "\"" + tfUserName.getText() + "\"";
				
				ResultSet rs = stmt.executeQuery(checkUsername);
				if( rs.next()) 
					state = true;
				else
					state = false;
				rs.close();
				if(state == false) 
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Wrong data inserted!");
					alert.setContentText("Wrong username!");
					alert.showAndWait();
				}
				else
				{
					ResultSet rs1 = stmt.executeQuery(checkPassword);
					if( rs1.next())
						state = true;
					else
						state = false;
					rs1.close();
					
					if (state == false)
					{
						Alert alert1 = new Alert(AlertType.ERROR);
						alert1.setTitle("Wrong data inserted!");
						alert1.setContentText("Wrong password!");
						alert1.showAndWait();
					}
					else
					{
						ResultSet rs2 = stmt.executeQuery(select);
						while(rs2.next())
						{
							tUsername.setText(rs2.getString("firstname"));
							username = rs2.getString("username");
							status = rs2.getString("role");
						}
	
						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("Login succesful!");
						alert2.setContentText("Welcome " + tUsername.getText() + "!");

						alert2.showAndWait();
						rs2.close();

						FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu.fxml"));
						Parent root = loader.load();
						//transfer data to MenuController
						MenuController controller = loader.getController();
						controller.displayUsername(username, status);
						
						Scene scene = new Scene(root);
						
						primaryStage.setTitle("Menu");
						primaryStage.setScene(scene);
						primaryStage.setResizable(false);
						primaryStage.show();
						Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
					}
				}
				
				stmt.close();
				connection.close();
				
			} catch (SQLException e) {
				System.out.println("Error, no connection established.");
				e.printStackTrace();
			}
		}
}		