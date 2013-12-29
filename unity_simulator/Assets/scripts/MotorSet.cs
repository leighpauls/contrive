using UnityEngine;
using System.Collections;

public class MotorSet : MonoBehaviour {
	const float freeSpinSpeed = 3.75f;
	const float stallForce = 718f;

	Wheel[] wheels;
	public float EncoderPosition { get; private set; }

	// Use this for initialization
	void Start () {
		wheels = GetComponentsInChildren<Wheel>();
		EncoderPosition = 0f;
	}

	public void ApplyVoltage(float voltage) {
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
		// FIXME: this causes a signularity when slowing to a stop out of a corner
		float slipSpeed = Mathf.Sign(staticForceInput) * freeSpinSpeed * (stallForce - netMaxForce) / stallForce;

		foreach (var wheel in wheels) {
			wheel.ApplyChainForce(staticForceInput, netMaxForce, slipSpeed);
		}
	}

	void Update() {
		float encoderSpeed = 0f;
		foreach (var wheel in wheels) {
			encoderSpeed += wheel.EncoderSpeed / wheels.Length;
			// draw encoder speed
			Debug.DrawRay(
				wheel.transform.position + Vector3.up * 0.2f,
				(wheel.transform.rotation * Vector3.forward) * wheel.EncoderSpeed,
				Color.white,
				0,
				false);
			// draw axel speed
			Debug.DrawRay(
				wheel.transform.position + Vector3.up * 0.4f,
				wheel.rigidbody.velocity,
				Color.blue,
				0,
				false);
		}
		EncoderPosition += encoderSpeed * Time.deltaTime;
	}
}
