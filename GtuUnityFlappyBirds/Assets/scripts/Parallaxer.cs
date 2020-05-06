using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

[RequireComponent (typeof (Text))]
public class Parallaxer : MonoBehaviour {

    class PoolObject {
        public Transform transform;
        public bool inUse = false;
        public PoolObject (Transform t) {
            transform = t;
        }
        public void Use () {
            inUse = true;
        }
        public void Dispose () {
            inUse = false;
        }
    }

    [System.Serializable]
    public struct YSpawnRange {
        public float min;
        public float max;
    }

    public GameObject Prefab;
    public int poolSize;
    public float shiftSpeed;
    public float spawnRate;

    public YSpawnRange ySpawnRange;
    public Vector3 defaultSpawnPos;
    public bool spawnImmediate; // prticle prewarm
    public Vector3 immediateSpawnPos;
    public Vector2 targetAspectRatio;

    float spawnTimer;
    float targetAspect;
    PoolObject[] poolObjects;
    GameManager game;

    void Awake () {
        Log.debug ("##---------------" + "Awake" + " : " + Prefab.name);
        Configure();
    }

    void Start() {
        Log.debug ("##---------------" + "Start");
        game = GameManager.Instance;
    }

    void OnEnable () {
        Log.debug ("##---------------" + "OnEnable");
        GameManager.OnGameOverConfirmed += OnGameOverConfirmed;
    }

    void Update() {
        // Log.debug ("##---------------" + "Update");
        if (game.GameOver) {
            return;
        }
        Shift();
        spawnTimer += Time.deltaTime;
        if(spawnTimer > spawnRate) {
            Spawn();
            spawnTimer = 0;
        }
    }

    void OnDisable () {
        Log.debug ("##---------------" + "OnDisable");
        GameManager.OnGameOverConfirmed -= OnGameOverConfirmed;
    }

    void OnGameOverConfirmed () {
        Log.debug ("##---------------" + "OnGameOverConfirmed");
        for( int ii = 0 ; ii < poolObjects.Length ; ii ++) {
            poolObjects[ii].Dispose();
            poolObjects[ii].transform.position = Vector3.one * 1000; 
        }
        if (spawnImmediate) {
            SpawnImmediate ();
        }
    }

    void Configure () {
        Log.debug ("##---------------" + "Configure");
        targetAspect = targetAspectRatio.x / targetAspectRatio.y;
        poolObjects = new PoolObject[poolSize];
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            GameObject go = Instantiate (Prefab) as GameObject;
            Transform t = go.transform;
            t.SetParent (transform);
            t.position = Vector3.one * 1000;
            poolObjects[ii] = new PoolObject (t);
        }
        if (spawnImmediate) {
            SpawnImmediate ();
        }
    }

    void Spawn () {
        Log.debug ("##---------------" + "Spawn");
        Transform t = GetPoolObject ();
        if (t == null) { // if true, this indicates that poolSize is too small
            return;
        }
        Vector3 pos = Vector3.zero;
        pos.x = defaultSpawnPos.x;
        pos.y = Random.Range (ySpawnRange.min, ySpawnRange.max);
        if(ySpawnRange.min == 0 && ySpawnRange.max == 0) {
            Log.debug ("##---------------" + "Spawn" + " : " + Prefab.name + " - custom Y " + immediateSpawnPos.y);
            pos.y = defaultSpawnPos.y;
        }
        t.position = pos;
    }

    void SpawnImmediate () {
        Log.debug ("##---------------" + "SpawnImmediate");
        Transform t = GetPoolObject ();
        if (t == null) { // if true, this indicates that poolSize is too small
            return;
        }
        Vector3 pos = Vector3.zero;
        pos.x = immediateSpawnPos.x;
        pos.y = Random.Range (ySpawnRange.min, ySpawnRange.max);
        if(ySpawnRange.min == 0 && ySpawnRange.max == 0) {
            Log.debug ("##---------------" + "SpawnImmediate" + " : " + Prefab.name + " - custom Y " + immediateSpawnPos.y);
            pos.y = immediateSpawnPos.y;
        }
        t.position = pos;
        Spawn();
    }

    void Shift () {
        // Log.debug ("##---------------" + "Shift");
        if(poolObjects == null || poolObjects.Length == 0) {
            Log.debug ("##---------------" + "Shift : " + "沒有poolObjects");
            return;
        }
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            poolObjects[ii].transform.position += -Vector3.right * shiftSpeed * Time.deltaTime;
            CheckDisposeObject (poolObjects[ii]);
        }
    }

    void CheckDisposeObject (PoolObject poolObject) {
        // Log.debug ("##---------------" + "CheckDisposeObject");
        if (poolObject.transform.position.x < -defaultSpawnPos.x) {
            poolObject.Dispose ();
            poolObject.transform.position = Vector3.one * 1000;
        }
    }

    Transform GetPoolObject () {
        Log.debug ("##---------------" + "GetPoolObject");
        for (int ii = 0; ii < poolObjects.Length; ii++) {
            if (!poolObjects[ii].inUse) {
                poolObjects[ii].Use ();
                return poolObjects[ii].transform;
            }
        }
        return null;
    }
}