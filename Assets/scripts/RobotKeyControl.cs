using UnityEngine;
using System.Collections;

public class RobotKeyControl : MonoBehaviour {
	
	public float ForwardForce { get; private set; }
	public float TurningForce { get; private set; }
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKey(KeyCode.UpArrow)) {
			ForwardForce = 1.0f;
		} else if (Input.GetKey(KeyCode.DownArrow)) {
			ForwardForce = -1.0f;
		} else {
			ForwardForce = 0.0f;
		}

		if (Input.GetKey(KeyCode.LeftArrow)) {
			TurningForce = -1.0f;
		} else if (Input.GetKey(KeyCode.RightArrow)) {
			TurningForce = 1.0f;
		} else {
			TurningForce = 0.0f;
		}
	}
}
