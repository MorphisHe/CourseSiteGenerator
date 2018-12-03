package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CourseSiteGeneratorApp;
import csg.CourseSitePropertyType;
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
import csg.data.Labs;
import csg.data.Lectures;
import csg.data.Recitations;
import csg.data.Schedule;
import csg.transactions.AddTableRow_Transaction;
import csg.transactions.AddUpdateSchedule_Transaction;
import csg.transactions.DeleteTableRow_Transaction;
import csg.transactions.SiteIcon_Transaction;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;


/**
 *
 * @author Jiang He
 */
public class CourseSiteController {

    CourseSiteGeneratorApp app;
    
    //following are the options for combo boxes
    public static final ObservableList<String> OH_START_TIME
            = FXCollections.observableArrayList(
                    "8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
                    "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm",
                    "4:30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm",
                    "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm"
            );
    
    public static final ObservableList<String> OH_END_TIME
            = FXCollections.observableArrayList(
                    "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
                    "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm",
                    "4:30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm",
                    "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm", "12:00am"
            );
    
    private static final ObservableList<String> SITE_SEMESTER
            = FXCollections.observableArrayList();
   
    private static final ObservableList<String> SITE_SUBJECT
            = FXCollections.observableArrayList();
    
    private static final ObservableList<String> CURRENT_YEARS
            = FXCollections.observableArrayList(
                    Integer.toString(Calendar.getInstance().get(Calendar.YEAR)), 
                    Integer.toString(Calendar.getInstance().get(Calendar.YEAR)+1)
            );
    
    private static final ObservableList<String> SUBJECT_NUMBER
            = FXCollections.observableArrayList();
    
    private static final ObservableList<String> SITE_CSS
            = FXCollections.observableArrayList();
    
    //ImageViews that keep tract of the old and new image displayed in site style page for undo redo
    private static ImageView oldImageView = null;
    private static ImageView newImageView = null;
    

    //CONSTRUCTOR
    public CourseSiteController(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }
    
    //GETTERS FOR OBSERVABLE_LIST
    public ObservableList<String> getSiteSemester(){
        return SITE_SEMESTER;
    }
    
    public ObservableList<String> getSiteSubject(){
        return SITE_SUBJECT;
    }
    
    public ObservableList<String> getSubjectNum(){
        return SUBJECT_NUMBER;
    }
    
    public ObservableList<String> getSiteCss(){
        return SITE_CSS;
    }
    
    public ObservableList<String> getOhStartTime(){
        return OH_START_TIME;
    }
    
    public ObservableList<String> getOhEndTime(){
        return OH_END_TIME;
    }
    
    public ObservableList<String> getCurrentYears(){
        return CURRENT_YEARS;
    }
    
