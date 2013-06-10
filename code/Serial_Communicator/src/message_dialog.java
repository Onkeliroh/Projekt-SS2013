import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;


public class message_dialog extends Dialog {

	protected Object result;
	protected Shell shlComposeMessage;
	private Text message_text;
	private List message_list;

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
		shlComposeMessage.setMinimumSize(new Point(400, 400));
		shlComposeMessage.setSize(450, 561);
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
		grpComposeMessage.setLayout(new GridLayout(3, false));
		FormData fd_grpComposeMessage = new FormData();
		fd_grpComposeMessage.top = new FormAttachment(0, 10);
		fd_grpComposeMessage.right = new FormAttachment(btnClose, 0, SWT.RIGHT);
		fd_grpComposeMessage.left = new FormAttachment(0, 10);
		grpComposeMessage.setLayoutData(fd_grpComposeMessage);
		
		Group grpSendMessage = new Group(shlComposeMessage, SWT.NONE);
		grpSendMessage.setText("Send Message");
		grpSendMessage.setLayout(new GridLayout(4, false));
		FormData fd_grpSendMessage = new FormData();
		fd_grpSendMessage.bottom = new FormAttachment(btnClose, -6);
		fd_grpSendMessage.left = new FormAttachment(grpComposeMessage, 0, SWT.LEFT);
		fd_grpSendMessage.right = new FormAttachment(100, -10);
		grpSendMessage.setLayoutData(fd_grpSendMessage);
		
		Group grpExsistingMessages = new Group(shlComposeMessage, SWT.NONE);
		fd_grpSendMessage.top = new FormAttachment(grpExsistingMessages, 6);
		fd_grpComposeMessage.bottom = new FormAttachment(grpExsistingMessages, -6);
		
		Label recipient_label = new Label(grpSendMessage, SWT.NONE);
		recipient_label.setText("Recipient :");
		
		Combo combo = new Combo(grpSendMessage, SWT.READ_ONLY);
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
		
		Button btnSend = new Button(grpSendMessage, SWT.NONE);
		btnSend.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 4, 1));
		btnSend.setText("Send");
		
		Label lblMessage = new Label(grpComposeMessage, SWT.NONE);
		lblMessage.setText("Message:");
		
		message_text = new Text(grpComposeMessage, SWT.BORDER);
		message_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpExsistingMessages.setText("Exsisting Messages");
		FormData fd_grpExsistingMessages = new FormData();
		fd_grpExsistingMessages.top = new FormAttachment(0, 82);
		fd_grpExsistingMessages.bottom = new FormAttachment(100, -209);
		fd_grpExsistingMessages.right = new FormAttachment(btnClose, 0, SWT.RIGHT);
		fd_grpExsistingMessages.left = new FormAttachment(0, 10);
		
		Button message_save_btn = new Button(grpComposeMessage, SWT.NONE);
		message_save_btn.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (message_text.getText() != "")
				{
					try {
						write_message_to_list(message_text.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
						System.err.println(e1.toString());
					}
					
					getList().removeAll();
					try {
						load_message_from_list();
					} catch (IOException e1) {
						e1.printStackTrace();
						System.err.println(e1.toString());
					}
				}
			}
		});
		message_save_btn.setText("Save");
		grpExsistingMessages.setLayout(new GridLayout(2, false));
		grpExsistingMessages.setLayoutData(fd_grpExsistingMessages);
		
		message_list = new List(grpExsistingMessages, SWT.BORDER | SWT.V_SCROLL);
		message_list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				message_text.setText(message_list.getItem(message_list.getSelectionIndex()));
			}
		});
		message_list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button message_delete_btn = new Button(grpExsistingMessages, SWT.NONE);
		message_delete_btn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		message_delete_btn.setText("Delete");
		
		try {
			load_message_from_list();
		} catch (IOException e1) {
			System.err.println(e1.toString());
			e1.printStackTrace();
		}

	}
	public List getList() {
		return message_list;
	}
	
	private void load_message_from_list() throws IOException 
	{
		if ( new File("res/messages.txt").exists())
		{
			BufferedReader reader = new BufferedReader( new FileReader ("res/messages.txt"));
		
			String line = null;
			
			while ((line = reader.readLine()) != null )
			{
				getList().add(line);
			}
			
			reader.close();
		}
		else
		{
			System.err.println("message.txt wasn't found");
		}
	}
	
	private void write_message_to_list(String str) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter("res/messages.txt", true));
		bw.write(str);
		bw.newLine();
		bw.flush();
		
		bw.close();
	}
	
	private void remove_message_from_list(int linenr) throws IOException
	{

		if ( new File("res/messages.txt").exists())
		{
			BufferedReader reader = new BufferedReader( new FileReader("res/messages.txt"));
			BufferedWriter writer = new BufferedWriter( new FileWriter("res/messages.txt"));
		
			String line = null;
			
			System.out.print(reader.toString());
			
			reader.close();
			writer.close();
		}
		else
		{
			System.err.println("message.txt wasn't found");
		}
	}
}
