package fsc.parking;

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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

@SuppressWarnings("unused")

public class Help {
	private Shell shell;
	
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
		shell.setSize(590, 450);
		shell.setText("FSC Parking");
		shell.setFocus();
		
		Color color = new Color(display, 0, 100, 86);
		
		Label lblHelpBanner = new Label(shell, SWT.NONE);
		lblHelpBanner.setBounds(0, 0, 584, 42);
		lblHelpBanner.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblHelpBanner.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblHelpBanner.setBackground(color);
		lblHelpBanner.setAlignment(SWT.CENTER);
		lblHelpBanner.setText("Help");
		
		Label lblHelp = new Label(shell, SWT.NONE);
		lblHelp.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblHelp.setBounds(10, 47, 564, 322);
		lblHelp.setText("Subscribe - Activate your subscription for authorization to start\n"
				+ "reserving your parking spots.\r\n"
				+ "Reserve Spot - Begin the reservation process.\r\n"
				+ "Cancel Reservation - Cancel a reservation that you have made.\r\n"
				+ "Register Vehicle - Register your vehicle to authorize parking\n"
				+ "with that vehicle.\r\n"
				+ "Delete Vehicle - Delete a vehicle that you have registered.\r\n");
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
			}
		});
		btnClose.setBounds(247, 375, 90, 30);
		btnClose.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnClose.setBackground(color);
		btnClose.setText("Close");
		
	}
}