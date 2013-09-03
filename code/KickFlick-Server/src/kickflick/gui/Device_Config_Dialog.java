package kickflick.gui;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import kickflick.device.*;

public class Device_Config_Dialog extends Dialog {

	protected Object result;
	protected Shell shlDeviceConfugurationDialog;
	private Text name_text;
    private Combo state_combo;
    private Combo preset_pers_combo;

    private Label name_Label;


    private device Device;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Device_Config_Dialog(Shell parent, int style, device Dev ) {
		super(parent, style);
		setText("SWT Dialog");


        this.Device = Dev;
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
//		shlDeviceConfugurationDialog = new Shell(getParent(), getStyle());
        shlDeviceConfugurationDialog = new Shell(getParent(), SWT.CLOSE | SWT.MAX | SWT.MIN);
        shlDeviceConfugurationDialog.setSize(450, 300);
		shlDeviceConfugurationDialog.setText("Device Confuguration Dialog");
		shlDeviceConfugurationDialog.setLayout(new GridLayout(1, false));
		
		Group grpDefaultConfigurations = new Group(shlDeviceConfugurationDialog, SWT.NONE);
		grpDefaultConfigurations.setLayout(new GridLayout(2, false));
		grpDefaultConfigurations.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		grpDefaultConfigurations.setText("Default Configurations");
		
		preset_pers_combo = new Combo(grpDefaultConfigurations, SWT.READ_ONLY);
		preset_pers_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        preset_pers_combo.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                set_pre_pers(presetpersonalities.valueOf(preset_pers_combo.getItem(preset_pers_combo.getSelectionIndex())).get_personality());
            }
        });

		Group grpConfuguration = new Group(shlDeviceConfugurationDialog, SWT.NONE);
		grpConfuguration.setText("Confuguration");
		grpConfuguration.setLayout(new GridLayout(3, false));
		grpConfuguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label name_label = new Label(grpConfuguration, SWT.NONE);
		name_label.setText("Name:");
		
		name_text = new Text(grpConfuguration, SWT.BORDER);
		name_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(grpConfuguration, SWT.NONE);
		
		Label state_label = new Label(grpConfuguration, SWT.NONE);
		state_label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		state_label.setText("State:");
		
		state_combo = new Combo(grpConfuguration, SWT.READ_ONLY);
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
        btnClose.addMouseListener(new MouseAdapter()
        {
            public void mouseDown(MouseEvent e) {
                shlDeviceConfugurationDialog.close();
            }
        });


        load_device();
	}

    private void load_device()
    {
        //load pre_set_personalities
        for ( int i = 0; i < presetpersonalities.values().length; ++i)
            this.preset_pers_combo.add(presetpersonalities.values()[i].name(),i);
        this.preset_pers_combo.select(0);


        //load device informations
        this.name_text.setText(this.Device.get_Personality().get_Name());

        for ( int i = 0; i< this.Device.get_Personality().state_count; ++i)
            this.state_combo.add(this.Device.get_Personality().get_state_name((short)i),i);

        this.state_combo.select(this.Device.get_Personality().get_State());
    }

    private void set_pre_pers(personality pers)
    {
        this.name_text.setText(pers.get_Name());

        for ( int i = 0; i< pers.state_count; ++i)
            this.state_combo.add(pers.get_state_name((short)i),i);

        this.state_combo.select(pers.get_State());
    }

}
