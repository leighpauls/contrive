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
			// get the wheel's speed in the plane of the wheel
			wheelSpeed += Vector3.RotateTowards(
				Vector3.Project(wheel.rigidbody.velocity, wheel.transform.forward), 
				Vector3.forward,
				Mathf.PI,
				0f).z / wheels.Length;
			netMaxForce += wheel.MaxFrictionForce;
		}

		Debug.Log("Wheel Speed: " + wheelSpeed + " My Speed: "
		          + Vector3.RotateTowards(
						Vector3.Project(rigidbody.velocity, transform.right),
						Vector3.forward,
						Mathf.PI,
						0f).z);
		Debug.Log("Max force: " + netMaxForce);

		float forceInput = stallForce * (voltage - wheelSpeed / freeSpinSpeed);

		if (Mathf.Abs(forceInput) > netMaxForce) {
			foreach (var wheel in wheels) {
				wheel.HasForwardSlip = true;
				wheel.ForwardSlipSpeed = -Mathf.Sign(wheelSpeed) * freeSpinSpeed * wheel.MaxFrictionForce / stallForce;
				wheel.ForwardForce = -Mathf.Sign(wheelSpeed) * wheel.MaxFrictionForce;
			}
		} else {
			foreach (var wheel in wheels) {
				wheel.ForwardSlipSpeed = 0f;
				wheel.ForwardForce = wheel.MaxFrictionForce / netMaxForce * forceInput;
				wheel.HasForwardSlip = false;
			}
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
