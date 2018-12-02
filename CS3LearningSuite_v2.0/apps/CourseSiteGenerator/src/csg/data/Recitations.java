
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a row of Recitation for the table of Recitation
 * @author Jiang He
 */
public class Recitations {
    
    // THE TABLE WILL STORE ALL FOLLOWING DATA
    private final StringProperty section;
    private final StringProperty daysTime;
    private final StringProperty room;
    private final StringProperty TA1;
    private final StringProperty TA2;
    
    public Recitations(String section, String daysTime, String room,
                       String TA1, String TA2){
        this.section = new SimpleStringProperty(section);
        this.daysTime = new SimpleStringProperty(daysTime);
        this.room = new SimpleStringProperty(room);
        this.TA1 = new SimpleStringProperty(TA1);
        this.TA2 = new SimpleStringProperty(TA2);
    }

    public String getSection() {
        return section.get();
    }

    public String getDaysTime() {
        return daysTime.get();
    }

    public String getTA1() {
        return TA1.get();
    }
    
    public String getTA2() {
        return TA2.get();
    }

    public String getRoom() {
        return room.get();
    }
    
    public void setSection(String section) {
        this.section.set(section);
    }
    
    public void setDaysTime(String daysTime) {
        this.daysTime.set(daysTime);
    }
    
    public void setTA1(String TA1) {
        this.TA1.set(TA1);
    }
    
    public void setTA2(String TA2) {
        this.TA2.set(TA2);
    }
    
    public void setRoom(String room) {
        this.room.set(room);
    }
}
