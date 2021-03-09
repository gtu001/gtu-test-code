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
        
        // testInput();

        getFireInputHeld();
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

    // https://www.raywenderlich.com/9671886-new-unity-input-system-getting-started

    public void testInput() {
        var gamepad = Gamepad.current;
        var keyboard = Keyboard.current;
        var mouse = Mouse.current;
        Log.debug("gamepad : " + gamepad);
        Log.debug("keyboard : " + keyboard);
        Log.debug("mouse : " + mouse);
    }

    public bool getFireInputHeld() {
        bool isGamepad = Input.GetAxis("Gamepad Fire") != 0f;
        if (isGamepad) {
            return Input.GetAxis("Gamepad Fire") >= triggerAxisThreshold;
        } else {
            return Input.GetButton("Fire");
        }
    }


    public const string k_AxisNameVertical                  = "Vertical";
    public const string k_AxisNameHorizontal                = "Horizontal";
    public const string k_MouseAxisNameVertical             = "Mouse Y";
    public const string k_MouseAxisNameHorizontal           = "Mouse X";
    public const string k_AxisNameJoystickLookVertical      = "Look Y";
    public const string k_AxisNameJoystickLookHorizontal    = "Look X";
    public const string k_ButtonNameJump                    = "Jump";
    public const string k_ButtonNameFire                    = "Fire";
    public const string k_ButtonNameGamepadFire             = "Gamepad Fire";
    public const string k_ButtonNameSprint                  = "Sprint";
    public const string k_ButtonNameCrouch                  = "Crouch";
    public const string k_ButtonNameAim                     = "Aim";
    public const string k_ButtonNameGamepadAim              = "Gamepad Aim";
    public const string k_ButtonNameSwitchWeapon            = "Mouse ScrollWheel";
    public const string k_ButtonNameGamepadSwitchWeapon     = "Gamepad Switch";
    public const string k_ButtonNameNextWeapon              = "NextWeapon";
    public const string k_ButtonNamePauseMenu               = "Pause Menu";
    public const string k_ButtonNameSubmit                  = "Submit";
    public const string k_ButtonNameCancel                  = "Cancel";
}
