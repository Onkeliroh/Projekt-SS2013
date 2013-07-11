package kickflick.gui;

import java.io.IOException;

import kickflick.utility.server;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Server_Main {

	private Server_Main window;
	private Shell shlKickflickServer;
	private server Server;
	private Combo combo_port;
	
	
	//unnessesary ?
	public Server_Main()
	{}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		shlKickflickServer = new Shell();
		shlKickflickServer.setSize(574, 463);
		shlKickflickServer.setText("KickFlick Server");
		shlKickflickServer.setLayout(new GridLayout(1, false));
		
		Menu menu = new Menu(shlKickflickServer, SWT.BAR);
		shlKickflickServer.setMenuBar(menu);
		
		MenuItem mntmSettings = new MenuItem(menu, SWT.CASCADE);
		mntmSettings.setText("Settings");
		
		Menu menu_1 = new Menu(mntmSettings);
		mntmSettings.setMenu(menu_1);
		
		MenuItem mntmCommunicator = new MenuItem(menu, SWT.NONE);
		mntmCommunicator.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				communicator comm_cator = new communicator(shlKickflickServer, 0, Server); 
				comm_cator.open();
			}
		});
		mntmCommunicator.setText("Communicator");
		
		Group grpConnection = new Group(shlKickflickServer, SWT.NONE);
		grpConnection.setLayout(new GridLayout(4, false));
		grpConnection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpConnection.setText("Connection");
		
		combo_port = new Combo(grpConnection, SWT.READ_ONLY);
		combo_port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Combo combo_baut = new Combo(grpConnection, SWT.READ_ONLY);
		combo_baut.setItems(new String[] {"300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600", "115200"});
		combo_baut.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_baut.select(5);
		
		Button btnRefresh = new Button(grpConnection, SWT.NONE);
		btnRefresh.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				set_up();
			}
		});
		btnRefresh.setText("Refresh");
		
		final Button btnConnect = new Button(grpConnection, SWT.NONE);
		GridData gd_btnConnect = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnConnect.widthHint = 100;
		gd_btnConnect.minimumWidth = 200;
		btnConnect.setLayoutData(gd_btnConnect);
		btnConnect.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if ( Server != null && !Server.serial_com.is_connected())
				{
					Server.connect_panstamp(combo_port.getItem(combo_port.getSelectionIndex()),
										Integer.parseInt(combo_baut.getItem(combo_baut.getSelectionIndex())));
					btnConnect.setText("Disconnect");
				}
				else
				{
					try {
						Server.serial_com.exit();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					btnConnect.setText("Connect");
				}
			}
		});
		btnConnect.setText("Connect");
		
		set_up();
		
		shlKickflickServer.open();
		shlKickflickServer.layout();
		while (!shlKickflickServer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void set_up()
	{
		java.util.List<String> port_list  = Server.get_SerialCom().get_port_names();
		getCombo_port().setItems(port_list.toArray(new String[port_list.size()]));
		getCombo_port().select(0);
	}

	public void set_Server ( kickflick.utility.server Server )
	{
		this.Server = Server;
	}

	public Combo getCombo_port() {
		return combo_port;
	}
}
