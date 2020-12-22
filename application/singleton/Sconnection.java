package application.singleton;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.DriverManager;

public class Sconnection{
	
private static Connection cnx;
	
private Sconnection() {}
	
	public static Connection getConnection(String user,String pass)
	{
		try{
			Class.forName ("oracle.jdbc.driver.OracleDriver");
		}
		catch(Exception e)
		{
			e.printStackTrace (); 
		}
		
		try
		{
			if(cnx==null || cnx.isClosed()) {
String url="jdbc:oracle:thin:@localhost:1521:orcl";	
		    cnx = DriverManager.getConnection(url,user,pass);
			}
			}
		catch(Exception e)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("Erreur de Connection à la Base de Donnée");
			alert.showAndWait();

         //   e.printStackTrace();

		}
		return cnx;
	}
	
	
	public static void close()
	{
		try
		{
			if(cnx!=null && !cnx.isClosed())
			{
				cnx.close();
				System.out.println("Connection fermé : "+cnx.isClosed());
			}
		}
		catch(Exception e)
		{
			//System.out.println(e.getMessage());
		}
	}
}
