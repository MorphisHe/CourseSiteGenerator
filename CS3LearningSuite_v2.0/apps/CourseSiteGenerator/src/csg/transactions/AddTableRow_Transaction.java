
package csg.transactions;

import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 * @author Jiang He
 */
public class AddTableRow_Transaction implements jTPS_Transaction{

    Object data;
    TableView table;
    
    public AddTableRow_Transaction(Object data, TableView table){
        this.data = data;
        this.table = table;
    }
    
    @Override
    public void doTransaction() {
        table.getItems().add(data);
    }

    @Override
    public void undoTransaction() {
        table.getItems().remove(data);
    }
    
    
    
}
