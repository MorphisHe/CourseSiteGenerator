
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
    LocalDate oldValue;
    LocalDate newValue;
    
    public DatePicker_Transaction(DatePicker datePicker, LocalDate oldValue, LocalDate newValue){
        this.datePicker = datePicker;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        datePicker.setValue(newValue);
    }

    @Override
    public void undoTransaction() {
        datePicker.setValue(oldValue);
    }
    
}
