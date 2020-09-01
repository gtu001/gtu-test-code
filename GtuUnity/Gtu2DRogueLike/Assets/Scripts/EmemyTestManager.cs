using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static Log;

public class EmemyTestManager : MonoBehaviour
{
	 Animator animator;
	 GameObject ememy2;

    // Start is called before the first frame update
    void Start()
    {
    	Log.debug("#Start --------" + "Start");
    }

    // Update is called once per frame
    void Update()
    {
    	Log.debug("#Update --------" + "Start");
    	if( ememy2 == null) {
    		ememy2 = GameObject.Find("Ememy2");
    		if(ememy2 != null) {
    			animator = ememy2.GetComponent<Animator>();
    			Log.debug("#animator --------" + animator);
    		}
    	}

        if (Input.GetKeyDown (KeyCode.Space)) {
            playAnimator("Emeny2Idle", animator);
        }
 
        if (Input.GetKeyDown (KeyCode.A)) {
            playAnimator("Emeny2Chop", animator);
        }
    }

    private void playAnimator(string animatorName, Animator animator) {
    	animator.Play("Base Layer." + animatorName, 0, 1f);
    }

    private void changeAnimatorController(string controllerName, Animator animator) {
    	animator.runtimeAnimatorController = (RuntimeAnimatorController)RuntimeAnimatorController.Instantiate(
    		Resources.Load("Animation/" + controllerName, typeof(RuntimeAnimatorController ))
    		);
    }
}
