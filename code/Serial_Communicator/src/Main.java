import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.custom.ScrolledComposite;


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
		fd_grpSetup.top = new FormAttachment(0, 10);
		fd_grpSetup.left = new FormAttachment(0, 10);
		fd_grpSetup.bottom = new FormAttachment(100, -292);
		fd_grpSetup.right = new FormAttachment(0, 843);
		grpSetup.setLayoutData(fd_grpSetup);
		
		Combo combo = new Combo(grpSetup, SWT.NONE);
		combo.setBounds(81, 26, 185, 25);
		
		Combo combo_1 = new Combo(grpSetup, SWT.NONE);
		combo_1.setBounds(81, 57, 185, 25);
		
		Label lblDevice = new Label(grpSetup, SWT.NONE);
		lblDevice.setBounds(10, 26, 65, 15);
		lblDevice.setText("Device:");
		
		Label lblBps = new Label(grpSetup, SWT.NONE);
		lblBps.setBounds(10, 57, 65, 15);
		lblBps.setText("Bps:");
		
		message_field = new Text(container, SWT.BORDER | SWT.CENTER);
		message_field.setToolTipText("type your message here");
		FormData fd_message_field = new FormData();
		fd_message_field.right = new FormAttachment(grpSetup, 0, SWT.RIGHT);
		fd_message_field.top = new FormAttachment(grpSetup, 6);
		fd_message_field.left = new FormAttachment(0, 10);
		message_field.setLayoutData(fd_message_field);
		
		Button send_btn = new Button(container, SWT.NONE);
		send_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		FormData fd_send_btn = new FormData();
		fd_send_btn.right = new FormAttachment(grpSetup, 0, SWT.RIGHT);
		fd_send_btn.top = new FormAttachment(message_field, 6);
		fd_send_btn.left = new FormAttachment(0, 10);
		send_btn.setLayoutData(fd_send_btn);
		send_btn.setText("Send");

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
		return new Point(450, 300);
	}
}
