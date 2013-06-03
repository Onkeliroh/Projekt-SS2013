import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//eigentlich cooler, aber beist sich mit org.eclipse.swt.widgets.List
//import java.util.List;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;


public class Window extends ApplicationWindow implements SerialPortEventListener{
	private Text message_field;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private rxtx_basic_lib serial_com;
	private Window window;
	private static Display display;
	private StyledText styledText;

	

	/**
	 * Create the application window.
	 */
	public Window() {
		super(null);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		this.serial_com = new rxtx_basic_lib( this );
		this.window = this;
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		
		Group grpSetup = new Group(container, SWT.NONE);
		grpSetup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		grpSetup.setText("Setup");
		grpSetup.setLayout(new FormLayout());
		
		final Label lblconnection_status = new Label(grpSetup, SWT.NONE);
		FormData fd_lblconnection_status = new FormData();
		fd_lblconnection_status.bottom = new FormAttachment(0, 53);
		fd_lblconnection_status.right = new FormAttachment(0, 570);
		fd_lblconnection_status.top = new FormAttachment(0, 38);
		fd_lblconnection_status.left = new FormAttachment(0, 360);
		lblconnection_status.setLayoutData(fd_lblconnection_status);
		lblconnection_status.setText("not connected");
		lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		final Combo device_combo = new Combo(grpSetup, SWT.NONE);
		FormData fd_device_combo = new FormData();
		fd_device_combo.right = new FormAttachment(0, 265);
		device_combo.setLayoutData(fd_device_combo);
		//set items
		java.util.List<String> port_list = serial_com.get_port_names();
		device_combo.setItems(port_list.toArray(new String[port_list.size()]));
		device_combo.select(0);
		
		final Combo bps_combo = new Combo(grpSetup, SWT.NONE);
		fd_device_combo.left = new FormAttachment(bps_combo, 0, SWT.LEFT);
		FormData fd_bps_combo = new FormData();
		fd_bps_combo.right = new FormAttachment(0, 265);
		fd_bps_combo.top = new FormAttachment(0, 38);
		fd_bps_combo.left = new FormAttachment(0, 80);
		bps_combo.setLayoutData(fd_bps_combo);
		bps_combo.setItems(new String[] {"300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600", "115200"});
		bps_combo.select(5);
		
		Label lblDevice = new Label(grpSetup, SWT.NONE);
		fd_device_combo.top = new FormAttachment(lblDevice, 0, SWT.TOP);
		FormData fd_lblDevice = new FormData();
		fd_lblDevice.top = new FormAttachment(0, 7);
		fd_lblDevice.right = new FormAttachment(0, 74);
		lblDevice.setLayoutData(fd_lblDevice);
		lblDevice.setText("Device:");
		
		Label lblBps = new Label(grpSetup, SWT.NONE);
		fd_lblDevice.bottom = new FormAttachment(lblBps, -16);
		fd_lblDevice.left = new FormAttachment(lblBps, 0, SWT.LEFT);
		FormData fd_lblBps = new FormData();
		fd_lblBps.bottom = new FormAttachment(0, 53);
		fd_lblBps.right = new FormAttachment(0, 74);
		fd_lblBps.top = new FormAttachment(0, 38);
		fd_lblBps.left = new FormAttachment(0, 9);
		lblBps.setLayoutData(fd_lblBps);
		lblBps.setText("Bps:");
		
		message_field = new Text(container, SWT.BORDER);
		message_field.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		message_field.setToolTipText("type your message here");
		
		Button send_btn = new Button(container, SWT.NONE);
		send_btn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		send_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				write(message_field.getText());
				message_field.setText("");
			}
		});
		
		final Button btnConnect = new Button(grpSetup, SWT.NONE);
		FormData fd_btnConnect = new FormData();
		fd_btnConnect.bottom = new FormAttachment(0, 32);
		fd_btnConnect.right = new FormAttachment(0, 444);
		fd_btnConnect.top = new FormAttachment(0, 5);
		fd_btnConnect.left = new FormAttachment(0, 360);
		btnConnect.setLayoutData(fd_btnConnect);
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (!serial_com.is_connected()) 
				{
					if (device_combo.getItemCount() > 0)
					try {
						serial_com.connect( 
										device_combo.getItem(device_combo.getSelectionIndex()),
										Integer.parseInt(bps_combo.getItem(bps_combo.getSelectionIndex()))
										);
				
						if (serial_com.is_connected()){
							btnConnect.setText("Disconnect");
							lblconnection_status.setText("connected");
							lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
							if (serial_com.initIOStream() == true) {
								SerialPort serialPort = serial_com.get_connected_Port();
								try
						        {
						            serialPort.addEventListener(window);
						            serialPort.notifyOnDataAvailable(true);    
						        }
						        catch (TooManyListenersException e1)
						        {
						        	System.err.println("ERROR: Too Many ListenerExceptions in initListener.");
						    	}	
							}
						}
						else{
							lblconnection_status.setText("not connected");
							lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
						}
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else{
					btnConnect.setText("Connect");
					try{
						serial_com.exit();
					}
					catch (IOException e1){
						e1.printStackTrace();
					}
				}
			}
		});
		btnConnect.setText("Connect");

		
		Button btnRefresh = new Button(grpSetup, SWT.NONE);
		FormData fd_btnRefresh = new FormData();
		fd_btnRefresh.bottom = new FormAttachment(0, 32);
		fd_btnRefresh.right = new FormAttachment(0, 534);
		fd_btnRefresh.top = new FormAttachment(0, 5);
		fd_btnRefresh.left = new FormAttachment(0, 450);
		btnRefresh.setLayoutData(fd_btnRefresh);
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				java.util.List<String> port_list = serial_com.get_port_names();
				device_combo.setItems(port_list.toArray(new String[port_list.size()]));
				device_combo.select(0);
			}
		});
		btnRefresh.setText("Refresh");
		send_btn.setText("Send");
		
		TextViewer textViewer = new TextViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY);
		textViewer.setEditable(false);
		styledText = textViewer.getTextWidget();
		styledText.setLeftMargin(1);
		styledText.setWrapIndent(1);
		styledText.setFont(SWTResourceManager.getFont("Source Sans Pro", 9, SWT.NORMAL));
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(styledText);
		styledText.addListener(SWT.Modify, new Listener(){
		    public void handleEvent(Event arg0){
		        styledText.setTopIndex(styledText.getLineCount() - 1);
		    }
		});

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
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}
	
	/**
	 * Configure the shell.
	 * @param newShell
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Serial Communicator of Doom");
	}

	/**
	 * Return the initial size of the window.
	 */
	protected Point getInitialSize() {
		return new Point(845, 655);
	}
	
	//a method to make changing the output widget easier
	public void write(String string) {
    		getStyledText().append(string);
    }
	
	public static void main(String args[]) {
		try {
			Window window = new Window();
			window.setBlockOnOpen(true);
			window.open();
			display = Display.getCurrent();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void serialEvent(SerialPortEvent evt) 
	{ 
		try {
			rxtx_basic_lib.com_listener horcher = new rxtx_basic_lib.com_listener(serial_com, window, serial_com.get_inputstream());
			Thread read = new Thread(horcher);
			this.getShell().getDisplay().asyncExec(read);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public StyledText getStyledText() {
		return styledText;
	}
}
