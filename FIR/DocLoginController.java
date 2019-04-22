package application;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DocLoginController implements Initializable {

	static String doctorID;
   @FXML private Button myButton;
   @FXML private Button loginButton;
   @FXML private TextField usernameField;
   @FXML private TextField passwordField;
   @FXML private TextField myTextField;
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
   }
   
   @FXML
   public void onEnter(){
      on_login_clicked(); 
   }
   
   @FXML
   public void on_login_clicked()
   {
	   System.out.print("The login button is clicked");
	   System.out.print(usernameField.getText());
	   System.out.print(passwordField.getText());
	   
	   /*If username or password input is null*/
	   if(usernameField.getText().equals(""))
	   {
		   
		   JOptionPane.showMessageDialog(null, "�û�������Ϊ�գ��������û���");
		   return;
	   }
	   if(passwordField.getText().equals(""))
	   {
		   JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�����������");
		   return;
	   }
	   
	   /*set up connection */
	   J2MySQL con = new J2MySQL();
	   Connection myCon = con.connect2MySQL();
	   if(myCon == null)
	   {
		   JOptionPane.showMessageDialog(null, "�������ݿ�ʧ�ܣ�");
		   return;
	   }
	   
	   /*run SQL */
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String gotPassword = "";
		try {
			pStatement=(PreparedStatement) myCon.prepareStatement("SELECT DLKL from t_ksys WHERE YSBH = ?");
			pStatement.setString(1, usernameField.getText().trim());
		
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			rs = pStatement.executeQuery();
			if(rs!= null) {
				rs.next();   //rs����ʼ�α�λ���ڵ�һ�е�ǰ�棬��ʼǰ����nextһ�¡� 
			}else
			{
				JOptionPane.showMessageDialog(null, "�˺Ų����ڣ�");
			}
			gotPassword = rs.getString("DLKL").trim();
			System.out.print(gotPassword);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		 
	   if(gotPassword.equals(passwordField.getText()))
	   {
		   doctorID = usernameField.getText();
		    Stage tempStage = (Stage) loginButton.getScene().getWindow();
	        tempStage.close();
	        
	        FXMLLoader loader = new FXMLLoader(
		          getClass().getResource("DoctorView.fxml")
		      );
	       
	        Date dNow = new Date();
			   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   String currentTime = sdf.format(dNow);
			   try {
				   String temp = "UPDATE t_ksys SET DLRQ = '%1$s' WHERE YSBH = '%2$s'";
				   String fetchStr = String.format(temp, currentTime,usernameField.getText().trim());
					pStatement=(PreparedStatement) 
							myCon.prepareStatement
							(fetchStr);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			   
			   try {
					pStatement.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			   
			   try {
				   Stage newStage = new Stage(StageStyle.DECORATED);
				   newStage.setTitle("ҽ��������̨");
				   newStage.setScene(new Scene((SplitPane) loader.load()));
				   newStage.show();
			   }catch (IOException ex) {
				   ex.printStackTrace();
			   }  
	   			}else
	   			{
	   				JOptionPane.showMessageDialog(null, "�˺��������");
	   			}
		/*close statement and connection*/
		try 
		{
			if(pStatement != null)
				pStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		 try 
		 {
			myCon.close();
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
	    return;  
   }
}