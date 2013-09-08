package kickflick.gui;

import kickflick.utility.color;
import kickflick.utility.keys;
import kickflick.utility.pattern;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;

import kickflick.device.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class Device_Config_Dialog extends Dialog {

	protected Object result;
	protected Shell shlDeviceConfugurationDialog;
	private Text name_text;
    private Combo state_combo;
    private Combo preset_pers_combo;

    private device Device;
    private Table trigger_table;
    private Text sensor_adr_text;
    private Text actuator_adr_text;
    private Table state_table;
    
    private final String[] state_names = {"Standby", "First contact", "Playing", "Playing (hard)"};
    private final String[] tabel_heads = {"State","Pattern","1. Color", "2. Color"};

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Device_Config_Dialog(Shell parent, int style, device Dev) {
		super(parent, SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
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
        shlDeviceConfugurationDialog = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        shlDeviceConfugurationDialog.setSize(515, 466);
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
				
				TabFolder tabFolder = new TabFolder(shlDeviceConfugurationDialog, SWT.NONE);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
				TabItem Basic_tab = new TabItem(tabFolder, SWT.NONE);
				Basic_tab.setText("Basic");
		
				Composite Config_composite = new Composite(tabFolder, SWT.NONE);
				Basic_tab.setControl(Config_composite);
				Config_composite.setLayout(new GridLayout(7, false));
				new Label(Config_composite, SWT.NONE);
				
				Label name_label = new Label(Config_composite, SWT.NONE);
				name_label.setText("Name:");
				
				name_text = new Text(Config_composite, SWT.BORDER);
				name_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
				new Label(Config_composite, SWT.NONE);
				new Label(Config_composite, SWT.NONE);
				
				Button btnIKnowWhat = new Button(Config_composite, SWT.CHECK);
				btnIKnowWhat.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});
				btnIKnowWhat.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				btnIKnowWhat.setText("I know what i am doing");
				new Label(Config_composite, SWT.NONE);
				
				Label state_label = new Label(Config_composite, SWT.NONE);
				state_label.setText("State:");
				
				state_combo = new Combo(Config_composite, SWT.READ_ONLY);
				state_combo.setItems(state_names);
				state_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				state_combo.select(0);
				
				Label lblSenderId = new Label(Config_composite, SWT.NONE);
				lblSenderId.setText("Sender Id:");
				
				sensor_adr_text = new Text(Config_composite, SWT.BORDER | SWT.READ_ONLY);
				sensor_adr_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label lblActuatorId = new Label(Config_composite, SWT.NONE);
				lblActuatorId.setText("Actuator Id:");
				
				actuator_adr_text = new Text(Config_composite, SWT.BORDER | SWT.READ_ONLY);
				actuator_adr_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				new Label(Config_composite, SWT.NONE);
				
				trigger_table = new Table(Config_composite, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
				GridData gd_trigger_table = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1);
				gd_trigger_table.widthHint = 242;
				gd_trigger_table.heightHint = 61;
				trigger_table.setLayoutData(gd_trigger_table);
				trigger_table.setHeaderVisible(true);
				trigger_table.setLinesVisible(true);
				
				TableColumn tblclmnAction = new TableColumn(trigger_table, SWT.NONE);
				tblclmnAction.setWidth(100);
				tblclmnAction.setText("Action");
				
				TabItem Personality_tab = new TabItem(tabFolder, SWT.NONE);
				Personality_tab.setText("State-Options");
				
				Composite state_composite = new Composite(tabFolder, SWT.NONE);
				Personality_tab.setControl(state_composite);
				state_composite.setLayout(new GridLayout(1, false));
				
				state_table = new Table(state_composite, SWT.BORDER | SWT.FULL_SELECTION);
				state_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				state_table.setHeaderVisible(true);
				state_table.setLinesVisible(true);
				
				
				
				//TODO fix column alignment
				for (int i = 0; i < 4; i++) 
				{
					TableColumn column = new TableColumn(state_table, SWT.CENTER);
					column.setText(tabel_heads[i]);
					column.setWidth(100);
				}
				
				for (int i = 0; i < state_names.length; i++) 
				{
					new TableItem(state_table, SWT.NONE);
			    }
				TableItem[] items = state_table.getItems();
			    for (int i = 0; i < items.length; i++) 
			    {
					TableEditor editor = new TableEditor(state_table);
					Text text = new Text(state_table, SWT.NONE);
					text.setText(state_names[i]);
					editor.grabHorizontal = true;
					editor.setEditor(text, items[i], 0);
					
					editor = new TableEditor(state_table);
					CCombo pattern_combo = new CCombo(state_table, SWT.NONE);
					for ( pattern p : pattern.values())
						pattern_combo.add(p.get_name());
					pattern_combo.select(0);
					editor.grabHorizontal = true;
					editor.setEditor(pattern_combo, items[i], 1);
					
					editor = new TableEditor(state_table);
					CCombo color1_combo = new CCombo(state_table, SWT.NONE);
					for (color c : color.values())
						color1_combo.add(c.get_name());
					color1_combo.select(0);
					editor.grabHorizontal = true;
					editor.setEditor(color1_combo, items[i], 2);
					
					editor = new TableEditor(state_table);
					CCombo color2_combo = new CCombo(state_table, SWT.NONE);
					for (color c : color.values())
						color2_combo.add(c.get_name());
					color2_combo.select(0);
					editor.grabHorizontal = true;
					editor.setEditor(color2_combo, items[i], 3);
			    }
			    
			    
		
		Composite composite = new Composite(shlDeviceConfugurationDialog, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, true));
		
		Button btnApply = new Button(composite, SWT.NONE);
		btnApply.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		btnApply.setText("Apply");
		btnApply.addMouseListener( new MouseAdapter()
		{
		    public void mouseDown(MouseEvent e) {

		        Map<kickflick.utility.keys,Boolean> trigger = new HashMap<keys,Boolean>();

		        trigger = Device.get_trigger_map();

		        int i = 0;
		        for (Map.Entry<kickflick.utility.keys,Boolean> entry : trigger.entrySet())
		        {
		            if ( entry.getKey().get_name() == trigger_table.getItem(i).getText())
		                entry.setValue(trigger_table.getItem(i).getChecked());
		        }

		        personality tmp_pers = new personality(
		                name_text.getText(),
		                (short)state_combo.getSelectionIndex(),
		                new byte[] { 1, 1, 1, 1 },    //Color 1
		                new byte[] { 1, 1, 1, 1 },    //Color 2
		                new byte[] { 1, 1, 1, 1 }     //Pattern
		        );

		        Device = new device(
		            tmp_pers,
		            (byte) 0,
		            (byte) 0,
		            trigger
		        );
		    }
		});
		
		Button btnClose = new Button(composite, SWT.RIGHT);
		btnClose.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
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

        this.state_combo.removeAll();
        for ( int i = 0; i< this.Device.get_Personality().state_count; ++i)
            this.state_combo.add(this.Device.get_Personality().get_state_name((short)i),i);

        this.state_combo.select(this.Device.get_Personality().get_State());


        this.trigger_table.removeAll();
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

    public device get_device()
    {
        return this.Device;
    }
}
