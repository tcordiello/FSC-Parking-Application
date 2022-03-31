package fsc.parking;

import java.sql.*;
import javax.swing.*;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("unused")

/**
 * @author 
 * @version 1.0.0
 */
public class Main {
  	 /*
	 * Launch the application.
	 * @param args
	 */
	private static Shell shell;
	protected static Text usernameText;
	private static Text passwordText;
	
	public static void main(String[] args) {
		
		try {
			Login login = new Login();
			// Opens login window for user to enter credentials.
			login.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
