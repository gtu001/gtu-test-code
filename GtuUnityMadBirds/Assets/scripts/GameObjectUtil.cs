using System;
using System.Diagnostics;
using System.Reflection;
using System.Text.RegularExpressions;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameObjectUtil {
	
    public static void showWidthHeight1(GameObject gameObj) {
        var spriteRenderer = gameObj.GetComponent<SpriteRenderer>();
        float width = spriteRenderer.bounds.size.x;
        float height = spriteRenderer.bounds.size.y;
        Console.WriteLine("width --- " + width);
        Console.WriteLine("height --- " + height);
    }
 
    public static void showWidthHeight2(GameObject gameObj) {
        RectTransform rt = (RectTransform)gameObj.transform;
        float width = rt.rect.width;
        float height = rt.rect.height;
        Console.WriteLine("width --- " + width);
        Console.WriteLine("height --- " + height);
    }
}