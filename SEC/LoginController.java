package application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class LoginController {
	
	@FXML private Button LoginButton;
	@FXML private TextField UserName;
	@FXML private PasswordField PassWord;
	
	@FXML public ChoiceBox<String> LoginType;
	public static String PatientID;
	public static String DoctorID;
	private Main myApp;
    public void setUp(Main application)
    {    
    	myApp = application;    
    }

	@FXML void on_LoginButton_clicked()
	{
		try {
			Connection con = ConSQL.connecToMySQL();
			//Statement sta = con.createStatement();
			
			String InType=LoginType.getValue();
			String Name=UserName.getText();
			String PWord=PassWord.getText();
			
			Date dNow = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dNow);
			
			ResultSet rs = null;
			
			//���˵�¼
			if(InType.equals("����"))
			{
				PreparedStatement ps = con.prepareStatement("SELECT password FROM patient WHERE pid=?");
				ps.setString(1,Name);
				rs = ps.executeQuery();
				//System.out.println(Name+PWord+rs.next());
				
				if(rs.next())
				{
					//System.out.println("there");
					if(PWord.equals(rs.getString("password")))
				    {
				    	//System.out.println("success");
				    	//JOptionPane.showMessageDialog(null, "success");
						//�޸ĵ�¼ʱ��
						ps = con.prepareStatement("UPDATE patient SET last_login_datetime=? WHERE pid=?");
						ps.setString(1,currentTime);
						ps.setString(2, Name);
						ps.executeUpdate();
				    	
						PatientID=Name;
				    	// �������߹ҺŽ���
						Stage tempStage = (Stage) LoginButton.getScene().getWindow();
				        tempStage.close();
				        
				        FXMLLoader loader = new FXMLLoader(
				        		getClass().getResource("PatientRegister.fxml")
					      );
				        AnchorPane root = new AnchorPane();
					    Scene myScene = new Scene(root);
				        try {
					        myScene.setRoot((Parent) loader.load());
					        Stage newStage = new Stage();
					        newStage.setTitle("�Һ�ƽ̨");
					        newStage.setScene(myScene);
					        newStage.show();
				        }catch (IOException ex) {
				   	    ex.printStackTrace();
				        } 
				    }
					
				    else	JOptionPane.showMessageDialog(null, "�˺��������");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "�˺Ų����ڣ�");
				}
			}
			else if(InType.equals("ҽ��"))
			{
				PreparedStatement ps = con.prepareStatement("SELECT password FROM doctor WHERE docid=?");
				ps.setString(1,Name);
				DoctorID=Name;
				rs = ps.executeQuery();
				//System.out.println(Name+PWord+rs.next());
				
				if(rs.next())
				{
					if(PWord.equals(rs.getString("password")))
				    {	
						//�޸ĵ�¼ʱ��
						ps = con.prepareStatement("UPDATE doctor SET last_login_datetime=? WHERE docid=?");
						ps.setString(1,currentTime);
						ps.setString(2, Name);
						ps.executeUpdate();
						
				    	//����ҽ������
						Stage tempStage = (Stage) LoginButton.getScene().getWindow();
				        tempStage.close();
				        
				        FXMLLoader loader = new FXMLLoader(
				        		getClass().getResource("DoctorView.fxml")
					      );
				        SplitPane root = new SplitPane();
					    Scene myScene = new Scene(root);
				        try {
				        	Stage newStage = new Stage(StageStyle.DECORATED);
							   newStage.setTitle("ҽ��������̨");
							   newStage.setScene(new Scene((SplitPane) loader.load()));
							   newStage.show();
							   /*
					        myScene.setRoot((Parent) loader.load());
					        Stage newStage = new Stage();
					        newStage.setTitle("ҽ��ƽ̨");
					        newStage.setScene(myScene);
					        newStage.show();*/
				        }catch (IOException ex) {
				   	    ex.printStackTrace();
				        } 
				    }
					
				    else	JOptionPane.showMessageDialog(null, "�˺��������");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "�˺Ų����ڣ�");
				}
			}
			

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
