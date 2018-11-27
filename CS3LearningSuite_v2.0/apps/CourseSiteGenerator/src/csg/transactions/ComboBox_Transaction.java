/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 * @author Jiang He
 */
public class ComboBox_Transaction implements jTPS_Transaction{

    ComboBox cb;
    String oldValue;
    String newValue;
    
    public ComboBox_Transaction(ComboBox cb, String oldValue, String newValue) {
        this.cb = cb;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        cb.getSelectionModel().select(newValue);
    }

    @Override
    public void undoTransaction() {
        cb.getSelectionModel().select(oldValue);
    }
    
}