    //method to check if a value is in specific oberservable list
    public boolean checkValInObvList(String typeOfList, String valueToCheck){
        switch(typeOfList){
            case "semester" :
                return SITE_SEMESTER.contains(valueToCheck);
            
            case "subject" : 
                return SITE_SUBJECT.contains(valueToCheck);
            
            case "number" : 
                return SUBJECT_NUMBER.contains(valueToCheck);
                
            default: return false;
        }
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
    
    //this method handles the site icon button actions
    //these actions are undoable and redoable
    public void processEditSiteStyle(FileChooser fileChooser, CourseSitePropertyType nodeId){
        AppGUIModule gui = app.getGUIModule();
        
        HBox box = (HBox) gui.getGUINode(nodeId);
        //save the old ImageView for undo
        oldImageView = (ImageView) box.getChildren().get(1);
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            newImageView = new ImageView(image);

            if(!oldImageView.getImage().impl_getUrl().equals(newImageView.getImage().impl_getUrl())){
                SiteIcon_Transaction siteIconTransaction = new SiteIcon_Transaction(
                    box, oldImageView, newImageView);
                app.processTransaction(siteIconTransaction);
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
     * that is picked. Will only show the rows within the picked time interval
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
    
    /**
     * this method will dynamically update the export directory
     * @param typeOfCB : the comboBox that calls this method
     * @param oldValue : old option in combo box, used here to check
     * which part of export directory we need to update
     * @param newValue : new option in combo box that we updating
     */
    public void updateExportDir(String typeOfCB, Object oldValue, Object newValue){
        //  .\\export\\CSE_219_Fall_2018\\public_html (format)
        AppGUIModule gui = app.getGUIModule();
        
        Label exportDir = (Label) gui.getGUINode(CourseSitePropertyType.SITE_EXPORT_DIR);
        String oldVal = (String)oldValue;
        String newVal = (String)newValue;
        StringBuilder sb = new StringBuilder();
        
        //if oldValue and newValue is a semester value that contains white space 
        //we transfer the white space to "_" instead
        if(oldVal.contains(" ")) oldVal = oldVal.replaceAll(" ", "_");
        if(newVal.contains(" ")) newVal = newVal.replaceAll(" ", "_");
        
        String[] temp = exportDir.getText().split("\\\\");
        String options = temp[4]; //this is the part we working on EX: "CSE_219_Fall_2018"
        String [] splitedOptions = options.split("_"); // we get ["Subject", "Number", "Semester", "Year"]
        
        //now we want to only replace the oldVal with newVal in once single correct place
        switch (typeOfCB) {
            case "subject":
                splitedOptions[0] = splitedOptions[0].replace(oldVal, newVal);
                break;
            case "number":
                splitedOptions[1] = splitedOptions[1].replace(oldVal, newVal);
                break;
            case "semester":
                //special case because semester can take up to more than 1 array space
                if(splitedOptions.length > 4){
                    String[] newArray = {splitedOptions[0], splitedOptions[1], newVal, 
                                         splitedOptions[splitedOptions.length-1]};
                    splitedOptions = newArray;
                }
                else{
                    splitedOptions[2] = splitedOptions[2].replace(oldVal, newVal);
                }
                break;
            case "year":
                splitedOptions[splitedOptions.length-1] = splitedOptions[splitedOptions.length-1].replace(oldVal, newVal);
                break;
            default:
                break;
        }
        
        //add all parts to StringBuilder
        //if the string is longer than 4
        if(splitedOptions.length > 4){
            sb.append(".\\\\") //add .\\
              .append(temp[2]) //add export
              .append("\\\\")  //add \\
              .append(splitedOptions[0]) //add Subject
              .append("_")
              .append(splitedOptions[1]); //add Number
            //this loop to get all semester pieces into string builder
            for(int i = 2; i < splitedOptions.length; i++){
                sb.append("_").append(splitedOptions[i]);
            }
            sb.append("\\\\").append(temp[6]);
            exportDir.setText(sb.toString());
        }
        //if it is just 4 element in the array
        else{
            exportDir.setText(
                sb.append(".\\\\") //add .\\
                  .append(temp[2]) //add export
                  .append("\\\\")  //add \\
                  .append(splitedOptions[0]) //add Subject
                  .append("_")
                  .append(splitedOptions[1]) //add Number
                  .append("_") 
                  .append(splitedOptions[2]) //add Semester
                  .append("_")
                  .append(splitedOptions[splitedOptions.length-1]) //add Year
                  .append("\\\\")  //add \\
                  .append(temp[6]) //add public_html
                  .toString()
            ); 
        }
        
    }
    
    /**
     * Insert a new default row to the table, select a cell of it and scroll to it. 
     * @param table : The TableView object that we are working on
     * @param dataType : type of data to add to the table
     */
    public void addRow(TableView table, String dataType) {
        
        // get current position
        TablePosition pos = table.getFocusModel().getFocusedCell();

        // clear current selection
        table.getSelectionModel().clearSelection();

        // create new record and add it to the model
        Object dataToAdd = null;
        
        switch(dataType){
            case "lectures" :
                dataToAdd = new Lectures("？", "？", "？", "？");
                break;
            
            case "recitation" :
                dataToAdd = new Recitations("？", "？", "？", "？", "?");
                break;
                
            case "labs" :
                dataToAdd = new Labs("？", "？", "？", "？", "?");
                break;
            
            default: break;
        }
        
        //making the transaction
        AddTableRow_Transaction addRowTransaction= new AddTableRow_Transaction(dataToAdd, table);
        app.processTransaction(addRowTransaction);

        // get last row
        int row = table.getItems().size() - 1;
        table.getSelectionModel().select(row, pos.getTableColumn());

        // scroll to new row
        table.scrollTo(dataToAdd);

    }
    
    /**
     * Remove all selected rows.
     * @param table : the TableView object that we working on
     * @param typeOfData : the type of data of the line we removing, this param is made of undo and redo
     */
    public void removeSelectedRows(TableView table, String typeOfData) {
        DeleteTableRow_Transaction deleteTableRow_Transaction = new DeleteTableRow_Transaction(
                                 table.getSelectionModel().getSelectedItem(), table, typeOfData,
                                 table.getSelectionModel().getFocusedIndex());
        app.processTransaction(deleteTableRow_Transaction);

        // table selects by index, so we have to clear the selection or else items with that index would be selected 
        table.getSelectionModel().selectBelowCell();
    }
    
    // method to setup how table views work
    public void initTableView(TableView table) {
        
        // switch to edit mode on keypress
        // this must be KeyEvent.KEY_PRESSED so that the key gets forwarded to the 
        // editing cell; it wouldn't be forwarded on KEY_RELEASED
        table.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
            //event.consume()
            //don't consume the event or else the values won't be updated;
                return;
            }

            // switch to edit mode on keypress, but only if we aren't already in edit mode
            if (table.getEditingCell() == null) {
                if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                    
                    TablePosition focusedCellPosition = table.getFocusModel().getFocusedCell();
                    table.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
                    
                }
            }
        });

