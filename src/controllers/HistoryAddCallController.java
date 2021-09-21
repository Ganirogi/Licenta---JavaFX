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

public class HistoryAddCallController implements Initializable{

	Stage primaryStage = new Stage();
	String username, status, excelName;
	
	public void getUsername(String username, String status) {
		this.username = username;
		this.status = status;
	}
	
	@FXML
	private TableView<ModelTableHAC> tableHAC; 
	@FXML
	private TableColumn<ModelTableHAC, String> col_username;
	@FXML
	private TableColumn<ModelTableHAC, String> col_IDticket;
	@FXML
	private TableColumn<ModelTableHAC, String> col_phonenumber;
	@FXML
	private TableColumn<ModelTableHAC, String> col_issueticket;
	@FXML
	private TableColumn<ModelTableHAC, String> col_description;
	@FXML
	private TableColumn<ModelTableHAC, String> col_date;
	
	ObservableList<ModelTableHAC> oblist = FXCollections.observableArrayList();
	
	public void getHistoryHAC() {
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();

		String select;
		if (status.equals("Adm") || status.equals("Staff"))  { select = "SELECT * FROM accountdb.workloaddone "; excelName = "Workload details All.xlsx"; }
		else { select = "SELECT * FROM accountdb.workloaddone WHERE username=" + "\"" + username + "\""; excelName = "Workload details personal.xlsx"; }
	
		Statement stmt;
		
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(select);
			while (rs.next()) {
				oblist.add(new ModelTableHAC(rs.getString("username"), rs.getString("IDticket"), rs.getString("phonenumber"), rs.getString("issueticket"), rs.getString("description"), rs.getString("date")));
			}
			
			connection.close();
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	
		col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
		col_IDticket.setCellValueFactory(new PropertyValueFactory<>("IDticket"));
		col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
		col_issueticket.setCellValueFactory(new PropertyValueFactory<>("issueticket"));
		col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
		col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
	
		tableHAC.setItems(oblist);
	}

	public void exportToExcelHAC() {
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
				
		String query;
		if (status.equals("Adm") || status.equals("Staff"))  { query = "SELECT * FROM accountdb.workloaddone "; }
			else { query = "SELECT * FROM accountdb.workloaddone WHERE username=" + "\"" + username + "\""; }
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Workload details");
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("username");
			header.createCell(1).setCellValue("IDticket");
			header.createCell(2).setCellValue("phonenumber");
			header.createCell(3).setCellValue("issueticket");
			header.createCell(4).setCellValue("description");
			header.createCell(4).setCellValue("date");
			
			int index = 1;
			while(rs.next()) {
				XSSFRow row = sheet.createRow(index);
				row.createCell(0).setCellValue(rs.getString("username"));
				row.createCell(1).setCellValue(rs.getString("IDticket"));
				row.createCell(2).setCellValue(rs.getString("phonenumber"));
				row.createCell(3).setCellValue(rs.getString("issueticket"));
				row.createCell(4).setCellValue(rs.getString("description"));
				row.createCell(4).setCellValue(rs.getString("date"));
				index++;
			}
			
			FileOutputStream fileout = new FileOutputStream(excelName);
			wb.write(fileout);
			fileout.close();
			System.out.println("The data has been export to excel");
			
			
			wb.close();
			rs.close();
			connection.close();
			stmt.close();
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void backToAddCall() throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AddCall.fxml"));
		Parent root = loader.load();

		AddCallController controller = loader.getController();
		controller.getUsername(username, status);

		Scene scene = new Scene(root);
		primaryStage.setTitle("History");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null).hide();
	}
	@FXML
	Button bBackHAC;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bBackHAC.setCancelButton(true);
		
	}

}
