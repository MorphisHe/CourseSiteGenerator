package csg.transactions;

import csg.CourseSiteGeneratorApp;
import csg.workspace.CourseSiteWorkspace;
import csg.workspace.controllers.CourseSiteController;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 * @author Jiang He
 */
public class TimeInterval_Transaction implements jTPS_Transaction{
    ComboBox ohStartTimeCB;
    ComboBox ohEndTimeCB;
    String CBstartTime;
    String CBendTime;
    String oldValue;
    String typeOfCB;
    CourseSiteGeneratorApp app;
    CourseSiteController controller;
    CourseSiteWorkspace workspace;
    
    public TimeInterval_Transaction(
            CourseSiteGeneratorApp app,
            ComboBox cb, ComboBox cb2, 
            String CBstartTime, String CBendTime, 
            String oldValue, String typeOfCB) {
        this.app = app;
        this.ohStartTimeCB = cb;
        this.ohEndTimeCB = cb2;
        this.oldValue = oldValue;
        this.typeOfCB = typeOfCB;
        this.CBstartTime = CBstartTime;
        this.CBendTime = CBendTime;
        controller = new CourseSiteController((CourseSiteGeneratorApp) app);
        workspace = (CourseSiteWorkspace) app.getWorkspaceComponent();
    }

    @Override
    public void doTransaction() {
        if (typeOfCB.equals("start")) {
            ohStartTimeCB.getSelectionModel().select(CBstartTime);
            ohEndTimeCB.setItems(workspace.getEtList(CBstartTime, controller.getOhEndTime()));
            controller.processOHdisplay(CBstartTime, CBendTime);
            if (!workspace.fullInterval())  controller.processTAdisplay();
            else    controller.showFullTAandOH();
        }
        else{
            ohEndTimeCB.getSelectionModel().select(CBendTime);
            controller.processOHdisplay(CBstartTime, CBendTime);
            ohStartTimeCB.setItems(workspace.getStList(CBendTime, controller.getOhStartTime(), controller.getOhEndTime()));
            if (!workspace.fullInterval())  controller.processTAdisplay();
            else  controller.showFullTAandOH();
        }
    }

    @Override
    public void undoTransaction() {
        //show full time interval list first
        controller.showFullTAandOH();
        
        if (typeOfCB.equals("start")) {
            ohStartTimeCB.getSelectionModel().select(oldValue);
            ohEndTimeCB.setItems(workspace.getEtList(oldValue, controller.getOhEndTime()));
            controller.processOHdisplay(oldValue, CBendTime);
            if (!workspace.fullInterval())  controller.processTAdisplay();
            else    controller.showFullTAandOH();
        }
        else{
            ohEndTimeCB.getSelectionModel().select(oldValue);
            controller.processOHdisplay(CBstartTime, oldValue);
            ohStartTimeCB.setItems(workspace.getStList(oldValue, controller.getOhStartTime(), controller.getOhEndTime()));
            if (!workspace.fullInterval())  controller.processTAdisplay();
            else  controller.showFullTAandOH();
        }
    }
}


