package kickflick.gui;

import kickflick.device.device;
import kickflick.device.personality;
import kickflick.device.presetpersonalities;
import kickflick.device.reaction;
import kickflick.utility.color;
import kickflick.utility.keys;
import kickflick.utility.pattern;

import org.eclipse.help.ui.internal.views.ComboPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class Device_Config_Dialog extends Dialog
{

    protected device result;
    private device Device;
    private int pers_count;
    private List<String> neighbor_pers = new ArrayList<String>();

    protected Shell shlDeviceConfugurationDialog;
    private Text name_text;
    private Combo preset_pers_combo;
    private Combo state_combo;


    private Table trigger_table;
    private Text sensor_adr_text;
    private Text actuator_adr_text;

    private String[] state_names = {};
    private final String[] tabel_heads = {"State", "Pattern", "1. Color", "2. Color"};
    private final String[] neighbor_table_heads = {"Neighbor", "Pattern", "1. Color", "2. Color"};

    private List<Combo> pattern_combo_list = new ArrayList<Combo>();
    private List<CCombo> color1_combo_list = new ArrayList<CCombo>();
    private List<CCombo> color2_combo_list = new ArrayList<CCombo>();

    private List<Combo> neighbor_pattern_combo_list = new ArrayList<Combo>();
    private List<CCombo> neighbor_color1_combo_list = new ArrayList<CCombo>();
    private List<CCombo> neighbor_color2_combo_list = new ArrayList<CCombo>();
    private List<Text> neighbor_text_list = new ArrayList<Text>();
    private Table standby_table;
    private Combo key_select_combo;

    private boolean pre_set_selected = false;


    public Device_Config_Dialog(Shell parent, int style, device Dev, List<String> neighbor_pers) {
        super(parent, SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
        setText("SWT Dialog");

        this.Device = Dev;
        this.neighbor_pers = neighbor_pers;
        this.pers_count = this.neighbor_pers.size();
        this.state_names = this.Device.get_Personality().get_state_names();
    }

    /**
     * Open the dialog.
     *
     * @return the result
     */
    public Object open() {
        createContents();
        shlDeviceConfugurationDialog.open();
        shlDeviceConfugurationDialog.layout();
        Display display = getParent().getDisplay();
        while (!shlDeviceConfugurationDialog.isDisposed())
        {
            if (!display.readAndDispatch())
            {
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
        shlDeviceConfugurationDialog.setSize(641, 522);
        shlDeviceConfugurationDialog.setText("Device Confuguration Dialog");
        GridLayout gl_shlDeviceConfugurationDialog = new GridLayout(1, false);
        shlDeviceConfugurationDialog.setLayout(gl_shlDeviceConfugurationDialog);
        
        result = this.Device;

        TabFolder tabFolder = new TabFolder(shlDeviceConfugurationDialog, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        TabItem Basic_tab = new TabItem(tabFolder, SWT.NONE);
        Basic_tab.setText("Basic");

        Composite Config_composite = new Composite(tabFolder, SWT.NONE);
        Basic_tab.setControl(Config_composite);
        Config_composite.setLayout(new GridLayout(8, false));
        
        Group grpDefaultConfigurations = new Group(Config_composite, SWT.NONE);
        GridData gd_grpDefaultConfigurations = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_grpDefaultConfigurations.widthHint = 267;
        grpDefaultConfigurations.setLayoutData(gd_grpDefaultConfigurations);
        grpDefaultConfigurations.setLayout(new GridLayout(2, false));
        grpDefaultConfigurations.setText("Default Configurations");
                
        preset_pers_combo = new Combo(grpDefaultConfigurations, SWT.READ_ONLY);
        preset_pers_combo.setToolTipText("Selects a pre configured personality. Please be carefull. This will overwrite your current settings!!!");
        preset_pers_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(grpDefaultConfigurations, SWT.NONE);
        preset_pers_combo.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e) {
                pre_set_selected = true;
                set_pre_pers(presetpersonalities.valueOf(preset_pers_combo.getItem(preset_pers_combo.getSelectionIndex())).get_personality());
            }
        });
        new Label(Config_composite, SWT.NONE);
        
        Label label = new Label(Config_composite, SWT.SEPARATOR | SWT.VERTICAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
        new Label(Config_composite, SWT.NONE);
        
        Group grpCurrentState = new Group(Config_composite, SWT.BORDER | SWT.SHADOW_NONE);
        grpCurrentState.setText("Current State");
        grpCurrentState.setLayout(new GridLayout(1, false));
        grpCurrentState.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        state_combo = new Combo(grpCurrentState, SWT.READ_ONLY);
        state_combo.addSelectionListener(new SelectionAdapter() {        	
        	public void widgetSelected(SelectionEvent e) {
        		result.get_Personality().set_State((short) state_combo.getSelectionIndex());
        	}
        });
        state_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        new Label(Config_composite, SWT.NONE);

        Label name_label = new Label(Config_composite, SWT.NONE);
        name_label.setText("Name:");

        name_text = new Text(Config_composite, SWT.BORDER);
        name_text.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent arg0) {
        		result.get_Personality().set_Name(name_text.getText());
        	}
        });
        GridData gd_name_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_name_text.widthHint = 200;
        name_text.setLayoutData(gd_name_text);
        new Label(Config_composite, SWT.NONE);
        new Label(Config_composite, SWT.NONE);

        Label lblActuatorId = new Label(Config_composite, SWT.NONE);
        lblActuatorId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblActuatorId.setText("Actuator Id:");

        actuator_adr_text = new Text(Config_composite, SWT.BORDER | SWT.READ_ONLY);
        actuator_adr_text.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent arg0) {
        		result.set_actuator_node((byte) Integer.parseInt(actuator_adr_text.getText()));
        	}
        });
        actuator_adr_text.setToolTipText("Sets the adress of the actuator node.");
        actuator_adr_text.setEditable(true);
        GridData gd_actuator_adr_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_actuator_adr_text.widthHint = 100;
        actuator_adr_text.setLayoutData(gd_actuator_adr_text);
        
        new Label(Config_composite, SWT.NONE);
        new Label(Config_composite, SWT.NONE);
        new Label(Config_composite, SWT.NONE);
        new Label(Config_composite, SWT.NONE);
        new Label(Config_composite, SWT.NONE);


        Label lblSenderId = new Label(Config_composite, SWT.NONE);
        lblSenderId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblSenderId.setText("Sensor Id:");

        sensor_adr_text = new Text(Config_composite, SWT.BORDER | SWT.READ_ONLY);
        sensor_adr_text.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent arg0) {
        		result.set_sensor_node((byte) Integer.parseInt(sensor_adr_text.getText()));
        	}
        });
        sensor_adr_text.setToolTipText("Sets the adress of the sensor node.");
        sensor_adr_text.setEditable(true);
        GridData gd_sensor_adr_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_sensor_adr_text.widthHint = 100;
        sensor_adr_text.setLayoutData(gd_sensor_adr_text);
        new Label(Config_composite, SWT.NONE);


        trigger_table = new Table(Config_composite, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
        trigger_table.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		String selected_item  = trigger_table.getSelection()[0].getText();
                for ( keys k : keys.values())
                    if ( k.get_name().equals(selected_item))
                        result.get_trigger_map().put(k, trigger_table.getSelection()[0].getChecked());
        	}
        });
        GridData gd_trigger_table = new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1);
        gd_trigger_table.widthHint = 242;
        gd_trigger_table.heightHint = 61;
        trigger_table.setLayoutData(gd_trigger_table);
        trigger_table.setHeaderVisible(true);
        trigger_table.setLinesVisible(true);

        TableColumn tblclmnAction = new TableColumn(trigger_table, SWT.NONE);
        tblclmnAction.setWidth(100);
        tblclmnAction.setText("Action");

        TabItem Personality_tab = new TabItem(tabFolder, SWT.NONE);
        Personality_tab.setText("Reaction-Options");

        Composite state_composite = new Composite(tabFolder, SWT.NONE);
        Personality_tab.setControl(state_composite);
        state_composite.setLayout(new GridLayout(3, false));
        
        Label lblStandbyReaction = new Label(state_composite, SWT.NONE);
        lblStandbyReaction.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
        lblStandbyReaction.setText("Standby Reaction:");
        
        standby_table = new Table(state_composite, SWT.BORDER | SWT.FULL_SELECTION);
        standby_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2));
        standby_table.setHeaderVisible(true);
        standby_table.setLinesVisible(true);

        Label label_1 = new Label(state_composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        
        Label lblSelectKey = new Label(state_composite, SWT.NONE);
        lblSelectKey.setText("Select Key:");
        
        //create and fill key combo box
        key_select_combo = new Combo(state_composite, SWT.READ_ONLY);
        key_select_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(state_composite, SWT.NONE);
        for ( keys k : keys.values())
        	key_select_combo.add(k.get_name());
        key_select_combo.select(0);
        key_select_combo.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	apply_state();
            	set_state_combos();
            }
        });
        

        //setup state table depending wether  pre set personality was selected or not
        key_select_combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if ( !pre_set_selected )
                    set_state_combos(Device.get_Personality());
                else
                    set_state_combos(presetpersonalities.valueOf(preset_pers_combo.getItem(preset_pers_combo.getSelectionIndex())).get_personality());
            }
        });

        Label lblStateTable = new Label(state_composite, SWT.NONE);
        lblStateTable.setText("Selected Reaction:");
        new Label(state_composite, SWT.NONE);
        new Label(state_composite, SWT.NONE);
        
        Table state_table = new Table(state_composite, SWT.BORDER | SWT.FULL_SELECTION);
        state_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2));
        state_table.setHeaderVisible(true);
        state_table.setLinesVisible(true);

        TabItem tbtmNeighbor = new TabItem(tabFolder, SWT.NONE);
        tbtmNeighbor.setText("Neighbor");

        Composite neighbor_composite = new Composite(tabFolder, SWT.NONE);
        tbtmNeighbor.setControl(neighbor_composite);
        neighbor_composite.setLayout(new GridLayout(1, false));

        Table neighbor_table = new Table(neighbor_composite, SWT.BORDER | SWT.FULL_SELECTION);
        neighbor_table.setToolTipText("This table allows you to configure the reacton to every currently known personality. Provided, this device detects the neighbor.");
        neighbor_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        neighbor_table.setHeaderVisible(true);
        neighbor_table.setLinesVisible(true);

        for (int i = 0; i < this.neighbor_table_heads.length; i++)
        {
            TableColumn column = new TableColumn(neighbor_table, SWT.CENTER);
            column.setText(neighbor_table_heads[i]);
            column.setWidth(100);
        }

        //create and fill neighbor table
        for (int i = 0; i < this.pers_count; i++)
        {
            new TableItem(neighbor_table, SWT.NONE);
        }
        TableItem[] neighbor_items = neighbor_table.getItems();
        for (int i = 0; i < neighbor_table.getItems().length; i++)
        {
            TableEditor editor = new TableEditor(neighbor_table);
            Text text = new Text(neighbor_table, SWT.NONE);
            text.setText(this.neighbor_pers.get(i));
            text.setEditable(false);
            text.setCapture(false);
            editor.grabHorizontal = true;
            editor.setEditor(text, neighbor_items[i], 0);
            neighbor_text_list.add(text);

            editor = new TableEditor(neighbor_table);
            final Combo pattern_combo = new Combo(neighbor_table, SWT.READ_ONLY);
            for (pattern p : pattern.values())
                pattern_combo.add(p.get_name());
            pattern_combo.select(0);
            editor.grabHorizontal = true;
            editor.setEditor(pattern_combo, neighbor_items[i], 1);
            neighbor_pattern_combo_list.add(pattern_combo);

            editor = new TableEditor(neighbor_table);
            CCombo color1_combo = new CCombo(neighbor_table, SWT.NONE);
            for (color c : color.values())
                color1_combo.add(c.get_name());
            color1_combo.setEditable(false);
            color1_combo.select(0);
            editor.grabHorizontal = true;
            editor.setEditor(color1_combo, neighbor_items[i], 2);
            neighbor_color1_combo_list.add(color1_combo);

            editor = new TableEditor(neighbor_table);
            CCombo color2_combo = new CCombo(neighbor_table, SWT.NONE);
            for (color c : color.values())
                color2_combo.add(c.get_name());
            color2_combo.setEditable(false);
            color2_combo.select(0);
            editor.grabHorizontal = true;
            editor.setEditor(color2_combo, neighbor_items[i], 3);
            neighbor_color2_combo_list.add(color2_combo);
        }


        for (int i = 0; i < 4; i++)
        {
            TableColumn column = new TableColumn(standby_table, SWT.CENTER);
            column.setText(tabel_heads[i]);
            column.setWidth(100);
        }

        new TableItem(standby_table, SWT.NONE);

        TableEditor standby_editor = new TableEditor(standby_table);
        Text standby_text = new Text(standby_table, SWT.NONE);
        standby_text.setText(this.state_names[1]);
        standby_text.setEditable(false);
        standby_text.setCapture(false);
        standby_editor.grabHorizontal = true;
        standby_editor.setEditor(standby_text, this.standby_table.getItem(0), 0);

        standby_editor = new TableEditor(standby_table);
        final Combo standby_pattern_combo = new Combo(standby_table, SWT.READ_ONLY);
        for (pattern p : pattern.values())
            standby_pattern_combo.add(p.get_name());

        standby_pattern_combo.select(0);
        standby_editor.grabHorizontal = true;
        standby_editor.setEditor(standby_pattern_combo, this.standby_table.getItem(0), 1);
        pattern_combo_list.add(standby_pattern_combo);

        standby_editor = new TableEditor(standby_table);
        CCombo standby_color1_combo = new CCombo(standby_table, SWT.NONE);
        for (color c : color.values())
            standby_color1_combo.add(c.get_name());
        standby_color1_combo.setEditable(false);
        standby_color1_combo.select(0);
        standby_editor.grabHorizontal = true;
        standby_editor.setEditor(standby_color1_combo, this.standby_table.getItem(0), 2);
        color1_combo_list.add(standby_color1_combo);

        standby_editor = new TableEditor(standby_table);
        CCombo standby_color2_combo = new CCombo(standby_table, SWT.NONE);
        for (color c : color.values())
            standby_color2_combo.add(c.get_name());
        standby_color2_combo.setEditable(false);
        standby_color2_combo.select(0);
        standby_editor.grabHorizontal = true;
        standby_editor.setEditor(standby_color2_combo, this.standby_table.getItem(0), 3);
        color2_combo_list.add(standby_color2_combo);
        
        standby_table.pack();

        //create state table and cell skeleton
        for (int i = 0; i < 4; i++)
        {
            TableColumn column = new TableColumn(state_table, SWT.CENTER);
            column.setText(tabel_heads[i]);
            column.setWidth(100);
        }

        for (int i = 0; i < (Device.get_Personality().state_count -1); i++)
        {
            new TableItem(state_table, SWT.NONE);
        }
        TableItem[] state_items = state_table.getItems();
        for (int i = 0; i < state_table.getItemCount(); i++)
        {
            TableEditor editor = new TableEditor(state_table);
            Text text = new Text(state_table, SWT.NONE);
            text.setText(this.state_names[i+2]);
            text.setEditable(false);
            text.setCapture(false);
            editor.grabHorizontal = true;
            editor.setEditor(text, state_items[i], 0);

            editor = new TableEditor(state_table);
            final Combo pattern_combo = new Combo(state_table, SWT.READ_ONLY);
            for (pattern p : pattern.values())
                pattern_combo.add(p.get_name());
            
            pattern_combo.select(0);
            editor.grabHorizontal = true;
            editor.setEditor(pattern_combo, state_items[i], 1);
            pattern_combo_list.add(pattern_combo);

            editor = new TableEditor(state_table);
            CCombo color1_combo = new CCombo(state_table, SWT.NONE);
            for (color c : color.values())
                color1_combo.add(c.get_name());
            color1_combo.setEditable(false);
            color1_combo.select(0);
            editor.grabHorizontal = true;
            editor.setEditor(color1_combo, state_items[i], 2);
            color1_combo_list.add(color1_combo);

            editor = new TableEditor(state_table);
            CCombo color2_combo = new CCombo(state_table, SWT.NONE);
            for (color c : color.values())
                color2_combo.add(c.get_name());
            color2_combo.setEditable(false);
            color2_combo.select(0);
            editor.grabHorizontal = true;
            editor.setEditor(color2_combo, state_items[i], 3);
            color2_combo_list.add(color2_combo);
        }

        Composite composite = new Composite(shlDeviceConfugurationDialog, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));
        composite.setLayout(new GridLayout(2, true));

        Button btnApply = new Button(composite, SWT.NONE);
        btnApply.setToolTipText("Apply the current settings the the device.");
        btnApply.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
        btnApply.setText("Apply");
        btnApply.addMouseListener(new MouseAdapter()
        {
            public void mouseDown(MouseEvent e) {
//                pattern p_setting = null;
//                color c1_setting = null;
//                color c2_setting = null;
//                
//                for (String str : neighbor_pers)
//                {
//                    for (int text = 0; text < neighbor_text_list.size(); ++text)
//                    {
//                        if (neighbor_text_list.get(text).getText().equalsIgnoreCase(str))
//                        {
//                            for (pattern p : pattern.values())
//                                if (p.get_name().equalsIgnoreCase(neighbor_pattern_combo_list.get(text).getText()))
//                                {
//                                   p_setting =  p;
//                                    break;
//                                }
//                            for (color c : color.values())
//                            {
//                                if (c.get_name().equalsIgnoreCase(neighbor_color1_combo_list.get(text).getText()))
//                                {
//                                    c1_setting = c;
//                                    break;
//                                }
//                            }
//                            for (color c : color.values())
//                                if (c.get_name().equalsIgnoreCase(neighbor_color2_combo_list.get(text).getText()))
//                                {
//                                    c2_setting = c;
//                                    break;
//                                }
//                        }
//                    }
//                    // if no entry of this personality exsists
//                    if (!Device.get_Personality().get_Neighbours().containsKey(str))
//                    {
//                        Device.get_Personality().get_Neighbours().put(str, new reaction(c1_setting, c2_setting, p_setting));
//                    }
//                    else //if personality allready exsistsgit
//                    {
//                        for (Map.Entry entry : Device.get_Personality().get_Neighbours().entrySet())
//                            if (entry.getKey().equals(str))
//                            {
//                                entry.setValue(new reaction(c1_setting, c2_setting, p_setting));
//                                break;
//                            }
//                    }
//                }
            }

        });

        Button btnClose = new Button(composite, SWT.RIGHT);
        btnClose.setToolTipText("Close and save the Settings.");
        btnClose.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
        btnClose.setText("Save|Close");

        btnClose.addMouseListener(new MouseAdapter()
        {
            public void mouseDown(MouseEvent e) {
                shlDeviceConfugurationDialog.dispose();
            }
        });


        load_device();
    }

    private void load_device() {
        //load pre_set_personalities
        for (int i = 0; i < presetpersonalities.values().length; ++i)
            this.preset_pers_combo.add(presetpersonalities.values()[i].name(), i);
        this.preset_pers_combo.select(0);


        //load device informations
        this.name_text.setText(this.Device.get_Personality().get_Name());
        this.actuator_adr_text.setText(Byte.toString(this.Device.get_actuator_node()));
        this.sensor_adr_text.setText(Byte.toString(this.Device.get_sensor_node()));
        
        this.state_combo.clearSelection();
        this.state_combo.removeAll();

        this.state_combo.add(this.state_names[1]);
        this.state_combo.add(this.state_names[2]);
        this.state_combo.add(this.state_names[3]);
        this.state_combo.add(this.state_names[4]); 
        
        this.state_combo.select(this.Device.get_Personality().get_State());
        System.out.println(this.Device.get_Personality().get_State());

        //setup trigger table with device configs
        this.trigger_table.removeAll();
        for (Map.Entry<kickflick.utility.keys, Boolean> entry : this.Device.get_trigger_map().entrySet())
        {
            TableItem tableItem = new TableItem(this.trigger_table, SWT.NONE);
            tableItem.setText(entry.getKey().get_name());
            if (entry.getValue())
                tableItem.setChecked(true);
            else
                tableItem.setChecked(false);
        }
        
        
        //setup state table
        set_state_combos(this.Device.get_Personality());


        //setup neighbor table
        for (int j = 0; j < this.neighbor_text_list.size(); ++j)
        {
            reaction settings = this.Device.get_Personality().get_neighbor(this.neighbor_text_list.get(j).getText());
            for (int k = 0; k < pattern.values().length; ++k)
            {
                if (settings.get_pattern() == pattern.values()[k])
                {
                    this.neighbor_pattern_combo_list.get(j).select(k);
                    break;
                }
            }
            for (int k = 0; k < color.values().length; ++k)
            {
                if (settings.get_color1() == color.values()[k])
                {
                    this.neighbor_color1_combo_list.get(j).select(k);
                }
                if (settings.get_color2() == color.values()[k])
                {
                    this.neighbor_color2_combo_list.get(j).select(k);
                }
            }
        }
        
        //the possible result equals the current device (Greetings from Cpt. Obvious) 
        result = Device;
    }

    private void set_pre_pers(personality pers) {
        this.name_text.setText(pers.get_Name());
        this.key_select_combo.select(0);

        //setup state table
        set_state_combos(pers);
    }

    private void set_state_combos()
    {
    	set_state_combos(this.Device.get_Personality());
    }
    
    private void set_state_combos(personality p)
    {
        short i = 0;
        for (Combo c : pattern_combo_list)
        {
            reaction tmp = null;
            for ( keys k : keys.values())
                if ( k.get_name().equals(key_select_combo.getText()))
                    tmp = p.get_reaction(k,i);

            for (int j = 0; j < pattern.values().length; ++j)
                if ( pattern.values()[j].get_key() == tmp.get_pattern().get_key())
                {
                    c.select(j);
                    break;
                }
            ++i;
        }

        i = 0;
        for (CCombo c : color1_combo_list)
        {
            reaction tmp = null;
            for ( keys k : keys.values())
                if ( k.get_name().equals(key_select_combo.getText()))
                    tmp = p.get_reaction(k, i);

            for (int j = 0; j < color.values().length; ++j)
                if ( color.values()[j].get_key() == tmp.get_color1().get_key())
                {
                    c.select(j);
                    break;
                }
            ++i;
        }

        i = 0;
        for (CCombo c : color2_combo_list)
        {
            reaction tmp = null;
            
            tmp = p.get_reaction(find_keys_element(key_select_combo.getText()), i);

            for (int j = 0; j < color.values().length; ++j)
                if ( color.values()[j].get_key() == tmp.get_color2().get_key())
                {
                    c.select(j);
                    break;
                }
            ++i;
        }
    }
    
    private void apply_state()
    {
    	reaction[] key_settings = new reaction[3];
        for ( int i = 0 ; i < (pattern_combo_list.size()-1); i++)
        {
            System.out.println("Itteration: " + i + "\t" + pattern_combo_list.size());
		    pattern tmp_p = find_pattern_element(pattern_combo_list.get(i+1).getText());
		    color tmp_c1 = find_color_element(color1_combo_list.get(i+1).getText());
		    color tmp_c2 = find_color_element(color2_combo_list.get(i+1).getText());

            key_settings[i] = new reaction(tmp_c1,tmp_c2,tmp_p);
        }
        System.out.println("Finished collectiong Data");
		  result.get_Personality().get_reactions().put(find_keys_element(key_select_combo.getText()), key_settings);
    }
    
    private pattern find_pattern_element(String str)
    {
    	for ( pattern p : pattern.values())
    		if ( p.get_name().equalsIgnoreCase(str))
    			return p;
		return null;
    }
    
    private color find_color_element(String str)
    {
    	for ( color c : color.values())
    		if ( c.get_name().equalsIgnoreCase(str))
    			return c;
		return null;
    }
    
    private keys find_keys_element(String str)
    {
    	for ( keys k : keys.values() )
    		if ( k.get_name().equalsIgnoreCase(str))
    			return k;
    	return null;
    }
}
