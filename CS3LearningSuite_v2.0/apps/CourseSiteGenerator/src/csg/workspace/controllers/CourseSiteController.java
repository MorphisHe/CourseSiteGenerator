package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CourseSiteGeneratorApp;
import csg.data.OfficeHoursData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.AddTA_Transaction;
import csg.transactions.EditTA_Transaction;
import csg.transactions.ToggleOfficeHours_Transaction;
import csg.workspace.dialogs.TADialog;
import static csg.CourseSitePropertyType.CSG_TAS_TABLE_VIEW;
import static csg.CourseSitePropertyType.CSG_NAME_TEXT_FIELD;
import static csg.CourseSitePropertyType.CSG_EMAIL_TEXT_FIELD;
import static csg.CourseSitePropertyType.CSG_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSitePropertyType.CSG_FOOLPROOF_SETTINGS;
import static csg.CourseSitePropertyType.CSG_TA_EDIT_DIALOG;
import static csg.CourseSitePropertyType.CSG_NO_TA_SELECTED_TITLE;
import static csg.CourseSitePropertyType.CSG_NO_TA_SELECTED_CONTENT;
import java.util.HashMap;
import javafx.collections.FXCollections;


/**
 *
 * @author Jiang He
 */
public class CourseSiteController {

    CourseSiteGeneratorApp app;

    public CourseSiteController(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(CSG_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(CSG_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        OfficeHoursData data = (OfficeHoursData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
        
        ((TableView<TeachingAssistantPrototype>)gui.getGUINode(CSG_TAS_TABLE_VIEW)).refresh();
    }
    
    public void processDeleteTA(){
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToDelete = data.getSelectedTA();
            data.removeTA(taToDelete);
        }
    }

    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(CSG_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, CSG_NO_TA_SELECTED_TITLE, CSG_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(CSG_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
    
    /**
     * This method is to set display for office hour after start time and end time
     * is picked. Will only show the rows within the picked time interval
     * @param startTime : starting time of the office hour
     * @param endTime : ending time of the office hour
     */
    public void processOHdisplay(String startTime, String endTime){
        AppGUIModule gui = app.getGUIModule();
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        TableView<TimeSlot> table = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        
        //get the full oh table first
        table.setItems(data.getOfficeHours());
        
        //this will store the rows that we are showing
        ObservableList subentries = FXCollections.observableArrayList();
 
        boolean laterThanStartTime = false;
        for (int i = 0; i < table.getItems().size(); i++) {
            String columnStartTime = "" + table.getColumns().get(0).getCellData(i);
            String columnEndTime = "" + table.getColumns().get(1).getCellData(i);
            if (columnStartTime.equals(startTime) || laterThanStartTime) {
                subentries.add(table.getItems().get(i));
                laterThanStartTime = true;
                //break the loop if we hit the endTime row
                if (columnEndTime.equals(endTime)) break;
            }
        }
        //add all rows to oh table
        table.setItems(subentries);
    }
    
    public void processTAdisplay(){
        AppGUIModule gui = app.getGUIModule();
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        TableView<TimeSlot> ohTable = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        TableView<TeachingAssistantPrototype> taTable = (TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW);
        
        //first put our full ta table data to a temp data holder
        //this way we dont change any data
        data.setTempTAs(data.getTeachingAssistants());
        
        //reset the table with full ta table data
        taTable.setItems(data.getTeachingAssistants());
        
        //this will store the rows that we are showing
        ObservableList subentries = FXCollections.observableArrayList();
        
        //this will store all ta's name and their time slot that are to be shown
        HashMap<String, Integer> tasToShow = new HashMap<>();
        
        //this loop will put the needed ta name and time slot to hashmap
        for (int i = 0; i < ohTable.getItems().size(); i++) {
            for(int j = 0; j < ohTable.getColumns().size(); j++){
                //incase if there is multiple name in one table cell
                String[] temp = ("" + ohTable.getColumns().get(j).getCellData(i)).split("\n");
                
                for (String name : temp){
                    //check if taName is already in hashmap
                    if (tasToShow.containsKey(name)) {
                        tasToShow.put(name, tasToShow.get(name) + 1);
                    } else {
                        tasToShow.put(name, 1);
                    }
                }
                
            }
        }
        
        //this loop with update the display of ta table
        for (int i = 0; i < taTable.getItems().size(); i++) {
            String taName = "" + taTable.getColumns().get(0).getCellData(i);
            if(tasToShow.containsKey(taName)){
                TeachingAssistantPrototype cloneTA = (TeachingAssistantPrototype) taTable.getItems().get(i).clone();
                cloneTA.setSlots(tasToShow.get(taName).toString());
                subentries.add(cloneTA);
            }
        }
        //add all ta to ta table
        taTable.setItems(subentries);
    }
    
    //this method shows the full ta and oh table data
    public void showFullTAandOH(){
        AppGUIModule gui = app.getGUIModule();
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        TableView<TimeSlot> ohTable = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        TableView<TeachingAssistantPrototype> taTable = (TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW);
        
        ohTable.setItems(data.getOfficeHours());
        taTable.setItems(data.getTeachingAssistants());
    }
}