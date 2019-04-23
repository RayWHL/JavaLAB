package application;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class ConSQL {
	public static Connection connecToMySQL()
	{
		Connection myCon = null;
		try {
			
			myCon =(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/his","root","412723");
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
