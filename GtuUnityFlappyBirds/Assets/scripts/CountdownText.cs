using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using static Log;

[RequireComponent(typeof(Text))]
public class CountdownText : MonoBehaviour {
    
    public delegate void CountdownFinished();
    public static event CountdownFinished OnCountdownFinished;

    Text countdown;
     
    void OnEnable() {
        Log.debug("##----------------" + "OnEnable");
        countdown = GetComponent<Text>();
        countdown.text = "3";
        StartCoroutine("Countdown");
    }

    IEnumerator Countdown() {
        Log.debug("##----------------" + "Countdown");
        int count = 3;
        for( int ii = 0 ; ii < count ; ii ++) {
            countdown.text = (count - 1).ToString();
            yield return new WaitForSeconds(1);
        }

        OnCountdownFinished();
    }
}