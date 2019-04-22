package application;

import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChooseController implements Initializable {
	@FXML private Button doctorButton;
	@FXML private Button patientButton;

	@FXML void on_doctorButton_clicked()
	{
		Stage tempStage = (Stage) patientButton.getScene().getWindow();
        tempStage.close();
       FXMLLoader loader = new FXMLLoader(
	          getClass().getResource("DoctorLogin.fxml")
	      );
       AnchorPane root = new AnchorPane();
	    Scene myScene = new Scene(root);
       try {
	       myScene.setRoot((Parent) loader.load());
	       Stage newStage = new Stage();
	       newStage.setTitle("医生工作台");
	       newStage.setScene(myScene);
	       newStage.show();
      }catch (IOException ex) {
   	  ex.printStackTrace();
      } 
	}
	
	@FXML void on_patientButton_clicked()
	{
	    Stage tempStage = (Stage) patientButton.getScene().getWindow();
         tempStage.close();
        FXMLLoader loader = new FXMLLoader(
	          getClass().getResource("Login.fxml")
	      );
        AnchorPane root = new AnchorPane();
	    Scene myScene = new Scene(root);
        try {
	       myScene.setRoot((Parent) loader.load());
	       Stage newStage = new Stage();
	       newStage.setTitle("病人登入界面");
	       newStage.setScene(myScene);
	       newStage.show();
       }catch (IOException ex) {
    	  ex.printStackTrace();
       } 
   }
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
