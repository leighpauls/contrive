using UnityEngine;
using System.Collections;

public class Wheel : MonoBehaviour {

	/**
	 * Set torque in N.m
	 */
	public void SetTorque(float torque) {
		WheelCollider wheelCollider = (WheelCollider) this.collider;
		wheelCollider.motorTorque = torque;
	}

	/**
	 * Rotation speed in rps
	 */
	public float Speed {
		get { return ((WheelCollider) this.collider).rpm / 60f;	}
	}

	public float Position { get; private set; }

	void Start() {
		Position = 0f;
	}

	void Update() {
		Position += Speed * Time.deltaTime;
	}
}
