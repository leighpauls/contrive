using UnityEngine;
using System.Collections;

public class RobotKeyControl : MonoBehaviour {
	Wheel[] leftWheels;
	Wheel[] rightWheels;

	void Start() {
		leftWheels = transform.FindChild("left").GetComponentsInChildren<Wheel>();
		rightWheels = transform.FindChild("right").GetComponentsInChildren<Wheel>();
	}

	const float freeSpinSpeed = 3.75f;
	const float stallForce = 718f;

	void applyVoltageToWheels(float voltage, Wheel[] wheels) {
		float wheelSpeed = 0f;
		float netMaxForce = 0f;
		foreach (var wheel in wheels) {
			// get the wheel's forward speed
			Vector3 forwardDir = wheel.transform.rotation * Vector3.forward;
			float forwardSpeed = Vector3.Dot(wheel.rigidbody.velocity, forwardDir);
			wheelSpeed += forwardSpeed / wheels.Length;
			netMaxForce += wheel.MaxFrictionForce;
		}


		float staticForceInput = stallForce * (voltage - wheelSpeed / freeSpinSpeed);
		float slipSpeed = Mathf.Sign(staticForceInput) * freeSpinSpeed * (stallForce - netMaxForce) / stallForce;

		foreach (var wheel in wheels) {
			wheel.ApplyChainForce(staticForceInput, netMaxForce, slipSpeed);
		}
	}

	// Update is called once per frame
	void Update () {
		float y = 0f;
		float x = 0f;
		if (Input.GetKey(KeyCode.UpArrow)) {
			y = 1.0f;
		} else if (Input.GetKey(KeyCode.DownArrow)) {
			y = -1.0f;
		}
		if (Input.GetKey(KeyCode.LeftArrow)) {
			x = -1.0f;
		} else if (Input.GetKey(KeyCode.RightArrow)) {
			x = 1.0f;
		}
		float left = Mathf.Max(-1.0f, Mathf.Min(1.0f, y + x));
		float right = Mathf.Max(-1.0f, Mathf.Min(1.0f, y - x));
		applyVoltageToWheels(left, leftWheels);
		applyVoltageToWheels(right, rightWheels);
	}
}
