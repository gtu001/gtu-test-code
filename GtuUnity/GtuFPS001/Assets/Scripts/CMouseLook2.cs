using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CMouseLook2 : MonoBehaviour
{
    public float mouseSensitivity = 100f;//100f;
    public Transform playerBody;
    float xRotation = 0f;

    // Start is called before the first frame update
    void Start()
    {
        Cursor.lockState = CursorLockMode.Locked;
    }

    // Update is called once per frame
    void Update()
    {
        // Log.debug("-----------------------------------start");

        float mouseX = Input.GetAxis("Mouse X") * Time.deltaTime * mouseSensitivity;
        float mouseY = Input.GetAxis("Mouse Y") * Time.deltaTime * mouseSensitivity;
        
        xRotation -= mouseY;
        xRotation = Mathf.Clamp(xRotation, -90f, 90f);

        // Log.debug("xRotation : " + xRotation);
        // Log.debug("mouseX : " + mouseX);
        
        Camera.main.transform.localRotation = Quaternion.Euler(xRotation, 0, 0);
        playerBody.Rotate(Vector3.up * mouseX);
    }
}