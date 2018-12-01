
package csg.transactions;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jiang He
 */
public class SiteIcon_Transaction implements jTPS_Transaction{

    HBox box;
    ImageView oldImageView;
    ImageView newImageView;
    
    public SiteIcon_Transaction(HBox box, ImageView oldImageView, ImageView newImageView) {
        this.box = box;
        this.oldImageView = oldImageView;
        this.newImageView = newImageView;
    }
    
    @Override
    public void doTransaction() {
        box.getChildren().set(1, newImageView);
    }

    @Override
    public void undoTransaction() {
        box.getChildren().set(1, oldImageView);
    }
    
}
