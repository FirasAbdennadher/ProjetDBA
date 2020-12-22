package application.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.prefs.Preferences;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import Relationelle.*;
import application.Main;
import application.dao.dao;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenu {

	@FXML
	private TreeView<String> treeview = null;

	@FXML
	private Label title;

	@FXML
	private ImageView logout_icon;

	@FXML
	private ImageView search_icon;

	@FXML
	private TextField t_search;

	@FXML
	private ComboBox<String> c_searchBy;

	@FXML
	private BorderPane borderPane;

	@FXML
	private JFXDrawer drawer;

	@FXML
	private VBox drawerVbox;

	
	@FXML
	private VBox vbox;

	@FXML
	private GridPane topPane;

	@FXML
	private JFXHamburger hamburger;

	@FXML
	private AnchorPane leftAnchorPane;

	@FXML
	private JFXButton rela;

	@FXML
	public TextField pw_txt;

	@FXML
	public TextField log_txt;

    @FXML
    public void initialize() {

    	Main.primaryStage.setMaximized(true);

    }

	
	public void modelRelationelle(String log1, String pw1) {
		pw_txt.setText(log1);
		log_txt.setText(pw1);

	}

	@FXML
	void modeleRela(ActionEvent event) {
		MainWindow window;
		Preferences userPreferences = Preferences.userRoot();
		userPreferences.put("login", log_txt.getText());
		userPreferences.put("password", pw_txt.getText());

		window = new MainWindow(log_txt.getText(), pw_txt.getText());
		window.setVisible(true);

	}

	@FXML
	void search_icon_Click(MouseEvent event) {
		

	}

	@FXML
	void logout_Click(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	private JFXTreeView<String> treeviewJX1;
	Image db = new Image("res/db.png");
	Image col = new Image("res/col.png");
	Image index = new Image("res/inde.png");
	Image user = new Image("res/user.png");
	Image seq = new Image("res/seq.png");
	Image col2 = new Image("res/col2.png");
	Image views = new Image("res/view.png");


	public void Initalize(String log, String password) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/Sql.fxml"));
			BorderPane root = (BorderPane) loader.load();
			borderPane.setCenter(root);
			BorderPane.setAlignment(root, Pos.CENTER);
			Sql sql = loader.getController();
			sql.initializer(log_txt.getText(),pw_txt.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		dao d = new dao(log, password);
		ObservableList<String> listeTable = FXCollections.observableArrayList(d.getTables());
		TreeItem<String> root = new TreeItem<String>("User :" + log, new ImageView(db));

		TreeItem<String> tables = new TreeItem<String>("Tables", new ImageView(col));
		root.getChildren().add(tables);

		for (String c : listeTable) {

			TreeItem<String> treeItemNoed = new TreeItem<String>(c.toString());
			tables.getChildren().add(treeItemNoed);
			TreeItem<String> cntr = new TreeItem<String>("Contraintes");
			treeItemNoed.getChildren().add(cntr);
			ObservableList<String> listeContraint = FXCollections
					.observableArrayList(d.getTableContraintes(c.toString()));
			for (String c5 : listeContraint) {
				TreeItem<String> sousCONTR = new TreeItem<String>(c5.toString());
				cntr.getChildren().add(sousCONTR);
			}

			TreeItem<String> Colonnes = new TreeItem<String>("Colonnes", new ImageView(col2));
			treeItemNoed.getChildren().add(Colonnes);
			ObservableList<String> listColumn = FXCollections.observableArrayList(d.getTableColumns(c.toString()));
			for (String c2 : listColumn) {
				TreeItem<String> sousColumn = new TreeItem<String>(c2.toString());
				Colonnes.getChildren().add(sousColumn);
			}

			TreeItem<String> Reference = new TreeItem<String>("Tables référencées");
			treeItemNoed.getChildren().add(Reference);
			ObservableList<String> listRef = FXCollections.observableArrayList(d.getTableRef(c.toString()));
			for (String c2 : listRef) {
				TreeItem<String> sousRef = new TreeItem<String>(c2.toString());
				Reference.getChildren().add(sousRef);
			}

		}

		TreeItem<String> view = new TreeItem<String>("Vues", new ImageView(views));
		ObservableList<String> views = FXCollections.observableArrayList(d.getViews());
		for (String cv : views) {
			TreeItem<String> sousview = new TreeItem<String>(cv.toString());
			view.getChildren().add(sousview);
		}
		root.getChildren().add(view);

		TreeItem<String> sequence = new TreeItem<String>("Séquences", new ImageView(seq));
		ObservableList<String> sequences = FXCollections.observableArrayList(d.getSequence());
		for (String cv : sequences) {
			TreeItem<String> soussequence = new TreeItem<String>(cv.toString());
			sequence.getChildren().add(soussequence);
		}
		root.getChildren().add(sequence);

		TreeItem<String> procedures = new TreeItem<String>("Procédures");
		ObservableList<String> proceduress = FXCollections.observableArrayList(d.getProcedures());
		for (String cv : proceduress) {
			TreeItem<String> sousproceduress = new TreeItem<String>(cv.toString());
			procedures.getChildren().add(sousproceduress);
		}
		root.getChildren().add(procedures);

		TreeItem<String> Déclencheurs = new TreeItem<String>("Déclencheurs");
		ObservableList<String> col_Déclencheurss = FXCollections.observableArrayList(d.getTrigger());
		for (String cv : col_Déclencheurss) {
			TreeItem<String> souscol_Déclencheurss = new TreeItem<String>(cv.toString());
			Déclencheurs.getChildren().add(souscol_Déclencheurss);
		}
		root.getChildren().add(Déclencheurs);

		TreeItem<String> Index = new TreeItem<String>("Index", new ImageView(index));
		ObservableList<String> col_col_Indexs = FXCollections.observableArrayList(d.getIndex());
		for (String cv : col_col_Indexs) {
			TreeItem<String> souscol_col_col_Indexs = new TreeItem<String>(cv.toString());
			Index.getChildren().add(souscol_col_col_Indexs);
		}
		root.getChildren().add(Index);

		TreeItem<String> users = new TreeItem<String>("Autres utilisateurs", new ImageView(user));
		ObservableList<String> col_userss = FXCollections.observableArrayList(d.getAllUser());
		for (String cv : col_userss) {
			TreeItem<String> soususers = new TreeItem<String>(cv.toString());
			users.getChildren().add(soususers);
		}
		root.getChildren().add(users);
		treeviewJX1.setRoot(root);

		treeviewJX1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			String colonne_specifique = "", column = "", table_view_index = "", table_view_index_NAME = "";
			try {
				colonne_specifique = newValue.getValue();
				column = newValue.getParent().getValue();
				table_view_index = newValue.getParent().getParent().getParent().getValue();
				table_view_index_NAME = newValue.getParent().getParent().getValue();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		try	{if ("Tables".equals(newValue.getValue())) {
			
				String tableColumn = "SELECT ut.TABLE_NAME,utc.COLUMN_NAME FROM USER_TABLES ut ,USER_TAB_COLUMNS utc where ut.TABLE_NAME = utc.TABLE_NAME";

				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable_Tables(tableColumn, log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};
						
			}

			else if ("Tables".equals(table_view_index)) {
				if ("Colonnes".equals(column)) {
					
						String queryCol = "SELECT TABLE_NAME,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,DEFAULT_LENGTH,HISTOGRAM,AVG_COL_LEN,CHAR_LENGTH FROM USER_TAB_COLUMNS WHERE COLUMN_NAME ='"
								+ colonne_specifique + "' AND TABLE_NAME = '" + table_view_index_NAME + "'";

						try {
							((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable_Tables(queryCol, log, password));
						} catch (SQLException e2) {
							e2.printStackTrace();
						};
					
				}
			} else if ("Vues".equals(newValue.getParent().getValue())) {				
				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable(dao.queryView, newValue.getValue(), log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};

			} else if ("Procédures".equals(newValue.getParent().getValue())) {
					
		
				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable(dao.queryPro, newValue.getValue(), log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};

			

			} else if ("Séquences".equals(newValue.getParent().getValue())) {
		
				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable(dao.querySeq, newValue.getValue(), log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};

			} else if ("Déclencheurs".equals(newValue.getParent().getValue())) {
			
				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable(dao.queryTrigger, newValue.getValue(), log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};
			} else if ("Index".equals(newValue.getParent().getValue())) {
				
				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable(dao.queryIndex, newValue.getValue(), log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};
			} else if ("Autres utilisateurs".equals(newValue.getParent().getValue())) {
				try {
					((BorderPane)((BorderPane)Main.primaryStage.getScene().getRoot()).getCenter()).setCenter(showDataTable(dao.queryUser, newValue.getValue(), log, password));
				} catch (SQLException e2) {
					e2.printStackTrace();
				};
			}else if(newValue.getValue().equals("Autres utilisateurs")
					||newValue.getValue().equals("Index")
					||newValue.getValue().equals("Déclencheurs")
					||newValue.getValue().equals("Séquences")
					||newValue.getValue().equals("Procédures")
					||newValue.getValue().equals("Séquences")||newValue.getValue().contains("User :")==true ) {
				System.out.println(newValue.getValue().contains("Dé"));
			}else if(newValue.getValue().contains("User :")) {
				
			}
		}catch (Exception e) {
			System.out.println(e.getMessage()+"Nothing !");
		}
		});

		HamburgerBackArrowBasicTransition hamTransition = new HamburgerBackArrowBasicTransition(hamburger);
		hamTransition.setRate(-1);

		hamburger.setOnMouseClicked(e -> {

			hamTransition.setRate(hamTransition.getRate() * -1);
			hamTransition.play();
			if (drawer.isShown()) {
				drawer.close();
			} else {
				drawer.open();
			}
		});

		drawer.setSidePane(drawerVbox);

		drawer.setPrefWidth(0);
		leftAnchorPane.setPrefWidth(0);

		leftAnchorPane.prefWidthProperty().bind(drawer.prefWidthProperty().add(vbox.prefWidthProperty()));
		drawer.setOnDrawerClosing(e -> drawer.setPrefWidth(0));
		drawer.setOnDrawerOpening(e -> drawer.setPrefWidth(159));

		borderPane.widthProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() < 790) {
				hamburger.setVisible(true);
				title.setText("V.simplifiée 'Oracle SQL Developer");
				title.setFont(Font.font(null, FontWeight.BOLD, 24));
				title.setMaxWidth(120);

				if (drawer.isShown()) {
					hamTransition.setRate(-1);
					hamTransition.play();
					drawer.close();
				}
			} else {
				hamburger.setVisible(true);

				if (((Stage) borderPane.getScene().getWindow()).isMaximized()) {
					if (!drawer.isShown()) {
						hamTransition.setRate(hamTransition.getRate() * -1);
						hamTransition.play();
						drawer.open();
					}
				}
				title.setText("Version simplifiée 'Oracle SQL Developer'");
				title.setFont(Font.font("Century Schoolbook", FontWeight.NORMAL, 24));
				title.setMaxWidth(Double.MAX_VALUE);

			}
		});

		// invoke the event of click in the search icon
		search_icon_Click(null);
		search_icon.setDisable(true);

		// -------- Use these TextField and comboBox as static Main attributes
		// everywhere -----------

		Main.forAllBorderPane = borderPane;
	}

	private TableView showDataTable(String query, String parm1, String log, String password) throws SQLException {
		TableView<List<Object>> table = new TableView<>();
		dao dao = new dao(log, password);
		DataResult data = dao.getAllData(query + parm1 + "'");

		for (int i = 0; i < data.getNumColumns(); i++) {
			TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
			int columnIndex = i;
			column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
			table.getColumns().add(column);
		}

		table.getItems().setAll(data.getData());
		return table;
	}

	private TableView showDataTable_Tables(String query, String log, String password) throws SQLException {
		TableView<List<Object>> table = new TableView<>();
		dao dao = new dao(log, password);
		DataResult data = dao.getAllData(query);

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
