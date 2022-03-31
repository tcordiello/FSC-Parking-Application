package fsc.parking;

import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.CCombo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
@SuppressWarnings("unused")

public class SelectVehicle {
	private Shell shell;
	private String username;
	private DateTime arrivalTime;
	private DateTime departureTime;
	private Combo comboBoxSpotNum;
	private Combo comboBoxRow;
	private boolean timesValid = false;
	private int arrTimeHour;
	private int depTimeHour;
	private int arrTimeMinute;
	private int depTimeMinute;
	private boolean row_spot_valid = false;
	private String selectedRow;
	private String selectedSpotNumberString;
	private int selectedSpotNumber;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL/dd/yyyy");
	private LocalDate currentDate = LocalDate.now();
	private String formattedStringCurrent = currentDate.format(formatter);
	private int numOfVehicles = 0;
	private String[] vehicles;
	private String userType;
	private int[] vehicleIdList;
	private int selectedVehicleId;
	private String spotID;
	private java.sql.Time arrival;
	private java.sql.Time departure;
	private java.sql.Date date;
	private boolean spotAVCheck = true;
	private boolean spotAvailable;
	private java.sql.Time leaveTime;
	private boolean spotReserved;
	
	
	public void setUserType(String type) {
		userType = type;
	}
	public void setUsername(String name) {
		username = name;
	}
	public void setSpotReserved(boolean tf) {
		spotReserved = tf;
	}
	public void setReservedSpotID(String id) {
		spotID = id;
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
	protected void createContents(){
		Display display = Display.getDefault();
		// Makes shell unable to be changed in height / width. Remove arguments to enable resizing.
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shell.setTouchEnabled(true);
		shell.setSize(721, 817);
		shell.setText("FSC Parking");
		shell.setFocus();
		
		Color color = new Color(display, 0, 100, 86);
		
		if(spotReserved != true) {
			spotReserved = false;
		}
		
		/**
		 * Get Number of vehicles
		 */
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
		    String queryCount = 
		    		"SELECT COUNT(userName) AS AMOUNT FROM Vehicle_Reg WHERE userName='"+ username +"'";
		    
		    try {
				connection = DriverManager.getConnection(connectionString);
		        stmt = connection.createStatement();
		        ResultSet num = stmt.executeQuery(queryCount);
		        while(num.next()) {
		        	numOfVehicles =  ((Number) num.getObject(1)).intValue();
		        }

				if(numOfVehicles > 0) {
					vehicles = new String[numOfVehicles];
					vehicleIdList = new int[numOfVehicles];
					int i = 0;
					String queryVehicles = 
							"SELECT vehicleID, vehicleColor, vehicleMake, vehicleModel FROM Vehicle_Reg  WHERE userName ='"+ username +"'";
					ResultSet rs = stmt.executeQuery(queryVehicles);
			        while (rs.next()) {
			        	int vId = rs.getInt("vehicleID");
			            String vColor = rs.getString("vehicleColor");
			            String vMake = rs.getString("vehicleMake");
			            String vModel = rs.getString("vehicleModel");
			            String vehicle = vColor + " " + vMake + " " + vModel;
			            vehicles[i] = vehicle;
			            vehicleIdList[i] = vId;
			            i++;
			        }
		        }
				stmt.close();
		    }
		    catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
		
		
		
		Label lblMakeReservation = new Label(shell, SWT.NONE);
		lblMakeReservation.setText("Make Reservation");
		lblMakeReservation.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMakeReservation.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblMakeReservation.setBackground(SWTResourceManager.getColor(0, 100, 86));
		lblMakeReservation.setAlignment(SWT.CENTER);
		lblMakeReservation.setBounds(0, 0, 715, 37);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
	        	Home home = new Home();
	        	home.setUsername(username);
	        	home.setUserType(userType);
	        	home.setSpotReserved(spotReserved);
	        	if(spotReserved == true) {
		        	home.setReservedSpotID(spotID);
	        	}
	            shell.close();
	            home.open();			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnCancel.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnCancel.setBounds(486, 712, 219, 60);
		
		Label lblRegisteredVehicle = new Label(shell, SWT.NONE);
		lblRegisteredVehicle.setBounds(10, 103, 200, 20);
		lblRegisteredVehicle.setText("Select a Registered Vehicle");
		
		arrivalTime = new DateTime(shell, SWT.TIME | SWT.SHORT);
		arrivalTime.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		arrivalTime.setBounds(20, 410, 136, 30);
		
		departureTime = new DateTime(shell, SWT.TIME | SWT.SHORT);
		departureTime.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		departureTime.setBounds(20, 470, 136, 30);
		
		Label ArrivalLabel = new Label(shell, SWT.NONE);
		ArrivalLabel.setBounds(10, 384, 102, 20);
		ArrivalLabel.setText("Arrival Time");
		
		Label DepartureLabel= new Label(shell, SWT.NONE);
		DepartureLabel.setBounds(10, 446, 111, 20);
		DepartureLabel.setText("Departure Time");
		
		
		Label DateLabel = new Label(shell, SWT.NONE);
		DateLabel.setBounds(10, 322, 102, 20);
		DateLabel.setText("Arrival Date");
		
		Label lblSelectParkingSpot = new Label(shell, SWT.NONE);
		lblSelectParkingSpot.setBounds(10, 178, 79, 20);
		lblSelectParkingSpot.setText("Select Row");
		
		Label lblSelectSpotNumber = new Label(shell, SWT.NONE);
		lblSelectSpotNumber.setBounds(10, 238, 136, 20);
		lblSelectSpotNumber.setText("Select Spot Number");
		
		comboBoxRow = new Combo(shell, SWT.NONE);
		comboBoxRow.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		comboBoxRow.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		if(userType == "Student") {
			comboBoxRow.setItems(new String[] {"A", "B", "C", "D", "E", "F", "G"});
		}
		else {
			comboBoxRow.setItems(new String[] {"A", "B", "C", "D", "E", "F", "G", "STAFF"});
		}
		comboBoxRow.setBounds(20, 204, 175, 28);
		comboBoxRow.setText("Select Row...");
		
		comboBoxSpotNum = new Combo(shell, SWT.NONE);
		comboBoxSpotNum.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		comboBoxSpotNum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		comboBoxSpotNum.setItems(new String[] 
				{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"});
		comboBoxSpotNum.setBounds(20, 265, 175, 28);
		comboBoxSpotNum.setText("Select Spot Number...");
		
		
		Label lblParkingLotImage = new Label(shell, SWT.CENTER);
		lblParkingLotImage.setBounds(282, 71, 414, 631);
		lblParkingLotImage.setImage(SWTResourceManager.getImage("assets\\studentlot18map.jpg"));
		
		Combo comboSelectVehicle = new Combo(shell, SWT.NONE);
		comboSelectVehicle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		if(numOfVehicles != 0) {
			comboSelectVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
			comboSelectVehicle.setItems(vehicles);
			comboSelectVehicle.setText("Select Vehicle...");
		}
		else {
			comboSelectVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			comboSelectVehicle.setText("NO VEHICLES REGISTERED");
		}
		comboSelectVehicle.setBounds(20, 128, 175, 28);

		Button btnConfirm = new Button(shell, SWT.NONE);
		btnConfirm.addMouseListener(new MouseAdapter() {
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
						spotID = rs5.getString("spotID");
						if(spotAVCheck == false) {
							try {
								String sql_CheckTime = "SELECT leaveT FROM Reservation WHERE spotID = '" + spotID + "'";
								PreparedStatement st6 = conn5.prepareStatement(sql_CheckTime);
								ResultSet rs6 = st6.executeQuery();

								while(rs6.next()) {
									leaveTime = rs6.getTime("leaveT");
									if(leaveTime.getTime() < java.sql.Time.valueOf(LocalTime.now()).getTime()) {
										String sql_UpdateTime = "UPDATE Parking SET spotAV = 1 WHERE spotID = '" + spotID + "'";
										PreparedStatement prepStat = conn5.prepareStatement(sql_UpdateTime);
										prepStat.execute();
									}
								}
							}
							catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
						}
					}
					conn5.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
				
				arrTimeHour = arrivalTime.getHours();
				depTimeHour = departureTime.getHours();
				arrTimeMinute = arrivalTime.getMinutes();
				depTimeMinute = departureTime.getMinutes();
				if(arrTimeHour < depTimeHour || 
						(arrTimeHour == depTimeHour && arrTimeMinute < depTimeMinute)) {
					timesValid = true;
				}
				
				selectedRow = comboBoxRow.getText();
				if(selectedRow.equals("STAFF")) {
					selectedRow = "S";
				}
				
				if(comboBoxSpotNum.getSelectionIndex() != -1) {
					
					selectedSpotNumberString = comboBoxSpotNum.getText();
					selectedSpotNumber = Integer.parseInt(selectedSpotNumberString);
					
					switch(selectedRow) {
						case "A":{
							if(selectedSpotNumber <= 20) {
								row_spot_valid = true;
							}
							break;
						}
						case "B":{
							row_spot_valid = true;
							break;
						}
						case "C":{
							row_spot_valid = true;
							break;
						}
						case "D":{
							row_spot_valid = true;
							break;
						}
						case "E":{
							row_spot_valid = true;
							break;
						}
						case "F":{
							row_spot_valid = true;
							break;
						}
						case "G":{
							if(selectedSpotNumber <= 12) {
								row_spot_valid = true;
							}
							break;
						}
						case "S":{
							if(selectedSpotNumber <= 16 && userType != "Student") {
								row_spot_valid = true;
							}
							break;
						}
						default:{
							break;
						}
					}
				}
				
				spotID = selectedRow + selectedSpotNumber;

				try {
					Connection conn = null;
					conn = DriverManager.getConnection(connectionString);
					String sql_spotCheck = "SELECT spotAV FROM Parking WHERE spotID = '" + spotID + "'";
					PreparedStatement st2 = conn.prepareStatement(sql_spotCheck);
					ResultSet rs1 = st2.executeQuery();

					while(rs1.next()) {
						spotAVCheck = rs1.getBoolean("spotAV");
					}
					conn.close();
				}
					catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
				
				if (comboSelectVehicle.getSelectionIndex() != -1 &&
						timesValid == true &&
						row_spot_valid == true &&
						spotAVCheck == true) 
				{
					selectedVehicleId = vehicleIdList[comboSelectVehicle.getSelectionIndex()];
					arrival = java.sql.Time.valueOf(Integer.toString(arrTimeHour) + ":" + Integer.toString(arrTimeMinute) + ":" + Integer.toString(0));
					departure = java.sql.Time.valueOf(Integer.toString(depTimeHour) + ":" + Integer.toString(depTimeMinute) + ":" + Integer.toString(0));
					
					try {
						Connection conn2 = null;
						conn2 = DriverManager.getConnection(connectionString);
						
						String sql_reserve = "INSERT INTO Reservation(vehicleID, spotID, arrivalT, leaveT, rDate) VALUES (?, ?, ?, ?, ?)";
						PreparedStatement st = conn2.prepareStatement(sql_reserve);
	  					st.setInt(1, selectedVehicleId);
						st.setString(2, spotID);
						st.setTime(3, arrival);
						st.setTime(4, departure);
						st.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
						st.executeUpdate();
						
						String sql_update = "UPDATE Parking SET spotAV = 0  WHERE spotID = ?";
						PreparedStatement st1 = conn2.prepareStatement(sql_update);
						st1.setString(1,  spotID);
						st1.executeUpdate();
		                  
						conn2.close();
					}
					catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
					
					
	            	JOptionPane.showMessageDialog(null, "Reservation confirmed.", "Success!", JOptionPane.INFORMATION_MESSAGE);
		        	Home home = new Home();
		        	home.setUsername(username);
		        	home.setUserType(userType);
		        	home.setSpotReserved(true);
		        	home.setReservedSpotID(spotID);
		            shell.close();
		            home.open();
	            }
				else if(comboSelectVehicle.getSelectionIndex() == -1) {
	                JOptionPane.showMessageDialog(null, "Select your vehicle!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(row_spot_valid == false) {
	                JOptionPane.showMessageDialog(null, "Row and/or Spot Number are invalid!\r\nCheck the map for reference.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(timesValid == false) {
	                JOptionPane.showMessageDialog(null, "Departure time invalid!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(spotAvailable == false) {
	                JOptionPane.showMessageDialog(null, "Spot is already taken.\r\nPlease choose a different spot.", "Error", JOptionPane.ERROR_MESSAGE);
				}
	            else
	            {
	                // Message box that is displayed when fields are invalid.
	                JOptionPane.showMessageDialog(null, "Invalid input!\r\nNot sure what you did, but you broke the program, good job.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
			}
		});
		btnConfirm.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnConfirm.setBounds(10, 712, 211, 60);
		btnConfirm.setText("Confirm");
		btnConfirm.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnConfirm.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnConfirm.setBackground(SWTResourceManager.getColor(0, 100, 86));
		
		Label CurrentDate = new Label(shell, SWT.NONE);
		CurrentDate.setBounds(19, 343, 102, 28);
		CurrentDate.setText(formattedStringCurrent);
		CurrentDate.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));		
	}

	public void cancelReservation(String spotID, boolean spotReserved)
	{
		if(spotReserved == true) {
			try {
				String connectionString =
						"jdbc:sqlserver://parking-server.database.windows.net:1433;"
						+ "database=ParkingReservation;"
						+ "user=Group4@parking-server;"
						+ "password=SeniorProject2019**;"
						+ "encrypt=true;"
						+ "trustServerCertificate=false;"
						+ "hostNameInCertificate=*.database.windows.net;"
						+ "loginTimeout=30;";
				
				Connection conCancelRes = null;
				conCancelRes  = DriverManager.getConnection(connectionString);
				
				String sql_cancelReservation = "DELETE FROM Reservation WHERE spotID = '" + spotID + "'";
				PreparedStatement st = conCancelRes .prepareStatement(sql_cancelReservation);
				st.executeUpdate();
				
				String sql_update = "UPDATE Parking SET spotAV = 1  WHERE spotID = ?";
				PreparedStatement pstUpdate = conCancelRes .prepareStatement(sql_update);
				pstUpdate.setString(1,  spotID);
				pstUpdate.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Reservation canceled.", "Success!", JOptionPane.INFORMATION_MESSAGE);
	              
				conCancelRes.close();
			}
			catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
		}
		else {
			JOptionPane.showMessageDialog(null, "No reservation found.", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
}
