package fsc.parking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
@SuppressWarnings("unused")

public class RegisterVehicle {

	private Shell shell;
	private Text vehicleMakeText;
	private Text vehicleModelText;
	private Text vehicleColorText;
	private Text vehiclePlateText;
	private Button radioButtonCar;
	private Button radioButtonMotorcycle;
	private String username;
	private String make;
	private String model;
	private String color;
	private String plate;
	private String type;

	
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
		shell.setSize(600, 469);
		shell.setText("FSC Parking");
		shell.setFocus();
		
		Color color = new Color(display, 0, 100, 86);
		
		Label vehicleRegistrationLabel = new Label(shell, SWT.NONE);
		vehicleRegistrationLabel.setText("Vehicle Registration");
		vehicleRegistrationLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		vehicleRegistrationLabel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		vehicleRegistrationLabel.setBackground(SWTResourceManager.getColor(0, 100, 86));
		vehicleRegistrationLabel.setAlignment(SWT.CENTER);
		vehicleRegistrationLabel.setBounds(0, 0, 594, 37);
		
		Label vehicleMakeLabel = new Label(shell, SWT.NONE);
		vehicleMakeLabel.setText("Vehicle Make");
		vehicleMakeLabel.setBounds(10, 43, 110, 20);
		
		vehicleMakeText = new Text(shell, SWT.BORDER);
		vehicleMakeText.setBounds(10, 69, 167, 26);
		
		Label vehicleModeLabel = new Label(shell, SWT.NONE);
		vehicleModeLabel.setText("Vehicle Model");
		vehicleModeLabel.setBounds(10, 101, 110, 20);
		
		vehicleModelText = new Text(shell, SWT.BORDER);
		vehicleModelText.setBounds(10, 127, 167, 26);
		
		Label vehicleColorLabel = new Label(shell, SWT.NONE);
		vehicleColorLabel.setText("Vehicle Color");
		vehicleColorLabel.setBounds(10, 159, 110, 20);
		
		vehicleColorText = new Text(shell, SWT.BORDER);
		vehicleColorText.setBounds(10, 185, 167, 26);
		
		Label vehiclePlateLabel = new Label(shell, SWT.NONE);
		vehiclePlateLabel.setText("License Plate Number");
		vehiclePlateLabel.setBounds(10, 217, 151, 20);
		
		vehiclePlateText = new Text(shell, SWT.BORDER);
		vehiclePlateText.setBounds(10, 243, 167, 26);
		
		Label vehicleTypeLabel = new Label(shell, SWT.NONE);
		vehicleTypeLabel.setBounds(10, 275, 97, 20);
		vehicleTypeLabel.setText("Vehicle Type");
		
		radioButtonCar = new Button(shell, SWT.RADIO);
		radioButtonCar.setBounds(20, 301, 111, 20);
		radioButtonCar.setText("Car");
		
		radioButtonMotorcycle = new Button(shell, SWT.RADIO);
		radioButtonMotorcycle.setBounds(20, 327, 111, 20);
		radioButtonMotorcycle.setText("Motorcycle");
		
		Button electricVehicleCheckbox = new Button(shell, SWT.CHECK);
		electricVehicleCheckbox.setBounds(10, 363, 298, 26);
		electricVehicleCheckbox.setText("This vehicle is electric.");
		
		Label lblWarningText = new Label(shell, SWT.NONE);
		lblWarningText.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblWarningText.setBounds(317, 86, 267, 183);
		lblWarningText.setText("Please be sure to provide\r\n"
				+ "information that can be used to\r\n"
				+ "accurately identify your vehicle.\r\n"
				+ "If you do not, Campus Police will\r\n"
				+ "not be able to identify if the\r\n"
				+ "correct car is in the reserved spot\r\n"
				+ "and they WILL give you a ticket.");

