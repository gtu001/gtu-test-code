using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

[RequireComponent(typeof(Text))]
public class HighscoreText : MonoBehaviour {
    
    Text highscore;

    public HighscoreText () {
        Log.debug("##------------" + "HighscoreText");
    }

    void Start() {
        Log.debug("##----------------" + "Start");
    }

    void OnEnable() {
        Log.debug("##----------------" + "OnEnable");
        highscore = GetComponent<Text>();
        highscore.text = PlayerPrefs.GetInt("HighScore").ToString();
    }
}