using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;

[RequireComponent(typeof(CharacterController))]
public class CPlayerMove : MonoBehaviour
{
    public float speed = 6.0f;
    public float jumpSpeed = 8.0f;
    public float gravity = 20.0f;
    public float jumpCount = 0.0f;
    float maxJump = 2.0f;

    private Vector3 moveDirection = Vector3.zero;
    private CharacterController controller;

    void Awake()
    {
        controller = gameObject.GetComponent<CharacterController>();
    }

    // Start is called before the first frame update
    void Start()
    {
        // Cursor.lockState = CursorLockMode.Locked;
    }

    // Update is called once per frame
    void Update()
    {
        // Log.debug("------------------------------start");
        MovePlayer();
    }

    private void MovePlayer() {
        controller.Move(moveDirection * Time.deltaTime);
 
        // Apply gravity 
        // moveDirection.y -= gravity * Time.deltaTime;

        // Log.debug("ground : " + controller.isGrounded);

        float moveAD = Input.GetAxis("Horizontal");
        float moveWS = Input.GetAxis("Vertical");

        // Log.debug("move : " + moveAD + " , " + moveWS);
 
        if (!controller.isGrounded) {
            //moveDirection.x = moveAD * speed;
            moveDirection = new Vector3(moveAD * speed, 0.0f, moveWS * speed);
        } else {
            moveDirection = new Vector3(moveAD * speed, 0.0f, moveWS * speed);
            jumpCount = 0.0f;
        }
 
        if (jumpCount < maxJump) {
            if (Input.GetButtonDown("Jump")) {
                moveDirection.y = jumpSpeed;
                jumpCount++;
            }
        }
 
        if (controller.collisionFlags == CollisionFlags.Sides) {
            jumpCount = 0.0f;
        }

        // Log.debug("move : " + moveDirection);
    }
}
