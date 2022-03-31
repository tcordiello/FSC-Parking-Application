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
import org.eclipse.swt.widgets.Combo;
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

public class DeleteVehicle {
	private Shell shell;
	private String username;
	private int numOfVehicles = 0;
	private String[] vehicles;
	private int[] vehicleIdList;
	private int selectedVehicleId;
	
	public void setUsername(String usernameText)
	{
		username = usernameText;
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
		shell.setSize(452, 287);
		shell.setText("FSC Parking");
		shell.setFocus();
		
		Color color = new Color(display, 0, 100, 86);
		
		Label lblWelcomeBanner = new Label(shell, SWT.NONE);
		lblWelcomeBanner.setBounds(0, 0, 446, 37);
		lblWelcomeBanner.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblWelcomeBanner.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblWelcomeBanner.setBackground(color);
		lblWelcomeBanner.setAlignment(SWT.CENTER);
		lblWelcomeBanner.setText("Delete Vehicle");
		
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
						"SELECT vehicleID, vehicleColor, vehicleMake, vehicleModel FROM Vehicle_Reg WHERE userName ='"+ username +"'";
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
		
		Combo comboSelectVehicle = new Combo(shell, SWT.NONE);
		comboSelectVehicle.setBounds(10, 83, 274, 28);
		if(numOfVehicles != 0) {
			comboSelectVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
			comboSelectVehicle.setItems(vehicles);
			comboSelectVehicle.setText("Select Vehicle...");
		}
		else {
			comboSelectVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			comboSelectVehicle.setText("NO VEHICLES REGISTERED");
		}
		
		Label lblSelectTheVehicle = new Label(shell, SWT.NONE);
		lblSelectTheVehicle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblSelectTheVehicle.setBounds(10, 52, 334, 28);
		lblSelectTheVehicle.setText("Select the vehicle you want to delete:");
		
		Button btnDeleteVehicle = new Button(shell, SWT.NONE);
		btnDeleteVehicle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(comboSelectVehicle.getSelectionIndex() != -1) {
					selectedVehicleId = vehicleIdList[comboSelectVehicle.getSelectionIndex()];

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
						
						Connection conn = null;
						conn  = DriverManager.getConnection(connectionString);
						
						String sql_deleteVehicle1 = "DELETE FROM Reservation WHERE vehicleID = " + selectedVehicleId;
						PreparedStatement st1 = conn .prepareStatement(sql_deleteVehicle1);
						st1.executeUpdate();
						
						String sql_deleteVehicle2 = "DELETE FROM Vehicle_Reg WHERE vehicleID = " + selectedVehicleId;
						PreparedStatement st2 = conn .prepareStatement(sql_deleteVehicle2);
						st2.executeUpdate();
						
						shell.close();
						
						JOptionPane.showMessageDialog(null, "Vehicle deleted.", "Success!", JOptionPane.INFORMATION_MESSAGE);
			              
						conn.close();
					}
					catch(Exception x) {JOptionPane.showMessageDialog(null, x);}	
				}
				else {
	        		  JOptionPane.showMessageDialog(null, "No vehicle selected!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDeleteVehicle.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnDeleteVehicle.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnDeleteVehicle.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnDeleteVehicle.setBounds(10, 195, 201, 47);
		btnDeleteVehicle.setText("Delete Vehicle");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
	            shell.close();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		btnCancel.setBackground(SWTResourceManager.getColor(0, 100, 86));
		btnCancel.setBounds(235, 195, 201, 47);
		
		
		
		
	}
}	


