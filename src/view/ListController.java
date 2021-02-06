package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
public class ListController {
	@FXML
	ListView<String> listView;
	private ObservableList<String> obsList;
	public void start() {
		obsList = FXCollections.observableArrayList();
		//get data from csv
		listView.setItems(obsList);
	}
	
	public void start(Stage mainstage) {
		//select the first item
		//listView.getSelectionModel().select(0);
	}
}
