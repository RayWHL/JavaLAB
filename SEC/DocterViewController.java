package application;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JOptionPane;

import com.mysql.fabric.xmlrpc.base.Data;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DocterViewController {
	
	@FXML Label welcomeLabel;
	@FXML Button button1;
	@FXML Label timeLabel;
	@FXML DatePicker beginTime;
	@FXML DatePicker toTime;
    private ObservableList<PatientInfo> personData = FXCollections.observableArrayList();
	@FXML private TableView<PatientInfo> patientInfoTable;
	@FXML private TableColumn<PatientInfo, String> GHBHColumn;
	@FXML private TableColumn<PatientInfo, String> BRMCColumn;
	@FXML private TableColumn<PatientInfo, String> registDateColumn;
	@FXML private TableColumn<PatientInfo, String> isProColumn;
	
	
	private ObservableList<Income> incomeData = FXCollections.observableArrayList();
	@FXML private TableView<Income> incomeInfoTable;
	@FXML private TableColumn<Income, String> KSMCColumn;
	@FXML private TableColumn<Income, String> YSBHColumn;
	@FXML private TableColumn<Income, String> YSMCColumn;
	@FXML private TableColumn<Income, String> HZLBColumn;
	@FXML private TableColumn<Income, String> GHRCColumn;
	@FXML private TableColumn<Income, String> SRHJColumn;
	
	@FXML
    public void initialize() {
		System.out.println("about to initialize.");
		Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        System.out.println(localDate);
		beginTime.setValue(localDate);
		toTime.setValue(localDate);
		setWelcome2Doctor();
		GHBHColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().GHBHProperty()));
		BRMCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().BRMCProperty()));
		registDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().registDateProperty()));
		isProColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isProProperty()));
		showPatientInfo();
		
		KSMCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().KSMCProperty()));
		YSBHColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().YSBHProperty()));
		YSMCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().YSMCProperty()));
		HZLBColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().HZLBProperty()));
		GHRCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().GHRCProperty()));
		SRHJColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().SRHJProperty()));
		showIncomeInfo();
    }	
	
	void setWelcome2Doctor() {
		/*set up connection */
		
		   Connection myCon = ConSQL.connecToMySQL();
		   if(myCon == null)
		   {
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		   }
		/*fetch data*/
		
		    PreparedStatement pStatement = null;
			ResultSet rs = null;
			String docterName = null;
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * from doctor where docid = ?");
				pStatement.setString(1, LoginController.DoctorID);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				while(rs.next())
				{
					docterName = rs.getString("name").trim();
				}
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		   
		 /*close the connection*/
		
		   try 
			{
			   myCon.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}  
		   String showWelcome2Doc = "欢迎，" + docterName + "医生：";
		  welcomeLabel.setText(showWelcome2Doc);   
	}
	
	void showPatientInfo()
	{
		
		//System.out.print("ready to show patient view");
		/*set up connection */
		
		Connection myCon = ConSQL.connecToMySQL();
		   if(myCon == null)
		   {
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		   }
		/*fetch data*/
		
		    PreparedStatement pStatement = null;
			ResultSet rs = null;
			System.out.println("Preparing table data\n");
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM register, patient, doctor, register_category "
						+ "WHERE register.pid = patient.pid "
						+ "AND register.docid = doctor.docid "
						+ "AND register.catid = register_category.catid "
						+ "AND register.docid = ?");
				pStatement.setString(1, LoginController.DoctorID);
				//pStatement.setString(2, beginTime.getValue().toString());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				String GHBH = null;
				String BRMC = null;
				String registDate = null;
				String isPro = null;
				while(rs.next())
				{
					GHBH = rs.getString("reg_id").trim();
					BRMC = rs.getString("patient.name").trim();
					registDate = rs.getString("reg_datetime").trim();
					registDate = registDate.substring(0, registDate.length()-2);
					if( rs.getInt("register_category.speciallist") == 1)
					{
						isPro = "专家号";
					}else
					{
						isPro = "普通号";
					}
					System.out.print(GHBH + BRMC + registDate + isPro);
					personData.add(new PatientInfo(GHBH, BRMC, registDate, isPro));
				}
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		   
		 /*close the connection*/
		
		   try 
			{
			   myCon.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}
		   patientInfoTable.setItems(personData);
	}
    
	@FXML
	public void showIncomeInfo()
	{
		System.out.print("ready to show Income view");
		/*set up connection */
		Connection myCon = ConSQL.connecToMySQL();
		   if(myCon == null)
		   {
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		   }
		/*fetch data*/
		    PreparedStatement pStatement = null;
			ResultSet rs = null;
			System.out.println("Preparing table data\n");
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT "
						+ "department.name, doctor.docid, doctor.name, register_category.speciallist, COUNT(*), register.reg_fee , MIN(register.reg_datetime), MAX(register.reg_datetime)"
						+ "FROM register, department, doctor, register_category "
						+ "WHERE doctor.depid = department.depid "
						+ "AND register.docid = doctor.docid "
						+ "AND register.catid = register_category.catid "
						+ "AND register.reg_datetime>= ? "
						+ "AND register.reg_datetime<=?"
						+ "GROUP BY doctor.docid, register_category.speciallist "
						);
				pStatement.setString(1,(beginTime.getValue().toString()+" 00:00:00"));
				System.out.println(beginTime.getValue().toString());
				pStatement.setString(2, toTime.getValue().toString()+"23:59:59");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				String KSMC = null;
				String YSBH = null;
				String YSMC = null;
				String HZLB = null;
				String GHRC = null;
				String SRHJ = null;
				String earliestTime = null;
				String latestTime = null;
				String timeLabelStr = null;
				incomeData.clear();
				while(rs.next())
				{
					earliestTime = rs.getString("MIN(register.reg_datetime)");
					latestTime = rs.getString("MAX(register.reg_datetime)");
					timeLabelStr = "起始时间 ： " + earliestTime.substring(0, earliestTime.length()-2) +
							"   " + "截止时间：" + latestTime.substring(0, latestTime.length()-2);
					//timeLabel.setText(timeLabelStr);
					
					KSMC = rs.getString("department.name").trim();
					YSBH = rs.getString("doctor.docid").trim();
					YSMC = rs.getString("doctor.name").trim();
					if(rs.getInt("register_category.speciallist") == 1)
					{
						HZLB = "专家号";
					}else {
						HZLB = "普通号";
					}
				
					int patientCount = rs.getInt("COUNT(*)");
					BigDecimal regiFeeDec = rs.getBigDecimal("register.reg_fee");
					double regiFee = regiFeeDec.doubleValue();
					double totalFee = patientCount * regiFee;
					
					GHRC = String.valueOf(patientCount);
					SRHJ = String.valueOf(totalFee);
					System.out.print(KSMC + YSBH + YSMC + HZLB + GHRC + SRHJ);
					incomeData.add(new Income(KSMC, YSBH, YSMC, HZLB, GHRC, SRHJ));
				}
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		   
		 /*close the connection*/
		   try 
			{
			   myCon.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}
		   incomeInfoTable.setItems(incomeData);
	}
	
	//退出系统
	@FXML
	public void button_clicked()
	{
		Stage tempStage = (Stage) button1.getScene().getWindow();
        tempStage.close();
        
        FXMLLoader loader = new FXMLLoader(
        		getClass().getResource("Login.fxml")
	      );
        AnchorPane root = new AnchorPane();
		
		Scene scene = new Scene(root,335,280);
	
        try {
        		
        	Stage newStage = new Stage(StageStyle.DECORATED);
			   newStage.setTitle("Login");
			   newStage.setScene(new Scene((AnchorPane) loader.load()));
			   newStage.show();
			  
        }catch (IOException ex) {
   	    ex.printStackTrace();
        }
	}
   	    		
}
