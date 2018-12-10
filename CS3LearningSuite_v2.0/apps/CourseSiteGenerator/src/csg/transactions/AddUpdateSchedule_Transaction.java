
package csg.transactions;

import csg.data.OfficeHoursData;
import csg.data.Schedule;
import csg.workspace.controllers.CourseSiteController;
import java.time.LocalDate;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 * @author Jiang He
 */
public class AddUpdateSchedule_Transaction implements jTPS_Transaction{

    Schedule schedule;
    Schedule oldScheduleData = null;
    TableView table;
    String typeOfAction;
    String type, date, title, topic, link;
    OfficeHoursData dataModule;
    LocalDate startDate, endDate;
    CourseSiteController controller;
    
    /**
     * @param schedule : the schedule object that we adding or updating
     * @param table : the table we are working on
     * @param typeOfAction : tells us if we are adding or updating
     * @param type : data of type for schedule
     * @param date : data of date for schedule
     * @param title : data of title for schedule
     * @param topic : data of topic for schedule
     * @param link : data of link for schedule
     * @param dataModule : use for accessing the sort schedule table method
     * @param startDate : startDate for processing schedule table display
     * @param endDate : endDate for processing schedule table display
     * @param controller : to call process schedule display method
     */
    public AddUpdateSchedule_Transaction(
            Schedule schedule,
            TableView table,
            String typeOfAction,
            String type, String date, 
            String title, String topic, String link,
            OfficeHoursData dataModule,
            LocalDate startDate, LocalDate endDate,
            CourseSiteController controller) {
        this.schedule = schedule;
        this.table = table;
        this.typeOfAction = typeOfAction;
        this.type = type;
        this.date = date;
        this.title = title;
        this.topic = topic;
        this.link = link;
        this.dataModule = dataModule;
        this.startDate = startDate;
        this.endDate = endDate;
        this.controller = controller;
    }
    
    @Override
    public void doTransaction() {
        if(typeOfAction.equals("Add")){
            dataModule.addSchedule(schedule);
        }
        else{
            oldScheduleData = schedule.clone();
            schedule.setDate(date);
            schedule.setLink(link);
            schedule.setType(type);
            schedule.setTitle(title);
            schedule.setTopic(topic);
            table.refresh();
        }
        dataModule.sortScheduleTable();
        controller.processScheduledisplay(startDate, endDate);
    }

    @Override
    public void undoTransaction() {
        if(typeOfAction.equals("Add")){
            dataModule.removeSchedule(schedule);
        }
        else{
            schedule.setDate(oldScheduleData.getDate());
            schedule.setLink(oldScheduleData.getLink());
            schedule.setTitle(oldScheduleData.getTitle());
            schedule.setTopic(oldScheduleData.getTopic());
            schedule.setType(oldScheduleData.getType());
            table.refresh();
        }
        dataModule.sortScheduleTable();
        controller.processScheduledisplay(startDate, endDate);
    }
    
}
