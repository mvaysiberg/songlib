package view;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class ListController {
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML Text name;
	@FXML Text artist;
	@FXML Text album;
	@FXML Text year;
	@FXML ListView<String> listView;
	
	private ArrayList<String[]> songList;
	private ObservableList<String> obsList;
	
	public void start() {
		songList = new ArrayList<String[]>();
		obsList = FXCollections.observableArrayList();
		
		//get data from csv
		try {
			Scanner sc = new Scanner(new File("./Data/songs.csv"));
			sc.useDelimiter("|");
			String[] songEntry = new String[4];
			while (sc.hasNext()) {
				songEntry = new String[] {sc.next(), sc.next(), sc.next(), sc.next()};
				//last entry may have a '\n'
				//need to add in sorted
				songList.add(songEntry);
			}
			sc.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		
		songList.add(new String[] {"song1", "artist1", "album1", "year1"});
		songList.add(new String[] {"song2", "artist2", "album2", "year2"});
		
		for (String[] songInfo: songList) {
			obsList.add(songInfo[0]);
		}
		listView.setItems(obsList);
	}
	public void changeData(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == add) {
			TextInputDialog dialog = new TextInputDialog();
			dialog.initOwner(b.getScene().getWindow());
			dialog.setHeaderText("Add Song to Song Library)");
			dialog.setContentText("Enter name: ");
			Optional<String> nameOutput = dialog.showAndWait();
			if(nameOutput.isPresent()) {
				String SongName = nameOutput.get();
				SongName = formattedString(SongName);
				if(SongName.equals("")) {
					Alert empty = new Alert(AlertType.INFORMATION);
					empty.initOwner(b.getScene().getWindow());
					empty.setTitle("Alert");
					empty.setHeaderText("Enter a song name.");
					empty.showAndWait();
				}
				for(int i = 0; i < songList.size(); i++) {
					//add check for name and artist -- currently only checks name
					if(songList.get(i)[0].toLowerCase().equals(SongName.toLowerCase())) {
						Alert exists = new Alert(AlertType.INFORMATION);
						exists.initOwner(b.getScene().getWindow());
						exists.setTitle("Alert");
						exists.setHeaderText("Song already exists in library.");
						exists.showAndWait();
					}
				}
			}
			
			System.out.println("add");
		}else if (b == edit) {
			
			
			System.out.println("edit");
		}else if (b == delete) {
			System.out.println("delete");
		}
	}
	
	public void start(Stage mainstage) {
		
		
		listView.getSelectionModel().selectedIndexProperty().
		addListener((obs, oldVal, newVal)-> showItem(mainstage));
		//select the first item
		listView.getSelectionModel().select(0);
	}
	
	private void showItem(Stage mainstage) {
		//System.out.println("test");
		int index = listView.getSelectionModel().getSelectedIndex();
		String content = listView.getSelectionModel().getSelectedItem();
		System.out.println("index: " + index + " content: " + content);
		name.setText("Name: " + songList.get(index)[0]);
		artist.setText("Artist: " + songList.get(index)[1]);
		album.setText("Album: " + songList.get(index)[2]);
		year.setText("Year: " + songList.get(index)[3]);
	}
	
	private String formattedString(String s){
		int first = 0;
		for (; first < s.length(); ++first){
			if(s.charAt(first) != ' ')
				break;
		}
		int last = s.length() -1;
		for (; last >= 0; --last){
			if (s.charAt(last) != ' '){
				break;
			}
		}
		if (first == s.length() || last == -1){
			return "";
		}
		return s.substring(first, last+1);
	}
}
