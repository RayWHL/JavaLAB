package application;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;

/*���ӵ�MySQL���ݿ�*/
public class J2MySQL {
	public Connection connect2MySQL()
	{
		Connection myCon = null;
		try {
			myCon =(Connection) DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/patientinfo?characterEncoding=utf8&useSSL=true","root","qiaoqiao123MSN");
			
			if(myCon != null)
			{
				System.out.println("Successfully connected!");
			}
			return myCon;
		}catch(Exception e)
		{
			System.out.print("not connect to the database!");
		}
		return myCon;
	}
}
