using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

public class WaitingHelper {

    float waittingTime;
    float startTimer = 0;

    public WaitingHelper (float waittingTime) {
        this.waittingTime = waittingTime;
    }

    public bool isTimeUp (bool reset = false) {
        startTimer += Time.deltaTime;
        if (startTimer > waittingTime) {
            if (reset) {
                startTimer = 0;
            }
            return true;
        }
        return false;
    }
}