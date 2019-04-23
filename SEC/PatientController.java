package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PatientController {
	
	@FXML private TextField keshiName;
	@FXML private TextField haozhongType;
	@FXML private TextField doctorName;
	@FXML private TextField haozhongName;
	@FXML private TextField jiaokuanValue;
	@FXML private TextField yinjiaoValue;
	@FXML private TextField zhaolingValue;
	@FXML private TextField guahaoNumber;
	@FXML private Button confirmButton;
	@FXML private Button clearButton;
	@FXML private Button back_button;
	private AutoCompletionBinding<String> textAuto_keshiName;
	private AutoCompletionBinding<String> textAuto_haozhongType;
	private AutoCompletionBinding<String> textAuto_doctorName;
	private AutoCompletionBinding<String> textAuto_haozhongName;
	boolean isBalanceSufficient;
	private double balance; //�˻����
	private double fee;
	
	private Set<String> autoCompletions;
	SuggestionProvider<String> provider;
	private LoginController myApp;
    public void setUp(LoginController application1)
    {    
    	myApp = application1;    
    }
	
	@FXML public void initialize() {
		//on_clearButton_clicked();
		keshiName.setDisable(false);
		haozhongType.setDisable(true);
		haozhongName.setDisable(true);
		doctorName.setDisable(true);
		//on_enter_keshiName();
		//on_enter_hongzhongType();
		//on_enter_hongzhongName();
		//on_enter_doctorName();
		textAuto_keshiName=TextFields.bindAutoCompletion(keshiName, "");
		textAuto_haozhongType=TextFields.bindAutoCompletion(haozhongType, "");
		textAuto_doctorName=TextFields.bindAutoCompletion(doctorName, "");
		textAuto_haozhongName=TextFields.bindAutoCompletion(haozhongName, "");
		
		//System.out.println("th1");
		//isBalanceSufficient = true;
		//autoCompletions = new HashSet<>(Arrays.asList("A","B","C"));
		//provider = SuggestionProvider.create(autoCompletions);
		//TextFields.bindAutoCompletion(doctorName, provider);
		

		doctorName.textProperty().addListener((obs, oldText, newText) -> {
			refreshPay();
		});
	}
	
	@FXML public void on_clicked_keshiName()
	{
		on_clearButton_clicked();
		//textAuto_keshiName.dispose();
		on_enter_keshiName();
		keshiName.setText(" ");
	}
	
	@FXML public void on_enter_keshiName()
	{
		System.out.println("there");
		System.out.println("keshiname triggered");
		/*set up connection */
		Connection myCon = ConSQL.connecToMySQL();
		if(myCon == null)
		{
			JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
			return;
		}
		/*fetch data*/
		
		    PreparedStatement pStatement = null;
			ResultSet rs = null;
			LinkedList<String> searchResult = new LinkedList<>();
			System.out.println("preparing keshiname info\n");
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM department");
				rs = pStatement.executeQuery();
				while(rs.next())
				{
					String str1 = rs.getString("depid").trim();
					String str2 = rs.getString("name").trim();
					String str3 = rs.getString("py").trim();
					String togetherStr = str1 + " " + str3 + " " + str2;
					searchResult.add(togetherStr);
				}
				textAuto_keshiName=TextFields.bindAutoCompletion(keshiName, searchResult);
				//textAutoBingding.dispose();//����󶨹�ϵ
				//TextFields.bindAutoCompletion(keshiName, new LinkedList<>());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		/*
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		//LinkedList<String> searchResult = new LinkedList<>();
		List<String> searchResult = new ArrayList<String>();
		System.out.println("preparing keshiname info\n");
		
		try {
			pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM department");
			rs = pStatement.executeQuery();
			while(rs.next())
			{
				String str1 = rs.getString("depid").trim();
				String str2 = rs.getString("name").trim();
				String str3 = rs.getString("py").trim();
				String togetherStr = str1 + " " + str2 + " " + str3;
				searchResult.add(togetherStr);
			}
			textAuto_keshiName=TextFields.bindAutoCompletion(keshiName,  FXCollections.observableArrayList(searchResult));
			//textAutoBingding.dispose();//����󶨹�ϵ
			//TextFields.bindAutoCompletion(keshiName, new LinkedList<>());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}*/
		 /*close the connection*/
		   try 
			{
			   myCon.close();
			   haozhongType.setDisable(false);
				
			} catch (SQLException e) {
			   e.printStackTrace();
			} 
	}
	
	@FXML public void on_clicked_hongzhongType()
	{
		//textAuto_haozhongType.dispose();
		on_enter_hongzhongType();
		haozhongType.setText(" ");
	}
	
	@FXML public void on_enter_hongzhongType()
	{
		System.out.println("type");
		if(keshiName.getText().isEmpty() || keshiName.getText().equals(" "))
		{
			JOptionPane.showMessageDialog(null, "����ѡ�����");
			haozhongType.clear();
			return;
		}
		
		 Connection myCon = ConSQL.connecToMySQL();
		 if(myCon == null)
		 {
			   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
			   return;
		 }
		
			ResultSet rs = null;
			LinkedList<String> searchResult = new LinkedList<>();
			
		try {
			PreparedStatement ps = (PreparedStatement) myCon.prepareStatement("SELECT DISTINCT speciallist FROM register_category WHERE depid=? ");
			ps.setString(1,keshiName.getText().substring(0,6));
			rs = ps.executeQuery();
			
			searchResult.clear();
			//pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM doctor WHERE docid= ?");
			//rs = pStatement.executeQuery();
			int flag=-1;
			while(rs.next())
			{
				if(rs.getBoolean("speciallist")) 
				{
					flag=1;
					break;
				}
				else flag=0;
			}
			if(flag==1)
			{
				searchResult.add("01"+" " +"ZJH" + " " +"ר�Һ�");
				searchResult.add("00" +" "+ "PTH"+ " " + " ��ͨ��");
			}
			else if (flag==0)
				searchResult.add("00" +" "+ "PTH"+ " " + " ��ͨ��");
			textAuto_haozhongType=TextFields.bindAutoCompletion(haozhongType, searchResult);
		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "�����������");
		}
		
		/*close the connection*/
		   try 
			{
			   myCon.close();
			   haozhongName.setDisable(false);
				
			} catch (SQLException e) {
			   e.printStackTrace();
			}
	}
	
	@FXML public void on_clicked_hongzhongName()
	{
		
		//textAuto_haozhongName.dispose();
		on_enter_hongzhongName();
		haozhongName.setText(" ");
	}
	
	@FXML public void on_enter_hongzhongName()
	{
		System.out.println("haozhongName");
		if(keshiName.getText().isEmpty() || haozhongType.getText().isEmpty() ||
				keshiName.getText().equals(" ") || haozhongType.getText().equals(" "))
		{
			JOptionPane.showMessageDialog(null, "����ѡ����Һͺ������");
			haozhongName.clear();
			return;
		}
		
		int isPro = 0;
		if(haozhongType.getText().equals("01 ZJH ר�Һ�")) isPro=1;
		else isPro=0;
		
		 Connection myCon = ConSQL.connecToMySQL();
		 if(myCon == null)
		 {
			   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
			   return;
		 }
		
			ResultSet rs = null;
			LinkedList<String> searchResult = new LinkedList<>();
			
		try {
			PreparedStatement ps = (PreparedStatement) myCon.prepareStatement("SELECT * FROM register_category WHERE depid=? AND speciallist = ? ");
			ps.setString(1,keshiName.getText().substring(0,6));
			ps.setInt(2, isPro);
			rs = ps.executeQuery();
			
			searchResult.clear();
			//pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM doctor WHERE docid= ?");
			//rs = pStatement.executeQuery();
			System.out.println("there");
			while(rs.next())
			{
				String str1 = rs.getString("catid").trim();
				String str2 = rs.getString("name").trim();
				String str3 = rs.getString("py").trim();
				String togetherStr = str1 + " " + str3 + " " + str2;
				System.out.println(togetherStr);
				searchResult.add(togetherStr);
			}
			textAuto_haozhongName=TextFields.bindAutoCompletion(haozhongName, searchResult);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		/*close the connection*/
		   try 
			{
			   myCon.close();
			   doctorName.setDisable(false);
			} catch (SQLException e) {
			   e.printStackTrace();
			}
	}
	
	void prepareDoctorNameAutocomplete()
	{
		
	}
	
	@FXML public void on_clicked_doctorName()
	{
		//textAuto_doctorName.dispose();
		on_enter_doctorName();
		doctorName.setText(" ");
	}
	
	@FXML public void on_enter_doctorName()
	{
		System.out.println("doctor");
		if(keshiName.getText().isEmpty() || haozhongType.getText().isEmpty() ||
				haozhongType.getText().equals(" "))
		{
			JOptionPane.showMessageDialog(null, "����ѡ����Һͺ������");
			doctorName.clear();
			return;
		}
		int isPro = 0;
		if(haozhongType.getText().equals("01 ZJH ר�Һ�")) isPro=1;
		else isPro=0;
		
		 Connection myCon = ConSQL.connecToMySQL();
		 if(myCon == null)
		 {
			   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
			   return;
		 }
		
			ResultSet rs = null;
			LinkedList<String> searchResult = new LinkedList<>();
			System.out.println("preparing doctor info\n");
			
		try {
			PreparedStatement ps = (PreparedStatement) myCon.prepareStatement("SELECT * FROM doctor WHERE depid=? AND (speciallist = ? OR speciallist = ?)");
			ps.setString(1,keshiName.getText().substring(0,6));
			ps.setInt(2, 1);
			if(isPro==1) ps.setInt(3, 1);
			else ps.setInt(3, 0);
			
			rs = ps.executeQuery();
			
			searchResult.clear();
			//pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * FROM doctor WHERE docid= ?");
			//rs = pStatement.executeQuery();
			while(rs.next())
			{
				String str1 = rs.getString("docid").trim();
				String str2 = rs.getString("name").trim();
				String str3 = rs.getString("py").trim();
				String togetherStr = str1 + " " + str3 + " " + str2;
				searchResult.add(togetherStr);
			}
			textAuto_doctorName=TextFields.bindAutoCompletion(doctorName, searchResult);
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
	
		  
		   
	}
	
	
	@FXML public void refreshPay()
	{
		System.out.println("refresh"+doctorName.getText().length());
		if(doctorName.getText().length()<10) return;
		   /*Connect to the database*/
		
		   Connection myCon = ConSQL.connecToMySQL();
		   if(myCon == null)
		   {
			   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
			   return;
		   }
		    /*fetch data*/
		   PreparedStatement ps;
			 ResultSet rs = null;
			 String haozhongnumber=haozhongName.getText().substring(0,6);
			 String patientnumber=LoginController.PatientID;
		 //�ɷ�
			 //��ȡ���
			 //double balance=0; //�˻����
			// double fee=0;
			 try {
				ps = (PreparedStatement) myCon.prepareStatement("SELECT balance FROM patient WHERE pid=?");
				ps.setString(1, patientnumber);
				 rs = ps.executeQuery();
				 rs.next();
				 balance=rs.getDouble("balance");
				 
				 //��ȡfee
				 ps = (PreparedStatement) myCon.prepareStatement("SELECT reg_fee FROM register_category WHERE catid=?");
				 ps.setString(1, haozhongnumber);
				 rs = ps.executeQuery();
				 rs.next();
				 fee=rs.getDouble("reg_fee");  //����
				 yinjiaoValue.setText(fee+"");
				 
				 if(fee<=balance)
				 {
					 isBalanceSufficient=true;
					 jiaokuanValue.setText("�����ʣ��"+(balance-fee));
					 jiaokuanValue.setDisable(true);
					 zhaolingValue.setDisable(true);
					 //balance-=fee;
					 
				 }
				 else
				 {
					 isBalanceSufficient=false;
					 jiaokuanValue.setText("����,�뽻��:");
				 }
			} catch (SQLException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
			 
		 /*close the connection*/
		
		   try 
			{
			   myCon.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}   	
			
	}
	
	 public void sfzjChanged(String newChoice)
	{
		  
	}

	 @FXML public void on_guahaoNumber_clicked()
	 {	 
		 if(keshiName.getText().equals("") || doctorName.getText().equals("")
				 || haozhongName.getText().equals("") || haozhongType.getText().equals("") )
		 {
			 JOptionPane.showMessageDialog(null, "����д������Ϣ");
			 return;
		 }
		 Connection myCon = ConSQL.connecToMySQL();
		 if(myCon == null)
		 {
			   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
			   return;
		 }
		 try {
			 
			 double zhaoling_flag=-1;
			 String haozhongnumber=haozhongName.getText().substring(0,6);
			 String doctornumber=doctorName.getText().substring(0,6);
			 String patientnumber=LoginController.PatientID;
			 int getNumber,current_number;
			 Boolean reg_flag=false;
			 
			 
			 PreparedStatement ps;
			 ResultSet rs = null;	
			 int mon=0;
			 
			 if(isBalanceSufficient)
			 {
				 ps = (PreparedStatement) myCon.prepareStatement("UPDATE patient SET balance=? WHERE pid=?");
				 ps.setDouble(1, balance-fee);
				 ps.setString(2, LoginController.PatientID);
				 ps.executeUpdate();
			 }
			 else 
			 {
				 if(jiaokuanValue.getText().length()<=9)
				 {
					 JOptionPane.showMessageDialog(null, "�����뽻����");
					 return;
				 }
				 else
				 {
					 mon=Integer.parseInt( jiaokuanValue.getText().substring(9,(jiaokuanValue.getText().length())));
					 if(mon>=fee)
					 {
						 System.out.println("����");
						 zhaolingValue.setText("����"+(mon-fee));
						 zhaoling_flag=mon-fee;
					 }
					 else
					 {
						 JOptionPane.showMessageDialog(null, "����");
						 return;
					 }
				 }
				 
			 }
			 
			 //�ú������Һ�����
			 ps = (PreparedStatement) myCon.prepareStatement("SELECT max_reg_number FROM register_category WHERE catid=?");
			 ps.setString(1, haozhongnumber);
			 rs = ps.executeQuery();
			 rs.next();
			 int max_number=rs.getInt("max_reg_number");
			 System.out.println("��������"+max_number);
			 //д�����ݿ�
			 ps = (PreparedStatement) myCon.prepareStatement("SELECT COUNT(*) FROM register");
			 //ps.setString(1,keshiName.getText().substring(0,6));
			 //ps.setInt(2, 0);
			 
			 rs = ps.executeQuery();
			 rs.next();
			 System.out.println(rs.getInt("COUNT(*)"));
			 //����������
			 getNumber =rs.getInt("COUNT(*)")+1;  //�Һź���
			 //ת��Ϊ6 λ�ַ�
			 String reg_id=String.valueOf(getNumber);
			 while(reg_id.length()<6)
			 {
				 reg_id="0"+reg_id;
			 }
			 
			 guahaoNumber.setText( getNumber+"" );
			 
			 //����Ԫ��
			 ps = (PreparedStatement) myCon.prepareStatement("SELECT MAX(current_reg_count) FROM register WHERE catid=?");
			 ps.setString(1, haozhongnumber);
			 rs = ps.executeQuery();
			 rs.next();
			 current_number = rs.getInt("MAX(current_reg_count)")+1;
			 
			 if(current_number>max_number)
			 {
				 JOptionPane.showMessageDialog(null, "�ú��ֹҺ����������ޣ�");
				 if(zhaoling_flag!=-1)
					 zhaolingValue.setText("����"+(mon));
				 return;
			 }
			 
			 Date dNow = new Date();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String currentTime = sdf.format(dNow);
			 
			 ps = (PreparedStatement) myCon.prepareStatement("INSERT INTO register VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			 ps.setString(1, reg_id);
			 ps.setString(2, haozhongnumber);
			 ps.setString(3, doctornumber);
			 ps.setString(4, patientnumber);
			 ps.setInt(5, current_number);
			 ps.setBoolean(6, reg_flag);
			 ps.setDouble(7, fee);
			 ps.setString(8, currentTime);
			 //ps.setDate(8, (java.sql.Date) dNow);
			 ps.executeUpdate();
			 
			 String s_print= "�Һųɹ� ������Ϊ��"+reg_id;
			 if(zhaoling_flag!=-1)
				 s_print+="\n����"+zhaoling_flag;
			 JOptionPane.showMessageDialog(null,s_print );
			 back_button_clicked();
			 
		 }catch (SQLException e) {
			   e.printStackTrace();
		 }
		 
		 try 
			{
			   myCon.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}  
		 
	 }
	 
	 @FXML public void on_clearButton_clicked()
	 {
		 System.out.println("Clear out all the text");
		 keshiName.clear();
		 doctorName.clear();
		 haozhongName.clear();
		 haozhongType.clear();
		 jiaokuanValue.clear();
		 yinjiaoValue.clear();
		 zhaolingValue.clear();
		 guahaoNumber.clear();
		 //jiaokuanValue.setEditable(true);
		 //zhaolingValue.setEditable(true);
		 jiaokuanValue.setDisable(false);
		 zhaolingValue.setDisable(false);
		 keshiName.setDisable(false);
		 haozhongType.setDisable(true);
		 haozhongName.setDisable(true);
		 doctorName.setDisable(true);
		 textAuto_keshiName.dispose();
			textAuto_haozhongType.dispose();
			textAuto_doctorName.dispose();
			textAuto_haozhongName.dispose();
	 }
	 
	 @FXML public void on_confirmButton_clicked()
	 {
		 /*����Ƿ���ڿ���*/
		 //if(keshiName.getText().equals("") || doctorName.getText().equals("")
		//		 || haozhongName.getText().equals("") || zhaolingValue.getText().equals("") )
		//	 return;
		 //refreshPay();
		 Connection myCon=null;
		 try {
			 System.out.println(jiaokuanValue.getText().length());
			 if(jiaokuanValue.getText().length()<=9)
			 {
				 JOptionPane.showMessageDialog(null, "�����뽻����");
				 return;
			 }
			 else
			 {
				 int mon=Integer.parseInt( jiaokuanValue.getText().substring(9,(jiaokuanValue.getText().length())));
				  myCon = ConSQL.connecToMySQL();
				   if(myCon == null)
				   {
					   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
					   return;
				   }
				    /*fetch data*/
				   PreparedStatement ps;
					 ResultSet rs = null;
					 //String haozhongnumber=haozhongName.getText().substring(0,6);
					 String patientnumber=LoginController.PatientID;
				 ps = (PreparedStatement) myCon.prepareStatement("SELECT balance FROM patient WHERE pid=?");
				 ps.setString(1, patientnumber);
			     rs = ps.executeQuery();
				 rs.next();
				 balance=rs.getDouble("balance");
				 
				 ps = (PreparedStatement) myCon.prepareStatement("UPDATE patient SET balance=? WHERE pid=?");
				 ps.setDouble(1, balance+mon);
				 balance+=mon;
				 System.out.println("����"+balance);
				 ps.setString(2, LoginController.PatientID);
				 ps.executeUpdate();
				 JOptionPane.showMessageDialog(null, "����ɹ�");
			 }
				 
		 }catch (SQLException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		 try 
			{
			  myCon.close();
			  refreshPay();
			} catch (SQLException e) {
			   e.printStackTrace();
			}  
	 }
	 
	 @FXML
		public void back_button_clicked()
		{
			Stage tempStage = (Stage) back_button.getScene().getWindow();
	        tempStage.close();
	        
	        FXMLLoader loader = new FXMLLoader(
	        		getClass().getResource("Login.fxml")
		      );
	        AnchorPane root = new AnchorPane();
			
			Scene scene = new Scene(root,335,280);
			System.out.println("back");
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
