package kickflick.gui;

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

public class communicator extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text data_compose_text;
	
	private server Server;

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
		shell.setSize(748, 233);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Group grpComposeMessage = new Group(shell, SWT.NONE);
		grpComposeMessage.setLayout(new GridLayout(4, false));
		grpComposeMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpComposeMessage.setText("Compose Header");
		
		Label lblReciever = new Label(grpComposeMessage, SWT.NONE);
		lblReciever.setText("Receiver");
		
		Label lblKey = new Label(grpComposeMessage, SWT.NONE);
		lblKey.setText("Key");
		
		Label lblElement = new Label(grpComposeMessage, SWT.NONE);
		lblElement.setText("Element");
		
		Label lblAction = new Label(grpComposeMessage, SWT.NONE);
		lblAction.setText("Action");
		
		Combo receiver_combo = new Combo(grpComposeMessage, SWT.NONE);
		GridData gd_receiver_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_receiver_combo.widthHint = 50;
		receiver_combo.setLayoutData(gd_receiver_combo);
		
		Combo key_combo = new Combo(grpComposeMessage, SWT.READ_ONLY);
		key_combo.setItems(new String[] {"test", "test", "test", "test"});
		GridData gd_key_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_key_combo.widthHint = 50;
		key_combo.setLayoutData(gd_key_combo);
		
		Combo element_combo = new Combo(grpComposeMessage, SWT.READ_ONLY);
		element_combo.setItems(new String[] {"test", "test", "test", "test"});
		GridData gd_element_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_element_combo.widthHint = 50;
		element_combo.setLayoutData(gd_element_combo);
		
		Combo action_combo = new Combo(grpComposeMessage, SWT.NONE);
		action_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpComposeData = new Group(shell, SWT.NONE);
		grpComposeData.setText("Compose Message");
		grpComposeData.setLayout(new GridLayout(1, false));
		grpComposeData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		data_compose_text = new Text(grpComposeData, SWT.BORDER);
		data_compose_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button send_btn = new Button(composite, SWT.NONE);
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

	}
	
	//ToDo:fill Combos with informations
	//ToDo: generate Byte Array from Selection
}
