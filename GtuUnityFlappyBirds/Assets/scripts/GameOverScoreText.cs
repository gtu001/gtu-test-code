using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

[RequireComponent(typeof(Text))]
public class GameOverScoreText : MonoBehaviour {
    
    Text gameOverScoreText;

    public GameOverScoreText () {
        Log.debug("##------------" + "GameOverScoreText");
    }

    void Start() {
        Log.debug("##----------------" + "Start");
    }

    void OnEnable() {
        Log.debug("##----------------" + "OnEnable");
        gameOverScoreText = GetComponent<Text>();
        gameOverScoreText.text = "Score : " + PlayerPrefs.GetInt("LatestScore").ToString();
    }
}