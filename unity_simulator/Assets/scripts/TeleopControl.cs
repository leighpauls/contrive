using UnityEngine;
using System.Collections;

public class TeleopControl : MonoBehaviour {
	MotorSet leftMotor, rightMotor;
	IOServer ioServer;

	void Start() {
		leftMotor = transform.FindChild("left").GetComponentInChildren<MotorSet>();
		rightMotor = transform.FindChild("right").GetComponentInChildren<MotorSet>();
		ioServer = GameObject.Find("Server").GetComponent<IOServer>();
	}

	// Update is called once per frame
	void FixedUpdate () {
		// only run on teleop mode
		if (ioServer.CurControlMode != IOServer.ControlMode.Teleop) {
			return;
		}
		float y = Input.GetAxis("Vertical");
		float x = Input.GetAxis("Horizontal");
		float left = Mathf.Clamp(y + x, -1f, 1f);
		float right = Mathf.Clamp(y - x, -1f, 1f);
		leftMotor.ApplyVoltage(left);
		rightMotor.ApplyVoltage(right);
	}
}
