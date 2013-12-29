using UnityEngine;
using System.Collections;

public class MotorSet : MonoBehaviour {
	const float freeSpinSpeed = 3.75f;
	const float stallForce = 718f;
	const float distPerTick = 0.01f;

	WheelCollider[] wheels;
	public float EncoderPosition { get; private set; }
	public float EncoderPeriod { get; private set; }
	public bool EncoderMovingForward { get; private set; }

	// Use this for initialization
	void Start () {
		wheels = GetComponentsInChildren<WheelCollider>();
		EncoderPosition = 0f;
		EncoderPeriod = 0.00001f;
		EncoderMovingForward = true;
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
				wheelSpeed = wheelSpeed / numWheelsOnGround;
			} else {
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
			wheel.motorTorque = torque;
		}
	}

	void Update() {
		float wheelSpeed = ApproxWheelSpeed;
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
