using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;
using static Vector3Util;

public class Bird : MonoBehaviour {

	private void OnMouseDown() {
		Log.debug("#Bird --" + "OnMouseDown");
		GetComponent<SpriteRenderer>().color = Color.red;
	}

    // Start is called before the first frame update
	void Start () {
		Log.debug("#Start --" + "start");
	}

    // Update is called once per frame
	void Update () {
		Log.debug("#Update --" + "start");
	}
}