using UnityEngine;
using System.Collections;

public class MotorSet : MonoBehaviour {
	const float freeSpinSpeed = 3.75f;
	const float stallForce = 718f * 0.9f;
	const float distPerTick = 0.01f;

	WheelCollider[] wheels;
	public float EncoderPosition { get; private set; }
	public float EncoderPeriod { get; private set; }
	public bool EncoderMovingForward { get; private set; }

	// Use this for initialization
	void Start () {
		wheels = GetComponentsInChildren<WheelCollider>();
		ResetSensor();
		ResetActuator();
	}

	public void ResetSensor() {
		EncoderPosition = 0f;
		EncoderPeriod = 0.00001f;
		EncoderMovingForward = true;
	}
	public void ResetActuator() {
		foreach (var wheel in wheels) {
			wheel.motorTorque = 0f;
			wheel.brakeTorque = Mathf.Infinity;
		}
	}

	private float ApproxWheelSpeed { get {
			float wheelSpeed = 0f;
			float allWheelSpeed = 0f;
			int numWheelsOnGround = 0;
			foreach (var wheel in wheels) {
				WheelHit hit;
				float speed = wheel.rpm * 2 * Mathf.PI * (1f / 60f) * wheel.radius;
				if (wheel.GetGroundHit(out hit)) {
					// If I can, only count wheels that are on the ground
					wheelSpeed += speed;
					numWheelsOnGround += 1;
				}
				allWheelSpeed += speed;
			}
			
			if (numWheelsOnGround > 0) {
				// only count wheels resisted by ground forces
				wheelSpeed = wheelSpeed / numWheelsOnGround;
			} else {
				// wheels are freespinning in the air, so count them all
				wheelSpeed = allWheelSpeed / wheels.Length;
			}
			return wheelSpeed;
		}
	}

	private int WheelsOnGround { get {
			int res = 0;
			foreach (var wheel in wheels) {
				WheelHit hit;
				if (wheel.GetGroundHit(out hit)) {
					res++;
				}
			}
			return res;
		}
	}

	public void ApplyVoltage(float voltage) {
		float wheelSpeed = ApproxWheelSpeed;

		float forceInput = stallForce * (voltage - wheelSpeed / freeSpinSpeed);

		int wheelsOnGround = WheelsOnGround;
		if (wheelsOnGround == 0) {
			wheelsOnGround = wheels.Length;
		}

		foreach (var wheel in wheels) {
			float torque = (forceInput * wheel.radius) / wheelsOnGround;
			wheel.brakeTorque = 0f;
			wheel.motorTorque = torque;
		}
	}

	void Update() {
		float wheelSpeed = ApproxWheelSpeed;

		Debug.DrawRay(
			transform.position,
			transform.rotation * Vector3.forward * wheelSpeed,
			Color.white,
			0,
			false);
		foreach (var wheelCollider in wheels) {
			WheelHit hit;
			if (wheelCollider.GetGroundHit(out hit)) {
				Debug.DrawRay(
					wheelCollider.transform.position + Vector3.up * 0.1f,
					this.transform.rotation * Vector3.forward * hit.forwardSlip,
					(Mathf.Abs(hit.forwardSlip) > wheelCollider.forwardFriction.asymptoteSlip ? Color.red : Color.green),
					0,
					false);
				Debug.DrawRay(
					wheelCollider.transform.position + Vector3.up * 0.1f,
					this.transform.rotation * Vector3.right * hit.sidewaysSlip,
					(Mathf.Abs(hit.sidewaysSlip) > wheelCollider.sidewaysFriction.asymptoteSlip ? Color.red : Color.green),
					0,
					false);
			}
		}
		
		EncoderPosition += wheelSpeed * Time.deltaTime;
		if (Mathf.Approximately(wheelSpeed, 0f)) {
			// it hasn't moved at all this frame
			EncoderPeriod += Time.deltaTime;
		} else {
			EncoderMovingForward = wheelSpeed > 0f;
			EncoderPeriod = Mathf.Abs(distPerTick / wheelSpeed);
		}

	}
}
