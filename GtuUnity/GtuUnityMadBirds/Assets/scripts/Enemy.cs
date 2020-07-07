using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;

public class Enemy : MonoBehaviour {
    [SerializeField] private GameObject _cloudParticlePrefab;

    // Start is called before the first frame update
    void Start() {
    }

    // Update is called once per frame
    void Update() {
    }

    private void OnCollisionEnter2D(Collision2D collision) {
    	Bird bird = collision.collider.GetComponent<Bird>();
    	if(bird != null) {
            Instantiate(_cloudParticlePrefab, transform.position, Quaternion.identity);
    		Destroy(gameObject);
            return;
    	}

        Enemy enemy = collision.collider.GetComponent<Enemy>();
        if(enemy != null) {
            return;
        }

        if(collision.contacts[0].normal.y < -0.5){
            Instantiate(_cloudParticlePrefab, transform.position, Quaternion.identity);
            Destroy(gameObject);
        }
    }
}
