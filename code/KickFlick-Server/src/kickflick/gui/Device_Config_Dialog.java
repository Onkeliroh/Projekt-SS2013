package kickflick.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class Device_Config_Dialog extends Dialog {

	protected Object result;
	protected Shell shlDeviceConfugurationDialog;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Device_Config_Dialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlDeviceConfugurationDialog.open();
		shlDeviceConfugurationDialog.layout();
		Display display = getParent().getDisplay();
		while (!shlDeviceConfugurationDialog.isDisposed()) {
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
		shlDeviceConfugurationDialog = new Shell(getParent(), getStyle());
		shlDeviceConfugurationDialog.setSize(450, 300);
		shlDeviceConfugurationDialog.setText("Device Confuguration Dialog");
		shlDeviceConfugurationDialog.setLayout(new GridLayout(1, false));
		
		Group grpDefaultConfigurations = new Group(shlDeviceConfugurationDialog, SWT.NONE);
		grpDefaultConfigurations.setLayout(new GridLayout(2, false));
		grpDefaultConfigurations.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		grpDefaultConfigurations.setText("Default Configurations");
		
		Combo preset_pers_combo = new Combo(grpDefaultConfigurations, SWT.NONE);
		preset_pers_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnApply = new Button(grpDefaultConfigurations, SWT.NONE);
		GridData gd_btnApply = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnApply.widthHint = 100;
		gd_btnApply.horizontalIndent = 1;
		gd_btnApply.minimumWidth = 200;
		btnApply.setLayoutData(gd_btnApply);
		btnApply.setText("Apply");
		
		Group grpConfuguration = new Group(shlDeviceConfugurationDialog, SWT.NONE);
		grpConfuguration.setText("Confuguration");
		grpConfuguration.setLayout(new GridLayout(3, false));
		grpConfuguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label name_lable = new Label(grpConfuguration, SWT.NONE);
		name_lable.setText("Name:");
		
		text = new Text(grpConfuguration, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(grpConfuguration, SWT.NONE);
		
		Label state_lable = new Label(grpConfuguration, SWT.NONE);
		state_lable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		state_lable.setText("State:");
		
		Combo state_combo = new Combo(grpConfuguration, SWT.READ_ONLY);
		state_combo.setItems(new String[] {"Standby", "First contact", "Playing", "Playing (hard)"});
		state_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		state_combo.select(0);
		new Label(grpConfuguration, SWT.NONE);
		
		Composite composite = new Composite(grpConfuguration, SWT.NONE);
		GridData gd_composite = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 3, 1);
		gd_composite.heightHint = 48;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnApply_1 = new Button(composite, SWT.NONE);
		btnApply_1.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
		btnApply_1.setText("Apply");
		
		Button btnClose = new Button(composite, SWT.NONE);
		btnClose.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
		btnClose.setText("Close");

	}
}
