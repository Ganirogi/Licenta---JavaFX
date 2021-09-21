package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RequestModificationController implements Initializable{

	Stage primaryStage = new Stage();
	
	String username, status, firstName, lastName, roleReq, checkAcceptUsername;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	@FXML
	private void backToSchedule() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Schedule.fxml"));
		Parent root = loader.load();
		
		ScheduleController controller = loader.getController();
		controller.getUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Schedule");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@FXML
	private TextField tfDateRequest, tfScheduleRequest, tfShortDescriptionRequest;
	public void makeRequest() {
		try {
			if( tfDateRequest.getText().equals("") || tfScheduleRequest.getText().equals("") || tfShortDescriptionRequest.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Data missing!");
				alert.setContentText("To make a request complete date, schedule and description");
				alert.showAndWait();
			}
			else {
			
				ConnectionClass connectionClass = new ConnectionClass();
				Connection connection = connectionClass.getConnection();
				
				System.out.println("Connected to the database accountdb");
				
				String insert = "INSERT INTO accountdb.requests (username, firstname, lastname, date, schedule, shortDescription, role, usernameAccept, approved) VALUES (?,?,?,?,?,?,?,?,?)";
				String oldValues = "SELECT firstname, lastname from accountdb.account where username=" + "\"" + username + "\"";
		
				Statement stmt;
				
				stmt = connection.createStatement();
				
				ResultSet rs = stmt.executeQuery(oldValues);
				if ( rs.next()) {
					firstName = rs.getString(1);
					lastName = rs.getString(2);
				}
				
				PreparedStatement statement = connection.prepareStatement(insert);
				statement.setString(1, username);
				statement.setString(2, firstName);
				statement.setString(3, lastName);
				statement.setString(4, tfDateRequest.getText());
				statement.setString(5, tfScheduleRequest.getText());
				statement.setString(6, tfShortDescriptionRequest.getText());
				statement.setString(7, status);
				statement.setString(8, "");
				statement.setString(9, "");
				
				int rows = statement.executeUpdate();
				if (rows > 0)
					System.out.println("A row has been inserted");
				
				rs.close();
				stmt.close();
				statement.close();
				connection.close();
				System.out.println("Connection closed!");
				
				tfDateRequest.setText("");
				tfScheduleRequest.setText("");
				tfShortDescriptionRequest.setText("");
			}
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private TextField tfIDRequest;
	
	public void acceptRequest() {
		try {
			if( tfIDRequest.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Data missing!");
				alert.setContentText("No request found, complete with a request ID!");
				alert.showAndWait();
			}
			
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			System.out.println("Connected to the database accountdb");
			
			String update = "UPDATE accountdb.requests SET usernameAccept=? WHERE ID=" + "\"" + tfIDRequest.getText() + "\"";

			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, username);
			
			int rows = statement.executeUpdate();
			if (rows > 0) {
				System.out.println("A row has been inserted.");
			}
			
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	}
	
	@FXML
	private Button bApproveRequest, bRefuseRequest;
	
	public void approveRefuseRequest() {
		try {	
			if ( status.equals("Operator")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Access restricted!");
				alert.setContentText("Operators don't have access!");
				alert.showAndWait();
			}
			else {
				if( tfIDRequest.getText().equals("")) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Data missing!");
					alert.setContentText("No request found, complete with a request ID!");
					alert.showAndWait();
				}
				else {
				
					ConnectionClass connectionClass = new ConnectionClass();
					Connection connection = connectionClass.getConnection();
					
					System.out.println("Connected to the database accountdb");
					
					String check = "SELECT usernameAccept from accountdb.requests WHERE ID=" + "\"" + tfIDRequest.getText() + "\"";
					Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(check);
					if (rs.next()) {
						checkAcceptUsername = rs.getString(1);
					}
					
					if( checkAcceptUsername == "null" || checkAcceptUsername.equals("")) {
						String update = "UPDATE accountdb.requests SET approved=? WHERE ID=" + "\"" + tfIDRequest.getText() + "\"";
			
						PreparedStatement statement = connection.prepareStatement(update);
						if( bApproveRequest.isFocused()) {
							statement.setString(1, "Da");
						}
						else {
							statement.setString(1, "Nu");
						}
						
						int rows = statement.executeUpdate();
						if (rows > 0) {
							System.out.println("A row has been inserted.");
						}
						
						tfIDRequest.setText("");
						statement.close();
						connection.close();
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	}
	
	@FXML
	private TableView<ModelTableRequests> tableRequests; 
	@FXML
	private TableColumn<ModelTableRequests, Integer> col_ID;
	@FXML
	private TableColumn<ModelTableRequests, String> col_username;
	@FXML
	private TableColumn<ModelTableRequests, String> col_firstname;
	@FXML
	private TableColumn<ModelTableRequests, String> col_lastname;
	@FXML
	private TableColumn<ModelTableRequests, String> col_date;
	@FXML
	private TableColumn<ModelTableRequests, String> col_schedule;
	@FXML
	private TableColumn<ModelTableRequests, String> col_shortDescription;
	@FXML
	private TableColumn<ModelTableRequests, String> col_role;
	@FXML
	private TableColumn<ModelTableRequests, String> col_usernameAccept;
	@FXML
	private TableColumn<ModelTableRequests, String> col_approved;
	
	ObservableList<ModelTableRequests> oblist = FXCollections.observableArrayList();
	
	
	public void showRequests() {
		try {	
			if (status.equals("Operatore")) {
				bApproveRequest.setDisable(true);
				bRefuseRequest.setDisable(true);
			}
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			String select = "SELECT * FROM accountdb.requests ORDER BY date DESC";
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(select);
			while (rs.next()) {
				oblist.add(new ModelTableRequests(rs.getString("ID"), rs.getString("username"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("date"), rs.getString("schedule"), rs.getString("shortDescription"), rs.getString("role"), rs.getString("usernameAccept"), rs.getString("approved")));			
			}
			stmt = connection.createStatement();
			
			connection.close();
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	
		col_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
		col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
		col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
		col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
		col_schedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
		col_shortDescription.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));
		col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
		col_usernameAccept.setCellValueFactory(new PropertyValueFactory<>("usernameAccept"));
		col_approved.setCellValueFactory(new PropertyValueFactory<>("approved"));
		
		tableRequests.setItems(oblist);

	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
