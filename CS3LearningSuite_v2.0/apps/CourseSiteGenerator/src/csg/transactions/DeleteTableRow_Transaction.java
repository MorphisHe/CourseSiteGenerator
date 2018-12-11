
package csg.transactions;

import csg.data.Labs;
import csg.data.Lectures;
import csg.data.Recitations;
import csg.data.Schedule;
import csg.workspace.controllers.CourseSiteController;
import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 * @author Jiang 
 */
public class DeleteTableRow_Transaction implements jTPS_Transaction{

    Object data;
    TableView table;
    String typeOfData;
    int index;
    AppGUIModule gui;
    CourseSiteController controller;
    
    /**
     * this class focus on redo and undo for deleting a table row
     * @param data : the data that we deleting
     * @param table : the table that we are working on 
     * @param typeOfData : the type of data we deleting (since we have many different type of tables)
     * @param index : the index of the row we deleting (so we can add back the row to its original row index when redo)
     * @param gui : gui to pass to clear editing section method
     * @param controller : to call clear editing section method
     */
    public DeleteTableRow_Transaction(Object data, TableView table, String typeOfData, int index, 
                                      AppGUIModule gui, CourseSiteController controller) {
        this.data = data;
        this.table = table;
        this.typeOfData = typeOfData;
        this.index = index;
        this.gui = gui;
        this.controller = controller;
    }
    
    @Override
    public void doTransaction() {
        table.getItems().remove(data);
    }

    @Override
    public void undoTransaction() {
        switch(typeOfData){
            case "lectures" :
                data = (Lectures)data;
                break;
            case "recitation" :
                data = (Recitations)data;
                break;
            case "labs" :
                data = (Labs)data;
                break;
            case "schedule" :
                data = (Schedule)data;
                break;
        }
        
        table.getItems().add(index, data);
        if(typeOfData.equals("schedule")) controller.clearScheduleEdit(gui);
    }
    
}
