using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;

public class Bird : MonoBehaviour {

	Vector3 _initialPosition;

	private void Awake() {
		Log.debug("#Awake --" + "start");
		_initialPosition = transform.position;
	}

	private void OnMouseDown() {
		Log.debug("#Bird --" + "OnMouseDown");
		GetComponent<SpriteRenderer>().color = Color.red;
	}

	private void OnMouseUp() {
		Log.debug("#Bird --" + "OnMouseUp");
		GetComponent<SpriteRenderer>().color = Color.white;


		Vector2 directionToInitialPosition = _initialPosition - transform.position;
		directionToInitialPosition = directionToInitialPosition * 100;
		GetComponent<Rigidbody2D>().AddForce(directionToInitialPosition);
		GetComponent<Rigidbody2D>().gravityScale = 1;
	}

	private void OnMouseDrag() {
		Log.debug("#Bird --" + "OnMouseDrag");
		Vector3 newPosition = Camera.main.ScreenToWorldPoint(Input.mousePosition);
		transform.position = new Vector3(newPosition.x, newPosition.y);//指複製x,y, 但z=0的座標
	}

    // 在第一禎之前執行
	void Start () {
		Log.debug("#Start --" + "start");
	}

    // 每一禎執行一次  ====>  https://www.youtube.com/watch?v=OR0e-1UBEOU&t=3408s   1:27
	void Update () {
		Log.debug("#Update --" + "start");
		if (transform.position.y > 3) {
			//復位
			string currentSceneName = UnityEngine.SceneManagement.SceneManager.GetActiveScene().name;
			UnityEngine.SceneManagement.SceneManager.LoadScene(currentSceneName); 
		}
	}
}