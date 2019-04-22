package application;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.JOptionPane;
import org.controlsfx.control.textfield.TextFields;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class GuahaoController {
	
	@FXML private TextField keshiName;
	@FXML public ChoiceBox<String> haozhongType;
	@FXML private TextField doctorName;
	@FXML private TextField haozhongName;
	@FXML private TextField jiaokuanValue;
	@FXML private TextField yinjiaoValue;
	@FXML private TextField zhaolingValue;
	@FXML private TextField guahaoNumber;
	@FXML private Button confirmButton;
	@FXML private Button clearButton;
	boolean isBalanceSufficient;
	
	private Set<String> autoCompletions;
	SuggestionProvider<String> provider;
	
	@FXML public void initialize() {
		isBalanceSufficient = true;
		autoCompletions = new HashSet<>(Arrays.asList("A","B","C"));
		provider = SuggestionProvider.create(autoCompletions);
		TextFields.bindAutoCompletion(doctorName, provider);
		System.out.println("keshiname triggered");
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
			LinkedList<String> searchResult = new LinkedList<>();
			System.out.println("preparing keshi info\n");
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT * from t_ksxx");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				while(rs.next())
				{
					String str1 = rs.getString("KSBH").trim();
					String str2 = rs.getString("KSMC").trim();
					String str3 = rs.getString("PYZS").trim();
					String togetherStr = str1 + " " + str2 + " " + str3;
					searchResult.add(togetherStr);
				}
				TextFields.bindAutoCompletion(keshiName, searchResult);
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
		   
		haozhongType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println(newValue);
				sfzjChanged(haozhongType.getItems().get((int) newValue));
			}
		    });
		jiaokuanValue.textProperty().addListener((obs, oldText, newText) -> {
			if(!jiaokuanValue.getText().equals("") && !yinjiaoValue.getText().equals("") && !isBalanceSufficient)
			{
				Double zhaoling = Double.parseDouble(jiaokuanValue.getText()) -  Double.parseDouble(yinjiaoValue.getText().substring(1));
				if(zhaoling >= 0.0)
				{
					zhaolingValue.setText(Double.toString(zhaoling));
				}else
				{
					zhaolingValue.setText("");
				}
			}else
			{
				zhaolingValue.setText("");
			}
		});
		keshiName.textProperty().addListener((obs, oldText, newText) -> {
			on_enter_keshiName();
		});
	}
	
	@FXML public void on_enter_keshiName()
	{	   
		   if(keshiName.getText().length() > 10 )
			{
				refreshPayandName();
			}
	}
	
	void prepareDoctorNameAutocomplete()
	{
		
	}
	
	@FXML public void on_enter_doctorName()
	{
		int isPro = 0;
		if(haozhongType.getValue().equals("专家号"))
    	{
    		isPro = 1;
    	}else {
    		isPro = 0;
		}
		
		   J2MySQL con = new J2MySQL();
		   Connection myCon = con.connect2MySQL();
		   if(myCon == null)
		   {
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		   }
		
		    PreparedStatement pStatement = null;
			ResultSet rs = null;
			LinkedList<String> searchResult = new LinkedList<>();
			System.out.println("preparing doctor info\n");
			
			try {
				String fetchString = null;
				if(keshiName.getText().length() > 10)
				{
					String temp = "SELECT * FROM t_ksys WHERE KSBH = '%1$s' AND SFZJ = %2$d";
					fetchString = String.format(temp, keshiName.getText().substring(0, 6),isPro);
				}else
				{
					fetchString =  "SELECT * FROM t_ksys";
				}
				
				pStatement=(PreparedStatement) myCon.prepareStatement(fetchString);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				while(rs.next())
				{
					String str1 = rs.getString("YSBH").trim();
					String str2 = rs.getString("YSMC").trim();
					String str3 = rs.getString("PYZS").trim();
					String togetherStr = str1 + " " + str2 + " " + str3;
					searchResult.add(togetherStr);
				}
				Set<String> filteredAutoCompletions = new HashSet<>(searchResult);
				provider.clearSuggestions();
				provider.addPossibleSuggestions(filteredAutoCompletions);
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
	
	
	@FXML public void refreshPayandName()
	{
		   /*Connect to the database*/
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
			LinkedList<String> searchResult = new LinkedList<>();
		
		  /*refresh pay amount*/
	    	int isPro;
	    	System.out.println("payment output!");
		    System.out.println(haozhongType.getValue());
	    	if(haozhongType.getValue().equals("专家号"))
	    	{
	    		isPro = 1;
	    	}else {
	    		isPro = 0;
    		}
	    	
			try {
				String myPreState = "SELECT * from t_hzxx WHERE KSBH = '%1$s' AND SFZJ = %2$d";  	
				String myfinalState = String.format(myPreState, keshiName.getText().substring(0, 6),isPro);
				System.out.println(myfinalState);
				pStatement=(PreparedStatement) myCon.prepareStatement(myfinalState);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			double need2Pay = 0;
			try {
				rs = pStatement.executeQuery();
				String togetherStr = null;
				String need2PayStr = null;
				while(rs.next())
				{
					String str1 = rs.getString("HZBH").trim();
					String str2 = rs.getString("HZMC").trim();
					String str3 = rs.getString("PYZS").trim();
					togetherStr = str1 + " " + str2 + " " + str3;
					searchResult.add(togetherStr);
					
					need2PayStr = rs.getString("GHFY").trim();
					need2Pay = Double.parseDouble(need2PayStr);
				}
				haozhongName.setText(togetherStr);
				yinjiaoValue.setText("￥" + need2PayStr);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			/*查看patient有多少钱，够不够付清费用*/
			String patientID = LoginController.patientID;
			
			try {
				String myPreState = "SELECT * from t_brxx WHERE BRBH = '%1$s'";  	
				String myfinalState = String.format(myPreState, patientID);
				System.out.println(myfinalState);
				pStatement=(PreparedStatement) myCon.prepareStatement(myfinalState);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				double ycValue = 0;
				while(rs.next())
				{
					BigDecimal dec = rs.getBigDecimal("YCJE");
					ycValue = dec.doubleValue();
				}
				if(ycValue >= need2Pay)
				{
					isBalanceSufficient = true;
					Double zero = 0.0;
					String aString = "余额充足, 还有: "+ Double.toString(ycValue - zero) + "元";
					jiaokuanValue.setEditable(true);
					jiaokuanValue.setText(aString);
					jiaokuanValue.setEditable(false);
					zhaolingValue.setText("支付后，余额剩余："+ Double.toString(ycValue - need2Pay) + "元");
					zhaolingValue.setEditable(false);
				}else
				{
					isBalanceSufficient = false;
					jiaokuanValue.setEditable(true);
					jiaokuanValue.clear();
					zhaolingValue.setEditable(true);
					zhaolingValue.clear();
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
	}
	
	 public void sfzjChanged(String newChoice)
	{
		  /*refresh pay amount*/
	    	int isPro;
	    	System.out.println("payment output!");
		    System.out.println(haozhongType.getValue());
	    	if(newChoice.equals("专家号"))
	    	{
	    		isPro = 1;
	    	}else {
	    		isPro = 0;
    		}
	    	
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
			LinkedList<String> searchResult = new LinkedList<>();
			System.out.println("preparing name info\n");
			
			try {
				String myPreState = "SELECT * from t_hzxx WHERE KSBH = '%1$s' AND SFZJ = %2$d";  
				
				String myfinalState = String.format(myPreState, keshiName.getText().substring(0, 6),isPro);
				System.out.println(myfinalState);
				pStatement=(PreparedStatement) myCon.prepareStatement(myfinalState);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			double need2Pay = 0;
			try {
				rs = pStatement.executeQuery();
				String togetherStr = null;
				String need2PayStr = null;
				while(rs.next())
				{
					String str1 = rs.getString("HZBH").trim();
					String str2 = rs.getString("HZMC").trim();
					String str3 = rs.getString("PYZS").trim();
					togetherStr = str1 + " " + str2 + " " + str3;
					searchResult.add(togetherStr);
					
					need2PayStr = rs.getString("GHFY").trim();
					need2Pay = Double.parseDouble(need2PayStr);
				}
				haozhongName.setText(togetherStr);
				yinjiaoValue.setText("￥" + need2PayStr);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			/*查看patient有多少钱，够不够付清费用*/
			String patientID = LoginController.patientID;
			
			try {
				String myPreState = "SELECT * from t_brxx WHERE BRBH = '%1$s'";  	
				String myfinalState = String.format(myPreState, patientID);
				System.out.println(myfinalState);
				pStatement=(PreparedStatement) myCon.prepareStatement(myfinalState);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				double ycValue = 0;
				while(rs.next())
				{
					BigDecimal dec = rs.getBigDecimal("YCJE");
					ycValue = dec.doubleValue();
				}
				if(ycValue >= need2Pay)
				{
					isBalanceSufficient = true;
					double zero = 0.0;
					String aString = "余额充足, 还有: "+ Double.toString(ycValue - zero) + "元";
					jiaokuanValue.setText(aString);
					jiaokuanValue.setEditable(false);
					zhaolingValue.setText("支付后，余额剩余："+ Double.toString(ycValue - need2Pay) + "元");
					zhaolingValue.setEditable(false);
				}else
				{
					isBalanceSufficient = false;
					jiaokuanValue.setEditable(true);
					jiaokuanValue.clear();
					zhaolingValue.setEditable(true);
					zhaolingValue.clear();
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
	}

	 @FXML public void on_guahaoNumber_clicked()
	 {		 
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
			//LinkedList<String> searchResult = new LinkedList<>();
			System.out.println("preparing guahao Number info\n");
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT COUNT(*) from t_ghxx");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				String numStr = null;
				while(rs.next())
				{
					numStr = rs.getString("COUNT(*)").trim();
					System.out.println("The total number of guahao is " + numStr);
				}
				int num = (int)Double.parseDouble(numStr) + 1;
				String outputStr = String.format("%06d", num);
				guahaoNumber.setText(outputStr + "（以最终弹窗为准）");
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
	 
	 @FXML public void on_clearButton_clicked()
	 {
		 System.out.println("Clear out all the text");
		 keshiName.clear();
		 doctorName.clear();
		 haozhongName.clear();
		 jiaokuanValue.clear();
		 yinjiaoValue.clear();
		 zhaolingValue.clear();
		 guahaoNumber.clear();
		 jiaokuanValue.setEditable(true);
		 zhaolingValue.setEditable(true);
	 }
	 
	 @FXML public void on_confirmButton_clicked()
	 {
		 /*检查是否存在空项*/
		 if(keshiName.getText().equals(""))
		 {
			 return;
		 }
		 if(doctorName.getText().equals(""))
		 {
			 return;
		 }
		 if(haozhongName.getText().equals(""))
		 {
			 return;
		 }
		 if(zhaolingValue.getText().equals(""))
		 {
			 return;
		 }
	 
		 String GHBH = null;  //需要遍历，看目前有多少个号
		 String HZBH = haozhongName.getText().substring(0, 6);
		 String YSBH = doctorName.getText().substring(0, 6);
		 String BRBH = LoginController.patientID;
		 int GHRC = 0;            //需要读出，看目前人次是多少,然后加1
		 boolean THBZ = true;
		 BigDecimal GHFY = new BigDecimal(yinjiaoValue.getText().substring(1));
		 Date dNow = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String RQSJ = sdf.format(dNow);
		 
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
			System.out.println("preparing to read\n");
			
			try {
				String preStr = "SELECT MAX(t_ghxx.GHRC), t_hzxx.GHRS from t_ghxx, t_hzxx"
						+ " WHERE t_hzxx.HZBH = t_ghxx.HZBH AND t_hzxx.HZBH = '%1$s'";
				preStr = String.format(preStr, HZBH);
				pStatement=(PreparedStatement) myCon.prepareStatement(preStr);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				int maxGHRC;
				while(rs.next())
				{
					GHRC = rs.getInt("MAX(t_ghxx.GHRC)");
					maxGHRC = rs.getInt("t_hzxx.GHRS");
					System.out.println(GHRC + "" + maxGHRC);
					
					if (maxGHRC <= GHRC)
					{
						JOptionPane.showMessageDialog(null, "挂号人数已满", "挂号失败", 1);
						return;
					}
					GHRC++;
					
					
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				pStatement=(PreparedStatement) myCon.prepareStatement("SELECT COUNT(*) from t_ghxx");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				rs = pStatement.executeQuery();
				String numStr = null;
				while(rs.next())
				{
					numStr = rs.getString("COUNT(*)").trim();
				}
				int num = (int)Double.parseDouble(numStr) + 1;
				GHBH = String.format("%06d", num);
				System.out.println("GHBH is " + GHBH);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			
			
		 /*插入内容*/
			try {
				String insertGHXX = "INSERT INTO t_ghxx VALUES(?,?,?,?,?,?,?,?)";
				System.out.println(insertGHXX);
				pStatement=(PreparedStatement) myCon.prepareStatement(insertGHXX);
				pStatement.setString(1, GHBH);
				pStatement.setString(2, HZBH);
				pStatement.setString(3, YSBH);
				pStatement.setString(4, BRBH);
				pStatement.setInt(5, GHRC);
				pStatement.setBoolean(6, THBZ);
				pStatement.setBigDecimal(7, GHFY);
				pStatement.setString(8, RQSJ);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				int isSuc = pStatement.executeUpdate();
				if(isSuc > 0)
				{
					String sucStr = "挂号成功,号码为：" + GHBH;
					JOptionPane.showMessageDialog(null, sucStr, "挂号成功", 1);
					on_clearButton_clicked();
					
				}else
				{
					JOptionPane.showMessageDialog(null, "挂号失败，请检查输入是否正确", "挂号失败", 1);
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
	 }
}
