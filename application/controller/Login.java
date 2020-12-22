package application.controller;

import java.io.IOException;
import java.sql.Connection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.glass.events.MouseEvent;

import application.Main;
import application.singleton.Sconnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Login {

	@FXML
	private AnchorPane loginPage;

	@FXML
	private Pane pane;

	@FXML
	private JFXTextField login;

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXButton b_login;

	@FXML
	private Label label;

	@FXML
	private ImageView power;

	@FXML
	void loginHandle(ActionEvent event) throws IOException {
		if("".equals(login.getText())==false  || "".equals(password.getText())) {
		Connection cnx = Sconnection.getConnection(login.getText(), password.getText());
		if (cnx != null) {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/MainMenu.fxml"));
			BorderPane root = (BorderPane) loader.load();

			/*
			 * FXMLLoader loader = new FXMLLoader(Main.class.getResource("Menu.fxml"));
			 * AnchorPane root = (AnchorPane)loader.load();
			 * 
			 * Menu menu =loader.getController();
			 * 
			 * menu.fn3(login.getText(),password.getText());
			 */
			
			MainMenu mm = loader.getController();
			mm.Initalize(login.getText(),password.getText());
			mm.modelRelationelle(login.getText(),password.getText());
			Scene scene = new Scene(root);
			Main.primaryStage.setScene(scene);			
			//Main.primaryStage.setFullScreen(true);
			//Main.primaryStage.setMaximized(true);
			/*Main.primaryStage.setMinWidth(1000);
			Main.primaryStage.setMinHeight(1000);*/

	    	

			Main.primaryStage.show();

		}else {
			label.setText("*Login/Password incorrect !");
		}

	}else {
		label.setText("*Veuillez saisir un login et un mot de passe !");

	}
	}

	@FXML
	void power_Click(MouseEvent event) {

	}

	@FXML
	private Button btnlog;

	

}
