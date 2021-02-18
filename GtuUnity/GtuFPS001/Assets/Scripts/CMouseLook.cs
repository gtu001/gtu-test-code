using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;

public class CMouseLook : MonoBehaviour
{

    public float mouseSensitivity = 100f;
    public GameObject player;

    void Start()
    { 
    }

    void Update()
    {
        // Log.debug("-----------------------------------start");

        float mouseX = Input.GetAxis("Mouse X") * Time.deltaTime * mouseSensitivity;
        float mouseY = Input.GetAxis("Mouse Y") * Time.deltaTime * mouseSensitivity;

        // Log.debug("mouseX : " + mouseX);
        // Log.debug("mouseY : " + mouseY);
        
        Camera.main.transform.Rotate(mouseY *-1, 0, 0);
        player.transform.Rotate(0, mouseX, 0);
    }
}
