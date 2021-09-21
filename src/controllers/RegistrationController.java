package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class RegistrationController implements Initializable {

	Stage primaryStage = new Stage();

	String username, firstName, lastName,buttonFocus, status;

	public void getUsername(String getUsername, String status) {
		username = getUsername;
		this.status = status;
	}

	public void backToMenu() throws Exception {
		timeline.stop();
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

	@FXML
	Label LCurrentTime = new Label();
	@FXML
	Button bRegistrationBack;
	RegistrationClock time = new RegistrationClock(new CurrentTime().currentTime());
	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
		time.oneSecondPassed();
		LCurrentTime.setText(time.getCurrentTime());
	}));
	
	@FXML
	private Button bIn, bOut;
	
	@FXML
	public void registrationEntryOut() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(System.currentTimeMillis());

		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			System.out.println("Connected to the database accountdb");
			
			if (bIn.isFocused()) buttonFocus = "Entry";
			else buttonFocus= "Out";
			
			String sql = "INSERT INTO accountdb.RegistrationTime (username, firstname, lastname, date, registrationType) VALUES (?, ?, ?, ?, ?)";
			String getFirstName = "SELECT firstname FROM accountdb.account WHERE username=" + "\"" + username + "\";"  ;
			String getLastName = "SELECT lastname FROM accountdb.account WHERE username=" + "\"" + username + "\";"  ;

			Statement stmt = connection.createStatement();
			ResultSet rs1 = stmt.executeQuery(getFirstName);
			if (rs1.next()) { firstName = rs1.getString(1); }

			ResultSet rs2 = stmt.executeQuery(getLastName);
			if (rs2.next()) { lastName = rs2.getString(1); }
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setString(4, formatter.format(date));
			statement.setString(5, buttonFocus);

			int rows = statement.executeUpdate();
			if (rows > 0)
				System.out.println("A row has been inserted");

			rs1.close();
			rs2.close();
			stmt.close();
			statement.close();
			connection.close();

			System.out.println("Connection closed!");

		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	}

	public void goToHistoryRegistration() throws Exception{
		timeline.stop();
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HistoryRegistration.fxml"));
		Parent root = loader.load();

		HistoryRegistrationController controller = loader.getController();
		controller.getUsername(username, status);

		Scene scene = new Scene(root);
		primaryStage.setTitle("History Registration");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		bRegistrationBack.setCancelButton(true);
		LCurrentTime.setFont(Font.font(160));
		LCurrentTime.setAlignment(Pos.CENTER);
		LCurrentTime.setText(time.getCurrentTime());

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

}
