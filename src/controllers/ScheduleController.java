package controllers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Row;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ScheduleController implements Initializable{

	Stage primaryStage = new Stage();
	
	String username, status;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	@FXML
	private void goToRequestModification() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RequestModification.fxml"));
		Parent root = loader.load();
		
		RequestModificationController controller = loader.getController();
		controller.getUsername(username, status);
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Request Modification");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
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
	Button bUpload;
	public void uploadSchedule() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			String insert = "INSERT INTO accountdb.schedule (username, weekno, date, monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES (?,?,?,?,?,?,?,?,?,?)";
				
			PreparedStatement statement = connection.prepareStatement(insert);
			
			FileInputStream fileIn = new FileInputStream(new File("Schedule update.xlsx"));
			
			XSSFWorkbook wb = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = wb.getSheetAt(0);
			Row row;

			if (status.equals("Operator")) {
				Alert alert2 = new Alert(AlertType.WARNING);
				alert2.setTitle("Access restricted!");
				alert2.setContentText("Operator users don't have access to this functionality!");
				alert2.showAndWait();
				alert2.close();
			}
			else {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("You are about to upload data in database!");
				alert.setContentText("Are you sure you want to upload the contents of file Schedule update to the database?");
				alert.showAndWait();
				alert.close();
				if (alert.getResult() == ButtonType.OK) {
					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
						row = sheet.getRow(i);
						statement.setString(1, row.getCell(0).getStringCellValue());
						statement.setInt(2, (int)row.getCell(1).getNumericCellValue());
						statement.setString(3, row.getCell(2).getStringCellValue());
						statement.setString(4, row.getCell(3).getStringCellValue());
						statement.setString(5, row.getCell(4).getStringCellValue());
						statement.setString(6, row.getCell(5).getStringCellValue());
						statement.setString(7, row.getCell(6).getStringCellValue());
						statement.setString(8, row.getCell(7).getStringCellValue());
						statement.setString(9, row.getCell(8).getStringCellValue());
						statement.setString(10, row.getCell(9).getStringCellValue());
						statement.execute();
					}
			}
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setTitle("Database updated!");
			alert1.setContentText("Database has been update with the contents of the file!");
			alert1.showAndWait();
			alert1.close();
			}
			
			wb.close();
			fileIn.close();
			statement.close();
			connection.close();
			
			
		} catch (SQLException | IOException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	}
	
	@FXML
	private TableView<ModelTableSchedule> tableSchedule; 
	@FXML
	private TableColumn<ModelTableSchedule, String> col_week;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_monday;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_tuesday;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_wednesday;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_thursday;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_friday;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_saturday;
	@FXML
	private TableColumn<ModelTableSchedule, String> col_sunday;
	
	ObservableList<ModelTableSchedule> oblist = FXCollections.observableArrayList();
	
	public void getHistorySchedule() {
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			String select = "SELECT * FROM accountdb.schedule WHERE username=" + "\"" + username + "\"" + " ORDER BY weekno DESC";
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(select);
			while (rs.next()) {
				oblist.add(new ModelTableSchedule(rs.getString("date"), rs.getString("monday"), rs.getString("tuesday"), rs.getString("wednesday"), rs.getString("thursday"), rs.getString("friday"), rs.getString("saturday"), rs.getString("sunday")));			
			}
			stmt = connection.createStatement();
			
			connection.close();
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Error, no connection established.");
			e.printStackTrace();
		}
	
		col_week.setCellValueFactory(new PropertyValueFactory<>("FirstDayofWeek"));
		col_monday.setCellValueFactory(new PropertyValueFactory<>("monday"));
		col_tuesday.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
		col_wednesday.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
		col_thursday.setCellValueFactory(new PropertyValueFactory<>("thursday"));
		col_friday.setCellValueFactory(new PropertyValueFactory<>("friday"));
		col_saturday.setCellValueFactory(new PropertyValueFactory<>("saturday"));
		col_sunday.setCellValueFactory(new PropertyValueFactory<>("sunday"));
		
		tableSchedule.setItems(oblist);

	}
	
	public void exportToExcelSchedule() {
		try {
			String excelName;
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
				
			String query;
			if (status.equals("Adm") || status.equals("Staff"))  { query = "SELECT * FROM accountdb.Schedule "; excelName="Schedule details All.xlsx"; }
				else { query = "SELECT * FROM accountdb.Schedule WHERE username=" + "\"" + username + "\""; excelName="Schedule details personal.xlsx"; }
			
			Statement stmt;
			
			stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Schedule details");
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("username");
			header.createCell(1).setCellValue("weekno");
			header.createCell(2).setCellValue("date");
			header.createCell(3).setCellValue("monday");
			header.createCell(4).setCellValue("tuesday");
			header.createCell(5).setCellValue("wednesday");
			header.createCell(6).setCellValue("thursday");
			header.createCell(7).setCellValue("friday");
			header.createCell(8).setCellValue("saturday");
			header.createCell(9).setCellValue("sunday");
			
			int index = 1;
			while(rs.next()) {
				XSSFRow row = sheet.createRow(index);
				row.createCell(0).setCellValue(rs.getString("username"));
				row.createCell(1).setCellValue(rs.getString("weekno"));
				row.createCell(2).setCellValue(rs.getString("date"));
				row.createCell(3).setCellValue(rs.getString("monday"));
				row.createCell(4).setCellValue(rs.getString("tuesday"));
				row.createCell(5).setCellValue(rs.getString("wednesday"));
				row.createCell(6).setCellValue(rs.getString("thursday"));
				row.createCell(7).setCellValue(rs.getString("friday"));
				row.createCell(8).setCellValue(rs.getString("saturday"));
				row.createCell(9).setCellValue(rs.getString("sunday"));
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
