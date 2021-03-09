using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;
using UnityEngine.InputSystem;

public class PlayerInputHandler : MonoBehaviour
{
    [Tooltip("Limit to consider an input when using a trigger on a controller")]
    public float triggerAxisThreshold = 0.4f;

    // Start is called before the first frame update
    void Start()
    {
        Log.debug("---------------------------------------------------------");
    }

    // Update is called once per frame
    void Update()
    {
        // Log.debug(" input : " + getMoveInput());
        getFireInputHeld();
        testInput();
    }

    public bool isCanProcessInput() {
        bool ok = Cursor.lockState == CursorLockMode.Locked; // && !m_GameFlowManager.gameIsEnding;
        Log.debug("isCanProcessInput : " + ok);
        return ok;
    }

    public Vector3 getMoveInput() {
        Vector3 move = new Vector3(Input.GetAxisRaw("Horizontal"), 0f, Input.GetAxisRaw("Vertical"));
        // constrain move input to a maximum magnitude of 1, otherwise diagonal movement might exceed the max move speed defined
        move = Vector3.ClampMagnitude(move, 1);
        return move;
    }

    public void testInput() {
        var gamepad = Gamepad.current;
        var keyboard = Keyboard.current;
        var mouse = Mouse.current;
        Log.debug("gamepad : " + gamepad);
        Log.debug("keyboard : " + keyboard);
        Log.debug("mouse : " + mouse);
    }

    public bool getFireInputHeld() {
        float gamepadFire = Input.GetAxis("Gamepad Fire");
        bool mouseFire = Input.GetButton("Fire");
        bool isGamepad = gamepadFire != 0f;
        Log.debug(" getFireInputHeld : " + gamepadFire + " , " + isGamepad + " , " + mouseFire);
        if (isGamepad) {
            return gamepadFire >= triggerAxisThreshold;
        } else {
            return mouseFire;
        }
    }
}
