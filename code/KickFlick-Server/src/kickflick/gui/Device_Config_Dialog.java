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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.Map;

public class Device_Config_Dialog extends Dialog {

	protected Object result;
	protected Shell shlDeviceConfugurationDialog;
	private Text name_text;
    private Combo state_combo;
    private Combo preset_pers_combo;

    private Label name_Label;


    private device Device;
    private Table trigger_table;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Device_Config_Dialog(Shell parent, int style, device Dev ) {
		super(parent, SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
		setText("SWT Dialog");


        this.Device = Dev;
        System.out.println(this.Device.get_trigger_map().toString());
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
        shlDeviceConfugurationDialog = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        shlDeviceConfugurationDialog.setSize(450, 300);
		shlDeviceConfugurationDialog.setText("Device Confuguration Dialog");
		GridLayout gl_shlDeviceConfugurationDialog = new GridLayout(1, false);
		shlDeviceConfugurationDialog.setLayout(gl_shlDeviceConfugurationDialog);
		
		Group grpDefaultConfigurations = new Group(shlDeviceConfugurationDialog, SWT.NONE);
		grpDefaultConfigurations.setLayout(new GridLayout(2, false));
		grpDefaultConfigurations.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		grpDefaultConfigurations.setText("Default Configurations");
		
		preset_pers_combo = new Combo(grpDefaultConfigurations, SWT.READ_ONLY);
		preset_pers_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpDefaultConfigurations, SWT.NONE);
        preset_pers_combo.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                set_pre_pers(presetpersonalities.valueOf(preset_pers_combo.getItem(preset_pers_combo.getSelectionIndex())).get_personality());
            }
        });

		Group grpConfuguration = new Group(shlDeviceConfugurationDialog, SWT.NONE);
		grpConfuguration.setText("Confuguration");
		grpConfuguration.setLayout(new GridLayout(5, false));
		grpConfuguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label name_label = new Label(grpConfuguration, SWT.NONE);
		name_label.setText("Name:");
		new Label(grpConfuguration, SWT.NONE);
		
		name_text = new Text(grpConfuguration, SWT.BORDER);
		name_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(grpConfuguration, SWT.NONE);
		new Label(grpConfuguration, SWT.NONE);
		
		Label state_label = new Label(grpConfuguration, SWT.NONE);
		state_label.setText("State:");
		new Label(grpConfuguration, SWT.NONE);
		
		state_combo = new Combo(grpConfuguration, SWT.READ_ONLY);
		state_combo.setItems(new String[]{"Standby", "First contact", "Playing", "Playing (hard)"});
		state_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		state_combo.select(0);
		new Label(grpConfuguration, SWT.NONE);
		new Label(grpConfuguration, SWT.NONE);
		
		trigger_table = new Table(grpConfuguration, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		GridData gd_trigger_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2);
		gd_trigger_table.widthHint = 242;
		gd_trigger_table.heightHint = 61;
		trigger_table.setLayoutData(gd_trigger_table);
		trigger_table.setHeaderVisible(true);
		trigger_table.setLinesVisible(true);
		
		TableColumn tblclmnAction = new TableColumn(trigger_table, SWT.NONE);
		tblclmnAction.setWidth(100);
		tblclmnAction.setText("Action");
		
		Composite composite = new Composite(grpConfuguration, SWT.NONE);
		GridData gd_composite = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 2, 1);
		gd_composite.heightHint = 48;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnApply = new Button(composite, SWT.NONE);
		btnApply.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
		btnApply.setText("Apply");
        btnApply.addMouseListener( new MouseAdapter()
        {
            public void mouseDown(MouseEvent e) {
                personality tmp_pers = new personality(
                        name_text.getText(),
                        (short)0,            //TODO make state configurable
                        new byte[] { 1, 1, 1, 1 },    //Color 1
                        new byte[] { 1, 1, 1, 1 },    //Color 2
                        new byte[] { 1, 1, 1, 1 }
                );

                Device = new device(
                    tmp_pers,
                    (byte) 0,
                    (byte) 0
                ); //TODO make call by reverence
            }
        });
		
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

        for (Map.Entry<kickflick.utility.keys,Boolean> entry : this.Device.get_trigger_map().entrySet())
        {
            TableItem tableItem = new TableItem(this.trigger_table, SWT.NONE);   //TODO evtl. tabelle vorher löschen
            tableItem.setText(entry.getKey().get_name());
            if ( entry.getValue() )
                tableItem.setChecked(true);
            else
                tableItem.setChecked(false);
        }
    }

    private void set_pre_pers(personality pers)
    {
        this.name_text.setText(pers.get_Name());

        for ( int i = 0; i< pers.state_count; ++i)
            this.state_combo.add(pers.get_state_name((short)i),i);

        this.state_combo.select(pers.get_State());
    }

}
