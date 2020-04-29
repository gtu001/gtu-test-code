using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

public class GameManager : MonoBehaviour {

    public delegate void GameDelegate ();
    public static event GameDelegate OnGameStarted;
    public static event GameDelegate OnGameOverConfirmed;

    public static GameManager Instance;

    public GameObject startPage;
    public GameObject gameOverPage;
    public GameObject countdownPage;
    public Text scoreText;

    enum PageState {
        None,
        Start,
        GameOver,
        Countdown
    }

    int score = 0;
    bool gameOver = false;

    public bool GameOver { get { return gameOver; } }

    void Awake () {
        Log.debug("----------------" + "Awake");
        Instance = this;
    }

    void OnEnable() {
        Log.debug("----------------" + "OnEnable");
        CountdownText.OnCountdownFinished += OnCountdownFinished;
        TapController.OnPlayerDied += OnPlayerDied;
        TapController.OnPlayerScored += OnPlayerScored;
    }

    void OnDisable() {
        Log.debug("----------------" + "OnDisable");
        CountdownText.OnCountdownFinished -= OnCountdownFinished;
        TapController.OnPlayerDied -= OnPlayerDied;
        TapController.OnPlayerScored -= OnPlayerScored;
    }

    void OnCountdownFinished() {
        Log.debug("----------------" + "OnCountdownFinished");
        SetPageState(PageState.None);
        // OnGameStarted(); XXXXXXXXXXXXXXXXXXXXXXX
        score = 0;
        gameOver = false;
    }

    void OnPlayerDied() {
        Log.debug("----------------" + "OnPlayerDied");
        gameOver = true;
        int savedScore = PlayerPrefs.GetInt("HighScore");
        if (score > savedScore) {
            PlayerPrefs.SetInt("HighScore", score);
        }
        SetPageState(PageState.GameOver);
    }

    void OnPlayerScored() {
        Log.debug("----------------" + "OnPlayerScored");
        score ++;
        scoreText.text = score.ToString();
    }

    void SetPageState (PageState state) {
        Log.debug("----------------" + "SetPageState");
        switch (state) {
            case PageState.None:
                startPage.SetActive(false);
                gameOverPage.SetActive(false);
                countdownPage.SetActive(false);
                break;
            case PageState.Start:
                startPage.SetActive(true);
                gameOverPage.SetActive(false);
                countdownPage.SetActive(false);
                break;
            case PageState.GameOver:
                startPage.SetActive(false);
                gameOverPage.SetActive(true);
                countdownPage.SetActive(false);
                break;
            case PageState.Countdown:
                startPage.SetActive(false);
                gameOverPage.SetActive(false);
                countdownPage.SetActive(true);
                break;
        }
    }

    public void ConfirmGameOver() {
        Log.debug("----------------" + "ConfirmGameOver");
        //activated when replay button is hit
        // OnGameOverConfirmed(); //event XXXXXXXXXXXXXXXXXX
        scoreText.text = "0";
        SetPageState(PageState.Start);
    }

    public void StartGame() {
        Log.debug("----------------" + "StartGame");
        //activated when play button is hit
        SetPageState(PageState.Countdown);
    }
}