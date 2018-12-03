
package csg.transactions;

import csg.data.Labs;
import csg.data.Lectures;
import csg.data.Recitations;
import javafx.scene.control.TableView;

/**
 * @author Jiang He
 */
public class EditTableCell_Transaction implements jtps.jTPS_Transaction{

    Object editingCell;
    String newData;
    String oldData = null; //this will be used to store old data through the doProcess method.
    String typeOfTable;
    String typeOfMethod;
    TableView table;
    
    /**
     * this class takes care of table cell editing undo and redo
     * @param editingCell : the cell we editing
     * @param newData : the new data we trying to process
     * @param typeOfTable : type of the table we working on
     * @param typeOfMethod : type of the method we trying to call to edit the new data
     * @param table : the table we working on, used to refresh to show the data representation
     */
    public EditTableCell_Transaction(Object editingCell, String newData, TableView table,
                                     String typeOfTable, String typeOfMethod){
        this.editingCell = editingCell;
        this.table = table;
        this.newData = newData;
        this.typeOfTable = typeOfTable;
        this.typeOfMethod = typeOfMethod;
    }
    
    @Override
    public void doTransaction() {
        if(typeOfTable.equals("lectures")){
            //type cast to cell's actual type
            Lectures currentCell = ((Lectures)editingCell);
            switch (typeOfMethod) {
                case "section":
                    oldData = currentCell.getSection();
                    currentCell.setSection(newData);
                    break;
                case "days":
                    oldData = currentCell.getDay();
                    currentCell.setDay(newData);
                    break;
                case "time":
                    oldData = currentCell.getTime();
                    currentCell.setTime(newData);
                    break;
                case "room":
                    oldData = currentCell.getRoom();
                    currentCell.setRoom(newData);
                    break;
            }
        }
        else {
            //type cast to cell's actual type
            if(typeOfTable.equals("labs")){
                Labs currentCell = ((Labs)editingCell);
                switch (typeOfMethod) {
                    case "section":
                        oldData = currentCell.getSection();
                        currentCell.setSection(newData);
                        break;
                    case "daysTime":
                        oldData = currentCell.getDaysTime();
                        currentCell.setDaysTime(newData);
                        break;
                    case "TA1":
                        oldData = currentCell.getTA1();
                        currentCell.setTA1(newData);
                        break;
                    case "TA2":
                        oldData = currentCell.getTA2();
                        currentCell.setTA2(newData);
                        break;
                    case "room":
                        oldData = currentCell.getRoom();
                        currentCell.setRoom(newData);
                        break;
                }
            }
            //else type will be Recitations
            else{
                Recitations currentCell = ((Recitations)editingCell);
                switch (typeOfMethod) {
                    case "section":
                        oldData = currentCell.getSection();
                        currentCell.setSection(newData);
                        break;
                    case "daysTime":
                        oldData = currentCell.getDaysTime();
                        currentCell.setDaysTime(newData);
                        break;
                    case "TA1":
                        oldData = currentCell.getTA1();
                        currentCell.setTA1(newData);
                        break;
                    case "TA2":
                        oldData = currentCell.getTA2();
                        currentCell.setTA2(newData);
                        break;
                    case "room":
                        oldData = currentCell.getRoom();
                        currentCell.setRoom(newData);
                        break;
                }
            }
        }
        table.refresh();
    }

    @Override
    public void undoTransaction() {
        if(typeOfTable.equals("lectures")){
            //type cast to cell's actual type
            Lectures currentCell = ((Lectures)editingCell);
            switch (typeOfMethod) {
                case "section":
                    currentCell.setSection(oldData);
                    break;
                case "days":
                    currentCell.setDay(oldData);
                    break;
                case "time":
                    currentCell.setTime(oldData);
                    break;
                case "room":
                    currentCell.setRoom(oldData);
                    break;
            }
        }
        else {
            //type cast to cell's actual type
            if(typeOfTable.equals("labs")){
                Labs currentCell = ((Labs)editingCell);
                switch (typeOfMethod) {
                    case "section":
                        currentCell.setSection(oldData);
                        break;
                    case "daysTime":
                        currentCell.setDaysTime(oldData);
                        break;
                    case "TA1":
                        currentCell.setTA1(oldData);
                        break;
                    case "TA2":
                        currentCell.setTA2(oldData);
                        break;
                    case "room":
                        currentCell.setRoom(oldData);
                        break;
                }
            }
            //else type will be Recitations
            else{
                Recitations currentCell = ((Recitations)editingCell);
                switch (typeOfMethod) {
                    case "section":
                        currentCell.setSection(oldData);
                        break;
                    case "daysTime":
                        currentCell.setDaysTime(oldData);
                        break;
                    case "TA1":
                        currentCell.setTA1(oldData);
                        break;
                    case "TA2":
                        currentCell.setTA2(oldData);
                        break;
                    case "room":
                        currentCell.setRoom(oldData);
                        break;
                }
            }
        }
        table.refresh();
    }
    
}
