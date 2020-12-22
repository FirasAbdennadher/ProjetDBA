package application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import application.dao.dao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Sql {

	@FXML
	private JFXTextField txtsql;

	@FXML
	private Label label;

	@FXML
	private JFXButton bl_b_ajouter1;

	@FXML
	private StackPane stackPane;

	@FXML
	private TableView<?> tabv1;

	@FXML
	public  BorderPane bd1;
	int i = 1;

	@FXML
	void modifier_Click(ActionEvent event) throws SQLException {

	}

    @FXML
    public  TabPane tab_forDetails;

    @FXML
    void tester(MouseEvent event) throws SQLException {
if((txtsql.getText().equals("")==false)) {
	if(txtsql.getText().toLowerCase().contains("select")==true && txtsql.getText().toLowerCase().contains("from")==true) {
		labelsql.setVisible(false);

		try {
			tab11.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

			Tab t = new Tab("Tab" + i, showDataTable());
			tab11.getTabs().add(t);
			bd1.setCenter(tab11);
			tab11.getSelectionModel().selectNext();
			i += 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}else {
	labelsql.setVisible(true);
	labelsql.setText("vérifier le syntaxe sql");
}
}else {
	labelsql.setVisible(true);
	labelsql.setText("Veuillez saisir une requête");

}
    }
    @FXML
    private ImageView img;
    @FXML
    public void initialize() {

        img.setImage(new Image ("res/sql.png"));
    	Main.primaryStage.setMaximized(true);
    }
	@FXML
	public TabPane tab11;

    @FXML
    private TextField logsql;

    @FXML
    private TextField pw_sql;
    public void initializer(String txt1,String txt2) {
    	logsql.setText(txt1);
    	pw_sql.setText(txt2);
    }

    @FXML
    private Label labelsql;
	public TableView showDataTable() throws SQLException {
		TableView<List<Object>> table = new TableView<>();
		dao dao = new dao(logsql.getText(),pw_sql.getText());
		String scale=txtsql.getText().replace(";","");
		DataResult data = dao.getAllData(scale);
		
		for (int i = 0; i < data.getNumColumns(); i++) {
			TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
			int columnIndex = i;
			column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
			table.getColumns().add(column);
		}

		table.getItems().setAll(data.getData());
		return table;

	}
}
