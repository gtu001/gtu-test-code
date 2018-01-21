package gtu.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Window;

import javax.swing.JDialog;

public class JDialogHelper {

    private JDialog dialog;
    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;
    private ModalityType modalityType = ModalityType.MODELESS;
    private String title;
    private Window owner;
    private Container contentPane;
    private Boolean isModel;
    private Component locationRelativeTo;

    public static JDialogHelper newInstance() {
        return new JDialogHelper();
    }

    private JDialogHelper() {
    }

    public JDialogHelper window(Window owner) {
        this.owner = owner;
        return this;
    }
    
    public JDialogHelper contentPane(Container contentPane) {
        this.contentPane = contentPane;
        return this;
    }

    public JDialogHelper title(String title) {
        this.title = title;
        return this;
    }

    public JDialogHelper size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public JDialogHelper location(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public JDialogHelper modalityTypeBlock() {
        this.modalityType = ModalityType.APPLICATION_MODAL;
        return this;
    }

    public JDialogHelper modalityType(ModalityType modalityType) {
        this.modalityType = modalityType;
        return this;
    }
    
    public JDialogHelper setModel(Boolean isModel) {
        this.isModel = isModel;
        return this;
    }
    
    public JDialogHelper locationRelativeTo(Component locationRelativeTo) {
        this.locationRelativeTo = locationRelativeTo;
        return this;
    }

    public JDialog build() {
        dialog = new JDialog(owner, title, modalityType);
        if(contentPane == null) {
            throw new RuntimeException("不可不設定 contentPane");
        }
        dialog.setContentPane(contentPane);
        if (width != null && height != null) {
            dialog.setSize(width, height);
        }
        if (x != null && y != null) {
            dialog.setLocation(width, height);
        }
        if(isModel != null) {
            dialog.setModal(isModel);
        }
        if(locationRelativeTo != null) {
            dialog.setLocationRelativeTo(locationRelativeTo);
        }
        dialog.pack();
        return dialog;
    }
}
