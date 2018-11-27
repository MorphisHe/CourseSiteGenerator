
package csg.transactions;

import javafx.scene.control.TextArea;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jiang He
 */
public class TextArea_Transaction implements jTPS_Transaction{

    TextArea textArea;
    String oldValue;
    String newValue;
    
    public TextArea_Transaction(TextArea textArea, String oldValue, String newValue){
        this.textArea = textArea;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        textArea.setText(newValue);
    }

    @Override
    public void undoTransaction() {
        textArea.setText(oldValue);
    }
    
}
