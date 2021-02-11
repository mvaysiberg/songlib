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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
	@FXML TextField inputName;
	@FXML TextField inputArtist;
	@FXML TextField inputAlbum;
	@FXML TextField inputYear;
	@FXML ListView<String> listView;
	
	private ArrayList<String[]> songList;
	private ObservableList<String> obsList;
	
	public void start() {
		songList = new ArrayList<String[]>();
		
		//get data from csv
		try {
			Scanner sc = new Scanner(new File("./Data/songs.csv"));
			sc.useDelimiter("|");
			String[] songEntry = new String[4];
			while (sc.hasNext()) {
				songEntry = new String[] {sc.next(), sc.next(), sc.next(), sc.next()};
				//last entry may have a '\n'
				//need to add in sorted
				insertSorted(songEntry);
			}
			sc.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		
		songList.add(new String[] {"song1", "artist1", "album1", "year1"});
		songList.add(new String[] {"song2", "artist2", "album2", "year2"});
		
		populateObsList();
	}
	public void changeData(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == add) {
			Alert confirmAdd = new Alert(AlertType.CONFIRMATION, "Add " + inputName.getText() + "?", ButtonType.OK, ButtonType.CANCEL);
			confirmAdd.showAndWait();
			if(confirmAdd.getResult() == ButtonType.OK) {
				String[] newSong = new String[]{inputName.getText(), inputArtist.getText(),
						inputAlbum.getText(), inputYear.getText()};
				for (int i = 0; i < 4; ++i) {
					newSong[i] = formattedString(newSong[i]);
				}
				if(newSong[0].equals("") || newSong[1].equals("")) {
					Alert empty = new Alert(AlertType.INFORMATION);
					empty.initOwner(b.getScene().getWindow());
					empty.setTitle("Alert");
					empty.setHeaderText("Enter both song name and artist.");
					empty.showAndWait();
					return;
				}
				for (int i = 0; i < songList.size(); ++i) {
					if (compareSongs(songList.get(i), newSong) == 0) {
						//reject edit
						Alert exists = new Alert(AlertType.INFORMATION);
						exists.initOwner(b.getScene().getWindow());
						exists.setTitle("Alert");
						exists.setHeaderText("Song already exists in library.");
						exists.showAndWait();
						return;
					}
				}
				int insertedIndex = insertSorted(newSong);
				populateObsList();
				//NEED TO SELECT NEW SONG
				listView.getSelectionModel().select(insertedIndex);
			}
			inputName.setText("");
			inputArtist.setText("");
			inputAlbum.setText("");
			inputYear.setText("");
			System.out.println("add");
		}else if (b == edit) {
			if (songList.isEmpty()) {
				//empty library
				Alert empty = new Alert(AlertType.INFORMATION);
				empty.initOwner(b.getScene().getWindow());
				empty.setTitle("Alert");
				empty.setHeaderText("The song library is empty.");
				empty.showAndWait();
				return;
			}
			int index = listView.getSelectionModel().getSelectedIndex();
			Alert confirmEdit = new Alert(AlertType.CONFIRMATION, "Edit " + songList.get(index)[0] + "?", ButtonType.OK, ButtonType.CANCEL);
			confirmEdit.showAndWait();
			if(confirmEdit.getResult() == ButtonType.OK) {
				String[] newSong = new String[]{inputName.getText(), inputArtist.getText(),
						inputAlbum.getText(), inputYear.getText()};
				for (int i = 0; i < 4; ++i) {
					newSong[i] = formattedString(newSong[i]);
				}
				if(newSong[0].equals("") || newSong[1].equals("")) {
					Alert empty = new Alert(AlertType.INFORMATION);
					empty.initOwner(b.getScene().getWindow());
					empty.setTitle("Alert");
					empty.setHeaderText("Enter both song name and artist.");
					empty.showAndWait();
					return;
				}
				for (int i = 0; i < songList.size(); ++i) {
					if (compareSongs(songList.get(i), newSong) == 0 && i!= index) {
						//reject edit
						Alert exists = new Alert(AlertType.INFORMATION);
						exists.initOwner(b.getScene().getWindow());
						exists.setTitle("Alert");
						exists.setHeaderText("Song already exists in library.");
						exists.showAndWait();
						return;
					}
				}
				songList.remove(index);
				int insertedIndex = insertSorted(newSong);
				populateObsList();
				listView.getSelectionModel().select(insertedIndex);
			}
			inputName.setText("");
			inputArtist.setText("");
			inputAlbum.setText("");
			inputYear.setText("");
			System.out.println("edit");
		}else if (b == delete) {
			System.out.println("delete");
			if (songList.isEmpty()) {
				//empty library
				Alert empty = new Alert(AlertType.INFORMATION);
				empty.initOwner(b.getScene().getWindow());
				empty.setTitle("Alert");
				empty.setHeaderText("The song library is empty.");
				empty.showAndWait();
				return;
			}else {
				int index = listView.getSelectionModel().getSelectedIndex();
				//ask for confirmation
				Alert confirmDelete = new Alert(AlertType.CONFIRMATION, "Delete " + songList.get(index)[0] + "?", ButtonType.OK, ButtonType.CANCEL);
				confirmDelete.showAndWait();
				if(confirmDelete.getResult() == ButtonType.OK) {
					songList.remove(index);
					populateObsList();
					if (songList.size() > index) {
						listView.getSelectionModel().select(index);
					}else if (songList.size() <= index && songList.size() > 0) {
						listView.getSelectionModel().select(index -1);
					}else {
						name.setText("Name:");
						artist.setText("Artist:");
						album.setText("Album:");
						year.setText("Year:");
					}
				}
			}
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
		if (index >= 0) {
			String content = listView.getSelectionModel().getSelectedItem();
			System.out.println("index: " + index + " content: " + content);
			name.setText("Name: " + songList.get(index)[0]);
			artist.setText("Artist: " + songList.get(index)[1]);
			album.setText("Album: " + songList.get(index)[2]);
			year.setText("Year: " + songList.get(index)[3]);
		}
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
	private int insertSorted(String[] song) {
		if (songList.isEmpty()) {
			songList.add(song);
			return 0;
		}else {
			int left = 0;
			int right = songList.size() -1;
			int middle = -1;
			while (left <= right) {
				 middle = (left + right)/2;
				 if (compareSongs(songList.get(middle), song) < 0) {
					left = middle + 1;
				 }else if (compareSongs(songList.get(middle), song) > 0) {
					right = middle -1;
				 }
				 //do not have case when they are equal, should be caught previously
			}
				if (compareSongs(songList.get(middle), song) < 0) {
					songList.add(middle + 1, song);
					return middle + 1;
				}else {
					songList.add(middle,song);
					return middle;
				}
		}
	}
	private int compareSongs(String[] song1, String[] song2) {
		int comp1 = song1[0].toLowerCase().compareTo(song2[0].toLowerCase());
		if (comp1 != 0) { //songs have different names
			return comp1;
		}else {//songs have same name, compare by artist name
			return song1[1].toLowerCase().compareTo(song2[1].toLowerCase());
		}
	}
	private void populateObsList() {
		obsList = FXCollections.observableArrayList();
		for (String[] songInfo: songList) {
			obsList.add(songInfo[0] + " by " + songInfo[1]);
		}
		listView.setItems(obsList);
	}
}
