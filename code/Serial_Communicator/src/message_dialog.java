import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class message_dialog extends Dialog {

	protected Object result;
	protected Shell shlComposeMessage;
	private Text message_text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public message_dialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlComposeMessage.open();
		shlComposeMessage.layout();
		Display display = getParent().getDisplay();
		while (!shlComposeMessage.isDisposed()) {
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
		shlComposeMessage = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shlComposeMessage.setSize(450, 452);
		shlComposeMessage.setText("Compose Message of Doom");
		shlComposeMessage.setLayout(new FormLayout());
		
		Button btnClose = new Button(shlComposeMessage, SWT.NONE);
		btnClose.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				shlComposeMessage.close();
			}
		});
		FormData fd_btnClose = new FormData();
		fd_btnClose.bottom = new FormAttachment(100, -10);
		fd_btnClose.right = new FormAttachment(100, -10);
		fd_btnClose.left = new FormAttachment(100, -115);
		btnClose.setLayoutData(fd_btnClose);
		btnClose.setText("Close");
		
		Group grpComposeMessage = new Group(shlComposeMessage, SWT.NONE);
		grpComposeMessage.setText("Compose Message");
		grpComposeMessage.setLayout(new GridLayout(2, false));
		FormData fd_grpComposeMessage = new FormData();
		fd_grpComposeMessage.top = new FormAttachment(0, 1);
		fd_grpComposeMessage.right = new FormAttachment(btnClose, 0, SWT.RIGHT);
		fd_grpComposeMessage.left = new FormAttachment(0, 10);
		grpComposeMessage.setLayoutData(fd_grpComposeMessage);
		
		Group grpSendMessage = new Group(shlComposeMessage, SWT.NONE);
		grpSendMessage.setText("Send Message");
		grpSendMessage.setLayout(new GridLayout(4, false));
		FormData fd_grpSendMessage = new FormData();
		fd_grpSendMessage.bottom = new FormAttachment(btnClose, -6);
		fd_grpSendMessage.left = new FormAttachment(0, 10);
		fd_grpSendMessage.right = new FormAttachment(100, -10);
		grpSendMessage.setLayoutData(fd_grpSendMessage);
		
		Group grpExsistingMessages = new Group(shlComposeMessage, SWT.NONE);
		fd_grpSendMessage.top = new FormAttachment(grpExsistingMessages, 6);
		fd_grpComposeMessage.bottom = new FormAttachment(100, -372);
		
		Label recipient_label = new Label(grpSendMessage, SWT.NONE);
		recipient_label.setText("Recipient :");
		
		Combo combo = new Combo(grpSendMessage, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Button btnSendToAll = new Button(grpSendMessage, SWT.CHECK);
		btnSendToAll.setText("send to all");
		
		Label lblScrollType = new Label(grpSendMessage, SWT.NONE);
		lblScrollType.setText("Scroll Type:");
		
		Button btnOnce = new Button(grpSendMessage, SWT.RADIO);
		btnOnce.setText("once");
		
		Button btnRadioButton = new Button(grpSendMessage, SWT.RADIO);
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnRadioButton.setText("repeat");
		new Label(grpSendMessage, SWT.NONE);
		
		Label lblMessage = new Label(grpComposeMessage, SWT.NONE);
		lblMessage.setText("Message:");
		
		message_text = new Text(grpComposeMessage, SWT.BORDER);
		message_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpExsistingMessages.setText("Exsisting Messages");
		grpExsistingMessages.setLayout(new FormLayout());
		FormData fd_grpExsistingMessages = new FormData();
		fd_grpExsistingMessages.top = new FormAttachment(grpComposeMessage, 6);
		fd_grpExsistingMessages.right = new FormAttachment(btnClose, 0, SWT.RIGHT);
		fd_grpExsistingMessages.left = new FormAttachment(0, 10);
		fd_grpExsistingMessages.bottom = new FormAttachment(100, -269);
		grpExsistingMessages.setLayoutData(fd_grpExsistingMessages);
		
		List list = new List(grpExsistingMessages, SWT.BORDER);
		FormData fd_list = new FormData();
		fd_list.top = new FormAttachment(0, 10);
		fd_list.left = new FormAttachment(0, 10);
		fd_list.bottom = new FormAttachment(100, -4);
		fd_list.right = new FormAttachment(100, -12);
		list.setLayoutData(fd_list);

	}
}
