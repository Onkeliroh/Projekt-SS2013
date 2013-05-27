import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//eigentlich cooler, aber beist sich mit org.eclipse.swt.widgets.List
//import java.util.List;
import java.util.ArrayList;
import org.eclipse.wb.swt.SWTResourceManager;


public class Main extends ApplicationWindow {
	private Text message_field;

	/**
	 * Create the application window.
	 */
	public Main() {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());
		
		Group grpSetup = new Group(container, SWT.NONE);
		grpSetup.setText("Setup");
		FormData fd_grpSetup = new FormData();
		fd_grpSetup.bottom = new FormAttachment(0, 111);
		fd_grpSetup.top = new FormAttachment(0, 10);
		fd_grpSetup.right = new FormAttachment(0, 843);
		fd_grpSetup.left = new FormAttachment(0, 10);
		grpSetup.setLayoutData(fd_grpSetup);
		
		final Label lblconnection_status = new Label(grpSetup, SWT.NONE);
		lblconnection_status.setBounds(361, 57, 210, 15);
		lblconnection_status.setText("not connected");
		lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		final Combo device_combo = new Combo(grpSetup, SWT.NONE);
		//set items
		java.util.List<String> port_list = rxtx_basic_lib.get_port_names();
		device_combo.setItems(port_list.toArray(new String[port_list.size()]));
		
		device_combo.setBounds(81, 26, 185, 25);
		device_combo.select(0);
		
		final Combo bps_combo = new Combo(grpSetup, SWT.NONE);
		bps_combo.setItems(new String[] {"300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600", "115200"});
		bps_combo.setBounds(81, 57, 185, 25);
		bps_combo.select(5);
		
		Label lblDevice = new Label(grpSetup, SWT.NONE);
		lblDevice.setBounds(10, 26, 65, 15);
		lblDevice.setText("Device:");
		
		Label lblBps = new Label(grpSetup, SWT.NONE);
		lblBps.setBounds(10, 57, 65, 15);
		lblBps.setText("Bps:");
		
		message_field = new Text(container, SWT.BORDER);
		FormData fd_message_field = new FormData();
		fd_message_field.top = new FormAttachment(grpSetup, 6);
		fd_message_field.left = new FormAttachment(grpSetup, 0, SWT.LEFT);
		fd_message_field.right = new FormAttachment(0, 843);
		message_field.setLayoutData(fd_message_field);
		message_field.setToolTipText("type your message here");
		
		Button send_btn = new Button(container, SWT.NONE);
		send_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				message_field.setText("Hello World.");
			}
		});
		FormData fd_send_btn = new FormData();
		fd_send_btn.top = new FormAttachment(message_field, 6);
		fd_send_btn.right = new FormAttachment(grpSetup, 0, SWT.RIGHT);
		
		Button btnConnect = new Button(grpSetup, SWT.NONE);
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (device_combo.getItemCount() > 0)
				try {
					rxtx_basic_lib.connect( device_combo.getItem(device_combo.getSelectionIndex()), Integer.parseInt(bps_combo.getItem(bps_combo.getSelectionIndex())));
					if (rxtx_basic_lib.connected)
					{
						lblconnection_status.setText("connected");
						lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
					}
					else
					{
						lblconnection_status.setText("not connected");
						lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					}
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnConnect.setBounds(361, 24, 84, 27);
		btnConnect.setText("Connect");
		
		//Label lblconnection_status = new Label(grpSetup, SWT.NONE);
		
		//lblconnection_status.setBounds(361, 57, 210, 15);
		
		Button btnRefresh = new Button(grpSetup, SWT.NONE);
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				java.util.List<String> port_list = rxtx_basic_lib.get_port_names();
				device_combo.setItems(port_list.toArray(new String[port_list.size()]));
				device_combo.select(0);
			}
		});
		btnRefresh.setBounds(451, 24, 84, 27);
		btnRefresh.setText("Refresh");
		fd_send_btn.left = new FormAttachment(0, 10);
		
		send_btn.setLayoutData(fd_send_btn);
		send_btn.setText("Send");
		
		List list = new List(container, SWT.BORDER);
		FormData fd_list = new FormData();
		fd_list.right = new FormAttachment(0, 843);
		fd_list.bottom = new FormAttachment(send_btn, 388, SWT.BOTTOM);
		fd_list.top = new FormAttachment(send_btn, 6);
		fd_list.left = new FormAttachment(0, 10);
		list.setLayoutData(fd_list);

		//run_setup();
		
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Main window = new Main();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Serial Communicator of Doom");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(845, 655);
	}
}
