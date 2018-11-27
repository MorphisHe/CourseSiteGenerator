
package csg.transactions;

import javafx.scene.control.CheckBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jiang He
 */
public class CheckBox_Transaction implements jTPS_Transaction{

    CheckBox checkBox;
    boolean currentState;
    
    public CheckBox_Transaction(CheckBox checkBox, boolean currentState) {
        this.checkBox = checkBox;
        this.currentState = currentState;
    }
    
    @Override
    public void doTransaction() {
        checkBox.setSelected(currentState);
    }

    @Override
    public void undoTransaction() {
        checkBox.setSelected(!currentState);
    }
    
}
