using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//https://www.youtube.com/watch?v=QkisHNmcK7Y
public class ScrollingBackground : MonoBehaviour {
    public float backgroundSize;

    private Transform cameraTransform;
    private Transform[] layers;
    private float viewZone = 10;
    private int leftIndex;
    private int rightIndex;
    private float lastCameraX;

    // Start is called before the first frame update
    void Start () {
        cameraTransform = Camera.main.transform;
        lastCameraX = cameraTransform.position.x;
        layers = new Transform[transform.childCount];
        for(int ii = 0; ii < transform.childCount ; ii ++) {
            layers[ii] = transform.GetChild(ii);
        }
        leftIndex = 0;
        rightIndex = layers.Length - 1;
    }

    // Update is called once per frame
    void Update () {
        /*
        if(Input.GetKeyDown(KeyCode.A)) {
            ScrollLeft();
        }
        if(Input.GetKeyDown(KeyCode.D)) {
            ScrollRight();
        }
         */

        float deltaX = cameraTransform.position.x - lastCameraX;
        transform.position += Vector3.right * 
        lastCameraX = cameraTransform.position.x;

        if(cameraTransform.position.x < (layers[leftIndex].transform.position.x + viewZone)) {
            ScrollLeft();
        }
        if(cameraTransform.position.x > (layers[rightIndex].transform.position.x - viewZone)) {
            ScrollRight();
        }
    }

    private void ScrollLeft() {
        int lastRight = rightIndex;
        layers[rightIndex].position = Vector3.right * (layers[leftIndex].position.x - backgroundSize);
        leftIndex = rightIndex;
        rightIndex --;
        if(rightIndex < 0) {
            rightIndex = layers.Length - 1;
        }
    }

    private void ScrollRight() {
        int lastLeft = leftIndex;
        layers[leftIndex].position = Vector3.right * (layers[rightIndex].position.x + backgroundSize);
        rightIndex = leftIndex;
        leftIndex ++;
        if(leftIndex == layers.Length) {
            leftIndex = 0;
        }
    }
}