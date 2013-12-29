using UnityEngine;
using System.Collections;

public class Wheel : MonoBehaviour {
	const float uDynamic = 0.95f;
	const float uStatic = 1.95f;
	const float zeroSpeedThreshold = 0.1f;
	const float zeroForceThreshold = 0.01f;

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
			forwardSlip = 0f;
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

		// resolve slipping as usual
		forwardSlip = slipSpeed;
		Vector2 slipDirection = slipVelocity.normalized;
		forwardForce = slipDirection.x * MaxFrictionForce;
		sidewaysForce = slipDirection.y * MaxFrictionForce;
	}

	public float EncoderSpeed {
		get { return Vector3.Dot(this.rigidbody.velocity, transform.rotation * Vector3.forward) + forwardSlip; }
	}

	void Update() {
		Vector3 relativeForce = Vector3.forward * forwardForce + Vector3.right * sidewaysForce;
		rigidbody.AddRelativeForce(relativeForce);
		// draw the wheels force applied to the robot
		Debug.DrawRay(transform.position, this.transform.rotation * relativeForce * 0.01f, Color.green, 0, false);
	}
}
