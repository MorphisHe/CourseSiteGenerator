
package csg.transactions;

import csg.workspace.controllers.CourseSiteController;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jiang He
 */
public class DatePicker_Transaction implements jTPS_Transaction{
    
    DatePicker datePicker;
    DatePicker editDatePicker; // we need to change this date picker's value if start date picker is on action
    String typeOfDP;
    LocalDate oldValue;
    LocalDate newValue;
    LocalDate otherDate;
    LocalDate dateHolder = null;
    CourseSiteController controller;
    
    public DatePicker_Transaction(DatePicker datePicker, DatePicker editDatePicker,
                                  String typeOfDP,LocalDate oldValue, LocalDate newValue,
                                   LocalDate otherDate, CourseSiteController controller){
        this.datePicker = datePicker;
        this.editDatePicker = editDatePicker;
        this.typeOfDP = typeOfDP;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.otherDate = otherDate;
        this.controller = controller;
    }
    
    @Override
    public void doTransaction() {
        if(typeOfDP.equals("start")){
            datePicker.setValue(newValue);
            dateHolder = editDatePicker.getValue();
            editDatePicker.setValue(newValue);
            controller.processScheduledisplay(newValue, otherDate);
        }
        else{
            datePicker.setValue(newValue);
            controller.processScheduledisplay(otherDate, newValue);
        }
    }

    @Override
    public void undoTransaction() {
        if(typeOfDP.equals("start")){
            datePicker.setValue(oldValue);
            editDatePicker.setValue(dateHolder);
            controller.processScheduledisplay(oldValue, otherDate);
        }
        else {
            datePicker.setValue(oldValue);
            controller.processScheduledisplay(otherDate, oldValue);
        }
    }
    
}
