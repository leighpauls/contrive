using UnityEngine;
using System.Collections;

public class RobotKeyControl : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	public float WheelPower { get; private set; }
	
	// Update is called once per frame
	void Update () {
		const float maxWheelPower = 10000f;
		if (Input.GetKey(KeyCode.UpArrow)) {
			WheelPower = maxWheelPower;
		} else if (Input.GetKey(KeyCode.DownArrow)) {
			WheelPower = -maxWheelPower;
		} else {
			WheelPower = 0.0f;
		}
		
	}
}
