package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import object.Account;
import object.AccountList;
import object.Album;

public class adminPageController {
	
    static ObservableList<Account> obsAccountList = FXCollections.observableArrayList();   
	File loader = new File("accounts.txt");
	
	AccountList accountList;

    @FXML
	private ListView<Account> userList;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button addUserButton;

    @FXML
    private Button removeUserButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text statusText;

    @FXML
    private Button logoutButton;

    public void start() {
//    	userStatus.setText("Photo Library: " + currentUser.username);
    }
    
    @FXML
    void addClick(ActionEvent event) {
    	setStatus("adding");
    	userList.setDisable(true);
    	clearDetails();

    }

    @FXML
    void cancelClick(ActionEvent event) {
    	setStatus("viewing");
    	userList.setDisable(false);
    	if(!obsAccountList.isEmpty())
    		showAccount();

    }

    @FXML
    void confirmClick(ActionEvent event) {
    	
    	if (statusText.getText().contentEquals("Currently adding account details")) {
    		String un = username.getText();
	    	String pw = password.getText();
	    	Account add = new Account(un, pw, 'b');
	    	boolean flag = true;
	    	
	    	for(Account a : obsAccountList) {
	    		if(a.username.equals(un.toLowerCase())) {
		    		Alert alert = new Alert(AlertType.ERROR, "Duplicate username is not allowed", ButtonType.OK);
		    		alert.showAndWait();
		    		flag = false;
	    		}
	    	}
	    	
	    	if(un.length() == 0 || pw.length() == 0) {
	    		Alert alert = new Alert(AlertType.ERROR, "Please enter username and password for new account", ButtonType.OK);
	    		alert.showAndWait();
	    	}
	    	else if(flag) {
		    	obsAccountList.add(add);
		    	userList.setDisable(false);
		    	userList.getSelectionModel().select(add);
		    	setStatus("viewing");
		    	File temp = new File("users/" + add.username);
		    	temp.mkdir();
	    	}
	    	
    	}
    	
	    if(statusText.getText().contentEquals("Confirm delete?")) {
	    	userList.setDisable(false);
    		Account selectedAccount = userList.getSelectionModel().getSelectedItem();
    		if(selectedAccount.username.equals("admin")) {
	    		Alert alert = new Alert(AlertType.ERROR, "Can't delete admin account", ButtonType.OK);
	    		alert.showAndWait();
	    		return;
    		}
    		File temp = new File("users/" + selectedAccount.username);
    		if(temp.list().length > 0) {
    			for(File f : temp.listFiles()) {
    				f.delete();
    			}
    		}
    		temp.delete();
    		userList.getSelectionModel().selectNext();
	    	if(userList.getSelectionModel().isEmpty()) {
	    		userList.getSelectionModel().select(selectedAccount);;
	    	}
	    	obsAccountList.remove(selectedAccount);
	    	if(!userList.getSelectionModel().isEmpty())
	    		showAccount();
	    	else
	    		clearDetails();
	    	
	    	setStatus("viewing");
    	}

    }

    @FXML
    void logoutClick(ActionEvent event) throws IOException {
    	accountList = new AccountList();
    	for (int i = 0; i < obsAccountList.size(); i++) {
    		accountList.passwords.add(obsAccountList.get(i).password);
    		accountList.usernames.add(obsAccountList.get(i).username);
    		accountList.type.add(obsAccountList.get(i).type);
    	}

    	writeToFile(accountList);
    	
    	Parent loginPage = FXMLLoader.load(Photos.class.getResource("/view/loginPage.fxml"));
		Scene scene = new Scene(loginPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();

    }

    @FXML
    void removeClick(ActionEvent event) {
    	setStatus("deleting");
    	userList.setDisable(true);
    }
    
    public void initialize(){
    	if (obsAccountList.size() == 0) {
    		try {
    			accountList = readFile("users/accountlist.bin");
    			for(int i = 0; i < accountList.usernames.size(); i++) {
    				obsAccountList.add(new Account(accountList.usernames.get(i), accountList.passwords.get(i), accountList.type.get(i)));
    			}
    			
    		} catch (Exception e) {
    			
    		}
    	}	
    	
	    
	   	userList.setItems(obsAccountList);
	   	try{
	       	if(!obsAccountList.isEmpty()) {
	       		userList.getSelectionModel().select(0);
	       		showAccount();
	       	}
	   	}
	   	catch(Exception e) {
	   		
	    }
    	

    }
    
    public void shutDown() throws FileNotFoundException, IOException {
    	accountList = new AccountList();
    	for (int i = 0; i < obsAccountList.size(); i++) {
    		accountList.passwords.add(obsAccountList.get(i).password);
    		accountList.usernames.add(obsAccountList.get(i).username);
    		accountList.type.add(obsAccountList.get(i).type);
    	}
    	writeToFile(accountList);    	
    }
    
    void showAccount() {
    	Account selectedAccount = userList.getSelectionModel().getSelectedItem();
    	username.setText(selectedAccount.username);
    	password.setText(selectedAccount.password);
    }
    
    void setVisibility(boolean visible) {
    	confirmButton.setVisible(visible);
    	cancelButton.setVisible(visible);
    }
    
    void setStatus(String status) {
    	if(!status.contentEquals("deleting")) {
    		statusText.setText("Currently " + status + " account details");
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
    	if (status.contentEquals("adding")) {
    		setVisibility(true);
    		setEditable(true);
    	}
    }
    
    void setEditable(boolean edit) {
    	username.setEditable(edit);
    	password.setEditable(edit);

    }
    
    void clearDetails() {
    	username.setText("");
    	password.setText("");
    }
    
    @FXML
    void onSelectAccount(MouseEvent event) {
    	if (userList.getSelectionModel().getSelectedItem() != null) 
    		showAccount();
    	
    }
    
    public static void writeToFile(AccountList a) throws FileNotFoundException, IOException {
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users/accountlist.bin"))) {
			oos.writeObject(a);
		}
		
    }
    
    public static AccountList readFile(String s) throws FileNotFoundException, IOException, ClassNotFoundException {
    	try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(s)))) {
			return (AccountList)ois.readObject();
		}
    	
    }
    

}
