import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


import java.io.IOException;
import java.util.TooManyListenersException;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;


public class Window extends ApplicationWindow implements SerialPortEventListener{
	protected Shell shell;
	
	private Text message_field;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	protected rxtx_basic_lib serial_com;
	private Window window;
	private StyledText styledText;

	

	/**
	 * Create the application window.
	 */
	public Window() {
		super(null);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER);
		createActions();
		this.serial_com = new rxtx_basic_lib();
		this.window = this;
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	protected Control createContents(Composite parent) {
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmClose = new MenuItem(menu_1, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (serial_com.is_connected())
				{
					try {
						serial_com.exit();
					} catch (IOException e1) {
						e1.printStackTrace();
						System.err.println(e.toString());
					}
				}
				shell.close();
			}
		});
		mntmClose.setText("Close");
		
		MenuItem mntmOptions = new MenuItem(menu, SWT.CASCADE);
		mntmOptions.setText("Options");
		
		Menu menu_2 = new Menu(mntmOptions);
		mntmOptions.setMenu(menu_2);
		
		MenuItem mntmMessageOptions = new MenuItem(menu_2, SWT.NONE);
		mntmMessageOptions.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				message_dialog mw = new message_dialog(shell, 0);
				mw.open();
			}
		});	
		mntmMessageOptions.setText("Message Options");
		
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		
		Group grpSetup = new Group(container, SWT.NONE);
		GridData gd_grpSetup = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_grpSetup.heightHint = 93;
		grpSetup.setLayoutData(gd_grpSetup);
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
		
		final Combo device_combo = new Combo(grpSetup, SWT.READ_ONLY);
		FormData fd_device_combo = new FormData();
		fd_device_combo.right = new FormAttachment(0, 265);
		device_combo.setLayoutData(fd_device_combo);
		//set items
		java.util.List<String> port_list = serial_com.get_port_names();
		device_combo.setItems(port_list.toArray(new String[port_list.size()]));
		device_combo.select(0);
		
		final Combo bps_combo = new Combo(grpSetup, SWT.READ_ONLY);
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
								write("connected to " + serial_com.get_connected_Port().getName() + "\n");
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
					write("disconnected from " + serial_com.get_connected_Port().getName() + "\n");
					lblconnection_status.setText("not connected");
					lblconnection_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
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
		
		Group send_group = new Group(container, SWT.NONE);
		send_group.setText("Send");
		send_group.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		send_group.setLayout(new FormLayout());
		GridData gd_send_group = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		gd_send_group.heightHint = 96;
		send_group.setLayoutData(gd_send_group);
		formToolkit.adapt(send_group);
		formToolkit.paintBordersFor(send_group);
		
		message_field = new Text(send_group, SWT.BORDER);
		FormData fd_message_field = new FormData();
		fd_message_field.top = new FormAttachment(0, 5);
		fd_message_field.left = new FormAttachment(0, 5);
		message_field.setLayoutData(fd_message_field);
		message_field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				if (e.equals(SWT.)))
			}
		});
		message_field.setToolTipText("type your message here");
		
		Button send_btn = new Button(send_group, SWT.NONE);
		fd_message_field.right = new FormAttachment(send_btn, 0, SWT.RIGHT);
		FormData fd_send_btn = new FormData();
		fd_send_btn.right = new FormAttachment(0, 259);
		fd_send_btn.left = new FormAttachment(0, 5);
		fd_send_btn.top = new FormAttachment(0, 35);
		send_btn.setLayoutData(fd_send_btn);
		send_btn.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				send();
			}
		});
		send_btn.setText("Send");
		
		Button btnOpenMessageOptions = new Button(send_group, SWT.NONE);
		btnOpenMessageOptions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				message_dialog mw = new message_dialog(shell, 0);
				mw.open();
			}
		});
		FormData fd_btnOpenMessageOptions = new FormData();
		fd_btnOpenMessageOptions.bottom = new FormAttachment(message_field, 0, SWT.BOTTOM);
		fd_btnOpenMessageOptions.left = new FormAttachment(message_field, 99);
		btnOpenMessageOptions.setLayoutData(fd_btnOpenMessageOptions);
		formToolkit.adapt(btnOpenMessageOptions, true, true);
		btnOpenMessageOptions.setText("open message options");
		
		TextViewer textViewer = new TextViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY);
		textViewer.setEditable(false);
		styledText = textViewer.getTextWidget();
		styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
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
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
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
		newShell.setMinimumSize(new Point(700, 550));
		super.configureShell(newShell);
		newShell.setText("Serial Communicator of Doom");
		
		shell = newShell;
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
	
	private void send()
	{
		String str = message_field.getText();
		
		rxtx_basic_lib.com_writer comWriter = null;
		
		try {
			comWriter = new rxtx_basic_lib.com_writer(serial_com.get_outputstream(), str, window);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Thread thread = new Thread(comWriter);
		
		//this.getShell().getDisplay().asyncExec(thread);
		
		Display.getCurrent().asyncExec(thread);
		
		message_field.setText("");
	}
	
	public static void main(String args[]) {
		try {
			Window window = new Window();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent();
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
