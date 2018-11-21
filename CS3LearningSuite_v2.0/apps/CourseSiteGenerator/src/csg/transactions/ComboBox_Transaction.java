/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CourseSiteGeneratorApp;
import csg.workspace.CourseSiteWorkspace;
import csg.workspace.controllers.CourseSiteController;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author turtle714804947
 */
public class ComboBox_Transaction implements jTPS_Transaction{
    ComboBox cb;
    String oldValue;
    String newValue;
    CourseSiteController controller;
    
    public ComboBox_Transaction(ComboBox cb, String oldValue, String newValue, CourseSiteController controller) {
        this.cb = cb;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.controller = controller;
    }

    @Override
    public void doTransaction() {
        cb.getSelectionModel().select(newValue);
        controller.updateExportDir("semester", oldValue, newValue);
    }

    @Override
    public void undoTransaction() {
        cb.getSelectionModel().select(oldValue);
        controller.updateExportDir("semester", oldValue, newValue);
    }
    
}
