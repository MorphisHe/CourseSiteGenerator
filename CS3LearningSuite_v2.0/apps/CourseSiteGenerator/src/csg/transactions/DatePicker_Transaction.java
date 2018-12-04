
package csg.transactions;

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
    LocalDate dateHolder = null;
    
    public DatePicker_Transaction(DatePicker datePicker, DatePicker editDatePicker,
                                  String typeOfDP,LocalDate oldValue, LocalDate newValue){
        this.datePicker = datePicker;
        this.editDatePicker = editDatePicker;
        this.typeOfDP = typeOfDP;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        if(typeOfDP.equals("start")){
            datePicker.setValue(newValue);
            dateHolder = editDatePicker.getValue();
            editDatePicker.setValue(newValue);
        }
        else datePicker.setValue(newValue);
    }

    @Override
    public void undoTransaction() {
        if(typeOfDP.equals("start")){
            datePicker.setValue(oldValue);
            editDatePicker.setValue(dateHolder);
        }
        else datePicker.setValue(oldValue);
    }
    
}
