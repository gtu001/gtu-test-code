package gtu.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Troy
 */
public class AntDebug extends Task {

    private String ref;

    @SuppressWarnings("unchecked")
    @Override
    public void execute() throws BuildException {
        // debug("######## getReferences");
        // MapUtil.showMapInfo(this.getProject().getReferences());
        //
        // debug("######## getProperties");
        // MapUtil.showMapInfo(this.getProject().getProperties());
        //
        // debug("######## getUserProperties");
        // MapUtil.showMapInfo(this.getProject().getUserProperties());

        Object obj = this.getProject().getReference(ref);
        debug("## show ref id : " + ref);

        try {
            // debug(ToStringUtil.trace("iterator").toString(obj));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void debug(Object message) {
        System.out.println("AntDebug - " + message);
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
