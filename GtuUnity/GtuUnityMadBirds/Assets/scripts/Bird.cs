﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;

public class Bird : MonoBehaviour {

	private Vector3 _initialPosition;
	private bool _birdWasLaunched;
	private float _timeSittingAround;
	[SerializeField] private float _launchPower = 500;
	private Bird.CaculateMaterialTiling mCaculateMaterialTiling;

	//改material tiling.x 
	private class CaculateMaterialTiling {
		Bird self;
		bool mouseState;
		public CaculateMaterialTiling(Bird mBird) {
			self = mBird;
		}
		public void mouseUp(){
			mouseState = false;
		}
		public void mouseDown() { 
			mouseState = true;
		}
		public void process() {
			float dist = Vector3.Distance(self._initialPosition, self.transform.position);
			dist = System.Math.Abs(dist);
			int tilingX = (int)(dist * 2);

			// Material mMaterial = (Material)Resources.Load("WhiteArrow", typeof(Material));
			Material mMaterial = self.GetComponent<LineRenderer>().material;
			// mMaterial.mainTextureScale = new Vector2(40, 1);
			mMaterial.SetTextureScale("_MainTex", new Vector2(tilingX, 1));
			mMaterial.SetTextureOffset("_MainTex", new Vector2(1, 1));
		}
	}
	

	private void Awake() {
		Log.debug("#Awake --" + "start");
		_initialPosition = transform.position;
		mCaculateMaterialTiling = new Bird.CaculateMaterialTiling(this);
	}

	private void OnMouseDown() {
		Log.debug("#Bird --" + "OnMouseDown");
		GetComponent<SpriteRenderer>().color = Color.red;
		GetComponent<LineRenderer>().enabled = true;

		mCaculateMaterialTiling.mouseDown();
	}

	private void OnMouseUp() {
		Log.debug("#Bird --" + "OnMouseUp");
		GetComponent<SpriteRenderer>().color = Color.white;

		Vector2 directionToInitialPosition = _initialPosition - transform.position;
		directionToInitialPosition = directionToInitialPosition * _launchPower;
		GetComponent<Rigidbody2D>().AddForce(directionToInitialPosition);
		GetComponent<Rigidbody2D>().gravityScale = 1;
		_birdWasLaunched = true;

		GetComponent<LineRenderer>().enabled = false;

		mCaculateMaterialTiling.mouseUp();
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

	void UpdateName() {
		this.name = "GameBird " + "x : " + 
			transform.position.x.ToString("N2") + ", y : " + transform.position.y.ToString("N2");
	}

    // 每一禎執行一次  ====>  https://www.youtube.com/watch?v=OR0e-1UBEOU&t=3408s   1:27
	void Update () {
		Log.debug("#Update --" + "start");
		UpdateName();

		GetComponent<LineRenderer>().SetPosition(1, _initialPosition);
		GetComponent<LineRenderer>().SetPosition(0, transform.position);

		if(_birdWasLaunched && 
			GetComponent<Rigidbody2D>().velocity.magnitude <= 0.1 //動能 
			) {
			_timeSittingAround += Time.deltaTime;
		}

		float outterPos = 10;
		if (transform.position.x > _initialPosition.x + outterPos ||
			transform.position.y > _initialPosition.y + outterPos ||
			transform.position.x < _initialPosition.x + outterPos * -1 ||
			transform.position.y < _initialPosition.y + outterPos * -1 || 
			_timeSittingAround > 3 //超過三秒靜止
			) {
			//復位
			string currentSceneName = UnityEngine.SceneManagement.SceneManager.GetActiveScene().name;
			UnityEngine.SceneManagement.SceneManager.LoadScene(currentSceneName); 
		}

		mCaculateMaterialTiling.process();
	}
}