using UnityEngine;
using UnityEngine.Events;
using static Log;

public class PlayerCharacterController : MonoBehaviour {
	
	PlayerInputHandler m_InputHandler;
	CharacterController m_Controller;

	[Tooltip("distance from the bottom of the character controller capsule to test for grounded")]
    public float groundCheckDistance = 0.05f;
	const float k_GroundCheckDistanceInAir = 0.07f;
	public bool isGrounded { get; private set; }
	Vector3 m_GroundNormal;
	float m_LastTimeJumped = 0f;
	const float k_JumpGroundingPreventionTime = 0.2f;
	[Tooltip("Physic layers checked to consider the player grounded")]
    public LayerMask groundCheckLayers = -1;

    float m_TargetCharacterHeight ;
	[Tooltip("Height of character when crouching")]
    public float capsuleHeightCrouching = 0.9f;
	[Tooltip("Height of character when standing")]
    public float capsuleHeightStanding = 1.8f;
	public UnityAction<bool> onStanceChanged;

	[Tooltip("Reference to the main camera used for the player")]
    public Camera playerCamera;
	[Tooltip("Ratio (0-1) of the character height where the camera will be at")]
    public float cameraHeightRatio = 0.9f;
	[Tooltip("Speed of crouching transitions")]
    public float crouchingSharpness = 10f;
	
	[Tooltip("Rotation speed for moving the camera")]
    public float rotationSpeed = 200f;
	public float RotationMultiplier { get {  return 1f; } }

    float m_CameraVerticalAngle = 0f;

    public bool isCrouching { get; private set; }

    private Vector3 moveDirection = Vector3.zero;
    public float gravity = 20.0f;






	void Start() {
		m_Controller = GetComponent<CharacterController>();
		m_InputHandler = GetComponent<PlayerInputHandler>();

		m_Controller.enableOverlapRecovery = true;

		// force the crouch state to false when starting
        setCrouchingState(false, true);
        updateCharacterHeight(true);
	}

	void Update() {

		bool wasGrounded = isGrounded;

		groundCheck();

		Log.debug("isGrounded : " + isGrounded);
		Log.debug("wasGrounded : " + wasGrounded);

		// landing
        if (isGrounded && !wasGrounded) {
        	m_Controller.Move(moveDirection * Time.deltaTime);
       		// Apply gravity 
        	moveDirection.y -= gravity * Time.deltaTime;
        }

        // crouching
        if (m_InputHandler.getCrouchInputDown()) {
            setCrouchingState(!isCrouching, false);
        }

        updateCharacterHeight(false);

        handleCharacterMovement();
	}

	// Gets the center point of the bottom hemisphere of the character controller capsule    
    Vector3 getCapsuleBottomHemisphere() {
        return transform.position + (transform.up * m_Controller.radius);
    }

    // Gets the center point of the top hemisphere of the character controller capsule    
    Vector3 getCapsuleTopHemisphere(float atHeight) {
        return transform.position + (transform.up * (atHeight - m_Controller.radius));
    }

    // Returns true if the slope angle represented by the given normal is under the slope angle limit of the character controller
    bool isNormalUnderSlopeLimit(Vector3 normal) {
        return Vector3.Angle(transform.up, normal) <= m_Controller.slopeLimit;
    }

    void applyGravity() {
    	if (!isGrounded) {
    		m_Controller.Move(moveDirection * Time.deltaTime);
        	// Apply gravity 
        	moveDirection.y -= gravity * Time.deltaTime;
    	}
    }

