package kickflick.gui;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.util.TooManyListenersException;

import kickflick.utility.keys;
import kickflick.utility.serial_lib;
import kickflick.utility.server;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class communicator extends Dialog implements SerialPortEventListener {

	protected Object result;
	protected Shell shell;
	private Text data_compose_text;
	
	private server Server;
	private Text message_text;
	private Combo action_combo;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public communicator(Shell parent, int style, server server) {
		super(parent, style);
		this.Server = server;
		setText("Communicator of Doom");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		if ( Server.serial_com.is_connected())
		{
			try {
				Server.serial_com.initIOStream();
				Server.serial_com.get_connected_Port().addEventListener(this);
				Server.serial_com.get_connected_Port().notifyOnDataAvailable(true);
			} catch (TooManyListenersException e1) {
				System.err.println(e1.toString());
			}	
		}
		
		
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(748, 561);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Group grpComposeMessage = new Group(shell, SWT.NONE);
		grpComposeMessage.setLayout(new GridLayout(2, false));
		grpComposeMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpComposeMessage.setText("Compose Header");
		
		Label lblReciever = new Label(grpComposeMessage, SWT.NONE);
		lblReciever.setText("Receiver");
		
		Label lblAction = new Label(grpComposeMessage, SWT.NONE);
		lblAction.setText("Action Key");
		
		Combo receiver_combo = new Combo(grpComposeMessage, SWT.NONE);
		GridData gd_receiver_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_receiver_combo.widthHint = 50;
		receiver_combo.setLayoutData(gd_receiver_combo);
		
		action_combo = new Combo(grpComposeMessage, SWT.READ_ONLY);
		action_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpComposeData = new Group(shell, SWT.NONE);
		grpComposeData.setText("Addtitionel Data");
		grpComposeData.setLayout(new GridLayout(1, false));
		grpComposeData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		data_compose_text = new Text(grpComposeData, SWT.BORDER);
		data_compose_text.setToolTipText("Insert additional data here.\nfor example colors! ");
		data_compose_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpIncomming = new Group(shell, SWT.NONE);
		grpIncomming.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpIncomming.setText("Incomming!");
		
		message_text = new Text(grpIncomming, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		message_text.setBounds(10, 23, 716, 295);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button send_btn = new Button(composite, SWT.NONE);
		send_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				byte[] dings = get_data_composition();
				
				
				byte[] west_package = Server.compose_bytearray(
						(byte) 2, 
						(byte) 0, 
						keys.values()[action_combo.getSelectionIndex()].get_key(),
						dings
						);
				
				if (Server.serial_com.is_connected())
				{
					try {
						serial_lib.com_writer writer = new serial_lib.com_writer(Server.serial_com.get_outputstream(), west_package);
						Thread thread = new Thread(writer);
						
						shell.getDisplay().asyncExec(thread);
						
					} catch (IOException e1) {
						System.err.println(e.toString());
					}
				}
			}
		});
		GridData gd_send_btn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_send_btn.widthHint = 100;
		gd_send_btn.minimumWidth = 100;
		send_btn.setLayoutData(gd_send_btn);
		send_btn.setText("Send");
		
		Button close_btn = new Button(composite, SWT.NONE);
		close_btn.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				shell.close();
			}
		});
		GridData gd_close_btn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_close_btn.widthHint = 100;
		close_btn.setLayoutData(gd_close_btn);
		close_btn.setText("Close");
		
		//Custom creations
		fill_action_combo();

	}
	public Combo getAction_combo() {
		return action_combo;
	}
	
	private void fill_action_combo() {
		for (keys key : keys.values()) {
			getAction_combo().add(key.get_name());
		}
		getAction_combo().select(0);
	}
	
	private byte[] get_data_composition()
	{
		String str = data_compose_text.getText();
		
		byte[] value = new byte[61];
		if (str.length() > 0)
		{
			for ( int i = 0 ; str.length() > i ; ++i )
			{
				value[i] = (byte) str.charAt(i);
			}
		}
			
		return value;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		try {
//			System.out.println("INCOMMING!!!");
			serial_lib.com_listener horcher = new serial_lib.com_listener(Server.serial_com, Server.serial_com.get_inputstream());
//			horcher.run();
			Thread read = new Thread(horcher);
			shell.getDisplay().asyncExec(read);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
