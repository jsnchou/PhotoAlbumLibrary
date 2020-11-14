package app;


/**
 * Jason Chou and Ty Goldin
 * Group#44
 *
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import object.Account;
import object.AccountList;
import javafx.scene.Node;

public class loginPageController {
	
	File loader = new File("accounts.txt");

    public void start() {
    	
    }

    public void shutDown() {

    }

    @FXML 
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    static ObservableList<Account> obsAccountList = FXCollections.observableArrayList();
    
    public void initialize() throws FileNotFoundException, ClassNotFoundException, IOException {
    	AccountList accountList = readFile("users/accountlist.bin");
    	obsAccountList.clear();
    	for (int i = 0; i < accountList.usernames.size(); i++) {
    		obsAccountList.add(new Account(accountList.usernames.get(i), accountList.passwords.get(i), accountList.type.get(i)));
    	}
    }
    
    @FXML
    void loginClick(ActionEvent event) throws IOException {
    	
    	Parent adminPage = FXMLLoader.load(Photos.class.getResource("/view/adminPage.fxml"));
    	FXMLLoader apl = new FXMLLoader(Photos.class.getResource("/view/adminPage.fxml"));
    	
    	try {
    		boolean found = false;
    		char type = 'a';
    		if (username.getText().contentEquals("admin") && password.getText().contentEquals("admin")){
    			found = true;
    		} else {
	    		for (int i = 0; i < obsAccountList.size(); i++){
	    			if (username.getText().contentEquals(obsAccountList.get(i).username) && password.getText().contentEquals(obsAccountList.get(i).password)) {
	    				userPageController.currentUser = obsAccountList.get(i);
	    				type = 'b';
	    				found = true;
	    			}
	    		}
    		}
    		if (found) {
    			Parent userPage = FXMLLoader.load(Photos.class.getResource("/view/userPage.fxml"));
		    	FXMLLoader upl = new FXMLLoader(Photos.class.getResource("/view/userPage.fxml"));
				Scene scene = type == 'a' ? new Scene(adminPage) : new Scene(userPage);
				adminPageController controller = type == 'a' ? apl.getController() : upl.getController();
				
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
				window.setScene(scene);
				window.show();
				
				window.setOnCloseRequest(new EventHandler<WindowEvent>() {
		            @Override
		            public void handle(WindowEvent e) {
		            	try{
		            		controller.shutDown();
		            	}
		            	catch(Exception ee) {
		            		
		            	}
		                Platform.exit();
		                System.exit(0);
		            }
		        });
    		} else {
    			Alert alert = new Alert(AlertType.ERROR, "Account does not exist", ButtonType.OK);
    			alert.showAndWait();
    		}
    	} catch (Exception e) {
        	Alert alert = new Alert(AlertType.ERROR, "Account does not exist", ButtonType.OK);
        	alert.showAndWait();

    	}
    	
//    	try {
//			Scanner input = new Scanner(loader);
//			boolean found = false;
//			while(input.hasNext()) {
//				String parse = input.nextLine();
//				int index1 = parse.indexOf("-USERNAME");
//				int index2 = parse.indexOf("-PASSWORD");
//				int index3 = parse.indexOf("-TYPE");
//				
//				String un = parse.substring(0, index1);
//				String pw = parse.substring(index1 + 10, index2); 
//				char type = parse.charAt(index3 - 1);
//				
//				if(username.getText().equals(un) && password.getText().equals(pw)) {
//					for(int i = 0; i < adminPageController.obsAccountList.size(); i++) {
//						if (adminPageController.obsAccountList.get(i).username.contentEquals(un)) {
//							userPageController.currentUser = adminPageController.obsAccountList.get(i);
//						}
//					}
//					
//					Parent userPage = FXMLLoader.load(Photos.class.getResource("/view/userPage.fxml"));
//			    	FXMLLoader upl = new FXMLLoader(Photos.class.getResource("/view/userPage.fxml"));
//					Scene scene = type == 'a' ? new Scene(adminPage) : new Scene(userPage);
//					adminPageController controller = type == 'a' ? apl.getController() : upl.getController();
//					
//					Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//					window.setScene(scene);
//					window.show();
//					
//					
//					found = true;
//					
//			        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			            @Override
//			            public void handle(WindowEvent e) {
//			            	try{
//			            		controller.shutDown();
//			            	}
//			            	catch(Exception ee) {
//			            		
//			            	}
//			                Platform.exit();
//			                System.exit(0);
//			            }
//			        });
//				
//				}
//				
//
//			}
//    		
//    		
//			if(!found) {
//				Alert alert = new Alert(AlertType.ERROR, "Account does not exist", ButtonType.OK);
//				alert.showAndWait();
//				input.close();
//			}
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//    		Alert alert = new Alert(AlertType.ERROR, "Account does not exist", ButtonType.OK);
//    		alert.showAndWait();
//	
//		}
    	
    	
    	
    }
    
    public static AccountList readFile(String s) throws FileNotFoundException, IOException, ClassNotFoundException {
    	try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(s)))) {
			return (AccountList)ois.readObject();
		}
    	
    }
}