        // single cell selection mode
        table.getSelectionModel().setCellSelectionEnabled(true);
        
        // select first cell
        table.getSelectionModel().selectFirst();
    }
    
    public void clearScheduleEdit(TableView scheduleTable, ComboBox typeCB,
                                  DatePicker sdDatePicker, TextField titleTF,
                                  TextField topicTF, TextField linkTF, Button addUpdateButton) {
        scheduleTable.getSelectionModel().clearSelection();
        typeCB.getSelectionModel().selectFirst();
        sdDatePicker.setValue(LocalDate.now());
        titleTF.clear();
        topicTF.clear();
        linkTF.clear();
        changeButtonText(addUpdateButton, true);
    }
    
    /**
     * this method is to help change meeting tab button state, this is language dependent
     * @param bt : button we working on (Only addUpdateButton)
     * @param addState : state of button : either add or update
     */
    public void changeButtonText(Button bt, boolean addState){
        String currentLanguage = app.getLanguageModule().getCurrentLanguage();
        if(currentLanguage.equals("English")){
            if(addState) bt.setText("Add");
            else bt.setText("Update");
        }
        else{
            if(addState) bt.setText("添加");
            else bt.setText("更新");
        }
    }
    
    public void addUpdateAction(Button addUpdateButton, DatePicker sdDatePicker,
                                DateTimeFormatter formatters, ComboBox typeCB,
                                TextField titleTF, TextField topicTF, TextField linkTF,
                                TableView scheduleTable) {
        if (addUpdateButton.getText().equals("Add") || addUpdateButton.getText().equals("添加")) {
            //adding new row
            String date = sdDatePicker.getValue().format(formatters);
            Schedule schedule = new Schedule(
                    typeCB.getSelectionModel().getSelectedItem().toString(),
                    date, titleTF.getText(), topicTF.getText(), linkTF.getText());
            AddUpdateSchedule_Transaction AUS_Transaction = new AddUpdateSchedule_Transaction(
                    schedule, scheduleTable, "Add", null, null, null, null, null);
            app.processTransaction(AUS_Transaction);
        } 
        else {
            //updating the selected row
            Schedule currentEditingItem = (Schedule) scheduleTable.getSelectionModel().getSelectedItem();
            AddUpdateSchedule_Transaction AUS_Transaction = new AddUpdateSchedule_Transaction(
                    currentEditingItem, scheduleTable, "Update",
                    typeCB.getSelectionModel().getSelectedItem().toString(),
                    sdDatePicker.getValue().format(formatters),
                    titleTF.getText(), topicTF.getText(), linkTF.getText());
            app.processTransaction(AUS_Transaction);
        }
        //after processing the transaction we clear the textfields
        titleTF.clear();
        topicTF.clear();
        linkTF.clear();
    }   
}