
package csg.data;

import static csg.files.CourseSiteFiles.loadedDate;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Jiang He
 * @param <E>
 */
public class Schedule <E extends Comparable<E>> implements Comparable<E> {
    
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
    
    public LocalDate getLocalDate(){
        return loadedDate(this.date.get().replace("/", "-"));
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
    
    @Override
    public int compareTo(E otherSchedule) {       
        //split them into arrays of formate  "mm", "dd", "yyyy"
        String[] firstDate = getDate().split("/");
        String[] secondDate = ((Schedule)otherSchedule).getDate().split("/");
        
        if(!firstDate[2].equals(secondDate[2])) {
            //if the year is different, compare by year
            if(Integer.parseInt(firstDate[2]) > Integer.parseInt(secondDate[2])) return 1;
            else return -1;
        }
        else if(!firstDate[0].equals(secondDate[0])){
            //if the month is different, compare by month
            if(Integer.parseInt(firstDate[0]) > Integer.parseInt(secondDate[0])) return 1;
            else return -1;
        }
        else if(!firstDate[1].equals(secondDate[1])) {
            //if the day is  different, compare by day
            if(Integer.parseInt(firstDate[1]) > Integer.parseInt(secondDate[1])) return 1;
            else return -1;
        }
        else return 0; //else they are the same
    }
}
