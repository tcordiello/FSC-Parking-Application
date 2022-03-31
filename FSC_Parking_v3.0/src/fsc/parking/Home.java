package fsc.parking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

@SuppressWarnings("unused")

/**
 * @author 
 * @version 1.0.0
 */
public class Home {

	private Shell shell;
	private String username;
	private boolean subscribed = false;
	private LocalDate expDateStudent = LocalDate.parse("2019-08-31");
	private LocalDate expDateStaff = LocalDate.parse("2020-08-31");
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL/dd/yyyy");
	private String formattedStringExpStudent = expDateStudent.format(formatter);
	private String formattedStringExpStaff = expDateStaff.format(formatter);
	private LocalDate currentDate = LocalDate.now();
	private String formattedStringCurrent = currentDate.format(formatter);
	private String userType = null;
	private char userTypeFirstChar;
	private boolean spotReserved = false;
	private String reservedSpotID = null;	
	private boolean spotAVCheck = true;
	private java.sql.Time resLeaveTime;

	public void setUsername(String usernameText){
		username = usernameText;
	}
	public void setSubscribed(boolean tf){
		subscribed = tf;
	}
	public void setUserType(String type) {
		userType = type;
	}
	public void setSpotReserved(boolean tf) {
		spotReserved = tf;
	}
	public void setReservedSpotID(String id) {
		reservedSpotID = id;
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
		shell.setSize(728, 683);
		shell.setText("FSC Parking");
		shell.setFocus();
		
		Color color = new Color(display, 0, 100, 86);
		Color active = new Color(display, 0, 200, 0);
		Color inactive = new Color(display, 200, 0, 0);

		Label lblWelcomeBanner = new Label(shell, SWT.NONE);
		lblWelcomeBanner.setBounds(0, 0, 722, 37);
		lblWelcomeBanner.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblWelcomeBanner.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblWelcomeBanner.setBackground(color);
		lblWelcomeBanner.setAlignment(SWT.CENTER);
		lblWelcomeBanner.setText("Welcome, " + username + "!");
		
		
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
		Statement stmt = null;
		String query_subscribed = 
				"SELECT hasPermit FROM User_Acc WHERE userName='"+ username +"'";
		    
		try {
			connection = DriverManager.getConnection(connectionString);
		    stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery(query_subscribed);
		    while(rs.next()) {
		    	boolean subscribedResult = rs.getBoolean("hasPermit");
		    	subscribed = subscribedResult;
		    }

		stmt.close();
		}
		catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
		    
		String queryType = 
				"SELECT userType FROM User_Acc WHERE userName='"+ username +"'";
		    
		try {
			connection = DriverManager.getConnection(connectionString);
		    stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery(queryType);
		    while(rs.next()) {
		        String userTypeResult = rs.getString("userType");
		        userTypeFirstChar = userTypeResult.charAt(0);
		        if(userTypeFirstChar == 'S') {
		            userType = "Student";
		        }
		        else {
		           	userType = "Faculty";
		        }
		    }

			stmt.close();
		}
		catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
		    
		Label lblReservedSpot = new Label(shell, SWT.NONE);
		if(spotReserved == true) {
			if(reservedSpotID.charAt(0) == 'S') {
				lblReservedSpot.setText("Student Lot 18, " + reservedSpotID + " (Staff)");
			}
			else {
				lblReservedSpot.setText("Student Lot 18, " + reservedSpotID);
			}
		}
		else {
			lblReservedSpot.setText("None");
		}
		lblReservedSpot.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lblReservedSpot.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.BOLD));
		lblReservedSpot.setBounds(369, 129, 290, 30);
		
		/**
		 * Help Button
		 */
		Button btnHelp = new Button(shell, SWT.NONE);
		btnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Help help = new Help();
				help.open();
			}
		});
		btnHelp.setBounds(10,512, 235, 60);
		btnHelp.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnHelp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnHelp.setBackground(color);
		btnHelp.setText("Help");
		
		/**
		 * Sign out Button
		 */
		Button btnSignOut = new Button(shell, SWT.NONE);
		btnSignOut.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		          switch (e.type) {
		          case SWT.Selection:
		            shell.close();
		            Login login = new Login();
		            login.open();
		            break;
		          }
		       }
		 });
		btnSignOut.setBounds(10, 578, 235, 60);
		btnSignOut.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnSignOut.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnSignOut.setBackground(color);
		btnSignOut.setText("Sign out");
		
		/**
		 * Register Vehicle Button
		 */
		Button btnRegisterVehicle = new Button(shell, SWT.NONE);
		btnRegisterVehicle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				RegisterVehicle regVeh = new RegisterVehicle();
				regVeh.setUsername(username);
				regVeh.open();
			}
		});
		btnRegisterVehicle.setText("Register Vehicle");
		btnRegisterVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnRegisterVehicle.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnRegisterVehicle.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnRegisterVehicle.setBounds(10, 380, 235, 60);
		
		/**
		 * Reserve Spot Button
		 */
		Button btnReserveSpot = new Button(shell, SWT.NONE);
		btnReserveSpot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					Connection conn5 = null;
					conn5 = DriverManager.getConnection(connectionString);
					String sql_GetUnavailable = "SELECT spotAV, spotID FROM Parking WHERE spotAV = 0";
					PreparedStatement st5 = conn5.prepareStatement(sql_GetUnavailable);
					ResultSet rs5 = st5.executeQuery();

					while(rs5.next()) {
						spotAVCheck = rs5.getBoolean("spotAV");
						reservedSpotID = rs5.getString("spotID");
						if(spotAVCheck == false) {
							try {
								String sql_CheckTime = "SELECT leaveT FROM Reservation WHERE spotID = '" + reservedSpotID + "'";
								PreparedStatement st6 = conn5.prepareStatement(sql_CheckTime);
								ResultSet rs6 = st6.executeQuery();

								while(rs6.next()) {
									resLeaveTime = rs6.getTime("leaveT");
									if(resLeaveTime.getTime() < java.sql.Time.valueOf(LocalTime.now()).getTime()) {
										String sql_UpdateTime = "UPDATE Parking SET spotAV = 1 WHERE spotID = '" + reservedSpotID + "'";
										PreparedStatement prepStat = conn5.prepareStatement(sql_UpdateTime);
										prepStat.execute();
										spotReserved = false;
										lblReservedSpot.setText("None");
									}
								}
							}
							catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
						}
					}
					conn5.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
				
				if(spotReserved == false) {
					SelectVehicle selVeh = new SelectVehicle();	//Select Vehicle == Reserve Spot
					selVeh.setUsername(username);
					selVeh.setUserType(userType);
					shell.close();
					selVeh.open();
				}
				else {
					JOptionPane.showMessageDialog(null, "Please cancel current reservation before making a new one.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnReserveSpot.setText("Reserve Spot");
		btnReserveSpot.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnReserveSpot.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnReserveSpot.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnReserveSpot.setBounds(10, 248, 235, 60);
		if(subscribed == false) {
			btnReserveSpot.setEnabled(false);
		}
		
		/**
		 * Subscribe Button
		 */
		Button btnSubscribe = new Button(shell, SWT.NONE);
		btnSubscribe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Subscribe subscribe = new Subscribe();
				subscribe.setUsername(username);
				subscribe.setUserType(userType);
				shell.close();
				subscribe.open();
			}
		});
		btnSubscribe.setText("Subscribe");
		btnSubscribe.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnSubscribe.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnSubscribe.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnSubscribe.setBounds(10, 182, 235, 60);
		if(subscribed == true) {
			btnSubscribe.setEnabled(false);
		}
		
		/**
		 * Cancel Reservation button
		 */
		Button buttonCancelReservation = new Button(shell, SWT.NONE);
		buttonCancelReservation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					Connection conn5 = null;
					conn5 = DriverManager.getConnection(connectionString);
					String sql_GetUnavailable = "SELECT spotAV, spotID FROM Parking WHERE spotAV = 0";
					PreparedStatement st5 = conn5.prepareStatement(sql_GetUnavailable);
					ResultSet rs5 = st5.executeQuery();

					while(rs5.next()) {
						spotAVCheck = rs5.getBoolean("spotAV");
						reservedSpotID = rs5.getString("spotID");
						if(spotAVCheck == false) {
							try {
								String sql_CheckTime = "SELECT leaveT FROM Reservation WHERE spotID = '" + reservedSpotID + "'";
								PreparedStatement st6 = conn5.prepareStatement(sql_CheckTime);
								ResultSet rs6 = st6.executeQuery();

								while(rs6.next()) {
									resLeaveTime = rs6.getTime("leaveT");
									if(resLeaveTime.getTime() < java.sql.Time.valueOf(LocalTime.now()).getTime()) {
										String sql_UpdateTime = "UPDATE Parking SET spotAV = 1 WHERE spotID = '" + reservedSpotID + "'";
										PreparedStatement prepStat = conn5.prepareStatement(sql_UpdateTime);
										prepStat.execute();
										spotReserved = false;
										lblReservedSpot.setText("None");
									}
								}
							}
							catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
						}
					}
					conn5.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
				
				if(spotReserved == false) {
					JOptionPane.showMessageDialog(null, "No reservation found.", "Error", JOptionPane.ERROR_MESSAGE);
	        	}
	        	else {
	        		SelectVehicle sv = new SelectVehicle();
			        sv.cancelReservation(reservedSpotID, spotReserved);
			        spotReserved = false;
					lblReservedSpot.setText("None");
			    }
			}
		});
		if(subscribed == false) {
			buttonCancelReservation.setEnabled(false);
		}
		buttonCancelReservation.setText("Cancel Reservation");
		buttonCancelReservation.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonCancelReservation.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		buttonCancelReservation.setBackground(SWTResourceManager.getColor(0, 100, 86));
		buttonCancelReservation.setBounds(10, 314, 235, 60);
		
		/**
		 * Delete Vehicle Button
		 */
		Button buttonDeleteVehicle = new Button(shell, SWT.NONE);
		buttonDeleteVehicle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					Connection conn5 = null;
					conn5 = DriverManager.getConnection(connectionString);
					String sql_GetUnavailable = "SELECT spotAV, spotID FROM Parking WHERE spotAV = 0";
					PreparedStatement st5 = conn5.prepareStatement(sql_GetUnavailable);
					ResultSet rs5 = st5.executeQuery();

					while(rs5.next()) {
						spotAVCheck = rs5.getBoolean("spotAV");
						reservedSpotID = rs5.getString("spotID");
						if(spotAVCheck == false) {
							try {
								String sql_CheckTime = "SELECT leaveT FROM Reservation WHERE spotID = '" + reservedSpotID + "'";
								PreparedStatement st6 = conn5.prepareStatement(sql_CheckTime);
								ResultSet rs6 = st6.executeQuery();

								while(rs6.next()) {
									resLeaveTime = rs6.getTime("leaveT");
									if(resLeaveTime.getTime() < java.sql.Time.valueOf(LocalTime.now()).getTime()) {
										String sql_UpdateTime = "UPDATE Parking SET spotAV = 1 WHERE spotID = '" + reservedSpotID + "'";
										PreparedStatement prepStat = conn5.prepareStatement(sql_UpdateTime);
										prepStat.execute();
										spotReserved = false;
										lblReservedSpot.setText("None");
									}
								}
							}
							catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
						}
					}
					conn5.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
				
				if(spotReserved == true) {
					JOptionPane.showMessageDialog(null, "Cannot delete vehicles while having an active reservation.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					DeleteVehicle delVeh = new DeleteVehicle();
					delVeh.setUsername(username);
					delVeh.open();
				}
			}
		});
		buttonDeleteVehicle.setText("Delete Vehicle");
		buttonDeleteVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonDeleteVehicle.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		buttonDeleteVehicle.setBackground(SWTResourceManager.getColor(0, 100, 86));
		buttonDeleteVehicle.setBounds(10, 446, 235, 60);
		
		/**
		 * Refresh Button
		 */
		Button btnRefresh = new Button(shell, SWT.NONE);
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				currentDate = LocalDate.now();
				try {
					Connection conn5 = null;
					conn5 = DriverManager.getConnection(connectionString);
					String sql_GetUnavailable = "SELECT spotAV, spotID FROM Parking WHERE spotAV = 0";
					PreparedStatement st5 = conn5.prepareStatement(sql_GetUnavailable);
					ResultSet rs5 = st5.executeQuery();

					while(rs5.next()) {
						spotAVCheck = rs5.getBoolean("spotAV");
						reservedSpotID = rs5.getString("spotID");
						if(spotAVCheck == false) {
							try {
								String sql_CheckTime = "SELECT leaveT FROM Reservation WHERE spotID = '" + reservedSpotID + "'";
								PreparedStatement st6 = conn5.prepareStatement(sql_CheckTime);
								ResultSet rs6 = st6.executeQuery();

								while(rs6.next()) {
									resLeaveTime = rs6.getTime("leaveT");
									if(resLeaveTime.getTime() < java.sql.Time.valueOf(LocalTime.now()).getTime()) {
										String sql_UpdateTime = "UPDATE Parking SET spotAV = 1 WHERE spotID = '" + reservedSpotID + "'";
										PreparedStatement prepStat = conn5.prepareStatement(sql_UpdateTime);
										prepStat.execute();
										spotReserved = false;
										lblReservedSpot.setText("None");
									}
								}
							}
							catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
						}
					}
					conn5.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
			}
		});
		btnRefresh.setText("Refresh");
		btnRefresh.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnRefresh.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnRefresh.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnRefresh.setBounds(591, 93, 68, 30);
		
		Label FSC_Seal_Image = new Label(shell, SWT.NONE);
		FSC_Seal_Image.setAlignment(SWT.CENTER);
		FSC_Seal_Image.setImage(SWTResourceManager.getImage("assets\\SUNY_Farmingdale_seal.png"));
		FSC_Seal_Image.setBounds(275, 192, 422, 406);
		
		Label labelUserType = new Label(shell, SWT.NONE);
		labelUserType.setText("User Type:");
		labelUserType.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		labelUserType.setBounds(359, 56, 98, 28);
		
		Label UserType = new Label(shell, SWT.NONE);
		UserType.setText(userType);
		UserType.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.BOLD));
		UserType.setBounds(467, 55, 156, 30);
		UserType.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));

		if(subscribed == true) {
			Label lblExpDate = new Label(shell, SWT.NONE);
			lblExpDate.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
			lblExpDate.setBounds(10, 134, 179, 33);
			lblExpDate.setText("Expiration Date:");
			
			Label lblActive = new Label(shell, SWT.NONE);
			lblActive.setText("ACTIVE");
			lblActive.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.BOLD));
			lblActive.setBounds(213, 56, 140, 28);
			lblActive.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
						
			if(userType == "Faculty") {
				Label DateOfExp = new Label(shell, SWT.NONE);
				DateOfExp.setText(formattedStringExpStaff);
				DateOfExp.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
				DateOfExp.setBounds(213, 134, 125, 33);
				DateOfExp.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));

			}
			else if (userType == "Student"){
				Label DateOfExp = new Label(shell, SWT.NONE);
				DateOfExp.setText(formattedStringExpStudent);
				DateOfExp.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
				DateOfExp.setBounds(213, 134, 125, 33);
				DateOfExp.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));

			}
		}
		else {			
			Label lblInactive = new Label(shell, SWT.NONE);
			lblInactive.setText("INACTIVE");
			lblInactive.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
			lblInactive.setBounds(213, 56, 131, 28);
			lblInactive.setForeground(display.getSystemColor(SWT.COLOR_RED));
		}
			
		Label lblSubscriptionStatus = new Label(shell, SWT.NONE);
		lblSubscriptionStatus.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblSubscriptionStatus.setBounds(10, 56, 197, 33);
		lblSubscriptionStatus.setText("Subscription Status:");
		
		Label labelCurrentDate = new Label(shell, SWT.NONE);
		labelCurrentDate.setText("Today's Date:");
		labelCurrentDate.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		labelCurrentDate.setBounds(10, 95, 156, 33);
		
		Label CurrentDate = new Label(shell, SWT.NONE);
		CurrentDate.setText(formattedStringCurrent);
		CurrentDate.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		CurrentDate.setBounds(213, 95, 125, 33);
		
		Label lblActiveReservation = new Label(shell, SWT.NONE);
		lblActiveReservation.setText("Active Reservation:");
		lblActiveReservation.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblActiveReservation.setBounds(359, 95, 197, 28);

		
	}
}
