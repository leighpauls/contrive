using UnityEngine;
using System.Collections;

public class Wheel : MonoBehaviour {
	const float uDynamic = 0.95f;
	const float uStatic = 1.95f;
	const float zeroSpeedThreshold = 0.001f;
	const float zeroForceThreshold = 0.001f;

	private float forwardForce, sidewaysForce;
	private float forwardSlip;
	private bool wasSlipping;

	private float NormalForce {
		get {
			WheelHit hit;
			if (((WheelCollider)collider).GetGroundHit(out hit)) {
				return hit.force;
			}
			return 0f;
		}
	}

	private float SidewaysVelocity { get { return Vector3.Dot(transform.rotation * Vector3.right, rigidbody.velocity); } }
	
	private bool Slipping { get { return SidewaysVelocity > zeroSpeedThreshold || forwardSlip > zeroSpeedThreshold; } }

	public float MaxFrictionForce { get { return NormalForce * (Slipping ? uDynamic : uStatic); } }

	public void ApplyChainForce(float staticForceInput, float netMaxForce, float slipSpeed) {
		if (MaxFrictionForce < zeroForceThreshold) {
			// not touching the ground
			forwardForce = 0f;
			sidewaysForce = 0f;
			forwardSlip = slipSpeed;
			return;
		}

		float forwardForceContribution = staticForceInput * MaxFrictionForce / netMaxForce;

		// it's now slipping, find the slip speed
		Vector2 slipVelocity = new Vector2(slipSpeed, -SidewaysVelocity);

		if ((!Slipping) || slipVelocity.magnitude < zeroSpeedThreshold) {
			if (Mathf.Abs(forwardForceContribution) <= MaxFrictionForce) {
				// in the static range
				rigidbody.velocity -= transform.rotation * Vector3.right * SidewaysVelocity;
				forwardForce = forwardForceContribution;
				sidewaysForce = 0f;
				forwardSlip = 0f;
				return;
			}
			// make it slip
			forwardSlip = slipSpeed;
		}

		// Debug.Log("Slip");
		// resolve slipping as usual
		forwardSlip = slipSpeed;
		Vector2 slipDirection = slipVelocity.normalized;
		forwardForce = slipDirection.x * MaxFrictionForce;
		sidewaysForce = slipDirection.y * MaxFrictionForce;
	}

	void Update() {
		rigidbody.AddRelativeForce(Vector3.forward * forwardForce + Vector3.right * sidewaysForce);
	}
}
