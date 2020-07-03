using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;


public class BackgroundLoop : MonoBehaviour {
    public GameObject[] levels;
    private Camera mainCamera;
    private Vector2 screenBounds;
    public float choke;
    public float scrollSpeed;

    private Vector3 lastScreenPosition;

    void Start () {
        mainCamera = gameObject.GetComponent<Camera>();
        screenBounds = mainCamera.ScreenToWorldPoint(new Vector3(Screen.width, Screen.height, mainCamera.transform.position.z));
        foreach(GameObject obj in levels){
            loadChildObjects(obj);
        }
        lastScreenPosition = transform.position;
    }

    void loadChildObjects (GameObject obj) {
        float objectWidth = obj.GetComponent<SpriteRenderer> ().bounds.size.x - choke;
        int childsNeeded = (int) Mathf.Ceil (screenBounds.x * 2 / objectWidth);
        GameObject clone = Instantiate (obj) as GameObject;
        for (int i = 0; i <= childsNeeded; i++) {
            GameObject c = Instantiate (clone) as GameObject;
            c.transform.SetParent (obj.transform);
            c.transform.position = new Vector3 (objectWidth * i, obj.transform.position.y, obj.transform.position.z);
            c.name = obj.name + i;
        }
        Destroy (clone);
        Destroy (obj.GetComponent<SpriteRenderer> ());
    }

    void repositionChildObjects (GameObject obj) {
        Transform[] children = obj.GetComponentsInChildren<Transform> ();
        if (children.Length > 1) {
            GameObject firstChild = children[1].gameObject;
            GameObject lastChild = children[children.Length - 1].gameObject;
            float halfObjectWidth = lastChild.GetComponent<SpriteRenderer> ().bounds.extents.x - choke;
            if (transform.position.x + screenBounds.x > lastChild.transform.position.x + halfObjectWidth) {
                firstChild.transform.SetAsLastSibling ();
                firstChild.transform.position = new Vector3 (lastChild.transform.position.x + halfObjectWidth * 2, lastChild.transform.position.y, lastChild.transform.position.z);
            } else if (transform.position.x - screenBounds.x < firstChild.transform.position.x - halfObjectWidth) {
                lastChild.transform.SetAsFirstSibling ();
                lastChild.transform.position = new Vector3 (firstChild.transform.position.x - halfObjectWidth * 2, firstChild.transform.position.y, firstChild.transform.position.z);
            }
        }
    }

    void Update () {
        /*
        Vector3 velocity = Vector3.zero;
        Vector3 desiredPosition = transform.position + new Vector3 (scrollSpeed, 0, 0);
        Vector3 smoothPosition = Vector3.SmoothDamp (transform.position, desiredPosition, ref velocity, 0.3f);
        transform.position = smoothPosition;
         */

        //顯示螢幕占比
        Camera mainCamera = Camera.main;
        
        screenBounds = mainCamera.ScreenToWorldPoint(new Vector3(Screen.width, Screen.height, mainCamera.transform.position.z));
        Log.debug("screenBounds " + screenBounds);

        LateUpdate();
    }

    void LateUpdate () {
        foreach (GameObject obj in levels) {
            repositionChildObjects (obj);
            float parallaxSpeed = 1 - Mathf.Clamp01 (Mathf.Abs (transform.position.z / obj.transform.position.z));
            float difference = transform.position.x - lastScreenPosition.x;
            obj.transform.Translate (Vector3.right * difference * parallaxSpeed);
        }
        lastScreenPosition = transform.position;
    }
}