package app;

/**
 * Jason Chou and Ty Goldin
 * Group#44
 *
 */
import object.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class userPageController {

	public static Account currentUser;
	
    @FXML
    private ListView<Album> albumList;

    @FXML
    private TextField albumName;

    @FXML
    private TextField photoNumber;

    @FXML
    private TextField dateRange;

    @FXML
    private Text statusText;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text userStatus;

    @FXML
    private Button logoutButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button addAlbumButton;

    @FXML
    private Button renameAlbumButton;

    @FXML
    private Button removeAlbumButton;

    @FXML
    private Button viewAlbumButton;

    public void initialize() {
    	try {
    		userStatus.setText("Photo Library: " + currentUser.username);
    		setVisibility(false);
    		
    		File folder = new File("users/" + currentUser.username);
    		File[] listOfFiles = folder.listFiles();
    		currentUser.albumList.clear();
    		
    		for(File f : listOfFiles) {
        		try {
    				readFile(f.toString());
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			
    		}
}
        
        	albumList.setItems(currentUser.albumList);
    	} catch (Exception e) {
    		
    	}
    	
    	
    }
    
    @FXML
    void onListClick(MouseEvent event) {
    	try {
    		albumName.setText(albumList.getSelectionModel().getSelectedItem().albumName);
    		photoNumber.setText("" + albumList.getSelectionModel().getSelectedItem().albumPhotos.size());
    	} catch (Exception e) {
    		
    	}
    	
    	try {
    		
    		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    		
    		
    		if(albumList.getSelectionModel().getSelectedItem().albumPhotos.size() == 1) {
    			Photo p = albumList.getSelectionModel().getSelectedItem().albumPhotos.get(0);
    			Date d = p.date;
    			dateRange.setText(format1.format(d) + "-" + format1.format(d));
    			return;
    		}
    		
    		Date min;
    		Date max;
    		ArrayList<Photo> ptr = albumList.getSelectionModel().getSelectedItem().albumPhotos;
    		
    		if(ptr.get(0).date.before(ptr.get(1).date)) {
    			min = ptr.get(0).date;
    			max = ptr.get(1).date;
    		}
    		else {
    			min = ptr.get(1).date;
    			max = ptr.get(0).date;
    		}
    		
    		for(int i = 2; i < ptr.size(); i++) {
    			if(ptr.get(i).date.after(max))
    				max = ptr.get(i).date;
    			else if(ptr.get(i).date.before(min))
    				min = ptr.get(i).date;
    		}
    		
    		dateRange.setText(format1.format(min) + "-" + format1.format(max));
    	}
    	catch(Exception e) {
    		
    	}
    }
    
    @FXML
    void addClick(ActionEvent event) {
    	setVisibility(true);
    	setStatus("adding");
    	albumName.setText("");
    	photoNumber.setText("");
    	dateRange.setText("");
    	albumList.setDisable(true);
    }

    @FXML
    void cancelClick(ActionEvent event) {
    	setVisibility(false);
    	setStatus("viewing");
    	albumList.setDisable(false);
    }

    @FXML
    void confirmClick(ActionEvent event) {
    	if (statusText.getText().contentEquals("Currently adding album...")) {
    		Album newAlbum = new Album(albumName.getText());
    		currentUser.albumList.add(newAlbum);
    		albumList.setDisable(false);
    		setStatus("viewing");
    		albumList.getSelectionModel().select(newAlbum);
    		try{
    			writeToFile(newAlbum);
    		}
    		catch(Exception e) {
    			System.out.println("File not found");
    		}
    		
    	}
    	if (statusText.getText().contentEquals("Confirm delete?")) {
    		try {
    			
    			
    			Path path = Paths.get("users/" + currentUser.username + "/" + albumList.getSelectionModel().getSelectedItem().albumName + ".bin");
    			
    			try {
					Files.delete(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			currentUser.albumList.remove(albumList.getSelectionModel().getSelectedItem());
    			
    			
    		} catch (Exception exception) {
    			
    		}
    		
	    	if(!albumList.getSelectionModel().isEmpty())
	    		showAlbum();
	    	else
	    		clearDetails();
    		
    		
    		albumList.setDisable(false);
    		setStatus("viewing");
    	}
    	
    	if (statusText.getText().contentEquals("Currently renaming album...")) {
	    	try {
	    		Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
	    		String oldName = selectedAlbum.albumName;
	    		albumList.getSelectionModel().getSelectedItem().albumName = albumName.getText();
	    		albumList.getSelectionModel().clearSelection();
		    	albumList.getSelectionModel().select(selectedAlbum);
	    		try{
	    			writeToFile(selectedAlbum);
	    			Path path = Paths.get("users/" + currentUser.username + "/" + oldName + ".bin");
	    			Files.delete(path);
	    			
	    		}
	    		catch(Exception e) {
	    			System.out.println("File not found");
	    		}
		    	
	    	} catch (Exception e) {
	    		
	    	}
	    	albumList.setDisable(false);
	    	setStatus("viewing");
    	}
    }

    @FXML
    void logoutClick(ActionEvent event) throws IOException {
    	Parent loginPage = FXMLLoader.load(Photos.class.getResource("/view/loginPage.fxml"));
		Scene scene = new Scene(loginPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }

    @FXML
    void removeClick(ActionEvent event) {
    	setVisibility(true);
    	setStatus("deleting");
//    	albumList.setDisable(true);
    }

    @FXML
    void renameClick(ActionEvent event) {
    	setVisibility(true);
    	setStatus("renaming");
    	albumList.setDisable(true);
    }

    @FXML
    void searchClick(ActionEvent event) throws IOException, ParseException {

    	
    	TextInputDialog dialog = new TextInputDialog("");
    	 
    	dialog.setTitle("Searching Photos by Tags");
    	dialog.setHeaderText("Search photos either by: \n tags with format tag=type (Example: person=sesh AND/OR location=prague) \n date with format date-date (Example: 04/13/2020-04/14/2020)");
    	 
    	Optional<String> result = dialog.showAndWait();
    	
    	if(!result.isPresent())
    		return;
    	
    	String result2 = result.toString().substring(8);
    	 
    	int and = result2.toString().indexOf("AND");
    	int or = result2.toString().indexOf("OR");
    	int date = result2.toString().indexOf("-");
    	
    	//search all albums for photos then create album and of those photos and display
    	Album searchAlbum = new Album("result");
    	searchAlbum.albumPhotos.clear();

    	
    	if(and > 0) {
    		
    		String first = result2.substring(1, and - 1);
    		String second = result2.substring(and + 4, result2.length() - 1);
    		
    		for(Album a : currentUser.albumList){
    			for(Photo p : a.albumPhotos) {
    				if(p.tags.contains(first) && p.tags.contains(second)) {
    					if(!searchAlbum.albumPhotos.contains(p))
    						searchAlbum.albumPhotos.add(p);
    				}
    			}
    		}
    		
    	}
    	
    	else if(or > 0) {
    		
    		String first = result2.substring(1, or - 1);
    		String second = result2.substring(or + 3, result2.length() - 1);
    		
    		
    		for(Album a : currentUser.albumList){
    			for(Photo p : a.albumPhotos) {
    				if(p.tags.contains(first) || p.tags.contains(second)) {
    					if(!searchAlbum.albumPhotos.contains(p))
    						searchAlbum.albumPhotos.add(p);
    				}
    			}
    		}
    	}
    	
    	else if(date > 0) {
    		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    		String first = result2.substring(1, date);
    		String second = result2.substring(date + 2, result2.length() - 1);
    		Date date1 = format1.parse(first);
    		Date date2 = format1.parse(second);
    		
    		for(Album a : currentUser.albumList){
    			for(Photo p : a.albumPhotos) {
    				if(p.date.after(date1) && p.date.before((date2))) {
    					if(!searchAlbum.albumPhotos.contains(p))
    						searchAlbum.albumPhotos.add(p);
    				}
    			}
    		}
    	}
    	
    	else {
    		for(Album a : currentUser.albumList){
    			for(Photo p : a.albumPhotos) {
    				if(p.tags.contains(result2.substring(1, result2.length() - 1))) {
    					searchAlbum.albumPhotos.add(p);
    				}
    				
    			}
    		}
    		
    	}
    	
    	userViewSearchResultController.currentAlbum = searchAlbum;
    	userViewSearchResultController.currentAccount = currentUser;
 
		Parent userViewSearchResultPage = FXMLLoader.load(Photos.class.getResource("/view/userViewSearchResultPage.fxml"));
		FXMLLoader uvsrp = new FXMLLoader(Photos.class.getResource("/view/userViewSearchResultPage.fxml"));
		
    	Scene scene = new Scene(userViewSearchResultPage);
    	userViewSearchResultController controller = uvsrp.getController();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    	
    }

    @FXML
    void viewClick(ActionEvent event) throws IOException {
    		if (albumList.getSelectionModel().getSelectedItem()!= null) {
	    		Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
	    		userViewPageController.currentAlbum = selectedAlbum;
	    		userViewPageController.currentAccount =  currentUser;
	    		
	    		Parent userViewPage = FXMLLoader.load(Photos.class.getResource("/view/userViewPage.fxml"));
	    		FXMLLoader uvp = new FXMLLoader(Photos.class.getResource("/view/userViewPage.fxml"));
	    		
		    	
		    	
		    	Scene scene = new Scene(userViewPage);
				userViewPageController controller = uvp.getController();
				
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
				window.setScene(scene);
				window.show();
				
				window.setOnCloseRequest(new EventHandler<WindowEvent>() {
		            @Override
		            public void handle(WindowEvent e) {
		            	try {
		            		writeToFile(userViewPageController.currentAlbum);
		            	} catch (Exception exc) {
		            		
		            	}
		                Platform.exit();
		                System.exit(0);
		            }
		        });
    		}
    }
    
    void setVisibility(boolean visible) {
    	confirmButton.setVisible(visible);
    	cancelButton.setVisible(visible);
    }
    
    void setStatus(String status) {
    	if(!status.contentEquals("deleting")) {
    		statusText.setText("Currently " + status + " album...");
    	}
    	else {
    		statusText.setText("Confirm delete?");
    	}
    
    	if(status.contentEquals("deleting")) {
    		setVisibility(true);
    	}
    	
    	if (status.contentEquals("viewing")) {
    		setVisibility(false);
    		setEditable(false);
    	}
    	if (status.contentEquals("adding") || status.contentEquals("renaming")) {
    		setVisibility(true);
    		setEditable(true);
    	}
    }
    
    void setEditable(boolean edit) {
    	albumName.setEditable(edit);

    }
    
    void showAlbum() {
    	Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    	albumName.setText(selectedAlbum.albumName);
    	/**
    	 * add other fields here once Album object is complete
    	 * 
    	 */
    	
    }
    
    void clearDetails() {
    	albumName.setText("");
    	photoNumber.setText("");
    	dateRange.setText("");
    }
    
    public static void writeToFile(Album a) throws FileNotFoundException, IOException {
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users/" + currentUser.username + "/" + a.albumName + ".bin"))) {
			oos.writeObject(a);
		}
		
    }
    
    public static void readFile(String s) throws FileNotFoundException, IOException, ClassNotFoundException {
    	try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(s)))) {
			currentUser.albumList.add((Album)ois.readObject());
		}
    	
    	
    }

}
