package application;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DocViewController {
	
	@FXML Label welcomeLabel;
	@FXML Label timeLabel;
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
		   J2MySQL con = new J2MySQL();
		   Connection myCon = con.connect2MySQL();
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
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * from t_ksys where YSBH = ?");
				pStatement.setString(1, DocLoginController.doctorID);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				while(rs.next())
				{
					docterName = rs.getString("YSMC").trim();
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
		
		System.out.print("ready to show patient view");
		/*set up connection */
		   J2MySQL con = new J2MySQL();
		   Connection myCon = con.connect2MySQL();
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
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM t_ghxx, t_brxx, t_ksys "
						+ "WHERE t_ghxx.BRBH = t_brxx.BRBH "
						+ "AND t_ghxx.YSBH = t_ksys.YSBH "
						+ "AND t_ghxx.YSBH = ?");
				pStatement.setString(1, DocLoginController.doctorID);
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
					GHBH = rs.getString("GHBH").trim();
					BRMC = rs.getString("BRMC").trim();
					registDate = rs.getString("RQSJ").trim();
					registDate = registDate.substring(0, registDate.length()-2);
					if( rs.getInt("SFZJ") == 1)
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
		   J2MySQL con = new J2MySQL();
		   Connection myCon = con.connect2MySQL();
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
						+ "KSMC, t_ksys.YSBH, YSMC, t_ksys.SFZJ, COUNT(*), t_ghxx.GHFY , MIN(t_ghxx.RQSJ), MAX(t_ghxx.RQSJ)"
						+ "FROM t_ghxx, t_ksxx, t_ksys, t_hzxx "
						+ "WHERE t_ksys.KSBH = t_ksxx.KSBH "
						+ "AND t_ghxx.YSBH = t_ksys.YSBH "
						+ "AND t_ghxx.HZBH = t_hzxx.HZBH "
						+ "GROUP BY t_ksys.YSBH"
						);
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
				while(rs.next())
				{
					earliestTime = rs.getString("MIN(t_ghxx.RQSJ)");
					latestTime = rs.getString("MAX(t_ghxx.RQSJ)");
					timeLabelStr = "起始时间 ： " + earliestTime.substring(0, earliestTime.length()-2) +
							"   " + "截止时间：" + latestTime.substring(0, latestTime.length()-2);
					timeLabel.setText(timeLabelStr);
					
					KSMC = rs.getString("KSMC").trim();
					YSBH = rs.getString("t_ksys.YSBH").trim();
					YSMC = rs.getString("YSMC").trim();
					if(rs.getInt("t_ksys.SFZJ") == 1)
					{
						HZLB = "专家号";
					}else {
						HZLB = "普通号";
					}
				
					int patientCount = rs.getInt("COUNT(*)");
					BigDecimal regiFeeDec = rs.getBigDecimal("t_ghxx.GHFY");
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
}
