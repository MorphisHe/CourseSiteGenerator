package csg.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorApp;
import csg.CourseSitePropertyType;
import static csg.CourseSitePropertyType.*;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.workspace.controllers.CourseSiteController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CourseSiteFoolproofDesign;
import static csg.workspace.style.OHStyle.*;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author McKillaGorilla
 */
public class CourseSiteWorkspace extends AppWorkspaceComponent {

    public CourseSiteWorkspace(CourseSiteGeneratorApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();
//
//        // 
//        initFoolproofDesign();
//
//        // INIT DIALOGS
//        initDialogs();
    }
    
    private void initLayout(){
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

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
        
        //making officehours
        initOfficeHourPage(ohBuilder, tabPane);
        
        //this is out workspace
        workspace = mainBox;
    }

    @Override
    public void showNewDialog() {
        
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        
    }
    
    private void initControllers(){
        CourseSiteController controller = new CourseSiteController((CourseSiteGeneratorApp) app);
        AppGUIModule gui = app.getGUIModule();
        
    }
    
    private void initOfficeHourPage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane ohGridPane = new GridPane();
        VBox mainBox = new VBox();
        
        //making the blocks
        HBox taHeaderBox = new HBox();
        taHeaderBox.setAlignment(Pos.CENTER_LEFT);
        
        ToggleButton tb = new ToggleButton("+");
        Label lb = ohBuilder.buildLabel(CSG_TAS_HEADER_LABEL, null, CLASS_LABEL, ENABLED);
        
        //radio buttons
        ToggleGroup tg = new ToggleGroup();
        RadioButton all = ohBuilder.buildRadioButton(CSG_ALL_RADIO_BUTTON, null, 
                CLASS_OH_RADIO_BUTTON, ENABLED, tg, ENABLED);
        RadioButton grad = ohBuilder.buildRadioButton(CSG_GRAD_RADIO_BUTTON, null, 
                CLASS_OH_RADIO_BUTTON, ENABLED, tg, ENABLED);
        RadioButton underGrad = ohBuilder.buildRadioButton(CSG_UNDERGRAD_RADIO_BUTTON, null, 
                CLASS_OH_RADIO_BUTTON, ENABLED, tg, ENABLED);
        
        taHeaderBox.getChildren().addAll(tb, lb, all, grad, underGrad);
        taHeaderBox.setSpacing(15);
        
        mainBox.getChildren().addAll(taHeaderBox);
        
        mainBox.setSpacing(15);
        ohGridPane.setVgap(5);
        
