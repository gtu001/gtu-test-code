using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

public static class Vector3Util {

    public static Vector3 Change (this Vector3 org, object x = null, object y = null, object z = null) {
        float newX;
        float newY;
        float newZ;
        if (x == null)
            newX = org.x;
        else
            newX = (float) x;
        if (y == null)
            newY = org.y;
        else
            newY = (float) y;
        if (z == null)
            newZ = org.z;
        else
            newZ = (float) z;
        return new Vector3 (newX, newY, newZ);
    }
}