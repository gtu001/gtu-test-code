using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;
using static GameObjectUtil;


public class CameraBackgroundCtl : MonoBehaviour {

	Camera mainCamera;

    // Start is called before the first frame update
    void Start() {
        mainCamera = Camera.main;
    }

    // Update is called once per frame
    void Update() {
        //Log.debug("Update --- start ");


        Vector3 pos = transform.position;
        //Log.debug("Camera : " + pos);

        var vertExtent = Camera.main.orthographicSize;    
        var horzExtent = vertExtent * Screen.width / Screen.height;

        //Log.debug("vertExtent " + vertExtent);
        //Log.debug("horzExtent " + horzExtent);

        //Log.debug("Rect " + mainCamera.rect);

        var backGround = GameObject.Find("Full-Background");
        // Log.showAll(backGround);

        //Log.debug("--->>> " + backGround.transform);

        // RectTransform rt = backGround.transform.GetComponent<RectTransform>();
        //GameObjectUtil.showWidthHeight1(backGround);

        
        Vector2 screenBounds = mainCamera.ScreenToWorldPoint(new Vector3(Screen.width, Screen.height, mainCamera.transform.position.z));
        Vector2 cameraPos = mainCamera.transform.position;
        string posString = "[X : " + mainCamera.transform.position.x.ToString("N2") + ", Y : " + mainCamera.transform.position.y.ToString("N2") + "]";
        this.name = "" + posString + ", Bound: " + screenBounds;
        // Log.debug("screenBounds " + screenBounds);
    }


}
