package gtu.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Window;

import javax.swing.JDialog;

public class JDialogSimpleCreater {

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

    public static JDialogSimpleCreater newInstance() {
        return new JDialogSimpleCreater();
    }

    private JDialogSimpleCreater() {
    }

    public JDialogSimpleCreater window(Window owner) {
        this.owner = owner;
        return this;
    }
    
    public JDialogSimpleCreater contentPane(Container contentPane) {
        this.contentPane = contentPane;
        return this;
    }

    public JDialogSimpleCreater title(String title) {
        this.title = title;
        return this;
    }

    public JDialogSimpleCreater size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public JDialogSimpleCreater location(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public JDialogSimpleCreater modalityTypeBlock() {
        this.modalityType = ModalityType.APPLICATION_MODAL;
        return this;
    }

    public JDialogSimpleCreater modalityType(ModalityType modalityType) {
        this.modalityType = modalityType;
        return this;
    }
    
    public JDialogSimpleCreater setModel(Boolean isModel) {
        this.isModel = isModel;
        return this;
    }
    
    public JDialogSimpleCreater locationRelativeTo(Component locationRelativeTo) {
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
