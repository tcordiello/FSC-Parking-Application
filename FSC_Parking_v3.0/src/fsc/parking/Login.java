package fsc.parking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

/**
 * @author 
 * @version 1.0.0
 */
public class Login {

	private Shell shell;
	protected Text usernameText;
	private Text passwordText;
	private String pass_compare;
	
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
		shell.setSize(450, 300);
		shell.setText("FSC Parking");
		
		//Image example
		Label lblHeader = new Label(shell, SWT.CENTER);
		lblHeader.setImage(SWTResourceManager.getImage("assets\\fsc-banner.gif"));
		lblHeader.setBounds(32, 28, 359, 64);
		
		Color color = new Color(display, 0, 100, 86);
		
		usernameText = new Text(shell, SWT.BORDER);
		usernameText.setBounds(115, 133, 206, 21);
		
		passwordText = new Text(shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		passwordText.setBounds(115, 172, 206, 21);
		passwordText.setEchoChar('*');
		passwordText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR) {
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
						String sql = "SELECT U_password FROM User_Acc WHERE userName = ?";
						PreparedStatement pst = connection.prepareStatement(sql);
						pst.setString(1, usernameText.getText());
						ResultSet rs_pass = pst.executeQuery();
						while(rs_pass.next()) {
							pass_compare = rs_pass.getString("U_password");
						}
					
						if(pass_compare.equals(passwordText.getText())) {
							Home home = new Home();
						    home.setUsername(usernameText.getText());
						    shell.close();
						    home.open();
						}
						else {
							System.out.print(passwordText.getText());
							JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
							passwordText.setText("");
						}
						connection.close();
					}
					catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
				}
			}
		});
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(32, 136, 77, 33);
		lblUsername.setText("Username:");
		
		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(32, 175, 77, 33);
		lblPassword.setText("Password:");
		
		Button btnSignIn = new Button(shell, SWT.NONE);
		btnSignIn.setBounds(243, 222, 87, 33);
		btnSignIn.setText("Sign In");
		btnSignIn.setBackground(color);
		btnSignIn.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		//Connect to Database Code
		btnSignIn.addListener(SWT.Selection, new Listener() {
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
					String sql = "SELECT * FROM User_Acc WHERE userName = ? AND U_password = ?";
					PreparedStatement pst = connection.prepareStatement(sql);
					pst.setString(1, usernameText.getText());
					pst.setString(2, passwordText.getText());
					ResultSet rs = pst.executeQuery();
					if(rs.next()) {	
						Home home = new Home();
					    home.setUsername(usernameText.getText());
					    shell.close();
					    home.open();
					}
					else {
						JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
						usernameText.setText("");
						passwordText.setText("");
					}
					connection.close();
				}
				catch(Exception x) {JOptionPane.showMessageDialog(null, x);}
			}
		});
		
		Link link = new Link(shell, SWT.NONE);
		link.setBounds(115, 214, 93, 41);
		link.setText("<a>Forgot password?</a>");
		
		// When users click on the "Forgot password?" link, he / she will be redirected to https://reset.farmingdale.edu/accounts/Reset.
		link.addSelectionListener(new SelectionAdapter()  {
		 
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	// Opens the link on the user's default browser.
		        Program.launch("https://reset.farmingdale.edu/accounts/Reset");
		    }
		     
		});
		
	}

}
