
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Jiang He
 */
public class Schedule {
    
    // THE TABLE WILL STORE ALL FOLLOWING DATA
    private final StringProperty type;
    private final StringProperty date;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty link;
    
    public Schedule(String type, String date, String title,
                       String topic, String link){
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.topic = new SimpleStringProperty(topic);
        this.link = new SimpleStringProperty(link);
    }

    public String getLink(){
        return link.get();
    }
    
    public String getType() {
        return type.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTopic() {
        return topic.get();
    }

    public String getTitle() {
        return title.get();
    }
    
    public void setLink(String link){
        this.link.set(link);
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
    
    public void setDate(String date) {
        this.date.set(date);
    }
    
    public void setTopic(String topic) {
        this.topic.set(topic);
    }
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public Schedule clone() {
        return new Schedule(this.getType(), this.getDate(), this.getTitle(),
                                         this.getTopic(), this.getLink());
    }
}
