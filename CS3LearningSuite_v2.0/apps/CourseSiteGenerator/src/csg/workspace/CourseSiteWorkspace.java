package csg.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorApp;
import csg.CourseSitePropertyType;
import static csg.CourseSitePropertyType.*;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.Labs;
import csg.data.Lectures;
import csg.data.Recitations;
import csg.data.Schedule;
import csg.workspace.controllers.CourseSiteController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CourseSiteFoolproofDesign;
import static csg.workspace.style.OHStyle.*;
import static djf.modules.AppGUIModule.DISABLED;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Jiang He
 */
public final class CourseSiteWorkspace extends AppWorkspaceComponent {
    
    private static final boolean EDITABLE = true;
    private static final boolean NOT_EDITABLE = false;
    private static final String FIRST_OPTION = "first";
    private static final String LAST_OPTION = "last";
    private static final ObservableList<String> OH_START_TIME
            = FXCollections.observableArrayList(
                    "8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
                    "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm",
                    "4:30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm",
                    "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm"
            );
    private static final ObservableList<String> OH_END_TIME
            = FXCollections.observableArrayList(
                    "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
                    "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm",
                    "4:30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm",
                    "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm", "12:00am"
            );

    public CourseSiteWorkspace(CourseSiteGeneratorApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        //  INIT FOOL PROOF 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }
    
    private void initLayout(){
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        //top level VBox, this is the workspace
        VBox mainBox = ohBuilder.buildVBox(CSG_CENTER_PANE, null, EMPTY_TEXT, ENABLED);
        
        //tabs
        TabPane tabPane = ohBuilder.buildTabPane(CSG_TAB_PANE, mainBox, EMPTY_TEXT, ENABLED);
        ohBuilder.buildTab(CSG_SITE_TAB_BUTTON, tabPane, CLASS_TOGGLE_BUTTON, ENABLED, true);
        ohBuilder.buildTab(CSG_SYLLUBUS_TAB_BUTTON, tabPane, CLASS_TOGGLE_BUTTON, ENABLED, false);
        ohBuilder.buildTab(CSG_MEETING_TIME_TAB_BUTTON, tabPane, CLASS_TOGGLE_BUTTON, ENABLED, false);
        ohBuilder.buildTab(CSG_OFFICE_HOUR_TAB_BUTTON, tabPane, CLASS_TOGGLE_BUTTON, ENABLED, false);
        ohBuilder.buildTab(CSG_SCHEDULE_TAB_BUTTON, tabPane, CLASS_TOGGLE_BUTTON, ENABLED, false);
        
        //making site page
        initSitePage(ohBuilder, tabPane);
        
        //making syllubus page
        initSyllubusPage(ohBuilder, tabPane);

        //making meeting time page
        initMeetingTimePage(ohBuilder, tabPane);
        
        //making schedule page
        initSchedulePage(ohBuilder, tabPane);
        
        //making office hours page
        initOfficeHourPage(ohBuilder, tabPane);
        
        //making the tabs equally splited
        tabPane.tabMinWidthProperty().bind(mainBox.widthProperty().multiply(1.21 / 7.0));
        
        //this is out workspace
        workspace = mainBox;
    }

    @Override
    public void showNewDialog() {
        
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        
    }
    
    public void initControllers() {
        CourseSiteController controller = new CourseSiteController((CourseSiteGeneratorApp) app);
        AppGUIModule gui = app.getGUIModule();

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(CSG_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(CSG_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(CSG_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
        
        ((Button) gui.getGUINode(CSG_DELETE_TA_BUTTON)).setOnAction(e -> {
            controller.processDeleteTA();
        });

        TableView officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });
        
        //set cell factory for combo box action
        ComboBox ohStartTimeCB = (ComboBox) gui.getGUINode(OH_START_TIME_COMBO_BOX);
        ComboBox ohEndTimeCB = (ComboBox) gui.getGUINode(OH_END_TIME_COMBO_BOX);
        
        ohStartTimeCB.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMousePressed(e -> {
                String CBstartTime = (String) ohStartTimeCB.getSelectionModel().getSelectedItem();
                String CBendTime = (String) ohEndTimeCB.getSelectionModel().getSelectedItem();
                ohEndTimeCB.setItems(getEtList(CBstartTime, OH_END_TIME));
                controller.processOHdisplay(CBstartTime, CBendTime);
                if(!fullInterval()) controller.processTAdisplay();
                else controller.showFullTAandOH();
            });
            return cell ;
        });
        
        ohEndTimeCB.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMousePressed(e -> {
                String CBstartTime = (String) ohStartTimeCB.getSelectionModel().getSelectedItem();
                String CBendTime = (String) ohEndTimeCB.getSelectionModel().getSelectedItem();
                controller.processOHdisplay(CBstartTime, CBendTime);
                ohStartTimeCB.setItems(getStList(CBendTime, OH_START_TIME, OH_END_TIME));
                if(!fullInterval()) controller.processTAdisplay();
                else controller.showFullTAandOH();
            });
            return cell ;
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(CSG_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
            if(!fullInterval()) controller.processTAdisplay();
            else controller.showFullTAandOH();
            
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(CSG_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
            if(!fullInterval()) controller.processTAdisplay();
            else controller.showFullTAandOH();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(CSG_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
            if(!fullInterval()) controller.processTAdisplay();
            else controller.showFullTAandOH();
        });
    }
    
    private void initSchedulePage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane sdGridPane = new GridPane();
        
        //adding a scroll pane on this page
        ScrollPane sdScroll = ohBuilder.buildScrollPane(SD_SCROLL_PANE, sdGridPane, CLASS_SCROLL_PANE, ENABLED);
        sdGridPane.prefWidthProperty().bind(sdScroll.widthProperty().multiply(0.99));
        
        //calendar block
        VBox calendarBox = ohBuilder.buildVBox(SD_CALENDAR_VBOX, null, CLASS_SITE_HBOX, ENABLED);
        //calendar header
        HBox calHeaderBox = new HBox();
        ohBuilder.buildLabel(SD_CALENDAR_BOUND_LABEL, calHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        //calendar 
        HBox calSecHeaderBox = new HBox();
        ohBuilder.buildLabel(SD_START_MON_LABEL, calSecHeaderBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildDatePicker(SD_START_MON_DATE_PICKER, calSecHeaderBox, CLASS_DATE_PICKER, ENABLED);
        ohBuilder.buildLabel(SD_END_FRI_LABEL, calSecHeaderBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildDatePicker(SD_END_FRI_DATE_PICKER, calSecHeaderBox, CLASS_DATE_PICKER, ENABLED);
        
        calendarBox.getChildren().addAll(calHeaderBox, calSecHeaderBox);
        
        //schedule table block
        VBox sdTableBox = ohBuilder.buildVBox(SD_TABLE_VBOX, null, CLASS_SITE_HBOX, ENABLED);
        //table header
        HBox tableHeaderBox = new HBox();
        ohBuilder.buildTextButton(SD_DELETE_SCHEDULE_ITEM_BUTTON, tableHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildLabel(SD_ITEM_LABEL, tableHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        //table
        TableView<Schedule> sdTable = ohBuilder.buildTableView(SD_SCHEDULE_ITEM_TABLE_VIEW, null, CLASS_OH_TABLE_VIEW, ENABLED);
        sdTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn typeColumn = ohBuilder.buildTableColumn(SD_TYPE_TABLE_COLUMN, sdTable, CLASS_OH_COLUMN);
        TableColumn dateColumn = ohBuilder.buildTableColumn(SD_DATE_TABLE_COLUMN, sdTable, CLASS_OH_COLUMN);
        TableColumn titleColumn = ohBuilder.buildTableColumn(SD_TITLE_TABLE_COLUMN, sdTable, CLASS_OH_COLUMN);
        TableColumn topicColumn = ohBuilder.buildTableColumn(SD_TOPIC_TABLE_COLUMN, sdTable, CLASS_OH_COLUMN);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("sd type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("sd date"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("sd title"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("sd topic"));
        typeColumn.prefWidthProperty().bind(sdTable.widthProperty().multiply(1.0 / 5.0));
        dateColumn.prefWidthProperty().bind(sdTable.widthProperty().multiply(2.0 / 5.0));
        titleColumn.prefWidthProperty().bind(sdTable.widthProperty().multiply(1.0 / 5.0));
        topicColumn.prefWidthProperty().bind(sdTable.widthProperty().multiply(1.0 / 5.0));
        
        sdTableBox.getChildren().addAll(tableHeaderBox, sdTable);
        
        //edit block
        VBox editBox = ohBuilder.buildVBox(SD_EDIT_VBOX, null, CLASS_SITE_HBOX, ENABLED);
        //edit header
        HBox editHeaderBox = new HBox();
        ohBuilder.buildLabel(SD_ADD_EDIT_LABEL, editHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        //main manipulation of edit
        HBox mainBox = new HBox();
        VBox labelBox = new VBox();
        VBox typingBox = new VBox();
        
        //labels of main edit section
        ohBuilder.buildLabel(SD_EDIT_TYPE_LABEL, labelBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildLabel(SD_EDIT_DATE_LABEL, labelBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildLabel(SD_EDIT_TITLE_LABEL, labelBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildLabel(SD_EDIT_TOPIC_LABEL, labelBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildLabel(SD_EDIT_LINK_LABEL, labelBox, CLASS_LABEL, ENABLED);
        
        //typing of main edit section
        ohBuilder.buildComboBox(SD_TYPE_COMBO_BOX, null, EMPTY_TEXT, typingBox, 
                CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        ohBuilder.buildDatePicker(SD_DATE_DATE_PICKER, typingBox, CLASS_DATE_PICKER, ENABLED);
        ohBuilder.buildTextField(SD_TITLE_TEXT_FIELD, typingBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(SD_TOPIC_TEXT_FIELD, typingBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(SD_LINK_TEXT_FIELD, typingBox, CLASS_OH_TEXT_FIELD, ENABLED);
        
        mainBox.getChildren().addAll(labelBox, typingBox);
        
        //button box
        HBox buttonBox = new HBox();
        ohBuilder.buildTextButton(SD_ADD_UPDATE_BUTTON, buttonBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildTextButton(SD_CLEAR_BUTTON, buttonBox, CLASS_OH_BUTTON, ENABLED);
        
        //add all to the top vbox then add vbox to GridPane
        editBox.getChildren().addAll(editHeaderBox, mainBox, buttonBox);
        
        
        //setting allignment
        calendarBox.setSpacing(15);
        sdTableBox.setSpacing(15);
        editBox.setSpacing(15);
        buttonBox.setSpacing(25);
        
        calHeaderBox.setAlignment(Pos.CENTER_LEFT);
        calSecHeaderBox.setAlignment(Pos.CENTER_LEFT);
        tableHeaderBox.setAlignment(Pos.CENTER_LEFT);
        
        calendarBox.prefWidthProperty().bind(sdGridPane.widthProperty());
        
        calHeaderBox.setSpacing(15);
        calSecHeaderBox.setSpacing(20);
        tableHeaderBox.setSpacing(15);
        labelBox.setSpacing(30);
        typingBox.setSpacing(15);
        mainBox.setSpacing(20);
        
        VBox.setVgrow(sdTable, Priority.ALWAYS);
        sdTable.prefWidthProperty().bind(sdGridPane.widthProperty());
        
        GridPane.setRowIndex(calendarBox, 0);
        GridPane.setRowIndex(sdTableBox, 1);
        GridPane.setRowIndex(editBox, 2);
        
        sdGridPane.getChildren().addAll(calendarBox, sdTableBox, editBox);
        sdGridPane.setVgap(5);
 
        tabPane.getTabs().get(4).setContent(sdScroll);
    }
    
    private void initMeetingTimePage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane mtGridPane = new GridPane();
        
        //adding a scroll pane on this page
        ScrollPane mtScroll = ohBuilder.buildScrollPane(SD_SCROLL_PANE, mtGridPane, CLASS_SCROLL_PANE, ENABLED);
        mtGridPane.prefWidthProperty().bind(mtScroll.widthProperty().multiply(0.99));
        
        //lecture block
        HBox lectureBox = ohBuilder.buildHBox(MT_LECTURE_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        VBox secLectureLayor = new VBox();
        //lecture header
        HBox lectureHeaderBox = new HBox();
        ohBuilder.buildTextButton(MT_ADD_LECUTURE_BUTTON, lectureHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildTextButton(MT_DELETE_LECUTURE_BUTTON, lectureHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildLabel(MT_LECTURE_LABEL, lectureHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        // make lecture table
        TableView<Lectures> lectureTable = ohBuilder.buildTableView(CSG_TAS_TABLE_VIEW, null, CLASS_OH_TABLE_VIEW, ENABLED);
        lectureTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn sectionColumn = ohBuilder.buildTableColumn(MT_LEC_SECTION_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn daysColumn = ohBuilder.buildTableColumn(MT_LEC_DAYS_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn timeColumn = ohBuilder.buildTableColumn(MT_LEC_TIME_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn roomColumn = ohBuilder.buildTableColumn(MT_LEC_ROOM_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("lec section"));
        daysColumn.setCellValueFactory(new PropertyValueFactory<>("lec days"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("lec time"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("lec room"));
        sectionColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 5.0));
        daysColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0 / 5.0));
        timeColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 5.0));
        roomColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 5.0));
        
        secLectureLayor.getChildren().addAll(lectureHeaderBox, lectureTable);
        lectureBox.getChildren().add(secLectureLayor);
        
        //recitation block
        HBox recitationBox = ohBuilder.buildHBox(MT_RECITATION_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        VBox secRecitationLayor = new VBox();
        //recitation header
        HBox recitationHeaderBox = new HBox();
        ohBuilder.buildTextButton(MT_ADD_RECITATION_BUTTON, recitationHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildTextButton(MT_DELETE_RECITATION_BUTTON, recitationHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildLabel(MT_RECITATION_LABEL, recitationHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        // make recitation table
        TableView<Recitations> recitationTable = ohBuilder.buildTableView(MT_RECITATION_TABLE_VIEW, null, CLASS_OH_TABLE_VIEW, ENABLED);
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn recSectionColumn = ohBuilder.buildTableColumn(MT_REC_SECTION_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn daysTimesColumn = ohBuilder.buildTableColumn(MT_REC_DAYS_TIMES_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn recRoomColumn = ohBuilder.buildTableColumn(MT_REC_ROOM_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn TA1Column = ohBuilder.buildTableColumn(MT_REC_TA1_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn TA2Column = ohBuilder.buildTableColumn(MT_REC_TA2_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        recSectionColumn.setCellValueFactory(new PropertyValueFactory<>("rec section"));
        daysTimesColumn.setCellValueFactory(new PropertyValueFactory<>("rec days & times"));
        TA1Column.setCellValueFactory(new PropertyValueFactory<>("rec ta1"));
        TA2Column.setCellValueFactory(new PropertyValueFactory<>("rec ta2"));
        recRoomColumn.setCellValueFactory(new PropertyValueFactory<>("rec room"));
        recSectionColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 6.0));
        daysTimesColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 6.0));
        TA1Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 6.0));
        TA2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 6.0));
        recRoomColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 6.0));
        
        secRecitationLayor.getChildren().addAll(recitationHeaderBox, recitationTable);
        recitationBox.getChildren().add(secRecitationLayor);
        
        //labs block
        HBox labsBox = ohBuilder.buildHBox(MT_LABS_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        VBox secLabsLayor = new VBox();
        //labs header
        HBox labsHeaderBox = new HBox();
        ohBuilder.buildTextButton(MT_ADD_LABS_BUTTON, labsHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildTextButton(MT_DELETE_LABS_BUTTON, labsHeaderBox, CLASS_OH_BUTTON, ENABLED);
        ohBuilder.buildLabel(MT_LABS_LABEL, labsHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        // make labs table
        TableView<Labs> labsTable = ohBuilder.buildTableView(MT_LABS_TABLE_VIEW, null, CLASS_OH_TABLE_VIEW, ENABLED);
        labsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn labsSectionColumn = ohBuilder.buildTableColumn(MT_LAB_SECTION_TABLE_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsDaysTimesColumn = ohBuilder.buildTableColumn(MT_LAB_DAYS_TIMES_TABLE_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsRoomColumn = ohBuilder.buildTableColumn(MT_LAB_ROOM_TABLE_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsTA1Column = ohBuilder.buildTableColumn(MT_LAB_TA1_TABLE_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsTA2Column = ohBuilder.buildTableColumn(MT_LAB_TA2_TABLE_COLUMN, labsTable, CLASS_OH_COLUMN);
        labsSectionColumn.setCellValueFactory(new PropertyValueFactory<>("labs section"));
        labsDaysTimesColumn.setCellValueFactory(new PropertyValueFactory<>("labs days & times"));
        labsTA1Column.setCellValueFactory(new PropertyValueFactory<>("labs ta1"));
        labsTA2Column.setCellValueFactory(new PropertyValueFactory<>("labs ta2"));
        labsRoomColumn.setCellValueFactory(new PropertyValueFactory<>("labs room"));
        labsSectionColumn.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 6.0));
        labsDaysTimesColumn.prefWidthProperty().bind(labsTable.widthProperty().multiply(2.0 / 6.0));
        labsTA1Column.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 6.0));
        labsTA2Column.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 6.0));
        labsRoomColumn.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 6.0));
        
        secLabsLayor.getChildren().addAll(labsHeaderBox, labsTable);
        labsBox.getChildren().addAll(secLabsLayor);
        
        
        //setting allignments
        lectureHeaderBox.setSpacing(15);
        recitationHeaderBox.setSpacing(15);
        labsHeaderBox.setSpacing(15);
        
        lectureHeaderBox.setAlignment(Pos.CENTER_LEFT);
        recitationHeaderBox.setAlignment(Pos.CENTER_LEFT);
        labsHeaderBox.setAlignment(Pos.CENTER_LEFT);
        
        // make sure the tables always grow
        VBox.setVgrow(lectureTable, Priority.ALWAYS);
        VBox.setVgrow(recitationTable, Priority.ALWAYS);
        VBox.setVgrow(labsTable, Priority.ALWAYS);
        
        lectureTable.prefWidthProperty().bind(mtGridPane.widthProperty());
        recitationTable.prefWidthProperty().bind(mtGridPane.widthProperty());
        labsTable.prefWidthProperty().bind(mtGridPane.widthProperty());
        
        secLectureLayor.setSpacing(15);
        secRecitationLayor.setSpacing(15);
        secLabsLayor.setSpacing(15);
        
        lectureBox.prefWidthProperty().bind(mtGridPane.widthProperty());
        
        GridPane.setRowIndex(lectureBox, 0);
        GridPane.setRowIndex(recitationBox, 1);
        GridPane.setRowIndex(labsBox, 2);
        
        mtGridPane.getChildren().addAll(lectureBox, recitationBox, labsBox);
        mtGridPane.setVgap(5);
        tabPane.getTabs().get(2).setContent(mtScroll);
    }
    
    private void initOfficeHourPage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane ohGridPane = new GridPane();
        VBox mainBox = ohBuilder.buildVBox(CSG_OH_VBOX, null, CLASS_SITE_HBOX, ENABLED);
        
        //making the TA table header
        HBox taHeaderBox = new HBox();
        taHeaderBox.setAlignment(Pos.CENTER_LEFT);
        
        Button tb = ohBuilder.buildTextButton(CSG_DELETE_TA_BUTTON, null, CLASS_OH_BUTTON, ENABLED);
        Label lb = ohBuilder.buildLabel(CSG_TAS_HEADER_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        
        //radio buttonsv
        ToggleGroup tg = new ToggleGroup();
        RadioButton all = ohBuilder.buildRadioButton(CSG_ALL_RADIO_BUTTON, null, 
                CLASS_OH_RADIO_BUTTON, ENABLED, tg, ENABLED);
        RadioButton grad = ohBuilder.buildRadioButton(CSG_GRAD_RADIO_BUTTON, null, 
                CLASS_OH_RADIO_BUTTON, ENABLED, tg, DISABLED);
        RadioButton underGrad = ohBuilder.buildRadioButton(CSG_UNDERGRAD_RADIO_BUTTON, null, 
                CLASS_OH_RADIO_BUTTON, ENABLED, tg, DISABLED);
        
        taHeaderBox.getChildren().addAll(tb, lb, all, grad, underGrad);
        taHeaderBox.setSpacing(15);
        
        //making TA table
        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(CSG_TAS_TABLE_VIEW, null, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(CSG_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = ohBuilder.buildTableColumn(CSG_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = ohBuilder.buildTableColumn(CSG_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_CENTERED_COLUMN);
        TableColumn typeColumn = ohBuilder.buildTableColumn(CSG_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(CSG_ADD_TA_PANE, null, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(CSG_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(CSG_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(CSG_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);
        taBox.setSpacing(15);
        
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS
        VBox.setVgrow(taTable, Priority.ALWAYS);
        
        //making office hour header
        HBox ohHeaderBox = new HBox();
        ohBuilder.buildLabel(CSG_OFFICE_HOURS_HEADER_LABEL, ohHeaderBox, CLASS_HEADER_LABEL, ENABLED);
        HBox ohTimeBox = new HBox();
        ohBuilder.buildLabel(OH_START_TIME_LABEL, ohTimeBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildComboBox(OH_START_TIME_COMBO_BOX, OH_START_TIME, null, ohTimeBox, 
                CLASS_COMBO_BOX, ENABLED, NOT_EDITABLE, FIRST_OPTION);
        ohBuilder.buildLabel(OH_END_TIME_LABEL, ohTimeBox, CLASS_LABEL, ENABLED);
        ohBuilder.buildComboBox(OH_END_TIME_COMBO_BOX, OH_END_TIME, EMPTY_TEXT, ohTimeBox, 
                CLASS_COMBO_BOX, ENABLED, NOT_EDITABLE, LAST_OPTION);
        
        //setting arrangement for oh header
        ohHeaderBox.getChildren().add(ohTimeBox);
        ohHeaderBox.setSpacing(70);
        ohTimeBox.setSpacing(25);
        ohHeaderBox.setAlignment(Pos.CENTER_LEFT);
        ohTimeBox.setAlignment(Pos.CENTER_LEFT);
        
        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(CSG_OFFICE_HOURS_TABLE_VIEW, null, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "friday");
         
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);
        
        //setting allignment
        ohHeaderBox.prefWidthProperty().bind(ohGridPane.widthProperty());
        mainBox.getChildren().addAll(taHeaderBox, taTable, taBox, ohHeaderBox, officeHoursTable);
        
        taHeaderBox.prefHeightProperty().bind(ohGridPane.heightProperty().multiply(0.05 / 5.0));
        taTable.prefHeightProperty().bind(ohGridPane.heightProperty().multiply(1.36 / 5.0));
        taBox.prefHeightProperty().bind(ohGridPane.heightProperty().multiply(0.05 / 5.0));
        ohHeaderBox.prefHeightProperty().bind(ohGridPane.heightProperty().multiply(0.05 / 5.0));
        officeHoursTable.prefHeightProperty().bind(ohGridPane.heightProperty().multiply(3.5 / 5.0));
        
        mainBox.setSpacing(5);
   
        ohGridPane.getChildren().add(mainBox);
        tabPane.getTabs().get(3).setContent(ohGridPane);
    }
    
    private void initSyllubusPage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane syllubusGridPane = new GridPane();
        AppGUIModule gui = app.getGUIModule();
        
        //adding a scroll pane on this page
        ScrollPane syllubusScroll = ohBuilder.buildScrollPane(SD_SCROLL_PANE, syllubusGridPane, CLASS_SCROLL_PANE, ENABLED);
        syllubusGridPane.prefWidthProperty().bind(syllubusScroll.widthProperty().multiply(0.99));
        
        //making the blocks
        HBox box1 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box1, ohBuilder, 
                SYLLUBUS_DES_TOGGLE_BUTTON, SYLLUBUS_DES_LABEL, SYLLUBUS_DES_TEXTAREA);
        
        HBox box2 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box2, ohBuilder, 
                SYLLUBUS_TOPIC_TOGGLE_BUTTON, SYLLUBUS_TOPIC_LABEL, SYLLUBUS_TOPIC_TEXTAREA);
        
        HBox box3 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box3, ohBuilder, 
                SYLLUBUS_PREQ_TOGGLE_BUTTON, SYLLUBUS_PREQ_LABEL, SYLLUBUS_PREQ_TEXTAREA);
        
        HBox box4 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box4, ohBuilder, 
                SYLLUBUS_OUTCOME_TOGGLE_BUTTON, SYLLUBUS_OUTCOME_LABEL, SYLLUBUS_OUTCOME_TEXTAREA);
        
        HBox box5 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box5, ohBuilder, 
                SYLLUBUS_TEXTBOOK_TOGGLE_BUTTON, SYLLUBUS_TEXTBOOK_LABEL, SYLLUBUS_TEXTBOOK_TEXTAREA);
        
        HBox box6 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box6, ohBuilder, 
                SYLLUBUS_GRADED_COMP_TOGGLE_BUTTON, SYLLUBUS_GRADED_COMP_LABEL, SYLLUBUS_GRADED_COMP_TEXTAREA);
        
        HBox box7 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box7, ohBuilder, 
                SYLLUBUS_GRADING_NOTE_TOGGLE_BUTTON, SYLLUBUS_GRADING_NOTE_LABEL, SYLLUBUS_GRADING_NOTE_TEXTAREA);
        
        HBox box8 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box8, ohBuilder, 
                SYLLUBUS_ACAD_DIS_TOGGLE_BUTTON, SYLLUBUS_ACAD_DIS_LABEL, SYLLUBUS_ACAD_DIS_TEXTAREA);
        
        HBox box9 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box9, ohBuilder, 
                SYLLUBUS_SPEC_ASSIST_TOGGLE_BUTTON, SYLLUBUS_SPEC_ASSIST_LABEL, SYLLUBUS_SPEC_ASSIST_TEXTAREA);
        
        //setting arrangements
        box1.prefWidthProperty().bind(syllubusGridPane.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_DES_TEXTAREA)).prefWidthProperty().bind(box1.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_TOPIC_TEXTAREA)).prefWidthProperty().bind(box2.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_PREQ_TEXTAREA)).prefWidthProperty().bind(box3.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_OUTCOME_TEXTAREA)).prefWidthProperty().bind(box4.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_TEXTBOOK_TEXTAREA)).prefWidthProperty().bind(box5.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_GRADED_COMP_TEXTAREA)).prefWidthProperty().bind(box6.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_GRADING_NOTE_TEXTAREA)).prefWidthProperty().bind(box7.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_ACAD_DIS_TEXTAREA)).prefWidthProperty().bind(box8.widthProperty());
        ((TextArea) gui.getGUINode(SYLLUBUS_SPEC_ASSIST_TEXTAREA)).prefWidthProperty().bind(box9.widthProperty());
        
        GridPane.setRowIndex(box1, 0);
        GridPane.setRowIndex(box2, 2);
        GridPane.setRowIndex(box3, 3);
        GridPane.setRowIndex(box4, 4);
        GridPane.setRowIndex(box5, 5);
        GridPane.setRowIndex(box6, 6);
        GridPane.setRowIndex(box7, 7);
        GridPane.setRowIndex(box8, 8);
        GridPane.setRowIndex(box9, 9);
        
        syllubusGridPane.setVgap(5);
        
        syllubusGridPane.getChildren().addAll(box1, box2, box3, box4,
                box5, box6, box7, box8, box9);
        
        tabPane.getTabs().get(1).setContent(syllubusScroll);  
    }
    
    private void initSyllubusHelper(HBox box, AppNodesBuilder ohBuilder,
            CourseSitePropertyType toggleButtonPropertyType,
            CourseSitePropertyType labelPropertyType,
            CourseSitePropertyType textAreaPropertyType){
        
        VBox headBox = new VBox();
        HBox box1 = new HBox();
        HBox box2 = new HBox();
        
        ToggleButton tb = ohBuilder.buildTextToggleButton(toggleButtonPropertyType, null, CLASS_OH_BUTTON, null, ENABLED, DISABLED);
        Label lb = ohBuilder.buildLabel(labelPropertyType, null, CLASS_LABEL, ENABLED);
        
        box1.getChildren().addAll(tb, lb);
        box1.setSpacing(15);
        box1.setAlignment(Pos.CENTER_LEFT);
        
        TextArea tf = ohBuilder.buildTextArea(textAreaPropertyType, null, CLASS_TEXT_AREA, ENABLED);
        box2.getChildren().add(tf);
        
        headBox.getChildren().add(box1);
        headBox.setSpacing(15);
        
        box.getChildren().add(headBox);
  
        //add listener to textarea so it expands
        tf.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                tf.setPrefRowCount(tf.getText().split("\n").length);
            }
        });
        
