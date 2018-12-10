package csg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CourseSiteGeneratorApp;
import csg.CourseSitePropertyType;
import csg.data.OfficeHoursData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import djf.modules.AppGUIModule;
import static csg.CourseSitePropertyType.*;
import csg.data.Labs;
import csg.data.Lectures;
import csg.data.Recitations;
import csg.data.Schedule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import csg.workspace.CourseSiteWorkspace;
import csg.workspace.controllers.CourseSiteController;
import static djf.AppPropertyType.SAVE_BUTTON;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javax.json.JsonObjectBuilder;


/**
 * This class serves as the file component for the TA
 * manager App. It provides all saving and loading 
 * services for the application.
 * 
 * @author Jiang He
 */
public class CourseSiteFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CourseSiteGeneratorApp app;
    
    // PATH FOR COMBO BOX DATA FILE
    static final String CB_DATA_FILE_PATH = "cb_data\\cb_data.json";   
    
    // FOLLOWING ARE USED FOR IDENTIFYING JSON TYPES
    
    // JSON TYPES FOR OFFICE HOURS PAGE
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_LINK = "link";
    static final String JSON_ROOM = "room";
    static final String JSON_HOURS = "hours";
    static final String JSON_TIME = "time";
    static final String JSON_OH_TABLE_START_INTERVAL = "oh_start_interval";
    static final String JSON_OH_TABLE_END_INTERVAL = "oh_end_interval";
    // JSON TYPES FOR SITE PAGE
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_LOGOS = "logos";
    static final String JSON_FAB_ICON = "favicon";
    static final String JSON_NAV_BAR = "navbar";
    static final String JSON_BOTTOM_LEFT = "bottom_left";
    static final String JSON_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_PAGES = "pages";
    static final String JSON_SRC = "src";
    static final String JSON_HREF = "href";
    static final String JSON_CSS = "css";
    // JSON TYPES FOR SYLLUBUS PAGE
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_TOPICS = "topics";
    static final String JSON_PREQ = "prerequisites";
    static final String JSON_OUTCOMES = "outcomes";
    static final String JSON_TEXTBOOKS = "textbooks";
    static final String JSON_GRADED_COMP = "gradedComponents";
    static final String JSON_GRADING_NOTE = "gradingNote";
    static final String JSON_ACAD_DIS = "academicDishonesty";
    static final String JSON_SPECIAL_ASIST = "specialAssistance";
    // JSON TYPES FOR SCHEDULE PAGE
    static final String JSON_START_MONDAY_MONTH = "startingMondayMonth";
    static final String JSON_START_MONDAY_DAY = "startingMondayDay";
    static final String JSON_END_FRIDAY_MONTH = "endingFridayMonth";
    static final String JSON_END_FRIDAY_DAY = "endingFridayDay";
    static final String JSON_MONTH = "month";
    static final String JSON_DAY = "day";
    static final String JSON_TOPIC = "topic";
    static final String JSON_CRITERIA = "criteria";
    static final String JSON_DATE = "date";
    static final String JSON_SCHEDULE = "schedule";
    static final String JSON_SD_HOLIDAYS = "schedule_holidays";
    static final String JSON_SD_LECTURES = "schedule_lectures";
    static final String JSON_SD_REFERENCES = "schedule_references";
    static final String JSON_SD_RECITATIONS = "schedule_recitations";
    static final String JSON_SD_HWS = "schedule_hws";
    // JSON TYPES FOR MEETING TIME PAGE
    // LECTURES TABLE
    static final String JSON_SECTION = "section";
    static final String JSON_DAYS = "days";
    static final String JSON_LECTURES = "lectures";
    // RECITATIONS AND LABS TABLE
    static final String JSON_LABS = "labs";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";
    // JSON TYPES FOR COMBO BOX DATA
    static final String JSON_SITE_SEMESTER = "siteSemester";
    static final String JSON_SITE_SUBJECT = "siteSubject";
    static final String JSON_SUBJECT_NUM = "subjectNumber";
    static final String JSON_SITE_CSS = "siteCss";
    

    public CourseSiteFiles(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        JsonObject json = loadJSONFile(filePath);
        AppGUIModule gui = app.getGUIModule();
        
        // LOAD DATA FOR OFFICE HOUR PAGE
	loadOfficeHoursPage(data, json, gui);
        
        // LOAD DATA FOR SITE PAGE
        loadSitePage(json, gui);
        
        // LOAD DATA FOR SYLLUBUS PAGE
        loadSyllubusPage(json, gui);
        
        // LOAD DATA FOR SCHEDULE PAGE
        loadSchedulePage(json, gui);
        
        // LOAD DATA FOR MEETING TIME PAGE
        loadMeetingTimePage(json, gui);
        
        // DISABLE SAVE BUTTON
        ((Button)gui.getGUINode(SAVE_BUTTON)).setDisable(true);
    }
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException     {
	// GET THE DATA
	OfficeHoursData dataManager = (OfficeHoursData)data;
        AppGUIModule gui = app.getGUIModule();
        
        // NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (DayOfWeek dow : DayOfWeek.values()) {
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                            .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                            .add(JSON_DAY_OF_WEEK, dow.toString())
                            .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        // BUILD THE INSTRUCTOR OFFICE HOUR DATA
        String[] breakDownArray = ((TextArea)gui.getGUINode(SITE_OFFICE_HOURS_TEXT_AREA))
                .getText().replace("[", "").replace("]", "").split("\n");
        JsonArrayBuilder insOhArrayBuilder = Json.createArrayBuilder();
        for (String breakDownArray1 : breakDownArray) {
            if (breakDownArray1.contains("{")) {
                String[] temp = breakDownArray1.split(",", 2);
                JsonObject insOHjson = Json.createObjectBuilder()
                        .add(JSON_DAY, temp[0].split(":")[1]
                                .replace("\"", "").trim()) //adding the day value
                        .add(JSON_TIME, temp[1].split(":", 2)[1]
                                .replace("\"", "").replace("}", "")
                                .replace(",", "").trim())
                        .build();
                insOhArrayBuilder.add(insOHjson);
            } 
        }
        JsonArray insOhArray = insOhArrayBuilder.build();
        
        // BUILD THE INSTRUCTOR INFO
        JsonObject instructorJson = Json.createObjectBuilder()
                .add(JSON_NAME, ((TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD)).getText())
                .add(JSON_EMAIL, ((TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD)).getText())
                .add(JSON_ROOM, ((TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD)).getText())
                .add(JSON_LINK, ((TextField)gui.getGUINode(SITE_HOME_PAGE_TEXT_FIELD)).getText())
                .add(JSON_HOURS, insOhArray)
                .build();
        
        // BUILD THE SITE LOGOS DATA
        HBox favHBox = ((HBox)gui.getGUINode(SITE_FAV_HBOX));
        HBox navHBox = ((HBox)gui.getGUINode(SITE_NAV_HBOX));
        HBox lfHbox = ((HBox)gui.getGUINode(SITE_LF_IMAGE_HBOX));
        HBox rfHbox = ((HBox)gui.getGUINode(SITE_RF_IMAGE_HBOX));
        String[] favSplit = ((ImageView)favHBox.getChildren().get(1)).getImage().impl_getUrl().split("/");
        String[] navSplit = ((ImageView)navHBox.getChildren().get(1)).getImage().impl_getUrl().split("/");
        String[] lfSplit = ((ImageView)lfHbox.getChildren().get(1)).getImage().impl_getUrl().split("/");
        String[] rfSplit = ((ImageView)rfHbox.getChildren().get(1)).getImage().impl_getUrl().split("/");
        
        JsonObject faviconJson = Json.createObjectBuilder()
                .add(JSON_HREF, "./images/SBUShieldFavicon.ico")
                .add(JSON_SRC, favSplit[favSplit.length-1])
                .build();
        JsonObject navBarJson = Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.stonybrook.edu")
                .add(JSON_SRC, navSplit[navSplit.length-1])
                .build();
        JsonObject lfImageJson = Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.cs.stonybrook.edu")
                .add(JSON_SRC, lfSplit[lfSplit.length-1])
                .build();
        JsonObject rfImageJson = Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.cs.stonybrook.edu")
                .add(JSON_SRC, rfSplit[rfSplit.length-1])
                .build();
        JsonObject logosJson = Json.createObjectBuilder()
                .add(JSON_FAB_ICON, faviconJson)
                .add(JSON_NAV_BAR, navBarJson)
                .add(JSON_BOTTOM_LEFT, lfImageJson)
                .add(JSON_BOTTOM_RIGHT, rfImageJson)
                .build();
        
        // NOW BUILD THE PAGE DATA
        JsonArrayBuilder sitePageArrayBuilder = Json.createArrayBuilder();
        JsonObject homePageJson = Json.createObjectBuilder()
                                      .add(JSON_NAME, pageIsSelected(gui, SITE_HOME_CHECK_BOX, "Home"))
                                      .add(JSON_LINK, "index.html")
                                      .build();
        JsonObject syllabusPageJson = Json.createObjectBuilder()
                                      .add(JSON_NAME, pageIsSelected(gui, SITE_SYLLUBUS_CHECK_BOX, "Syllabus"))
                                      .add(JSON_LINK, "syllabus.html")
                                      .build();
        JsonObject schedulePageJson = Json.createObjectBuilder()
                                      .add(JSON_NAME, pageIsSelected(gui, SITE_SCHEDULE_CHECK_BOX, "Schedule"))
                                      .add(JSON_LINK, "schedule.html")
                                      .build();
        JsonObject hwsPageJson = Json.createObjectBuilder()
                                      .add(JSON_NAME, pageIsSelected(gui, SITE_HWS_CHECK_BOX, "HWs"))
                                      .add(JSON_LINK, "hws.html")
                                      .build();
        JsonArray sitePageJson = sitePageArrayBuilder.add(homePageJson)
                                                     .add(syllabusPageJson)
                                                     .add(schedulePageJson)
                                                     .add(hwsPageJson)
                                                     .build();
        
        // BUILD DATA FOR DATA PICKER
        String[] StartDate = ((DatePicker)gui.getGUINode(SD_START_MON_DATE_PICKER))
                                             .getValue().toString().split("-");
        String[] endDate = ((DatePicker)gui.getGUINode(SD_END_FRI_DATE_PICKER))
                                             .getValue().toString().split("-");
       
        // BUILD THE DATA FOR SCHEDULE TABLE
        JsonArrayBuilder scheduleArrayBuilder = Json.createArrayBuilder();
        
        Iterator<Schedule> scheduleIterator = dataManager.scheduleIterator();
        while (scheduleIterator.hasNext()) {
            Schedule schedule = scheduleIterator.next();
            JsonObject scheduleObject = Json.createObjectBuilder()
                                            .add(JSON_TYPE, schedule.getType())
                                            .add(JSON_DATE, schedule.getDate())
                                            .add(JSON_TOPIC, schedule.getTopic())
                                            .add(JSON_TITLE, schedule.getTitle())
                                            .add(JSON_LINK, schedule.getLink()).build();
            scheduleArrayBuilder.add(scheduleObject);
        }
        JsonArray scheduleJsonArray = scheduleArrayBuilder.build();
        //FOLLOWING ARE USED FOR EXPORTING
//        JsonArrayBuilder sdHolidaysJsonArrayBuilder = Json.createArrayBuilder();
//        JsonArrayBuilder sdLecturesJsonArrayBuilder = Json.createArrayBuilder();
//        JsonArrayBuilder sdReferencesJsonArrayBuilder = Json.createArrayBuilder();
//        JsonArrayBuilder sdRecitationsJsonArrayBuilder = Json.createArrayBuilder();
//        JsonArrayBuilder sdHWsJsonArrayBuilder = Json.createArrayBuilder();
//        
//        Iterator<Schedule> scheduleIterator = dataManager.scheduleIterator();
//        while (scheduleIterator.hasNext()) {
//            Schedule schedule = scheduleIterator.next();
//            switch(schedule.getType()){
//                case "Holiday" :
//                    JsonObject holidayObject = Json.createObjectBuilder()
//                                                   .add(JSON_MONTH, schedule.getDate().split("\\\\")[0])
//                                                   .add(JSON_DAY, schedule.getDate().split("\\\\")[1])
//                                                   .add(JSON_TITLE, schedule.getTitle())
//                                                   .add(JSON_LINK, schedule.getLink()).build();
//                    sdHolidaysJsonArrayBuilder.add(holidayObject);
//                    break;
//                case "Lecture" :
//                    JsonObject lecturesObject = Json.createObjectBuilder()
//                                                    .add(JSON_MONTH, schedule.getDate().split("\\\\")[0])
//                                                    .add(JSON_DAY, schedule.getDate().split("\\\\")[1])
//                                                    .add(JSON_TITLE, schedule.getTitle())
//                                                    .add(JSON_TOPIC, schedule.getTopic())
//                                                    .add(JSON_LINK, schedule.getLink()).build();
//                    sdLecturesJsonArrayBuilder.add(lecturesObject);
//                    break;
//                case "Reference" :
//                    JsonObject referencesObject = Json.createObjectBuilder()
//                                                      .add(JSON_MONTH, schedule.getDate().split("\\\\")[0])
//                                                      .add(JSON_DAY, schedule.getDate().split("\\\\")[1])
//                                                      .add(JSON_TITLE, schedule.getTitle())
//                                                      .add(JSON_TOPIC, schedule.getTopic())
//                                                      .add(JSON_LINK, schedule.getLink()).build();
//                    sdReferencesJsonArrayBuilder.add(referencesObject);
//                    break;
//                case "Recitation" :
//                    JsonObject recitationsObject = Json.createObjectBuilder()
//                                                       .add(JSON_MONTH, schedule.getDate().split("\\\\")[0])
//                                                       .add(JSON_DAY, schedule.getDate().split("\\\\")[1])
//                                                       .add(JSON_TITLE, schedule.getTitle())
//                                                       .add(JSON_TOPIC, schedule.getTopic())
//                                                       .add(JSON_LINK, schedule.getLink()).build();
//                    sdRecitationsJsonArrayBuilder.add(recitationsObject);
//                    break;
//                case "HW" :
//                    JsonObject hwsObject = Json.createObjectBuilder()
//                                               .add(JSON_MONTH, schedule.getDate().split("\\\\")[0])
//                                               .add(JSON_DAY, schedule.getDate().split("\\\\")[1])
//                                               .add(JSON_TITLE, schedule.getTitle())
//                                               .add(JSON_TOPIC, schedule.getTopic())
//                                               .add(JSON_LINK, schedule.getLink())
//                                               .add(JSON_TIME, "")
//                                               .add(JSON_CRITERIA, "none").build();
//                    sdHWsJsonArrayBuilder.add(hwsObject);
//                    break;    
//            }
//        }
//        
//        JsonArray sdHolidaysJsonArray = sdHolidaysJsonArrayBuilder.build();
//        JsonArray sdLecturesJsonArray = sdLecturesJsonArrayBuilder.build();
//        JsonArray sdReferencesJsonArray = sdReferencesJsonArrayBuilder.build();
//        JsonArray sdRecitationsJsonArray = sdRecitationsJsonArrayBuilder.build();
//        JsonArray sdHWsJsonArray = sdHWsJsonArrayBuilder.build();
        
        // BUILD DATA FOR LECTURE TABLE IN MEETING TIME TAB       
        // BUILD LECTURES TABLE DATA
        JsonArrayBuilder lecturesJsonArrayBuilder = Json.createArrayBuilder();
        Iterator<Lectures> lecturesIterator = dataManager.lecturesIterator();
        while (lecturesIterator.hasNext()) {
            Lectures lectures = lecturesIterator.next();
	    JsonObject lectureJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, lectures.getSection())
		    .add(JSON_DAYS, lectures.getDay())
                    .add(JSON_TIME, lectures.getTime())
                    .add(JSON_ROOM, lectures.getRoom()).build();
            lecturesJsonArrayBuilder.add(lectureJson);
	}
        JsonArray lecturesJsonArray = lecturesJsonArrayBuilder.build();
        
        // BUILD RECITATIONS TABLE DATA
        JsonArrayBuilder recitationsJsonArrayBuilder = Json.createArrayBuilder();
        Iterator<Recitations> recitationsIterator = dataManager.recitationsIterator();
        while (recitationsIterator.hasNext()) {
            Recitations recitation = recitationsIterator.next();
	    JsonObject recitationJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, recitation.getSection())
		    .add(JSON_DAY_TIME, recitation.getDaysTime())
                    .add(JSON_LOCATION, recitation.getRoom())
                    .add(JSON_TA1, recitation.getTA1())
                    .add(JSON_TA2, recitation.getTA2()).build();
            recitationsJsonArrayBuilder.add(recitationJson);
	}
        JsonArray recitationsJsonArray = recitationsJsonArrayBuilder.build();
        
        // BUILD LABS TABLE DATA
        JsonArrayBuilder labsJsonArrayBuilder = Json.createArrayBuilder();
        Iterator<Labs> labsIterator = dataManager.labsIterator();
        while (labsIterator.hasNext()) {
            Labs lab = labsIterator.next();
	    JsonObject labJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, lab.getSection())
		    .add(JSON_DAY_TIME, lab.getDaysTime())
                    .add(JSON_LOCATION, lab.getRoom())
                    .add(JSON_TA1, lab.getTA1())
                    .add(JSON_TA2, lab.getTA2()).build();
            labsJsonArrayBuilder.add(labJson);
	}
        JsonArray labsJsonArray = labsJsonArrayBuilder.build();
        
        
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject fullJsonData = Json.createObjectBuilder()
                                      .add(JSON_START_HOUR, "" + dataManager.getStartHour())
                                      .add(JSON_END_HOUR, "" + dataManager.getEndHour())
                                      .add(JSON_OH_TABLE_START_INTERVAL, ((ComboBox)gui.getGUINode(OH_START_TIME_COMBO_BOX)).getValue().toString())
                                      .add(JSON_OH_TABLE_END_INTERVAL, ((ComboBox)gui.getGUINode(OH_END_TIME_COMBO_BOX)).getValue().toString())
                                      .add(JSON_INSTRUCTOR, instructorJson)
                                      .add(JSON_GRAD_TAS, gradTAsArray)
                                      .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                                      .add(JSON_OFFICE_HOURS, officeHoursArray) //above inclusive are oh page
                                      .add(JSON_SUBJECT, ((ComboBox) gui.getGUINode(SITE_SBJ_COMBO_BOX)) //subject combobox item
                                                    .getSelectionModel().getSelectedItem().toString())
                                      .add(JSON_NUMBER, ((ComboBox) gui.getGUINode(SITE_NUMBER_COMBO_BOX)) //number combobox item
                                                    .getSelectionModel().getSelectedItem().toString())
                                      .add(JSON_SEMESTER, ((ComboBox) gui.getGUINode(SITE_SEMESTER_COMBO_BOX)) //semester combobox item
                                                    .getSelectionModel().getSelectedItem().toString())
                                      .add(JSON_YEAR, ((ComboBox) gui.getGUINode(SITE_YEAR_COMBO_BOX)) //year combobox item
                                                    .getSelectionModel().getSelectedItem().toString())
                                      .add(JSON_TITLE, ((TextField) gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText()) //title text field
                                      .add(JSON_LOGOS, logosJson) //adding the logo data
                                      .add(JSON_CSS, ((ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX)) //css combo box value
                                              .getSelectionModel().getSelectedItem().toString())
                                      .add(JSON_PAGES, sitePageJson) //adding the site page checkbox data
                                      .add(JSON_DESCRIPTION, ((TextArea)gui.getGUINode(SYLLUBUS_DES_TEXTAREA)).getText().replace("\"", "")) //syllabus des
                                      .add(JSON_TOPICS, stringToJsonArray(gui, SYLLUBUS_TOPIC_TEXTAREA)) //syllabus topics
                                      .add(JSON_PREQ, ((TextArea)gui.getGUINode(SYLLUBUS_PREQ_TEXTAREA)).getText().replace("\"", "")) //syllabus prerequisites
                                      .add(JSON_OUTCOMES, stringToJsonArray(gui, SYLLUBUS_OUTCOME_TEXTAREA)) //syllabus outcomes
                                      .add(JSON_TEXTBOOKS, StringToArrayOfDic(gui, SYLLUBUS_TEXTBOOK_TEXTAREA)) //syllabus textbook
                                      .add(JSON_GRADED_COMP, StringToArrayOfDic(gui, SYLLUBUS_GRADED_COMP_TEXTAREA)) //syllabus graded components
                                      .add(JSON_GRADING_NOTE, ((TextArea)gui.getGUINode(SYLLUBUS_GRADING_NOTE_TEXTAREA)).getText().replace("\"", "")) //syllabus grading note
                                      .add(JSON_ACAD_DIS, ((TextArea)gui.getGUINode(SYLLUBUS_ACAD_DIS_TEXTAREA)).getText().replace("\"", "")) //syllabus academic dishonesty
                                      .add(JSON_SPECIAL_ASIST, ((TextArea)gui.getGUINode(SYLLUBUS_SPEC_ASSIST_TEXTAREA)).getText().replace("\"", "")) //syllabus special assistance
                                      .add(JSON_START_MONDAY_MONTH, StartDate[1]) //schedule page date picker starting month
                                      .add(JSON_START_MONDAY_DAY, StartDate[2]) //schedule page date picker starting day
                                      .add(JSON_END_FRIDAY_MONTH, endDate[1]) //schedule page date picker ending month
                                      .add(JSON_END_FRIDAY_DAY, endDate[2]) //schedule page date picker ending day
                                      .add(JSON_SCHEDULE, scheduleJsonArray) //schedule table data
                                      .add(JSON_LECTURES, lecturesJsonArray) //meeting time page lectures table data
                                      .add(JSON_RECITATIONS, recitationsJsonArray) //meeting time page recitations table data
                                      .add(JSON_LABS, labsJsonArray) //meeting time page labs table data
                                      .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
        try (JsonWriter jsonWriter = writerFactory.createWriter(sw)) {
            jsonWriter.writeObject(fullJsonData);
        }

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(fullJsonData);
	String prettyPrinted = sw.toString();
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.write(prettyPrinted);
        }
    }
    
    /**
     * this is helper method for saving the site page data
     * @param gui : gui for getting the node
     * @param nodeId : node id for gui
     * @param nameOfPage : value to be return for JsonObject
     * @return 
     */
    private String pageIsSelected(AppGUIModule gui, CourseSitePropertyType nodeId, String nameOfPage){
        CheckBox cb = ((CheckBox)gui.getGUINode(nodeId));
        return cb.isSelected() ? nameOfPage : "";
    }
    
    /**
     * This method is helper method for saving syllabus page data
     * will return the text in text area in the JsonArray form
     * @param gui : gui to get the node
     * @param nodeId : id for gui to get specific node
     * @return 
     */
    private JsonArray stringToJsonArray(AppGUIModule gui, CourseSitePropertyType nodeId){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        String[] blockOfString = ((TextArea)gui.getGUINode(nodeId)).getText().split("\n");
        
        for(String line : blockOfString){
            if(!line.contains("[") && !line.contains("]")){
                jsonArrayBuilder.add(line.replaceAll("\"", "")
                                         .replaceAll("\t", "")
                                         .replaceAll(",", ""));
            }
        }
        
        return ((JsonArray)jsonArrayBuilder.build());
    }
    
    private JsonArray StringToArrayOfDic(AppGUIModule gui, CourseSitePropertyType nodeId){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        String[] blockString = ((TextArea)gui.getGUINode(nodeId)).getText().split("\n");
        String keyOfArray = null; //this will hold the key of array inside the json array
        JsonArrayBuilder jsonSubArrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder jsonTempObject = Json.createObjectBuilder();
        boolean inArray = false; //this boolean keep track of weather we are looping in a sub array
        
        for(String line : blockString){
            if(!line.startsWith("[") && !line.startsWith("]")){ //line cant start with [ ]
                if(!line.contains("{") && !line.contains("}")){ //line cant contain {
                    
                    if(line.contains("[")){ //contain [ means it is the first line of array
                        keyOfArray = line.split(":")[0].replaceAll("\"", "").replaceAll("\t", "");
                        inArray = true;
                        continue;
                    }
                    
                    if(line.contains("]")){ //end of sub array
                        inArray = false; //set inArray false;
                        jsonTempObject.add(keyOfArray, ((JsonArray)jsonSubArrayBuilder.build()));
                        continue;
                    }
                    
                    if(inArray){ //when we inside the sub array
                        String[] temp = line.split(",");
                        for(String element : temp){ 
                            jsonSubArrayBuilder.add(element.replaceAll("\"", "")
                                               .replaceAll("\t", ""));
                        }
                    }
                    else{ //not inside the sub array
                        String[] temp = line.split(":", 2);
                        if(temp.length > 1)
                        jsonTempObject.add(temp[0].replaceAll("\"", "").replaceAll("\t", "")
                                , temp[1].replaceAll("\"", "").replaceAll("\t", ""));
                    }
                }
                else if(line.contains("}")){
                    jsonArrayBuilder.add(jsonTempObject.build());
                    jsonTempObject = Json.createObjectBuilder();
                }
            }
        }
        
        return ((JsonArray)jsonArrayBuilder.build());
    }
    
    private void loadTAs(OfficeHoursData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        JsonObject json;
        try (InputStream is = new FileInputStream(jsonFilePath); 
                JsonReader jsonReader = Json.createReader(is)) {
            json = jsonReader.readObject();
        }
	return json;
    }
    
    /**
     * THIS METHOD TRANSFERS JSON ARRAY TO STRINGS 
     * THEN PUT THE STRINGS IN TEXTAREA IN JSON FORMAT
     * @param arr : Json Array
     * @param csPropertyType : nodeId for text area
     * @param arrOfDic : boolean to show if the json arr is in dic format
     */
    private void loadJsonArr(JsonArray arr, CourseSitePropertyType csPropertyType, boolean arrOfdic) {
        AppGUIModule gui = app.getGUIModule();
        StringBuilder sb = new StringBuilder();
        boolean inBlockOfDic = false;
        
        sb.append("[\n");
        //if this json arr is a arr of dictionaries
        //then we need to break down the format more
        if (arrOfdic) {
            for (int i = 0; i < arr.size(); i++) { //looping json arr
                
                String[] list = arr.get(i).toString().split(",");
                sb.append("\t{\n");
                
                for(int j = 0; j < list.length; j++){ //looping arr that that is correctly formated json style            
                    if(list[j].contains("[")){ //if this element of arr is a block of data
                        inBlockOfDic = true;
                        String[] temp = list[j].split("\\[");
                        sb.append("\t\t")
                          .append(temp[0])
                          .append("\t[\n")
                          .append("\t\t\t")
                          .append(temp[1])
                          .append(",\n");
                    }
                    else if(list[j].contains("]")){
                        inBlockOfDic = false;
                        sb.append("\t\t\t")
                          .append(list[j].split("\\]")[0]);
                        if(i != list.length - 1){
                            sb.append("\n\t\t],\n");
                        }
                        else{
                            sb.append("\n\t\t]\n");
                        }
                    }
                    else if(inBlockOfDic){
                        sb.append("\t\t\t")
                          .append(list[j].replaceAll("\\{", ""))
                          .append(",\n");
                    }
                    else {
                        if (j != list.length - 1) {
                            if(!list[j].endsWith("\"")){
                                sb.append("\t\t")
                                  .append(list[j].replaceAll("\\{", ""))
                                  .append(list[j+1].replaceAll("\\{", ""))
                                  .append(",\n");
                                j++;
                            } 
                            else{
                                sb.append("\t\t")
                                  .append(list[j].replaceAll("\\{", ""))
                                  .append(",\n");
                            }
                        } else {
                            sb.append("\t\t")
                              .append(list[j].replaceAll("\\}", ""))
                              .append("\n");
                        }
                    } 
                }
                if(i == arr.size() -1){
                    sb.append("\t}\n");
                }
                else sb.append("\t},\n");
            }
        } 
        // if not a arr of dictionaries then do it simply
        else {
            for (int i = 0; i < arr.size(); i++) {
                if (i != arr.size() - 1) {
                    sb.append("\t")
                      .append(arr.get(i))
                      .append(",\n");
                } else {
                    sb.append("\t")
                      .append(arr.get(i))
                      .append("\n");
                }
            }
        }
        sb.append("]");

        ((TextArea)gui.getGUINode(csPropertyType)).setText(sb.toString());
    }
    
    //this method is only for loading the syllabus grading component part
    private void loadSyllabusGradeComp(JsonArray arr, CourseSitePropertyType csPropertyType){
        AppGUIModule gui = app.getGUIModule();
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");

        for (int i = 0; i < arr.size(); i++) { //looping json arr
            String[] temp = arr.get(i).toString().split(",");
            
            sb.append("\t{\n");
            
            for(int j = 0; j < temp.length; j++){
                if(j > 0 && j < temp.length - 1){
                    if(temp[j].contains(":")){
                        sb.append("\t\t")
                          .append(temp[j]);
                    }
                    else{
                        sb.append(",")
                          .append(temp[j]);
                    }
                }
                if(j == 0){
                    if(i != 0){
                        sb.append("\n");
                    }
                    sb.append("\t\t")
                      .append(temp[j].replaceAll("\\{", ""))
                      .append("\n");
                }
                if(j == temp.length - 1){
                    sb.append("\n\t\t")
                      .append(temp[j].replaceAll("\\}", ""));
                }
            }
            
            sb.append("\n\t}\n");
        }
        
        sb.append("]");

        ((TextArea)gui.getGUINode(csPropertyType)).setText(sb.toString());
    }
    
    /**
     * this method is for loading the site page icons
     * @param jsonLogos : json logo object for convinence
     * @param csPropertyType : nodeId for the correct hbox to render image
     * @param gui : gui to get the object with nodeId
     * @param typeOfImage : this tells what image we rendering to, can be fav, nav, lf image, rf image
     * corresponding to (1, 2, 3, 4)
     */
    private void loadSiteIcons(JsonObject jsonLogos, String fileNameId,
            CourseSitePropertyType csPropertyType, AppGUIModule gui, int typeOfImage){
        //get the image path set up
        String jsonFav = jsonLogos.getJsonObject(fileNameId).getJsonString(JSON_SRC).toString().replaceAll("\"", "");
        
        //lets see which part of image we rendering
        String typePath;
        switch (typeOfImage) {
            case 1:
                typePath = ((CourseSiteWorkspace)app.getWorkspaceComponent()).getFavPath();
                break;
            case 2:
                typePath = ((CourseSiteWorkspace)app.getWorkspaceComponent()).getNavPath();
                break;
            case 3:
                typePath = ((CourseSiteWorkspace)app.getWorkspaceComponent()).getLFpath();
                break;
            default:   
                typePath = ((CourseSiteWorkspace)app.getWorkspaceComponent()).getRFpath();
                break;
        }
        //get the relative path
        String pathString = (typePath + "\\" + jsonFav);
        Path imagePath = Paths.get(pathString);
        
        //maing the image view object
        Image icon = new Image(new File(imagePath.toAbsolutePath().toString()
                .replace("\\", "/")).toURI().toString());
        ImageView imageView = new ImageView(icon);
        
        //rendering the image view to gui
        ((HBox)gui.getGUINode(csPropertyType)).getChildren().set(1, imageView);
    }
    
    private void loadOfficeHoursPage(AppDataComponent data, JsonObject json, AppGUIModule gui) throws IOException{
        // CLEAR THE OLD DATA OUT
	OfficeHoursData dataManager = (OfficeHoursData)data;
        dataManager.reset();
        CourseSiteController controller = new CourseSiteController((CourseSiteGeneratorApp) app);
        CourseSiteWorkspace workspace = (CourseSiteWorkspace) app.getWorkspaceComponent();

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);
        
        // LOAD THE INSTRUCTOR INFORMATIONS
        loadJsonArr(json.getJsonObject(JSON_INSTRUCTOR).getJsonArray(JSON_HOURS), SITE_OFFICE_HOURS_TEXT_AREA, false);
        ((TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD))
                .setText(json.getJsonObject(JSON_INSTRUCTOR).getString(JSON_NAME));
        ((TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD))
                .setText(json.getJsonObject(JSON_INSTRUCTOR).getString(JSON_EMAIL));
        ((TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD))
                .setText(json.getJsonObject(JSON_INSTRUCTOR).getString(JSON_ROOM));
        ((TextField)gui.getGUINode(SITE_HOME_PAGE_TEXT_FIELD))
                .setText(json.getJsonObject(JSON_INSTRUCTOR).getString(JSON_LINK));

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
        }
        
        // LOAD THE START AND END TIME INTERVAL TO COMBOBOX
        ComboBox ohStartTimeCB = ((ComboBox)gui.getGUINode(OH_START_TIME_COMBO_BOX));
        ComboBox ohEndTimeCB = ((ComboBox)gui.getGUINode(OH_END_TIME_COMBO_BOX));
        ohStartTimeCB.getSelectionModel().select(json.getString(JSON_OH_TABLE_START_INTERVAL));
        ohEndTimeCB.getSelectionModel().select(json.getString(JSON_OH_TABLE_END_INTERVAL));
        // CHANGE THE TABLES PRESENTATION WHEN LOADED TIME INTERVALS
        String CBstartTime = (String) ohStartTimeCB.getSelectionModel().getSelectedItem();
        String CBendTime = (String) ohEndTimeCB.getSelectionModel().getSelectedItem();
        controller.processOHdisplay(CBstartTime, CBendTime);
        ohStartTimeCB.setItems(workspace.getStList(CBendTime, controller.getOhStartTime(), controller.getOhEndTime()));
        if (!workspace.fullInterval()) {
            controller.processTAdisplay();
        } else {
            controller.showFullTAandOH();
        }
    }
    
    private void loadSitePage(JsonObject json, AppGUIModule gui) throws IOException{
        // LOAD THE DATA FOR COMBO BOX
        ((ComboBox) gui.getGUINode(SITE_SBJ_COMBO_BOX)).getSelectionModel()
                .select(((String)json.getJsonString(JSON_SUBJECT).toString()).replaceAll("\"", ""));
        ((ComboBox) gui.getGUINode(SITE_NUMBER_COMBO_BOX)).getSelectionModel()
                .select(((String)json.getJsonString(JSON_NUMBER).toString()).replaceAll("\"", ""));
        ((ComboBox) gui.getGUINode(SITE_SEMESTER_COMBO_BOX)).getSelectionModel()
                .select(((String)json.getJsonString(JSON_SEMESTER).toString()).replaceAll("\"", ""));
        ((ComboBox) gui.getGUINode(SITE_YEAR_COMBO_BOX)).getSelectionModel()
                .select(json.getJsonString(JSON_YEAR).toString().replaceAll("\"", ""));
        ((TextField) gui.getGUINode(SITE_TITLE_TEXT_FIELD))
                .setText(json.getJsonString(JSON_TITLE).toString().replaceAll("\"", ""));
        
        // LOAD THE IMAGE DATA
        JsonObject jsonIcons = json.getJsonObject(JSON_LOGOS);
        loadSiteIcons(jsonIcons, JSON_FAB_ICON, SITE_FAV_HBOX, gui, 1);
        loadSiteIcons(jsonIcons, JSON_NAV_BAR, SITE_NAV_HBOX, gui, 2);
        loadSiteIcons(jsonIcons, JSON_BOTTOM_LEFT, SITE_LF_IMAGE_HBOX, gui, 3);
        loadSiteIcons(jsonIcons, JSON_BOTTOM_RIGHT, SITE_RF_IMAGE_HBOX, gui, 4);
        
        // LOAD THE CHECK BOX DATA
        JsonArray jsonSiteCB = json.getJsonArray(JSON_PAGES);
        if(!jsonSiteCB.getString(0, JSON_NAME).isEmpty()){
            ((CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX)).setSelected(true);
        }
        if(!jsonSiteCB.getString(1, JSON_NAME).isEmpty()){
            ((CheckBox)gui.getGUINode(SITE_SYLLUBUS_CHECK_BOX)).setSelected(true);
        }
        if(!jsonSiteCB.getString(2, JSON_NAME).isEmpty()){
            ((CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX)).setSelected(true);
        }
        if(!jsonSiteCB.getString(3, JSON_NAME).isEmpty()){
            ((CheckBox)gui.getGUINode(SITE_HWS_CHECK_BOX)).setSelected(true);
        }
        
        // LOAD CSS COMBO BOX DATA
        ((ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX)).getSelectionModel().select(json.getString(JSON_CSS));
        
    }
    
    private void loadSyllubusPage(JsonObject json, AppGUIModule gui) throws IOException{
        //Load Description data
        ((TextArea)gui.getGUINode(SYLLUBUS_DES_TEXTAREA)).setText(json.getJsonString(JSON_DESCRIPTION).toString());
        
        //Load topics data
        loadJsonArr(json.getJsonArray(JSON_TOPICS), SYLLUBUS_TOPIC_TEXTAREA, false);
        
        //Load prerequisites data
        ((TextArea)gui.getGUINode(SYLLUBUS_PREQ_TEXTAREA)).setText(json.getJsonString(JSON_PREQ).toString());
        
        //Load outcomes data
        loadJsonArr(json.getJsonArray(JSON_OUTCOMES), SYLLUBUS_OUTCOME_TEXTAREA, false);
        
        //Load textbooks data
        loadJsonArr(json.getJsonArray(JSON_TEXTBOOKS), SYLLUBUS_TEXTBOOK_TEXTAREA, true);
        
        //Load graded components data
        loadSyllabusGradeComp(json.getJsonArray(JSON_GRADED_COMP), SYLLUBUS_GRADED_COMP_TEXTAREA);
        
        //Load grading note data
        ((TextArea)gui.getGUINode(SYLLUBUS_GRADING_NOTE_TEXTAREA)).setText(json.getJsonString(JSON_GRADING_NOTE).toString());
        
        //Load academic dsihonesty data
        ((TextArea)gui.getGUINode(SYLLUBUS_ACAD_DIS_TEXTAREA)).setText(json.getJsonString(JSON_ACAD_DIS).toString());
        
        //Load special asistance data
        ((TextArea)gui.getGUINode(SYLLUBUS_SPEC_ASSIST_TEXTAREA)).setText(json.getJsonString(JSON_SPECIAL_ASIST).toString());
    }
    
    // METHOD TO CREATE A DATE OBBJECT FOR DATE PICKER
    public static final LocalDate loadedDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }
    
    private void loadSchedulePage(JsonObject json, AppGUIModule gui) throws IOException{
        OfficeHoursData data = (OfficeHoursData) app.getDataComponent();
        
        //Loading the data picker time
        String startingMonday = json.getJsonString(JSON_START_MONDAY_MONTH).toString().replaceAll("\"", "") + "-" 
                + json.getJsonString(JSON_START_MONDAY_DAY).toString().replaceAll("\"", "") + "-" 
                + Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        String endingFriday = json.getJsonString(JSON_END_FRIDAY_MONTH).toString().replaceAll("\"", "") + "-" 
                + json.getJsonString(JSON_END_FRIDAY_DAY).toString().replaceAll("\"", "") + "-" 
                + Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        
        ((DatePicker)gui.getGUINode(SD_START_MON_DATE_PICKER)).setValue(loadedDate(startingMonday));
        ((DatePicker)gui.getGUINode(SD_END_FRI_DATE_PICKER)).setValue(loadedDate(endingFriday));
        
        //Load the data for holiday array in schedule table
        JsonArray jsonScheduleArray = json.getJsonArray(JSON_SCHEDULE);
        for (int i = 0; i < jsonScheduleArray.size(); i++) {
            JsonObject jsonSchedule = jsonScheduleArray.getJsonObject(i);
            String type = jsonSchedule.getString(JSON_TYPE);
            String date = jsonSchedule.getString(JSON_DATE);
            String title = jsonSchedule.getString(JSON_TITLE);
            String topic = jsonSchedule.getString(JSON_TOPIC);
            String link = jsonSchedule.getString(JSON_LINK);
            Schedule schedule = new Schedule(type, date, title, topic, link);
            data.addSchedule(schedule);
        }
        
    }
    
    private void loadMeetingTimePage(JsonObject json, AppGUIModule gui) throws IOException{
        OfficeHoursData data = (OfficeHoursData) app.getDataComponent();
        
        //Load the data for lecture table
        JsonArray jsonLecturesArray = json.getJsonArray(JSON_LECTURES);
        for (int i = 0; i < jsonLecturesArray.size(); i++) {
            JsonObject jsonLecture = jsonLecturesArray.getJsonObject(i);
            String section = jsonLecture.getString(JSON_SECTION);
            String days = jsonLecture.getString(JSON_DAYS);
            String time = jsonLecture.getString(JSON_TIME);
            String room = jsonLecture.getString(JSON_ROOM);
            Lectures lecture = new Lectures(section, days, time, room);
            data.addLecture(lecture);
        } 
        
        //Load the data for recitation table
        JsonArray jsonRecitationArray = json.getJsonArray(JSON_RECITATIONS);
        for (int i = 0; i < jsonRecitationArray.size(); i++) {
            JsonObject jsonRecitation = jsonRecitationArray.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION);
            String daysTime = jsonRecitation.getString(JSON_DAY_TIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String ta_1 = jsonRecitation.getString(JSON_TA1);
            String ta_2 = jsonRecitation.getString(JSON_TA2);
            Recitations recitation = new Recitations(section, daysTime, location, ta_1, ta_2);
            data.addRecitation(recitation);
        }
        
        //Load the data for lab table
        JsonArray jsonLabArray = json.getJsonArray(JSON_LABS);
        for (int i = 0; i < jsonLabArray.size(); i++) {
            JsonObject jsonLab = jsonLabArray.getJsonObject(i);
            String section = jsonLab.getString(JSON_SECTION);
            String daysTime = jsonLab.getString(JSON_DAY_TIME);
            String location = jsonLab.getString(JSON_LOCATION);
            String ta_1 = jsonLab.getString(JSON_TA1);
            String ta_2 = jsonLab.getString(JSON_TA2);
            Labs lab = new Labs(section, daysTime, location, ta_1, ta_2);
            data.addLab(lab);
        } 
    }
    
    public void loadComboBoxData() throws IOException{
        //turn the combo box data file to relative path first
        Path path = Paths.get(CB_DATA_FILE_PATH);
        
        JsonObject json = loadJSONFile(path.toString().replace("\\", "/"));
        CourseSiteController controller = new CourseSiteController((CourseSiteGeneratorApp) app);
        
        // GET ALL JSON ARRAYS
        JsonArray siteSemesterArray = json.getJsonArray(JSON_SITE_SEMESTER);
        JsonArray siteSubjectArray = json.getJsonArray(JSON_SITE_SUBJECT);
        JsonArray siteSubjectNumArray = json.getJsonArray(JSON_SUBJECT_NUM);
        JsonArray siteCssArray = json.getJsonArray(JSON_SITE_CSS);
        
        // ADDING DATA TO SITE SEMESTER COMBO BOX
        for (int i = 0; i < siteSemesterArray.size(); i++) {
            controller.getSiteSemester();
            ((ObservableList<String>) controller.getSiteSemester()).add(siteSemesterArray.getString(i));
        }
        
        // ADDING DATA TO SITE SUBJECT COMBO BOX
        for (int i = 0; i < siteSubjectArray.size(); i++) {
            controller.getSiteSubject().add(siteSubjectArray.getString(i));
        }
        
        // ADDING DATA TO SUBJECT NUMBER COMBO BOX
        for (int i = 0; i < siteSubjectNumArray.size(); i++) {
            controller.getSubjectNum().add(siteSubjectNumArray.getString(i));
        }
        
        // ADDING DATA TO SITE CSS COMBO BOX
        for (int i = 0; i < siteCssArray.size(); i++) {
            controller.getSiteCss().add(siteCssArray.getString(i));
        }
    }
    
    //method to add a new value to specific observable list
    public void addToObvList(String typeOfList, String valueToAdd, CourseSiteController controller) throws IOException{
        //turn the combo box data file to relative path first
        Path path = Paths.get(CB_DATA_FILE_PATH);
        
        JsonObject json = loadJSONFile(path.toString().replace("\\", "/"));
        JsonObjectBuilder fullJsonData = Json.createObjectBuilder();
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        switch(typeOfList){
            case "semester" :
                //add to the comboBox first
                controller.getSiteSemester().add(valueToAdd);
                
                //update the json file
                JsonArray arraySeme = json.getJsonArray(JSON_SITE_SEMESTER);
                
                for(int i = 0; i < arraySeme.size(); i++){
                    builder.add(arraySeme.get(i));
                }

                fullJsonData.add(JSON_SITE_SEMESTER, builder.add(valueToAdd).build());
                
                //fill out rest of the unchanged data
                fillRestCBfile(typeOfList, fullJsonData, json);
                break;
            
            case "subject" : 
                //add to the comboBox first
                controller.getSiteSubject().add(valueToAdd);
                
                //update the json file
                JsonArray arraySubject = json.getJsonArray(JSON_SITE_SUBJECT);
                
                for(int i = 0; i < arraySubject.size(); i++){
                    builder.add(arraySubject.get(i));
                }
                
                fullJsonData.add(JSON_SITE_SUBJECT, builder.add(valueToAdd).build());
                
                //fill out rest of the unchanged data
                fillRestCBfile(typeOfList, fullJsonData, json);
                break;
            
            case "number" : 
                //add to the comboBox first
                controller.getSubjectNum().add(valueToAdd);
                
                //update the json file
                JsonArray arraySubjectNum = json.getJsonArray(JSON_SUBJECT_NUM);
                
                for(int i = 0; i < arraySubjectNum.size(); i++){
                    builder.add(arraySubjectNum.get(i));
                }
                
                fullJsonData.add(JSON_SUBJECT_NUM, builder.add(valueToAdd).build());
                
                //fill out rest of the unchanged data
                fillRestCBfile(typeOfList, fullJsonData, json);
                break;
                
            default: break;
        }
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
        try (JsonWriter jsonWriter = writerFactory.createWriter(sw)) {
            jsonWriter.writeObject(fullJsonData.build());
        }

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(path.toString().replace("\\", "/"));
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(fullJsonData.build());
	String prettyPrinted = sw.toString();
        try (PrintWriter pw = new PrintWriter(path.toString().replace("\\", "/"))) {
            pw.write(prettyPrinted);
        }
    }
    
    /**
     * this method helps fill out the rest of the unchanged json file in combo box data json file
     * @param typeOfCBtoSkip : the type of combo box data that we skipping, b/c it is changing
     * @param fullJsonObject : the json object builder
     * @param json : json object file
     */
    private void fillRestCBfile(String typeOfCBtoSkip, 
            JsonObjectBuilder fullJsonObject, JsonObject json){
     
        switch(typeOfCBtoSkip){
            case "semester" :
                fullJsonObject.add(JSON_SITE_SUBJECT, json.getJsonArray(JSON_SITE_SUBJECT));
                fullJsonObject.add(JSON_SUBJECT_NUM, json.getJsonArray(JSON_SUBJECT_NUM));
                fullJsonObject.add(JSON_SITE_CSS, json.getJsonArray(JSON_SITE_CSS));
                break;
            
            case "subject" : 
                fullJsonObject.add(JSON_SITE_SEMESTER, json.getJsonArray(JSON_SITE_SEMESTER));
                fullJsonObject.add(JSON_SUBJECT_NUM, json.getJsonArray(JSON_SUBJECT_NUM));
                fullJsonObject.add(JSON_SITE_CSS, json.getJsonArray(JSON_SITE_CSS));
                break;
            
            case "number" : 
                fullJsonObject.add(JSON_SITE_SUBJECT, json.getJsonArray(JSON_SITE_SUBJECT));
                fullJsonObject.add(JSON_SITE_SEMESTER, json.getJsonArray(JSON_SITE_SEMESTER));
                fullJsonObject.add(JSON_SITE_CSS, json.getJsonArray(JSON_SITE_CSS));
                break;
                
            default: break;
        }
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
    }
}