package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
public class ListController {
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	
	@FXML ListView<String> listView;
	private ObservableList<String> obsList;
	public void start() {
		obsList = FXCollections.observableArrayList();
		//get data from csv
		listView.setItems(obsList);
	}
	public void changeData(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == add) {
			System.out.println("add");
		}else if (b == edit) {
			System.out.println("edit");
		}else if (b == delete) {
			System.out.println("delete");
		}
	}
	public void start(Stage mainstage) {
		//select the first item
		//listView.getSelectionModel().select(0);
	}
}
