import java.util.List;

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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;


public class main_window extends ApplicationWindow {

	/**
	 * Create the application window.
	 */
	public main_window() {
		super(null);
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
		container.setLayout(new GridLayout(3, false));
		{
			Label AvailableDevices_label = new Label(container, SWT.NONE);
			AvailableDevices_label.setText("available Devices:");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			Combo device_combo_list = new Combo(container, SWT.NONE);
			
			List<String> port_names = rxtx_basic_lib.get_port_names();
			
			if (port_names != null)
			{
				if (!port_names.isEmpty())
				{
					for (int i = 0; port_names.size() > i; i++)
					{
						device_combo_list.add(port_names.get(i));
					}
				}
				device_combo_list.select(0);
			}
		}
		new Label(container, SWT.NONE);
		{
			Button Connect_button = new Button(container, SWT.NONE);
			Connect_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			Connect_button.setText("Connect to");
		}
		{
			Label connection_status_label = new Label(container, SWT.NONE);
			connection_status_label.setText("Connection:");
		}
		new Label(container, SWT.NONE);
		{
			Label connection_status = new Label(container, SWT.NONE);
		}

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
			main_window window = new main_window();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//custom
		get_devices();
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application of Doom");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	public static void get_devices()
	{
		List<String> port_names = rxtx_basic_lib.get_port_names();
	}

}
