
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jiang He
 */
public class Lectures {
    
    // THE TABLE WILL STORE ALL FOLLOWING DATA
    private final StringProperty section;
    private final StringProperty day;
    private final StringProperty time;
    private final StringProperty room;
    
    public Lectures(String section, String day, String time, String room){
        this.section = new SimpleStringProperty(section);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);
        this.room = new SimpleStringProperty(room);
    }

    public String getSection() {
        return section.get();
    }

    public String getDay() {
        return day.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getRoom() {
        return room.get();
    }
    
    public void setSection(String section) {
        this.section.set(section);
    }
    
    public void setDay(String day) {
        this.day.set(day);
    }
    
    public void setTime(String time) {
        this.time.set(time);
    }
    
    public void setRoom(String room) {
        this.room.set(room);
    }
}
