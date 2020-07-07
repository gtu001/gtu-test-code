using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using static Log;
using static Vector3Util;


public class LevelController : MonoBehaviour
{
    private static int _nextLevelIndex = 1;
    private Enemy[] _enemies;

    private void OnEnable() {
        _enemies = FindObjectsOfType<Enemy>();
    }

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        foreach(Enemy enemy in _enemies) {
            if(enemy != null) {
                return;
            }
        }

        Debug.Log("You killed all enemies");

        _nextLevelIndex ++;
        string nextLevelName = "Level" + _nextLevelIndex;
        UnityEngine.SceneManagement.SceneManager.LoadScene(nextLevelName); 
    }
}
