
package csg.transactions;

import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 * @author Jiang He
 */
public class TextField_Transaction implements jTPS_Transaction{

    TextField tf;
    String oldValue;
    String newValue;
    
    public TextField_Transaction(TextField tf, String oldValue, String newValue){
        this.tf = tf;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        tf.setText(newValue);
    }

    @Override
    public void undoTransaction() {
        tf.setText(oldValue);
    }
    
}
