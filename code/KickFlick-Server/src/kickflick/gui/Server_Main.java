package kickflick.gui;

import java.io.IOException;

import kickflick.device.device;
import kickflick.utility.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TableColumn;

public class Server_Main {

	private Server_Main window;
	private Shell shlKickflickServer;
	private server Server;
	private Combo combo_port;

	private Timer timer_;
	private Table DeviceTable;


	/*
	public Server_Main()
	{}*/

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		final Shell shlKickflickServer = new Shell( display );
		shlKickflickServer.setMinimumSize(new Point(1, 23));
		shlKickflickServer.setSize(500, 353);
		shlKickflickServer.setLocation(750, 250);
		shlKickflickServer.setText("KickFlick Server");
		GridLayout gl_shlKickflickServer = new GridLayout(1, false);
		gl_shlKickflickServer.marginHeight = 0;
		gl_shlKickflickServer.marginWidth = 0;
		shlKickflickServer.setLayout(gl_shlKickflickServer);

		Group grpConnection = new Group(shlKickflickServer, SWT.NONE);
		grpConnection.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		grpConnection.setLayout(new GridLayout(4, false));
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

		Menu menu = new Menu(shlKickflickServer, SWT.BAR);
		shlKickflickServer.setMenuBar(menu);

		Group grpDevices = new Group(shlKickflickServer, SWT.NONE);
		grpDevices.setText("Devices");
		grpDevices.setLayout(new GridLayout(3, false));
		grpDevices.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		DeviceTable = new Table( grpDevices, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION );
		DeviceTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		DeviceTable.setLinesVisible(true);
		DeviceTable.setHeaderVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(DeviceTable, SWT.NONE);
		tblclmnNewColumn.setWidth(248);
		tblclmnNewColumn.setText("Personality");

		TableColumn tblclmnNewColumn_1 = new TableColumn(DeviceTable, SWT.RIGHT);
		tblclmnNewColumn_1.setWidth(79);
		tblclmnNewColumn_1.setText("State");

		TableColumn tblclmnNewColumn_2 = new TableColumn(DeviceTable, SWT.RIGHT);
		tblclmnNewColumn_2.setWidth(107);
		tblclmnNewColumn_2.setText("last seen");

		if ( this.Server.get_devices().size() > 0 )
		{
			for ( int i = 0; i < this.Server.get_devices().size() ; ++i)
			{
				TableItem tableItem = new TableItem(DeviceTable, SWT.NONE,i);
				tableItem.setText(new String[] {
													this.Server.get_device(0).get_Personality().get_Name(),
													this.Server.get_device(0).get_Personality().get_state_name()
												});
			}
		}



		Button Config = new Button(grpDevices, SWT.NONE);
		Config.setText("Config");
		Config.addSelectionListener( new SelectionAdapter() {
		/* TODO write better Config - Window!!
			public void widgetSelected( SelectionEvent e ) {
				Shell ConfigerationShell = new Shell( Display.getDefault() );
				ConfigerationShell.setText( "Configeration" );
				ConfigerationShell.setLayout( new GridLayout(1, false) );

				Label Head = new Label( ConfigerationShell, SWT.BORDER );
				Head.setText( "Configeration" );
				Label Headline = new Label( ConfigerationShell, SWT.NONE ); 
				Headline.setText( "Configeration" );

				Label PersonalitysColors = new Label( ConfigerationShell, SWT.NONE );
				PersonalitysColors.setText( "set Personalities Colors ()" );
				Menu ConfigerationMenu = new Menu( ConfigerationShell, SWT.POP_UP );
				MenuItem ConfigerationItem = new MenuItem( ConfigerationMenu, SWT.RADIO );
				ConfigerationItem.setText( "Personality Colors" );
				ConfigerationItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected( SelectionEvent e) {
						MenuItem Item = (MenuItem)e.widget;
						if( Item.getSelection() ) {
							this.Server.get_Devices().;
						}
					}
				});
			}
		*/	
		});

		Button newDevice = new Button(grpDevices, SWT.NONE);
		newDevice.setText("New Device");
		

		Button Reload = new Button(grpDevices, SWT.NONE);
		Reload.setText("Reload");

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

	private void init_timer()
	{
        this.timer_ = new Timer();

        TimerTask refresh_Table = new TimerTask()
        {
            private Table table_;
            private List<device> dev_  = new ArrayList<device>();
            public void init(Table table, List<device> devs)
            {
                this.table_ = table;
                this.dev_ = devs;
            }
            @Override
            public void run() 
            {
                for ( int i = 0 ; i < this.dev_.size() ; ++i)
                {
                    TableItem tableItem = new TableItem(this.table_, SWT.NONE,i);
                    tableItem.setText(new String[] {
                            this.dev_.get(i).get_Personality().get_Name(),
                            this.dev_.get(i).get_Personality().get_state_name()
                    });
                }
            }
        };
       // refresh_Table.init()
       // this.timer_.schedule(refresh_Table,100,30000);
	}
	protected Table getDeviceTable() {
		return DeviceTable;
	}
}
