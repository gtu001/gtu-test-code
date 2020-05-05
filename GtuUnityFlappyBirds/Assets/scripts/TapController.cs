using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;

[RequireComponent (typeof (Rigidbody2D))]
public class TapController : MonoBehaviour {

    public delegate void PlayerDelegate ();
    public static event PlayerDelegate OnPlayerDied;
    public static event PlayerDelegate OnPlayerScored;

    public float tapForce = 10;
    public float tiltSmooth = 5;
    public Vector3 startPos;

    Rigidbody2D rigidbody2;
    Quaternion downRotation;
    Quaternion forwardRotation;

    public TapController () {
        Log.debug("##------------" + "TapController");
    }

    void Start () {
        Log.debug("##------------" + "Start");
        rigidbody2 = GetComponent<Rigidbody2D> ();
        downRotation = Quaternion.Euler (0, 0, -90);
        forwardRotation = Quaternion.Euler (0, 0, 35);
        //rigidbody2.simulated = false;
    }

    void OnEnable() {
        Log.debug("##------------" + "OnEnable");
        GameManager.OnGameStarted += OnGameStarted;
        GameManager.OnGameOverConfirmed += OnGameOverConfirmed;
    }

    void OnDisable() {
        Log.debug("##------------" + "OnDisable");
        GameManager.OnGameStarted -= OnGameStarted;
        GameManager.OnGameOverConfirmed -= OnGameOverConfirmed;
    }

    void OnGameStarted() {
        Log.debug("##------------" + "OnGameStarted");
        rigidbody2.velocity = Vector3.zero;
        rigidbody2.simulated = true;
    }

    void OnGameOverConfirmed() {
        Log.debug("##------------" + "OnGameOverConfirmed");
        transform.localPosition = startPos;
        transform.rotation = Quaternion.identity;// freeze
    }

    void Update () {
        Log.debug("##------------" + "Update");
        if (Input.GetMouseButtonDown (0)) {
            transform.rotation = forwardRotation; //逆時鐘旋轉
            rigidbody2.velocity = Vector3.zero; //設定重力加速度為0
            rigidbody2.AddForce (Vector2.up * tapForce, ForceMode2D.Force); //向上飛的力量
        }

        transform.rotation = Quaternion.Lerp (transform.rotation, downRotation, tiltSmooth * Time.deltaTime); //定時順時鐘轉
    }

    void OnTriggerEnter2D (Collider2D col) {
        Log.debug("##------------" + "OnTriggerEnter2D");
        if (col.gameObject.tag == "SocreZone") {
            // register a score event
            OnPlayerScored (); // event sent to GameManager
            // play a sound
        }

        if (col.gameObject.tag == "DeadZone") {
            rigidbody2.simulated = false;
            // register a dead event
            OnPlayerDied (); // event sent to GameManager
            // play a sound
        }
    }
}