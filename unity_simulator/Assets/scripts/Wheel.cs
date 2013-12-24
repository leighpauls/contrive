using UnityEngine;
using System.Collections;

public class Wheel : MonoBehaviour {
	const float uDynamic = 0.95f;
	const float uStatic = 1.95f;

	public bool HasForwardSlip {get; set;}

	private float NormalForce {
		get {
			WheelCollider wc = (WheelCollider) this.collider;
			WheelHit hit;
			if (wc.GetGroundHit(out hit)) {
				return hit.force;
			}
			return 0f;
		}
	}

	const float zeroSpeedThreshold = 0.001f;

	private float SidewaysVelocity {
		get {
			return Vector3.RotateTowards(
				Vector3.Project(rigidbody.velocity, transform.right),
				Vector3.right,
				Mathf.PI*2,
				0f).x;
		}
	}

	private bool HasSidewaysVelocity {
		get {
			return Mathf.Abs(SidewaysVelocity) > zeroSpeedThreshold;
		}
	}

	public float MaxFrictionForce {
		get {
			if (Slipping) {
				return uDynamic * NormalForce;
			}
			return uStatic * NormalForce;
		}
	}

	public float ForwardForce {get; set;}
	public float ForwardSlipSpeed {get; set;}

	private bool Slipping {
		get {
			return HasForwardSlip || HasSidewaysVelocity;
		}
	}

	const float zeroForceThreshold = 0.001f;
	
	void Update() {
		if (MaxFrictionForce < zeroForceThreshold) {
			return;
		}
		if (!Slipping) {
			Debug.Log("Stick");
			// rigidbody.velocity -= Vector3.Project(rigidbody.velocity, transform.right);
			rigidbody.AddRelativeForce(Vector3.forward * ForwardForce);
			return;
		}
		Debug.Log("Slip");
		Vector3 direction = Vector3.forward * ForwardSlipSpeed;// + Vector3.right * SidewaysVelocity;
		rigidbody.AddRelativeForce(direction.normalized * MaxFrictionForce);
	}
}
