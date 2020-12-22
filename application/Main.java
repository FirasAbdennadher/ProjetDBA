package application;
	

import application.controller.Sql;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
//	public static Menu menu;
	public static Stage primaryStage;
	public static BorderPane forAllBorderPane;
	public static String log;
	public static String pw;
	public static Sql sql;
	public static Tab taab;
	@Override
	public void start(Stage primaryStage) {
		try 
		{
			
			AnchorPane root = (AnchorPane)FXMLLoader.load(Main.class.getResource("views/Login.fxml"));

			Scene scene = new Scene(root);
			scene.getStylesheets().add(Main.class.getResource("views/application.css").toExternalForm());
			primaryStage.setScene(scene);
			Main.primaryStage = primaryStage;
			primaryStage.setMaximized(true);
			primaryStage.show();
			
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