        //set toggle button on action
        tb.setOnAction(e ->{
            if (tb.isSelected()){
                headBox.getChildren().add(box2);
            }
            else{
                headBox.getChildren().remove(1);
            }
        });
    }
    
    private void initSitePage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane siteGridPane = new GridPane();
        
        //adding a scroll pane on this page
        ScrollPane siteScroll = ohBuilder.buildScrollPane(SD_SCROLL_PANE, siteGridPane, CLASS_SCROLL_PANE, ENABLED);
        siteGridPane.prefWidthProperty().bind(siteScroll.widthProperty().multiply(0.99));
        
        //banner block
        HBox banner = ohBuilder.buildHBox(SITE_BANNER_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        VBox biggestBox = new VBox();
        
        //first HBox
        Label bannerLabel = ohBuilder.buildLabel(SITE_BANNER_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        HBox firstBox = new HBox(bannerLabel);
        
        //second HBox with 2 VBox inside
        Label subject = ohBuilder.buildLabel(SITE_SBJ_LABEL, null, CLASS_LABEL, ENABLED);
        Label number = ohBuilder.buildLabel(SITE_NUMBER_LABEL, null, CLASS_LABEL, ENABLED);
        VBox labelVBox1 = new VBox(subject, number);
        
        ComboBox subjectCB = ohBuilder.buildComboBox(SITE_SBJ_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        ComboBox numberCB = ohBuilder.buildComboBox(SITE_NUMBER_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        VBox comboVBox1 = new VBox(subjectCB, numberCB);
        
        Label semester = ohBuilder.buildLabel(SITE_SEMESTER_LABEL, null, CLASS_LABEL, ENABLED);
        Label year = ohBuilder.buildLabel(SITE_YEAR_LABEL, null, CLASS_LABEL, ENABLED);
        VBox labelVBox2 = new VBox(semester, year);
        
        ComboBox yearCB = ohBuilder.buildComboBox(SITE_YEAR_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        ComboBox semesterCB = ohBuilder.buildComboBox(SITE_SEMESTER_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        VBox comboVBox2 = new VBox(semesterCB, yearCB);
        
        labelVBox1.setSpacing(20.0);
        comboVBox1.setSpacing(10.0);
        labelVBox2.setSpacing(20.0);
        comboVBox2.setSpacing(10.0);
        HBox secondBox = new HBox(labelVBox1, comboVBox1, labelVBox2, comboVBox2);
        secondBox.setSpacing(50);
        
        //third HBox
        Label title = ohBuilder.buildLabel(SITE_TITLE_LABEL, null, CLASS_LABEL, ENABLED);
        TextField titleTextField = ohBuilder.buildTextField(SITE_TITLE_TEXT_FIELD, null, CLASS_OH_TEXT_FIELD, ENABLED);
        HBox thirdBox = new HBox(title, titleTextField);
        thirdBox.setSpacing(71);
        
        //fourth HBox
        Label exportDirLabel = ohBuilder.buildLabel(SITE_EXPORT_DIR_LABEL, null, CLASS_LABEL, ENABLED);
        Label exportDir = new Label(".\\export\\CSE_219_Fall_2018\\public_html");
        HBox fourthBox = new HBox(exportDirLabel, exportDir);
        fourthBox.setSpacing(38);
        
        biggestBox.getChildren().addAll(firstBox, secondBox, thirdBox, fourthBox);
        biggestBox.setSpacing(20.0);
        banner.getChildren().add(biggestBox);
        
        
        //page block
        HBox page = ohBuilder.buildHBox(SITE_PAGE_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        Label pageLabel = ohBuilder.buildLabel(SITE_PAGE_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        CheckBox homeCB = ohBuilder.buildCheckBox(SITE_HOME_CHECK_BOX, null, CLASS_CHECKBOX, ENABLED);
        CheckBox syllubusCB = ohBuilder.buildCheckBox(SITE_SYLLUBUS_CHECK_BOX, null, CLASS_CHECKBOX, ENABLED);
        CheckBox scheduleCB = ohBuilder.buildCheckBox(SITE_SCHEDULE_CHECK_BOX, null, CLASS_CHECKBOX, ENABLED);
        CheckBox hwCB = ohBuilder.buildCheckBox(SITE_HWS_CHECK_BOX, null, CLASS_CHECKBOX, ENABLED);
        page.getChildren().addAll(pageLabel, homeCB, syllubusCB, scheduleCB, hwCB);
        page.setSpacing(40);
        
        
        //style block
        HBox style = ohBuilder.buildHBox(SITE_STYLE_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        VBox biggestStyleBox = new VBox();
        
        //first style HBox
        Label styleLabel = ohBuilder.buildLabel(SITE_STYLE_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        HBox firstStyleBox = new HBox(styleLabel);
        
        //second style HBox
        Label noteLabel = ohBuilder.buildLabel(SITE_NOTE_LABEL, null, CLASS_LABEL, ENABLED);
        HBox secondStyleBox = new HBox(noteLabel);
        
        biggestStyleBox.getChildren().addAll(firstStyleBox, secondStyleBox);
        biggestStyleBox.setSpacing(20.0);
        style.getChildren().add(biggestStyleBox);
        
        
        //instructor block
        HBox instructor = ohBuilder.buildHBox(SITE_INSTRUCTOR_HBOX, null, CLASS_SITE_HBOX, ENABLED);
        VBox biggestInsBox = new VBox();
        
        //first HBox
        Label instructorLabel = ohBuilder.buildLabel(SITE_INSTRUCTOR_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        HBox firstInsBox = new HBox(instructorLabel);
        
        //second HBox with 2 VBox inside
        Label name = ohBuilder.buildLabel(SITE_NAME_LABEL, null, CLASS_LABEL, ENABLED);
        Label email = ohBuilder.buildLabel(SITE_EMAIL_LABEL, null, CLASS_LABEL, ENABLED);
        VBox insLabelVBox1 = new VBox(name, email);
        
        ComboBox nameCB = ohBuilder.buildComboBox(SITE_NAME_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        ComboBox emailCB = ohBuilder.buildComboBox(SITE_EMAIL_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        VBox insComboVBox1 = new VBox(nameCB, emailCB);
        
        Label roomLabel = ohBuilder.buildLabel(SITE_ROOM_LABEL, null, CLASS_LABEL, ENABLED);
        Label homePageLabel = ohBuilder.buildLabel(SITE_HOME_PAGE_LABEL, null, CLASS_LABEL, ENABLED);
        VBox insLabelVBox2 = new VBox(roomLabel, homePageLabel);
        
        ComboBox roomCB = ohBuilder.buildComboBox(SITE_ROOM_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        ComboBox homePageCB = ohBuilder.buildComboBox(SITE_HOME_PAGE_COMBO_BOX, null, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED, EDITABLE, FIRST_OPTION);
        VBox insComboVBox2 = new VBox(roomCB, homePageCB);
        
        insLabelVBox1.setSpacing(20.0);
        insComboVBox1.setSpacing(10.0);
        insLabelVBox2.setSpacing(20.0);
        insComboVBox2.setSpacing(10.0);
        HBox secondInsBox = new HBox(insLabelVBox1, insComboVBox1, insLabelVBox2, insComboVBox2);
        secondInsBox.setSpacing(50);
        
        biggestInsBox.getChildren().addAll(firstInsBox, secondInsBox);
        biggestInsBox.setSpacing(20.0);
        instructor.getChildren().add(biggestInsBox);
        
        //Setting arrangement
        GridPane.setRowIndex(banner, 0);
        GridPane.setRowIndex(page, 1);
        GridPane.setRowIndex(style, 2);
        GridPane.setRowIndex(instructor, 3);
        banner.prefWidthProperty().bind(siteGridPane.widthProperty());
        banner.prefHeightProperty().bind(siteScroll.heightProperty().multiply(3.0 / 10.0));
        page.prefHeightProperty().bind(siteScroll.heightProperty().multiply(1.0 / 10.0));
        style.prefHeightProperty().bind(siteScroll.heightProperty().multiply(4.0 / 10.0));
        instructor.prefHeightProperty().bind(siteScroll.heightProperty().multiply(2.0 / 10.0));
        
        siteGridPane.setVgap(5);
        
        siteGridPane.getChildren().addAll(banner, page, style, instructor);
        
        tabPane.getTabs().get(0).setContent(siteScroll);
    }
    
    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(CSG_FOOLPROOF_SETTINGS,
                new CourseSiteFoolproofDesign((CourseSiteGeneratorApp) app));
    }
    
    private void initDialogs() {
        TADialog taDialog = new TADialog((CourseSiteGeneratorApp) app);
        app.getGUIModule().addDialog(CSG_TA_EDIT_DIALOG, taDialog);
    }
    
    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(CSG_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains    (selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }

    // method which returns start time sublist we need for oh time range comboBox
    private ObservableList<String> getStList(String endTime, 
            ObservableList<String> stOptions, ObservableList<String> etOptions) {
        
        ObservableList<String> toBeDisplayedList = FXCollections.<String>observableArrayList();

        toBeDisplayedList.addAll(stOptions.subList(0, (etOptions.indexOf(endTime))+1));
        
        return toBeDisplayedList;
    }
    
    // method which returns end time sublist we need for oh time range comboBox
    private ObservableList<String> getEtList(String startTime, ObservableList<String> etOptions) {

        ObservableList<String> toBeDisplayedList = FXCollections.<String>observableArrayList();
        
        toBeDisplayedList.addAll(etOptions.subList(
                (etOptions.indexOf(startTime))+1, etOptions.size()));

        return toBeDisplayedList;
    }
    
    // method to check if the oh table is shown with full interval
    private boolean fullInterval(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox startTimeCB = (ComboBox) gui.getGUINode(OH_START_TIME_COMBO_BOX);
        ComboBox endTimeCB = (ComboBox) gui.getGUINode(OH_END_TIME_COMBO_BOX);
        
        return (startTimeCB.getSelectionModel().getSelectedItem().equals("8:00am") &&
                endTimeCB.getSelectionModel().getSelectedItem().equals("12:00am"));
    }
    
}
