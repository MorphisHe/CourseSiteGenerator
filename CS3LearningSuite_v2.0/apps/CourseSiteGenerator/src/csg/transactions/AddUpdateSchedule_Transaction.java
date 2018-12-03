
package csg.transactions;

import csg.data.Schedule;
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
    
    /**
     * @param schedule : the schedule object that we adding or updating
     * @param table : the table we are working on
     * @param typeOfAction : tells us if we are adding or updating
     * @param type : data of type for schedule
     * @param date : data of date for schedule
     * @param title : data of title for schedule
     * @param topic : data of topic for schedule
     * @param link : data of link for schedule
     */
    public AddUpdateSchedule_Transaction(
            Schedule schedule,
            TableView table,
            String typeOfAction,
            String type, String date, 
            String title, String topic, String link) {
        this.schedule = schedule;
        this.table = table;
        this.typeOfAction = typeOfAction;
        this.type = type;
        this.date = date;
        this.title = title;
        this.topic = topic;
        this.link = link;
    }
    
    @Override
    public void doTransaction() {
        if(typeOfAction.equals("Add")){
            table.getItems().add(schedule);
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
    }

    @Override
    public void undoTransaction() {
        if(typeOfAction.equals("Add")){
            table.getItems().remove(schedule);
        }
        else{
            schedule.setDate(oldScheduleData.getDate());
            schedule.setLink(oldScheduleData.getLink());
            schedule.setTitle(oldScheduleData.getTitle());
            schedule.setTopic(oldScheduleData.getTopic());
            schedule.setType(oldScheduleData.getType());
            table.refresh();
        }
    }
    
}
