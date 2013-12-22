using UnityEngine;
using System.Collections;

public class WheelScript : MonoBehaviour {

	public enum WheelType {
		Left,
		Right
	};

	public WheelType? wheelType {private get; set; }

	void Start() {
		if (wheelType == null) {
			throw new UnityException("Wheel Type not set");
		}
	}

	// Update is called once per frame
	void Update () {

		WheelCollider wheelCollider = (WheelCollider) this.collider;

		var keyControls = transform
			.parent	
			.gameObject
			.GetComponentInChildren<RobotKeyControl>();
		float y = keyControls.ForwardForce;
		float x = keyControls.TurningForce;

		float normalOut = y + (wheelType.Equals(WheelType.Left) ? x : -x);
		normalOut = Mathf.Max(-1.0f, Mathf.Min(1.0f, normalOut));

		const float maxWheelTorque = 100f;
		wheelCollider.motorTorque = normalOut * maxWheelTorque;
	}
}
