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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegisterController implements Initializable {

	Stage primaryStage = new Stage();

	String staff = "DELTA";
	String adm = "OMEGA";
	
	public void backToMain() throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Counter Call");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}

	@FXML
	private Circle cChecker;
	@FXML
	private CheckBox cbPswCheck;
	@FXML
	private Button bBackR;

	@Override
	public void initialize(URL url1, ResourceBundle arg1) {
		pfConfirmPassword.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ObservableValue, String s, String s2) {
				if (pfConfirmPassword.getText().equals(pfPassword.getText()))
					cbPswCheck.setSelected(true);
				else
					cbPswCheck.setSelected(false);
			}
		});

		tfUsername.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ObservableValue, String s, String s2) {
				try {
					ConnectionClass connectionClass = new ConnectionClass();
					Connection connection = connectionClass.getConnection();

					Statement stmt = connection.createStatement();
					Boolean status;
					String check = "SELECT * FROM account WHERE username=" + "\"" + tfUsername.getText() + "\"";
					ResultSet rs = stmt.executeQuery(check);
					if (rs.next())
						status = true;
					else
						status = false;
					rs.close();
					stmt.close();
					connection.close();

					if (status == true)
						cChecker.setFill(Color.RED);
					else
						cChecker.setFill(Color.GREEN);

				} catch (SQLException e) {
					System.out.println("Error, no connection established.");
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	private TextField tfFirstName, tfLastName, tfUsername = new TextField(), pfPassword = new PasswordField(),
			pfConfirmPassword = new PasswordField(), tfRole;
	
	public void RegisterData() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());

		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();

			System.out.println("Connected to the database accountdb!");

			if (tfRole.getText().equals(staff))
				tfRole.setText("Staff");
			else if (tfRole.getText().equals(adm))
				tfRole.setText("Adm");
			else
				tfRole.setText("Operator");

			String sql = "INSERT INTO account (firstname, lastname, username, password, status, date, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

			Statement stmt = connection.createStatement();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, tfFirstName.getText());
			statement.setString(2, tfLastName.getText());
			statement.setString(3, tfUsername.getText());
			statement.setString(4, pfPassword.getText());
			statement.setString(5, "ACTIVE");
			statement.setString(6, formatter.format(date));
			statement.setString(7, tfRole.getText());

			Boolean status;
			
			String check = "SELECT * FROM account WHERE username=" + "\"" + tfUsername.getText() + "\"";
			ResultSet rs = stmt.executeQuery(check);
			if (rs.next()) {
				status = true;
			} else {
				status = false;
			}
			rs.close();
			stmt.close();

			if (tfFirstName.getText() == "" || tfLastName.getText() == "" || tfUsername.getText() == ""
					|| pfPassword.getText() == "" || pfConfirmPassword.getText() == "") {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Data missing!");
				alert.setContentText("A field with * is empty, recheck informations inserted.");
				alert.showAndWait();
			} else {
				if (status == true) {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Data error!");
					alert2.setContentText("Username " + tfUsername.getText() + " already exists!");
					alert2.showAndWait();
				}

				else if (pfPassword.getText().equals(pfConfirmPassword.getText()) == false) {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.setTitle("Data error!");
					alert1.setContentText("Password and Confirm Password must be the same");
					alert1.showAndWait();
				} else {
					int rows = statement.executeUpdate();
					if (rows > 0) {
						System.out.println("A row has been inserted.");
					}
				}

			}

			statement.close();
			connection.close();

			tfFirstName.setText("");
			tfLastName.setText("");
			tfUsername.setText("");
			tfRole.setText("");
			pfPassword.setText("");
			pfConfirmPassword.setText("");
			cbPswCheck.setSelected(false);

			System.out.println("Connection closed!");

		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	}
	
}
