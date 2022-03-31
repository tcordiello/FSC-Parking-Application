package fsc.parking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Scanner;

import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Combo;

@SuppressWarnings("unused")

public class Subscribe {
	private Shell shell;
	private String username;
	private Text textUsername;
	private Text textCCNumber;
	private Text textName;
	private Text textSecurityCode;
	private Text textFacultyID;
	private String Name;
	private String userType;


	public void setUsername(String name) {
		username = name;
	}
	
	public void setUserType(String type) {
		userType = type;
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		Image image = new Image(display, "assets\\fsc-icon.png");
		shell.setImage(image);
	    
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    // Centers shell when the program is launched.
	    shell.setLocation(x, y);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		Display display = Display.getDefault();
		// Makes shell unable to be changed in height / width. Remove arguments to enable resizing.
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shell.setTouchEnabled(true);
		shell.setSize(710, 622);
		shell.setText("FSC Parking");
		shell.setFocus();
		
		Color color = new Color(display, 0, 100, 86);
		
		Label lblSubscribe = new Label(shell, SWT.NONE);
		lblSubscribe.setText("Subscribe");
		lblSubscribe.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblSubscribe.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblSubscribe.setBackground(SWTResourceManager.getColor(0, 100, 86));
		lblSubscribe.setAlignment(SWT.CENTER);
		lblSubscribe.setBounds(0, 0, 704, 37);
		
		/**
		 * Cancel button
		 */
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
	        	Home home = new Home();
	        	home.setUsername(textUsername.getText());
	        	home.setUserType(userType);
	            shell.close();
	            home.open();			
	        }
		});
		btnCancel.setText("Cancel");
		btnCancel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnCancel.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnCancel.setBounds(417, 517, 261, 60);
		
		textUsername = new Text(shell, SWT.BORDER);
		textUsername.setText(username);
		textUsername.setEditable(false);
		textUsername.setBounds(33, 91, 167, 26);
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(23, 65, 70, 20);
		lblUsername.setText("Username:");
		
		Label lblCreditCardIssuer = new Label(shell, SWT.NONE);
		lblCreditCardIssuer.setBounds(23, 183, 146, 20);
		lblCreditCardIssuer.setText("Credit Card Issuer:");
		//lblCreditCardIssuer.setVisible(false);
		
		textCCNumber = new Text(shell, SWT.BORDER);
		textCCNumber.setBounds(33, 282, 207, 26);
		//textCCNumber.setVisible(false);

		Label lblCardNumber = new Label(shell, SWT.NONE);
		lblCardNumber.setBounds(23, 256, 111, 20);
		lblCardNumber.setText("Card Number:");
		//lblCardNumber.setVisible(false);

		Label lblNameOnCard = new Label(shell, SWT.NONE);
		lblNameOnCard.setBounds(23, 320, 121, 20);
		lblNameOnCard.setText("Name on Card:");
		//lblNameOnCard.setVisible(false);

		textName = new Text(shell, SWT.BORDER);
		textName.setBounds(33, 346, 207, 26);
		//textName.setVisible(false);

		Label lblSecurityCode = new Label(shell, SWT.NONE);
		lblSecurityCode.setBounds(23, 384, 121, 20);
		lblSecurityCode.setText("Security Code:");
		//lblSecurityCode.setVisible(false);

		textSecurityCode = new Text(shell, SWT.BORDER);
		textSecurityCode.setBounds(33, 410, 78, 26);
		//textSecurityCode.setVisible(false);
		
		Label lblEnterFacultyID = new Label(shell, SWT.NONE);
		lblEnterFacultyID.setBounds(23, 123, 179, 20);
		lblEnterFacultyID.setText("Enter your Faculty ID:");
		//lblEnterFacultyID.setVisible(false);
		
		textFacultyID = new Text(shell, SWT.BORDER);
		textFacultyID.setBounds(33, 149, 179, 26);
		//textFacultyID.setVisible(false);
		
		/**
		 * Faculty Confirm Subscription
		 */
		Button buttonConfirmSubscription = new Button(shell, SWT.NONE);
		buttonConfirmSubscription.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String connectionString =
						"jdbc:sqlserver://parking-server.database.windows.net:1433;"
						+ "database=ParkingReservation;"
						+ "user=Group4@parking-server;"
						+ "password=SeniorProject2019**;"
						+ "encrypt=true;"
						+ "trustServerCertificate=false;"
						+ "hostNameInCertificate=*.database.windows.net;"
						+ "loginTimeout=30;";

					// Declare the JDBC objects.
					Connection connection = null;

				try {
					connection = DriverManager.getConnection(connectionString);
					String sql = "INSERT INTO Permit(userName, subscriptionType, expiration, cost) VALUES (?, 'Faculty', '2021-08-30', '0.00')";
					String sql1 = "UPDATE User_Acc SET hasPermit = 1 WHERE userName = ?";

				String facultyID = textFacultyID.getText();
				if(facultyID.length() == 9 && facultyID.charAt(0) == 'R') {
					PreparedStatement pst = connection.prepareStatement(sql);
					pst.setString(1, username);
					pst.executeUpdate();
					PreparedStatement st = connection.prepareStatement(sql1);
					st.setString(1, username);
					st.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Subscription Activated.", "Success!", JOptionPane.INFORMATION_MESSAGE);
		        	Home home = new Home();
		        	home.setUsername(textUsername.getText());
		        	home.setSubscribed(true);
		        	home.setUserType(userType);
		            shell.close();
		            home.open();
				}
				else {
					JOptionPane.showMessageDialog(null, "Please enter a valid Faculty RAM ID\nYour RAM ID should be 9 "
							+ "characters long, and start with a capital 'R'", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
				connection.close();
			}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
			}
		});
		buttonConfirmSubscription.setText("Confirm Subscription");
		buttonConfirmSubscription.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonConfirmSubscription.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		buttonConfirmSubscription.setBackground(SWTResourceManager.getColor(0, 100, 86));
		buttonConfirmSubscription.setBounds(417, 451, 261, 60);
		//buttonConfirmSubscription.setVisible(false);

		Combo comboCCIssuer = new Combo(shell, SWT.NONE);
		comboCCIssuer.setItems(new String[] {"Visa", "Mastercard", "American Express"});
		comboCCIssuer.setBounds(33, 209, 207, 28);
		comboCCIssuer.setText("Select Credit Card Issuer...");
		//comboCCIssuer.setVisible(false);
		
		/**
		 * Student Confirm Subscription
		 */
		Button buttonConfirmPurchase = new Button(shell, SWT.NONE);
		buttonConfirmPurchase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String connectionString =
						"jdbc:sqlserver://parking-server.database.windows.net:1433;"
						+ "database=ParkingReservation;"
						+ "user=Group4@parking-server;"
						+ "password=SeniorProject2019**;"
						+ "encrypt=true;"
						+ "trustServerCertificate=false;"
						+ "hostNameInCertificate=*.database.windows.net;"
						+ "loginTimeout=30;";

					// Declare the JDBC objects.
					Connection connection = null;

				try {
					connection = DriverManager.getConnection(connectionString);

					boolean ccTypeSelected = false;
					if (comboCCIssuer.getText() != "Select Credit Card Issuer...") {
						ccTypeSelected = true;
					}
					String ccNumber = textCCNumber.getText();
					String ccSecNum = textSecurityCode.getText();
					String name = textName.getText();
					
					if(ccNumber.length() > 0 && 
							ccSecNum.length() > 0 && 
							name.length() > 0 && 
							ccTypeSelected == true) 
					{
						String sql = "INSERT INTO Permit(userName, subscriptionType, expiration, cost) VALUES (?, 'Student', '2019-08-30', '40.00')";
						String sql1 = "UPDATE User_Acc SET hasPermit = 1 WHERE userName = ?";
						PreparedStatement pst = connection.prepareStatement(sql);
						pst.setString(1, username);
						pst.executeUpdate();
						PreparedStatement st = connection.prepareStatement(sql1);
						st.setString(1, username);
						st.executeUpdate();
						
						JOptionPane.showMessageDialog(null, "Subscription Activated.", "Success!", JOptionPane.INFORMATION_MESSAGE);
					
			        	Home home = new Home();
			        	home.setUserType(userType);
			        	home.setUsername(textUsername.getText());
			            shell.close();
			            home.open();
					}
					else {
						JOptionPane.showMessageDialog(null, "Please enter valid Credit Card info.", "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					connection.close();
			}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
			}
		});
		buttonConfirmPurchase.setText("Confirm Purchase");
		buttonConfirmPurchase.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonConfirmPurchase.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		buttonConfirmPurchase.setBackground(SWTResourceManager.getColor(0, 100, 86));
		buttonConfirmPurchase.setBounds(417, 451, 261, 60);
		

		/**
		 * Student visibility
		 */
		if(userType == "Student") {
           	//student true
           	textCCNumber.setVisible(true);
           	textName.setVisible(true);
           	textSecurityCode.setVisible(true);
           	lblCreditCardIssuer.setVisible(true);
           	comboCCIssuer.setVisible(true);
           	lblCardNumber.setVisible(true);
           	lblSecurityCode.setVisible(true);
           	lblNameOnCard.setVisible(true);
           	buttonConfirmPurchase.setVisible(true);

               //student false
           	buttonConfirmSubscription.setVisible(false);
           	lblEnterFacultyID.setVisible(false);
           	textFacultyID.setVisible(false);
            	
        }           
		
		/**
		 * Faculty visibility
		 */

		if(userType == "Faculty") {
            	//faculty true
            	buttonConfirmSubscription.setVisible(true);
            	lblEnterFacultyID.setVisible(true);
            	textFacultyID.setVisible(true);
            	
            	//faculty false
            	buttonConfirmPurchase.setVisible(false);
            	textCCNumber.setVisible(false);
            	textName.setVisible(false);
            	textSecurityCode.setVisible(false);
            	lblCreditCardIssuer.setVisible(false);
            	comboCCIssuer.setVisible(false);
            	lblCardNumber.setVisible(false);
            	lblSecurityCode.setVisible(false);
            	lblNameOnCard.setVisible(false);
            	
       	}             
	}
}