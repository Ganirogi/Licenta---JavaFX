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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

public class HistoryRegistrationController implements Initializable{

	Stage primaryStage = new Stage();
	String username, status, excelName;
	
	public void getUsername(String getUsername, String status) {
		username = getUsername;
		this.status = status;
	}
	
	public void exportToExcel() {
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
				
		String query;
		if (status.equals("Adm") || status.equals("Staff"))  { query = "SELECT * FROM accountdb.RegistrationTime "; excelName="Registration details All.xlsx"; }
			else { query = "SELECT * FROM accountdb.RegistrationTime WHERE username=" + "\"" + username + "\""; excelName="Registration details personal.xlsx"; }
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Registration details");
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("username");
			header.createCell(1).setCellValue("firstname");
			header.createCell(2).setCellValue("lastname");
			header.createCell(3).setCellValue("date");
			header.createCell(4).setCellValue("registrationType");
			
			int index = 1;
			while(rs.next()) {
				XSSFRow row = sheet.createRow(index);
				row.createCell(0).setCellValue(rs.getString("username"));
				row.createCell(1).setCellValue(rs.getString("firstname"));
				row.createCell(2).setCellValue(rs.getString("lastname"));
				row.createCell(3).setCellValue(rs.getString("date"));
				row.createCell(4).setCellValue(rs.getString("registrationType"));
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

	
	}
	
	public void backToRegistration() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Registration.fxml"));
		Parent root = loader.load();
		
		RegistrationController controller = loader.getController();
		controller.getUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Registration");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	
	@FXML
	private TableView<ModelTable> table; 
	@FXML
	private TableColumn<ModelTable, String> col_username;
	@FXML
	private TableColumn<ModelTable, String> col_firstname;
	@FXML
	private TableColumn<ModelTable, String> col_lastname;
	@FXML
	private TableColumn<ModelTable, String> col_date;
	@FXML
	private TableColumn<ModelTable, String> col_registrationType;
	
	ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
	 
	public void getHistory() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			String sql;
			if (status.equals("Adm") || status.equals("Staff"))  { sql = "SELECT * FROM accountdb.RegistrationTime "; }
				else { sql = "SELECT * FROM accountdb.RegistrationTime WHERE username=" + "\"" + username + "\""; }
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				oblist.add(new ModelTable(rs.getString("username"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("date"), rs.getString("registrationType")));
			}
			
			connection.close();
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
		
		col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
		col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
		col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
		col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
		col_registrationType.setCellValueFactory(new PropertyValueFactory<>("registrationType"));
		
		table.setItems(oblist);
		
	}
	
	@FXML
	private Button bBackRH,bDownloadRH;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bBackRH.setCancelButton(true);
		bDownloadRH.setDefaultButton(true);
	}
}