        ohGridPane.getChildren().add(mainBox);
        tabPane.getTabs().get(3).setContent(ohGridPane);
    }
    
    private void initSyllubusPage(AppNodesBuilder ohBuilder, TabPane tabPane){
        GridPane syllubusGridPane = new GridPane();
        
        //making the blocks
        HBox box1 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box1, ohBuilder, SYLLUBUS_DES_LABEL, SYLLUBUS_DES_TEXTFIELD);
        
        HBox box2 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box2, ohBuilder, SYLLUBUS_TOPIC_LABEL, SYLLUBUS_TOPIC_TEXTFIELD);
        
        HBox box3 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box3, ohBuilder, SYLLUBUS_PREQ_LABEL, SYLLUBUS_PREQ_TEXTFIELD);
        
        HBox box4 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box4, ohBuilder, SYLLUBUS_OUTCOME_LABEL, SYLLUBUS_OUTCOME_TEXTFIELD);
        
        HBox box5 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box5, ohBuilder, SYLLUBUS_TEXTBOOK_LABEL, SYLLUBUS_TEXTBOOK_TEXTFIELD);
        
        HBox box6 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box6, ohBuilder, SYLLUBUS_GRADED_COMP_LABEL, SYLLUBUS_GRADED_COMP_TEXTFIELD);
        
        HBox box7 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box7, ohBuilder, SYLLUBUS_GRADING_NOTE_LABEL, SYLLUBUS_GRADING_NOTE_TEXTFIELD);
        
        HBox box8 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box8, ohBuilder, SYLLUBUS_ACAD_DIS_LABEL, SYLLUBUS_ACAD_DIS_TEXTFIELD);
        
        HBox box9 = ohBuilder.buildHBox(EMPTY_TEXT, null, CLASS_SITE_HBOX, ENABLED);
        initSyllubusHelper(box9, ohBuilder, SYLLUBUS_SPEC_ASSIST_LABEL, SYLLUBUS_SPEC_ASSIST_TEXTFIELD);
        
        //setting arrangements
        box1.prefWidthProperty().bind(syllubusGridPane.widthProperty());
        
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
        
        tabPane.getTabs().get(1).setContent(syllubusGridPane);
        
        
    }
    
    private void initSyllubusHelper(HBox box, AppNodesBuilder ohBuilder, 
            CourseSitePropertyType labelPropertyType,
            CourseSitePropertyType textfieldPropertyType){
        
        VBox headBox = new VBox();
        HBox box1 = new HBox();
        HBox box2 = new HBox();
        
        ToggleButton tb = new ToggleButton("+");
        Label lb = ohBuilder.buildLabel(labelPropertyType, null, CLASS_LABEL, ENABLED);
        
        box1.getChildren().addAll(tb, lb);
        box1.setSpacing(15);
        box1.setAlignment(Pos.CENTER_LEFT);
        
        TextArea tf = new TextArea();
        tf.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.err.println("here: " +  tf.getText().split("\n").length);
                tf.setPrefRowCount(tf.getText().split("\n").length);
                System.err.println(tf.getPrefRowCount());
            }
        });
        
        box2.getChildren().add(tf);
        
        headBox.getChildren().add(box1);
        headBox.setSpacing(15);
        
        box.getChildren().add(headBox);
  
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
        
        ComboBox subjectCB = ohBuilder.buildComboBox(SITE_SBJ_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
        ComboBox numberCB = ohBuilder.buildComboBox(SITE_NUMBER_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
        VBox comboVBox1 = new VBox(subjectCB, numberCB);
        
        Label semester = ohBuilder.buildLabel(SITE_SEMESTER_LABEL, null, CLASS_LABEL, ENABLED);
        Label year = ohBuilder.buildLabel(SITE_YEAR_LABEL, null, CLASS_LABEL, ENABLED);
        VBox labelVBox2 = new VBox(semester, year);
        
        ComboBox yearCB = ohBuilder.buildComboBox(SITE_YEAR_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
        ComboBox semesterCB = ohBuilder.buildComboBox(SITE_SEMESTER_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
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
        
        ComboBox nameCB = ohBuilder.buildComboBox(SITE_NAME_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
        ComboBox emailCB = ohBuilder.buildComboBox(SITE_EMAIL_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
        VBox insComboVBox1 = new VBox(nameCB, emailCB);
        
        Label roomLabel = ohBuilder.buildLabel(SITE_ROOM_LABEL, null, CLASS_LABEL, ENABLED);
        Label homePageLabel = ohBuilder.buildLabel(SITE_HOME_PAGE_LABEL, null, CLASS_LABEL, ENABLED);
        VBox insLabelVBox2 = new VBox(roomLabel, homePageLabel);
        
        ComboBox roomCB = ohBuilder.buildComboBox(SITE_ROOM_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
        ComboBox homePageCB = ohBuilder.buildComboBox(SITE_HOME_PAGE_COMBO_BOX, OH_CANCEL_PROMPT, 
                CLASS_OH_PROMPT, null, CLASS_COMBO_BOX, ENABLED);
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
        
        //scrollpane for this blocker
        ohBuilder.buildScrollPane(SITE_SCROLL_PANE, siteGridPane, ENABLED);
        
        
        //Setting arrangement
        GridPane.setRowIndex(banner, 0);
        GridPane.setRowIndex(page, 1);
        GridPane.setRowIndex(style, 2);
        GridPane.setRowIndex(instructor, 3);
        banner.prefWidthProperty().bind(siteGridPane.widthProperty());
        banner.prefHeightProperty().bind(siteGridPane.heightProperty().multiply(3.0 / 10.0));
        page.prefHeightProperty().bind(siteGridPane.heightProperty().multiply(1.0 / 10.0));
        style.prefHeightProperty().bind(siteGridPane.heightProperty().multiply(4.0 / 10.0));
        instructor.prefHeightProperty().bind(siteGridPane.heightProperty().multiply(2.0 / 10.0));
        
        siteGridPane.setVgap(5);
        
        siteGridPane.getChildren().addAll(banner, page, style, instructor);
        
        tabPane.getTabs().get(0).setContent(siteGridPane);
    }

    
}
