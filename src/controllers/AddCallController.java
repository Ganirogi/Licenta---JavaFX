package controllers;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AddCallController implements Initializable{

	Stage primaryStage = new Stage();
	
	String username, status;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	public void backToMenu() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu.fxml"));
		Parent root = loader.load();

		MenuController controller = loader.getController();
		controller.displayUsername(username, status);

		Scene scene = new Scene(root);
		primaryStage.setTitle("Menu");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	public void goToHistoryAddCall() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HistoryAddCall.fxml"));
		Parent root = loader.load();

		HistoryAddCallController controller = loader.getController();
		controller.getUsername(username, status);

		Scene scene = new Scene(root);
		primaryStage.setTitle("History");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@FXML
	private TextField tfIdTicket, tfPhoneNumber, tfIssueTicket;
	@FXML
	private TextArea taDescription;
	@FXML
	private Button bRegisterAC;
	
	public void addCall() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();

		try {
			
			String insert = "INSERT INTO workloaddone(username, IDticket, phonenumber, issueticket, description, date) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, username);
			statement.setString(2, tfIdTicket.getText());
			statement.setString(3, tfPhoneNumber.getText());
			statement.setString(4, tfIssueTicket.getText());
			statement.setString(5, taDescription.getText());
			statement.setString(6, formatter.format(date));
			
			int rows = statement.executeUpdate();
			if( rows > 0)
				System.out.println("A row has been inserted");
			
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		tfIdTicket.setText(null); tfPhoneNumber.setText(null); tfIssueTicket.setText(null); taDescription.setText(null);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bRegisterAC.setDefaultButton(true);
		
	}

}
