using UnityEngine;
using System.Collections;

public class GameManager : MonoBehaviour
{
    public BoardManager boardScript;

    private int level = 3;

    void Awake()
    {
        boardScript = GetComponent<BoardManager>();
        InitGame();
    }

    void InitGame()
    {
        boardScript.SetupScene(level);
    }

    void Update()
    {
        /*
        if (Input.touchCount > 0) {
            Touch myTouch = Input.touches[0];
            if(myTouch.phase == TouchPhase.Began) {
                touchOrigin = myTouch.position;
            }else if (myTouch.phase == TouchPhase.Ended && touchOrigin.x >= 0) {
                Vector2 touchEnd = myTouch.position;
                float x = touchEnd.x - touchOrigin.x;
                float y = touchEnd.y - touchOrigin.y;
                touchOrigin.x = -1;
                if(Mathf.Abs(x) > Mathf.Abs(y)) {
                    horizontal = x > 0 ? 1 : -1;
                }else {
                    vertical = y > 0 ? 1 : -1;
                }
            }
        }
        */
    }
}