    void groundCheck()  {
        // Make sure that the ground check distance while already in air is very small, to prevent suddenly snapping to ground
        float chosenGroundCheckDistance = isGrounded ? (m_Controller.skinWidth + groundCheckDistance) : k_GroundCheckDistanceInAir;

        // reset values before the ground check
        isGrounded = false;
        m_GroundNormal = Vector3.up;

        // only try to detect ground if it's been a short amount of time since last jump; otherwise we may snap to the ground instantly after we try jumping
        bool isNeedCheckGround = Time.time >= m_LastTimeJumped + k_JumpGroundingPreventionTime;
        Log.debug("isNeedCheckGround : " + isNeedCheckGround);

        if (isNeedCheckGround) {
            // if we're grounded, collect info about the ground normal with a downward capsule cast representing our character capsule

            bool physicsBool = Physics.CapsuleCast(getCapsuleBottomHemisphere(), 
					            	getCapsuleTopHemisphere(m_Controller.height), 
					            	m_Controller.radius, 
					            	Vector3.down, 
					            	out RaycastHit hit, 
					            	chosenGroundCheckDistance, 
					            	groundCheckLayers, 
					            	QueryTriggerInteraction.Ignore);

            Log.debug("physicsBool : " + physicsBool);

            if (physicsBool) {
                // storing the upward direction for the surface found
                m_GroundNormal = hit.normal;

                // Only consider this a valid ground hit if the ground normal goes in the same direction as the character up
                // and if the slope angle is lower than the character controller's limit
                if (Vector3.Dot(hit.normal, transform.up) > 0f &&
                    isNormalUnderSlopeLimit(m_GroundNormal)) {
                    isGrounded = true;

                    // handle snapping to the ground
                    if (hit.distance > m_Controller.skinWidth) {
                        m_Controller.Move(Vector3.down * hit.distance);
                    }
                }
            }
        }
    }

	// // returns false if there was an obstruction
    bool setCrouchingState(bool crouched, bool ignoreObstructions) {
        // set appropriate heights
        if (crouched) {
            m_TargetCharacterHeight = capsuleHeightCrouching;
        }  else  {
            // Detect obstructions
            if (!ignoreObstructions)  {
                Collider[] standingOverlaps = Physics.OverlapCapsule(
                    getCapsuleBottomHemisphere(),
                    getCapsuleTopHemisphere(capsuleHeightStanding),
                    m_Controller.radius,
                    -1,
                    QueryTriggerInteraction.Ignore);
                foreach (Collider c in standingOverlaps) {
                    if (c != m_Controller) {
                        return false;
                    }
                }
            }
            m_TargetCharacterHeight = capsuleHeightStanding;
        }
        if (onStanceChanged != null) {
            onStanceChanged.Invoke(crouched);
        }
        isCrouching = crouched;
        return true;
    }

    void updateCharacterHeight(bool force) {
        // Update height instantly
        if (force) {
            m_Controller.height = m_TargetCharacterHeight;
            m_Controller.center = Vector3.up * m_Controller.height * 0.5f;
            playerCamera.transform.localPosition = Vector3.up * m_TargetCharacterHeight * cameraHeightRatio;
            // m_Actor.aimPoint.transform.localPosition = m_Controller.center;
        }
        // Update smooth height
        else if (m_Controller.height != m_TargetCharacterHeight) {
            // resize the capsule and adjust camera position
            m_Controller.height = Mathf.Lerp(m_Controller.height, m_TargetCharacterHeight, crouchingSharpness * Time.deltaTime);
            m_Controller.center = Vector3.up * m_Controller.height * 0.5f;
            playerCamera.transform.localPosition = Vector3.Lerp(playerCamera.transform.localPosition, Vector3.up * m_TargetCharacterHeight * cameraHeightRatio, crouchingSharpness * Time.deltaTime);
            // m_Actor.aimPoint.transform.localPosition = m_Controller.center;
        }
    }

    void handleCharacterMovement() {
    	// horizontal character rotation
        {
            // rotate the transform with the input speed around its local Y axis
            Vector3 rotateVal = new Vector3(0f, (m_InputHandler.getLookInputsHorizontal() * rotationSpeed * RotationMultiplier), 0f);
            Log.debug("rotateVal : " + rotateVal);
            transform.Rotate(rotateVal, Space.Self);
        }

        // vertical camera rotation
        {
            // add vertical inputs to the camera's vertical angle
            m_CameraVerticalAngle += m_InputHandler.getLookInputsVertical() * rotationSpeed * RotationMultiplier;

            // limit the camera's vertical angle to min/max
            m_CameraVerticalAngle = Mathf.Clamp(m_CameraVerticalAngle, -89f, 89f);

            // apply the vertical angle as a local rotation to the camera transform along its right axis (makes it pivot up and down)
            playerCamera.transform.localEulerAngles = new Vector3(m_CameraVerticalAngle, 0, 0);
        }
    }
}