package gtu.javafx.traynotification.animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;


//import java.util.function.Predicate;

/**
 * A provider class that houses all animation objects that implement TrayAnimation
 * This makes it easier to lookup any existing animations and retrieve them
 * This means the user can easily switch animations, and in the background all possible animation types
 * Are stores in here, so when a user selects an animation type, this class will return the TrayAnimation that satisfies that request
 * It's much like a mini database
 */
public class AnimationProvider {

    private List<TrayAnimation> animationsList;

    public AnimationProvider(TrayAnimation... animations) {
        animationsList = new ArrayList<TrayAnimation>();
        Collections.addAll(animationsList, animations);
    }

    public void addAll(TrayAnimation... animations) {
        Collections.addAll(animationsList, animations);
    }

    public TrayAnimation get(int index) {
        return animationsList.get(index);
    }

//    public TrayAnimation findFirstWhere(Predicate<? super TrayAnimation> predicate) {
//        return animationsList.stream().filter(predicate).findFirst().orElse(null);
//    }
//    public List<TrayAnimation> where(Predicate<? super TrayAnimation> predicate) {
//        return animationsList.stream().filter(predicate).collect(Collectors.toList());
//    }
    public TrayAnimation findFirstWhere(Predicate predicate) {
        List<TrayAnimation> rtnLst = new ArrayList<TrayAnimation>();
        rtnLst.addAll(animationsList);
        CollectionUtils.filter(rtnLst, predicate);
        if(!rtnLst.isEmpty()) {
            return rtnLst.get(0);
        }
        return null;
    }
    public List<TrayAnimation> where(Predicate predicate) {
        List<TrayAnimation> rtnLst = new ArrayList<TrayAnimation>();
        rtnLst.addAll(animationsList);
        CollectionUtils.filter(rtnLst, predicate);
        return rtnLst;
    }
}