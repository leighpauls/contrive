using UnityEngine;
using System.Collections;

public class WheelScript : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
		float power = transform
			.parent
			.gameObject
			.GetComponentInChildren<RobotKeyControl>()
			.WheelPower;
		Debug.Log(power);
		this.rigidbody.AddRelativeTorque(0, 0, power);
	}
}
