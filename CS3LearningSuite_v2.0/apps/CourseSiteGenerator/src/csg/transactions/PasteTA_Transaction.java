package csg.transactions;

import jtps.jTPS_Transaction;
import csg.CourseSiteGeneratorApp;
import csg.data.OfficeHoursData;
import csg.data.TeachingAssistantPrototype;

public class PasteTA_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TeachingAssistantPrototype taToPaste;

    public PasteTA_Transaction(  CourseSiteGeneratorApp initApp, 
                                 TeachingAssistantPrototype initTAToPaste) {
        app = initApp;
        taToPaste = initTAToPaste;
    }

    @Override
    public void doTransaction() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        data.addTA(taToPaste);
    }

    @Override
    public void undoTransaction() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        data.removeTA(taToPaste);
    }   
}