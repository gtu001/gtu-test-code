package gtu.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesContextTest {

    // action
    public String execute() {
        this.createMessage("資訊", "報表產製中...");
        return null;
    }

    private void createMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }
}
