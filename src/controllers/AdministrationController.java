package controllers;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AdministrationController implements Initializable{

	Stage primaryStage = new Stage();
	
	String username, status;
	
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
		primaryStage.setTitle("Menu");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	public void goToChangePassword() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ChangePassword.fxml"));
		Parent root = loader.load();
		
		ChangePasswordController controller= loader.getController();
		controller.getUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Change password");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	
	@FXML
	private TableView<ModelTableProfile> tableProfile; 
	@FXML
	private TableColumn<ModelTableProfile, String> col_username;
	@FXML
	private TableColumn<ModelTableProfile, String> col_taxe;
	@FXML
	private TableColumn<ModelTableProfile, String> col_role;
	@FXML
	private TableColumn<ModelTableProfile, String> col_phonenumber;
	@FXML
	private TableColumn<ModelTableProfile, Double> col_grosssalary;
	@FXML
	private TableColumn<ModelTableProfile, Double> col_netsalary;
	
	ObservableList<ModelTableProfile> oblist = FXCollections.observableArrayList();
	
	
	public void refreshProfile() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			String select = "SELECT * FROM accountdb.profile WHERE username=" + "\"" + username + "\"";
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(select);
			while (rs.next()) {
				oblist.add(new ModelTableProfile(rs.getString("username"), rs.getString("taxe"), rs.getString("role"), rs.getString("phonenumber"), rs.getDouble("grosssalary"), rs.getDouble("netsalary")));			
			}
			stmt = connection.createStatement();
			
			connection.close();
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	
		col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
		col_taxe.setCellValueFactory(new PropertyValueFactory<>("taxe"));
		col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
		col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
		col_grosssalary.setCellValueFactory(new PropertyValueFactory<>("grosssalary"));
		col_netsalary.setCellValueFactory(new PropertyValueFactory<>("netsalary"));
		
		tableProfile.setItems(oblist);
	}
	
	public void exportToExcelProfiles() {
		
		if( status.equals("Adm")) {
			
			try {
				String excelName;
				ConnectionClass connectionClass = new ConnectionClass();
				Connection connection = connectionClass.getConnection();
					
				String query = "SELECT * FROM accountdb.profile"; 
				excelName="Profiles details All.xlsx";
	
				Statement stmt;
				
				stmt = connection.createStatement();
				
				ResultSet rs = stmt.executeQuery(query);
				
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("Profiles details");
				XSSFRow header = sheet.createRow(0);
				header.createCell(0).setCellValue("username");
				header.createCell(1).setCellValue("grosssalary");
				header.createCell(2).setCellValue("taxe");
				header.createCell(3).setCellValue("netsalary");
				header.createCell(4).setCellValue("role");
				header.createCell(5).setCellValue("phone_number");
				
				int index = 1;
				while(rs.next()) {
					XSSFRow row = sheet.createRow(index);
					row.createCell(0).setCellValue(rs.getString("username"));
					row.createCell(1).setCellValue(rs.getDouble("grosssalary"));
					row.createCell(2).setCellValue(rs.getString("taxe"));
					row.createCell(3).setCellValue(rs.getDouble("netsalary"));
					row.createCell(4).setCellValue(rs.getString("role"));
					row.createCell(5).setCellValue(rs.getString("phonenumber"));
					index++;
				}
				
				FileOutputStream fileout = new FileOutputStream(excelName);
				wb.write(fileout);
				fileout.close();
				System.out.println("The data has been exported to excel");
				
				wb.close();
				rs.close();
				connection.close();
				stmt.close();
			} catch (SQLException | IOException e) {
				System.out.println("Error, no connection established.");
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Access restricted!");
			alert.setContentText("Only Adm role can use this functionality");
			alert.showAndWait();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
