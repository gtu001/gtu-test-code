using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;

public class Enemy : MonoBehaviour {
    // Start is called before the first frame update
    void Start() {
    }

    // Update is called once per frame
    void Update() {
    }

    private void OnCollisionEnter2D(Collision2D collision) {
    	Bird bird = collision.collider.GetComponent<Bird>();
    	if(bird != null) {
    		Destroy(gameObject);
    	}
    }
}
