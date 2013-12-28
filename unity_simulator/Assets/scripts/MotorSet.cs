using UnityEngine;
using System.Collections;

public class MotorSet : MonoBehaviour {
	const float freeSpinSpeed = 3.75f;
	const float stallForce = 718f;

	Wheel[] wheels;

	// Use this for initialization
	void Start () {
		wheels = GetComponentsInChildren<Wheel>();
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
		float slipSpeed = Mathf.Sign(staticForceInput) * freeSpinSpeed * (stallForce - netMaxForce) / stallForce;

		foreach (var wheel in wheels) {
			wheel.ApplyChainForce(staticForceInput, netMaxForce, slipSpeed);
		}
	}
}