		Button registerButton = new Button(shell, SWT.NONE);
		registerButton.setText("Register");
		registerButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		registerButton.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		registerButton.setBackground(SWTResourceManager.getColor(0, 100, 86));
		registerButton.setBounds(375, 301, 209, 60);
		registerButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
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
					String sql = "INSERT INTO Vehicle_Reg (userName, vehicleMake, vehicleModel, vehicleColor, plateNum, vehicleType)" 
					+"VALUES (?,?,?,?,?,?)";
					
					PreparedStatement pst = connection.prepareStatement(sql);

					if (vehicleMakeText.getText().isEmpty() == true || 
							vehicleModelText.getText().isEmpty() == true || 
							vehicleColorText.getText().isEmpty() == true  || 
							vehiclePlateText.getText().isEmpty() == true)
	                  {
	                      // Message box that is displayed when fields are empty.
	                      JOptionPane.showMessageDialog(null, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	                  }
					else if(radioButtonMotorcycle.getSelection() == false && radioButtonCar.getSelection() == false) {
	                      JOptionPane.showMessageDialog(null, "Need to select Car or Motorcycle.", "Error", JOptionPane.ERROR_MESSAGE);
					}
	                else
	                {
	                	pst.setString(1, username);
						pst.setString(2, vehicleMakeText.getText());
						pst.setString(3, vehicleModelText.getText());
						pst.setString(4, vehicleColorText.getText());
						pst.setString(5, vehiclePlateText.getText());
						pst.setString(6, radioButtonCar.getText());
						pst.executeUpdate();
	                    JOptionPane.showMessageDialog(null, "Vehicle Registered.", "Success!", JOptionPane.INFORMATION_MESSAGE);
	                    shell.close();
	                }
					connection.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
			}
		});
		
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  shell.close();
		      }
		});
		cancelButton.setText("Cancel");
		cancelButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cancelButton.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		cancelButton.setBackground(SWTResourceManager.getColor(0, 100, 86));
		cancelButton.setBounds(375, 367, 209, 60);
		
		Label lblWarning = new Label(shell, SWT.NONE);
		lblWarning.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblWarning.setBounds(317, 53, 129, 26);
		lblWarning.setText("WARNING:");
		lblWarning.setForeground(display.getSystemColor(SWT.COLOR_RED));


	}
	
	/**
	 * This method returns the vehicle's make.
	 * @return vehicle make.
	 */
	public String getVehicleMake()
	{
		return vehicleMakeText.getText();
	}
	
	/**
	 * This method returns the vehicle's model.
	 * @return vehicle model.
	 */
	public String getVehicleModel()
	{
		return vehicleModelText.getText();
	}
	
	/**
	 * This method returns the vehicle's color.
	 * @return vehicle color.
	 */
	public String getVehicleColor()
	{
		return vehicleColorText.getText();
	}
	
	/**
	 * This method returns the vehicle's plate.
	 * @return vehicle plate.
	 */
	public String getVehiclePlate()
	{
		return vehiclePlateText.getText();
	}
	
	/**
	 * This method stores the vehicle make that is entered.
	 * @param vehicleMake that is entered in the vehicleMakeText.
	 */
	public void setVehicleMake(String vehicleMake)
	{
		make = vehicleMake;
	}
	
	/**
	 * This method stores the vehicle model that is entered.
	 * @param vehicleModel that is entered in the vehicleModelText.
	 */
	public void setVehicleModel(String vehicleModel)
	{
		model = vehicleModel;
	}
	
	/**
	 * This method stores the vehicle color that is entered.
	 * @param vehicleColor that is entered in the vehicleColorText.
	 */
	public void setVehicleColor(String vehicleColor)
	{
		color = vehicleColor;
	}
	
	/**
	 * This method stores the vehicle plate that is entered.
	 * @param vehiclePlate that is entered in the vehiclePlateText.
	 */
	public void setVehiclePlate(String vehiclePlate)
	{
		plate = vehiclePlate;
	}
	
	public void setUsername(String name) {
		username = name;
	}
	
	public String getUsername() {
		return username;
	}

	public String getVehicleTypeButton() {
		return radioButtonCar.getText();
	}
	
	public void setVehicleTypeButton(String Type){
		type = Type;
	}
}