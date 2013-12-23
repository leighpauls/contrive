using UnityEngine;
using System.Collections;

public class RobotKeyControl : MonoBehaviour {
	Wheel[] leftWheels;
	Wheel[] rightWheels;

	const float stallTorqueNm = 67.23f;
	const float freeSpeedRps = 3.18f;
	const float springTorquePerRot = 1000f;

	void Start() {
		leftWheels = transform.FindChild("left").GetComponentsInChildren<Wheel>();
		rightWheels = transform.FindChild("right").GetComponentsInChildren<Wheel>();
	}

	void applyVoltageToWheels(float voltage, Wheel[] wheels) {
		Wheel front = wheels[0];
		Wheel back = wheels[1];
		float speed = (front.Speed + back.Speed) / 2f;
		float torqueOutput = stallTorqueNm * (voltage  - speed / freeSpeedRps);

		/*
		float positionDiff = front.Position - back.Position;
		float frontTorque = (springTorquePerRot * positionDiff + torqueOutput) / 2f;
		float backTorque = torqueOutput - frontTorque;
		front.SetTorque(frontTorque);
		back.SetTorque(backTorque);
		*/

		back.SetTorque(torqueOutput);
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
