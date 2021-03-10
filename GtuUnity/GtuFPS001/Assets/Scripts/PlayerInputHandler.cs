using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;
using UnityEngine.InputSystem;

public class PlayerInputHandler : MonoBehaviour
{
    [Tooltip("Limit to consider an input when using a trigger on a controller")]
    public float triggerAxisThreshold = 0.4f;
    [Tooltip("Used to flip the vertical input axis")]
    public bool invertYAxis = false;
    [Tooltip("Sensitivity multiplier for moving the camera around")]
    public float lookSensitivity = 1f;



    // Start is called before the first frame update
    void Start()
    {
        Log.debug("---------------------------------------------------------");
    }

    // Update is called once per frame
    void Update()
    {
        Log.debug("---------------------------------------------------------");

        testInput();

        Log.debug("isCanProcessInput" + " -- " + isCanProcessInput());
        Log.debug("getFireInputHeld" + " -- " + getFireInputHeld());
        Log.debug("getMoveInput" + " -- " + getMoveInput());
        Log.debug("getJumpInputDown" + " -- " + getJumpInputDown());
        Log.debug("getJumpInputHeld" + " -- " + getJumpInputHeld());
        Log.debug("getAimInputHeld" + " -- " + getAimInputHeld());
        Log.debug("getSprintInputHeld" + " -- " + getSprintInputHeld());
        Log.debug("getCrouchInputDown" + " -- " + getCrouchInputDown());
        Log.debug("getCrouchInputReleased" + " -- " + getCrouchInputReleased());
        Log.debug("getSwitchWeaponInput" + " -- " + getSwitchWeaponInput());
        Log.debug("getSelectWeaponInput" + " -- " + getSelectWeaponInput());
        Log.debug("getLookInputsHorizontal" + " -- " + getLookInputsHorizontal());
        Log.debug("getLookInputsVertical" + " -- " + getLookInputsVertical());
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

    public bool getFireInputHeld() {
        bool isGamepad = Input.GetAxis("Gamepad Fire") != 0f;
        if (isGamepad) {
            return Input.GetAxis("Gamepad Fire") >= triggerAxisThreshold;
        } else {
            return Input.GetButton("Fire");
        }
    }

    public bool getJumpInputDown() {
        return Input.GetButtonDown("Jump");
    }

    public bool getJumpInputHeld() {
        return Input.GetButton("Jump");
    }

    public bool getAimInputHeld() {
        bool isGamepad = Input.GetAxis("Gamepad Aim") != 0f;
        bool i = isGamepad ? (Input.GetAxis("Gamepad Aim") > 0f) : Input.GetButton("Aim");
        return i;
    }

    public bool getSprintInputHeld() {
        return Input.GetButton("Sprint");
    }

    public bool getCrouchInputDown() {
        return Input.GetButtonDown("Crouch");
    }

    public bool getCrouchInputReleased() {
        return Input.GetButtonUp("Crouch");
    }

    public int getSwitchWeaponInput() {
        bool isGamepad = Input.GetAxis("Gamepad Switch") != 0f;
        string axisName = isGamepad ? "Gamepad Switch" : "Mouse ScrollWheel";
        if (Input.GetAxis(axisName) > 0f)
            return -1;
        else if (Input.GetAxis(axisName) < 0f)
            return 1;
        else if (Input.GetAxis("NextWeapon") > 0f)
            return -1;
        else if (Input.GetAxis("NextWeapon") < 0f)
            return 1;
        return 0;
    }

    public int getSelectWeaponInput() {
        if (Input.GetKeyDown(KeyCode.Alpha1))
            return 1;
        else if (Input.GetKeyDown(KeyCode.Alpha2))
            return 2;
        else if (Input.GetKeyDown(KeyCode.Alpha3))
            return 3;
        else if (Input.GetKeyDown(KeyCode.Alpha4))
            return 4;
        else if (Input.GetKeyDown(KeyCode.Alpha5))
            return 5;
        else if (Input.GetKeyDown(KeyCode.Alpha6))
            return 6;
        else
            return 0;
        // return 0;
    }

    float getMouseOrStickLookAxis(string mouseInputName, string stickInputName)  {
        // Check if this look input is coming from the mouse
        bool isGamepad = Input.GetAxis(stickInputName) != 0f;
        float i = isGamepad ? Input.GetAxis(stickInputName) : Input.GetAxisRaw(mouseInputName);

        // handle inverting vertical input
        if (invertYAxis) {
            i *= -1f;
        }

        // apply sensitivity multiplier
        i *= lookSensitivity;

        if (isGamepad) {
            // since mouse input is already deltaTime-dependant, only scale input with frame time if it's coming from sticks
            i *= Time.deltaTime;
        } else {
            // reduce mouse input amount to be equivalent to stick movement
            i *= 0.01f;
#if UNITY_WEBGL
            // Mouse tends to be even more sensitive in WebGL due to mouse acceleration, so reduce it even more
            i *= webglLookSensitivityMultiplier;
#endif
        }
        return i;
        //return 0f;
    }

    public float getLookInputsHorizontal() {
        return getMouseOrStickLookAxis("Mouse X", "Look X");
    }

    public float getLookInputsVertical() {
        return getMouseOrStickLookAxis("Mouse Y", "Look Y");
    }
}
