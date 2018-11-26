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
    ComboBox cb;
    ComboBox cb2;
    String CBstartTime;
    String CBendTime;
    CourseSiteGeneratorApp app;
    CourseSiteController controller;
    CourseSiteWorkspace workspace;
    
    public TimeInterval_Transaction(
            CourseSiteGeneratorApp app,
            ComboBox cb, ComboBox cb2, 
            String CBstartTime, String CBendTime) {
        this.app = app;
        this.cb = cb;
        this.cb2 = cb2;
        this.CBstartTime = CBstartTime;
        this.CBendTime = CBendTime;
        controller = new CourseSiteController((CourseSiteGeneratorApp) app);
        workspace = (CourseSiteWorkspace) app.getWorkspaceComponent();
    }

    @Override
    public void doTransaction() {
        cb2.setItems(workspace.getEtList(CBstartTime, controller.getOhEndTime()));
        controller.processOHdisplay(CBstartTime, CBendTime);
        if (!workspace.fullInterval()) {
            controller.processTAdisplay();
        } else {
            controller.showFullTAandOH();
        }
    }

    @Override
    public void undoTransaction() {
        cb2.setItems(workspace.getEtList(CBstartTime, controller.getOhEndTime()));
        controller.processOHdisplay(CBstartTime, CBendTime);
        if (!workspace.fullInterval()) {
            controller.processTAdisplay();
        } else {
            controller.showFullTAandOH();
        }
    }
}


