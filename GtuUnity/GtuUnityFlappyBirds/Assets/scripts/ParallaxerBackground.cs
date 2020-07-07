using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;
using static Vector3Util;

[RequireComponent (typeof (Text))]
public class ParallaxerBackground : MonoBehaviour {

    class PoolObject {
        public GameObject obj;
        public Transform transform;
        public bool inUse = false;

        private string name;
        private string prevLbl;
        private int lblCount = 0;

        public PoolObject (GameObject obj) {
            this.name = obj.name;
            this.obj = obj;
            transform = obj.transform;
        }
        public void Use () {
            inUse = true;
        }
        public void Dispose () {
            inUse = false;
        }
        public void SetLabel (string lbl) {
            if (lbl != null) {
                if (string.Equals (lbl, prevLbl)) {
                    lblCount++;
                    obj.name = name + "(" + lbl + " / " + lblCount + ")";
                } else {
                    prevLbl = lbl;
                    lblCount = 0;
                    obj.name = name + "(" + lbl + ")";
                }
            } else {
                obj.name = name;
            }
        }
    }

    [System.Serializable]
    public struct YSpawnRange {
        public float min;
        public float max;
    }

    public GameObject Prefab;
    public int poolSize = 3;
    public float shiftSpeed;

    public bool spawnImmediate; // prticle prewarm
    public YSpawnRange ySpawnRange;
    public Vector3 startSpawnPos;//右邊開始位置
    public Vector3 defaultSpawnPos;//預設位置
    public Vector3 circleSpawnPos;//前一個結束 trigger 下一個產生位置
    public Vector3 disposePos;
    public long spawnStartTime;
    public Vector2 targetAspectRatio;
    private bool spawnDefaultCompelete = false;

    PoolObject[] poolObjects;
    GameManager game;
    WaitingHelper mWaitingHelper;

    void Awake () {
        Log.debug ("##---------------" + "Awake" + " : " + Prefab.name);
        Configure ();
    }

    void Start () {
        Log.debug ("##---------------" + "Start");
        game = GameManager.Instance;
        mWaitingHelper = new WaitingHelper (spawnStartTime);
    }

    void OnEnable () {
        Log.debug ("##---------------" + "OnEnable");
        GameManager.OnGameOverConfirmed += OnGameOverConfirmed;
    }

    void Update () {
        // Log.debug ("##---------------" + "Update");
        if (game.GameOver) {
            return;
        }
        Shift ();
        if(spawnImmediate && spawnDefaultCompelete == false) {
            SpawnDefault();
            spawnDefaultCompelete = true;
        }
        if (!mWaitingHelper.isTimeUp ()) {
            return;
        }
        if (IsNoOneInUse ()) {
            Spawn ();
        }
    }

    void OnDisable () {
        Log.debug ("##---------------" + "OnDisable");
        GameManager.OnGameOverConfirmed -= OnGameOverConfirmed;
    }

    void OnGameOverConfirmed () {
        Log.debug ("##---------------" + "OnGameOverConfirmed");
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            poolObjects[ii].Dispose ();
            poolObjects[ii].transform.position = Vector3.one * 1000;
        }
    }

    void Configure () {
        Log.debug ("##---------------" + "Configure");
        poolObjects = new PoolObject[poolSize];
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            GameObject go = Instantiate (Prefab) as GameObject;
            Transform t = go.transform;
            t.SetParent (transform);
            t.position = Vector3.one * 1000;
            poolObjects[ii] = new PoolObject (go);
        }
    }

    bool IsNoOneInUse () {
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            if (poolObjects[ii].inUse == true) {
                // Log.debug ("##---------------" + "IsNoOneInUse" + " : " + "false");
                return false;
            }
        }
        // Log.debug ("##---------------" + "IsNoOneInUse" + " : " + "true");
        return true;
    }

    void Spawn () {
        Log.debug ("##---------------" + "Spawn" + " : " + Prefab.name);
        Transform t = GetPoolObject ();
        if (t == null) { // if true, this indicates that poolSize is too small
            return;
        }
        Vector3 pos = Vector3.zero;
        pos.x = startSpawnPos.x;
        pos.y = Random.Range (ySpawnRange.min, ySpawnRange.max);
        if (ySpawnRange.min == 0 && ySpawnRange.max == 0) {
            Log.debug ("##---------------" + "Spawn" + " : " + Prefab.name + " - custom Y " + startSpawnPos.y);
            pos.y = startSpawnPos.y;
        }
        t.position = pos;
    }

    void SpawnDefault () {
        Log.debug ("##---------------" + "SpawnDefault" + " : " + Prefab.name);
        Transform t = GetPoolObject ();
        if (t == null) { // if true, this indicates that poolSize is too small
            return;
        }
        Vector3 pos = Vector3.zero;
        pos.x = defaultSpawnPos.x;
        pos.y = Random.Range (ySpawnRange.min, ySpawnRange.max);
        if (ySpawnRange.min == 0 && ySpawnRange.max == 0) {
            Log.debug ("##---------------" + "SpawnDefault" + " : " + Prefab.name + " - custom Y " + defaultSpawnPos.y);
            pos.y = defaultSpawnPos.y;
        }
        t.position = pos;
    }

    void Shift () {
        // Log.debug ("##---------------" + "Shift");
        if (poolObjects == null || poolObjects.Length == 0) {
            Log.debug ("##---------------" + "Shift : " + "沒有poolObjects");
            return;
        }
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            if (poolObjects[ii].inUse) {
                poolObjects[ii].transform.position += -Vector3.right * shiftSpeed * Time.deltaTime;
                poolObjects[ii].SetLabel ("Moving");
                CheckCircleSpawnObject (poolObjects[ii]);
                CheckDisposeObject (poolObjects[ii]);
            }
        }
    }

    void CheckDisposeObject (PoolObject poolObject) {
        // Log.debug ("##---------------" + "CheckDisposeObject");
        if (poolObject.transform.position.x < disposePos.x) {
            poolObject.Dispose ();
            poolObject.SetLabel ("NoUse");
            poolObject.transform.position = Vector3.one * 1000;
        }
    }

    void CheckCircleSpawnObject (PoolObject poolObject) {
        // Log.debug ("##---------------" + "CheckDisposeObject");
        if (poolObject.transform.position.x < circleSpawnPos.x) {
            Spawn ();
        }
    }

    Transform GetPoolObject () {
        Log.debug ("##---------------" + "GetPoolObject");
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            if (!poolObjects[ii].inUse) {
                poolObjects[ii].Use ();
                poolObjects[ii].SetLabel ("Use");
                return poolObjects[ii].transform;
            }
        }
        return null;
    }
}