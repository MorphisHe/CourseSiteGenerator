package csg.clipboard;

import djf.components.AppClipboardComponent;
import java.util.ArrayList;
import java.util.HashMap;
import csg.CourseSiteGeneratorApp;
import csg.data.OfficeHoursData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.CutTA_Transaction;
import csg.transactions.PasteTA_Transaction;


public class CourseSiteClipboard implements AppClipboardComponent {
    CourseSiteGeneratorApp app;
    TeachingAssistantPrototype clipboardCutTA;
    TeachingAssistantPrototype clipboardCopiedTA;

    public CourseSiteClipboard(CourseSiteGeneratorApp initApp) {
        app = initApp;
        clipboardCutTA = null;
        clipboardCopiedTA = null;
    }

    @Override
    public void cut() {
//        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
//        if (data.isTASelected()) {
//            clipboardCutTA = data.getSelectedTA();
//            clipboardCopiedTA = null;
//            HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours = data.getTATimeSlots(clipboardCutTA);
//            CutTA_Transaction transaction = new CutTA_Transaction((CourseSiteGeneratorApp)app, clipboardCutTA, officeHours);
//            app.processTransaction(transaction);
//        }
    }

    @Override
    public void copy() {
//        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
//        if (data.isTASelected()) {
//            TeachingAssistantPrototype tempTA = data.getSelectedTA();
//            copyToCopiedClipboard(tempTA);
//        }
    }
    
    private void copyToCopiedClipboard(TeachingAssistantPrototype taToCopy) {
        clipboardCutTA = null;
        clipboardCopiedTA = copyTA(taToCopy);
        app.getFoolproofModule().updateAll();        
    }
    
    private TeachingAssistantPrototype copyTA(TeachingAssistantPrototype taToCopy) {
        TeachingAssistantPrototype tempCopy = (TeachingAssistantPrototype)taToCopy.clone(); 
        tempCopy.setName(tempCopy.getName() + "_1");
        tempCopy.setEmail(tempCopy.getEmail() + "_1");
        return tempCopy;
    }

    @Override
    public void paste() {
        OfficeHoursData data = (OfficeHoursData)app.getDataComponent();
        if (clipboardCutTA != null) {
            PasteTA_Transaction transaction = new PasteTA_Transaction((CourseSiteGeneratorApp)app, clipboardCutTA);
            app.processTransaction(transaction);
            
            this.clipboardCutTA = null;
            app.getFoolproofModule().updateAll();
        }
        else if (clipboardCopiedTA != null) {
            // FIGURE OUT THE PROPER NAME AND
            // EMAIL ADDRESS SO THAT THERE ISN'T A DUPLICATE             
            PasteTA_Transaction transaction = new PasteTA_Transaction((CourseSiteGeneratorApp)app, clipboardCopiedTA);
            app.processTransaction(transaction);
            
            this.clipboardCopiedTA = null;
            app.getFoolproofModule().updateAll();
        }
    }    

    @Override
    public boolean hasSomethingToCut() {
//        return ((OfficeHoursData)app.getDataComponent()).isTASelected();
          return true;
    }

    @Override
    public boolean hasSomethingToCopy() {
//        return ((OfficeHoursData)app.getDataComponent()).isTASelected();
          return true;
    }

    @Override
    public boolean hasSomethingToPaste() {
//        if ((clipboardCutTA != null) || (clipboardCopiedTA != null))
//            return true;
//        else
//            return false;
          return true;
    }
}