using UnityEngine;
using System.Collections;

/// <summary>
/// Represets a collection of wheels mechancially joined (by gears, chain, etc.) such that they
/// are all controlled by the same set of motors, and are constrained to the same speed.
/// </summary>
public class MotorSet : MonoBehaviour {
	// high gear
	const float FREE_SPIN_SPEED = 4.87f;
	const float STALL_FORCE = 43.8f * 2f;

	// low gear
	// const float FREE_SPIN_SPEED = 4.87f * 6f / 16f;
	// const float STALL_FORCE = 42f * 2f * 16f / 6f;


	const float DIST_PER_TICK = 0.01f;

	const float U_STATIC_FRICTION = 0.8f;
	const float U_DYNAMIC_FRICTION = 0.4f;

	const float MIN_SLIP_VELOCITY = 0.05f;
	
	WheelCollider[] wheels;
	public float EncoderPosition { get; private set; }
	public float EncoderPeriod { get; private set; }
	public bool EncoderMovingForward { get; private set; }

	private bool slipping = false;
	float encoderSpeed = 0f;

	// Use this for initialization
	void Start () {
		wheels = GetComponentsInChildren<WheelCollider>();
		ResetSensor();
		ResetActuator();
	}

	public void ApplyVoltage(float voltage) {
		// find the maximum forward force that can be applied by each wheel
		float maxForwardStaticForce = 0f;
		float[] staticForceContributions = new float[wheels.Length];

		// average forward speed of grounded wheels
		float freeRollWheelSpeed = 0f;
		// normal-force-weighted average of grounded wheels' sideways speed magnitude
		float sideSlipMagnitude = 0f;

		float netDownForce = 0f;
		int numTouching = 0;

		float[] normalForces = new float[wheels.Length];
		float[] sideSlips = new float[wheels.Length];

		for (int i = 0; i < wheels.Length; ++i) {
			WheelCollider wheel = wheels[i];
			WheelHit hit;
			if (wheel.GetGroundHit(out hit)) {
				numTouching++;
				netDownForce += hit.force;

				float sidewaysStaticForce = hit.force * wheel.sidewaysFriction.extremumValue * (hit.sidewaysSlip / wheel.sidewaysFriction.extremumSlip);
				if (hit.sidewaysSlip > wheel.sidewaysFriction.extremumSlip) {
					sidewaysStaticForce = 0f;
				}

				float staticFrictionMagnitude = hit.force * U_STATIC_FRICTION;
				float staticForwardForce = Mathf.Sqrt(Mathf.Max(0f, staticFrictionMagnitude*staticFrictionMagnitude - sidewaysStaticForce*sidewaysStaticForce));
				maxForwardStaticForce += staticForwardForce;
				staticForceContributions[i] = staticForwardForce;

				freeRollWheelSpeed += Vector3.Dot(wheel.rigidbody.velocity, transform.forward);
				sideSlipMagnitude += Mathf.Abs(hit.sidewaysSlip) * hit.force;

				normalForces[i] = hit.force;
				sideSlips[i] = hit.sidewaysSlip;;
			} else {
				staticForceContributions[i] = 0f;
				normalForces[i] = 0f;
				sideSlips[i] = 0f;
			}
		}
		if (numTouching > 0) {
			freeRollWheelSpeed = freeRollWheelSpeed / numTouching;
			sideSlipMagnitude = sideSlipMagnitude / netDownForce;
		} else {
			// TODO: set the encoder speed
			slipping = true;
			return;
		}

		// how much force would the motor apply if in the static range
		float staticForceInput = STALL_FORCE * (voltage - freeRollWheelSpeed / FREE_SPIN_SPEED);

		float forwardSlipSpeed = 0f;;
		// update the slipping state
		if (!slipping) {
			if (Mathf.Abs(staticForceInput) > maxForwardStaticForce || sideSlipMagnitude >= MIN_SLIP_VELOCITY) {
				slipping = true;
			}
		}

		if (slipping) {
			// resolve how much dynamic forward force each wheel uses
			WheelSlipFunction slipFunction = new WheelSlipFunction {
				normalForce = normalForces,
				sideSlip = sideSlips,
				uDynamic = U_DYNAMIC_FRICTION,
				stallForce = STALL_FORCE,
				axelSpeed = freeRollWheelSpeed,
				freeSpinSpeed = FREE_SPIN_SPEED,
				voltage = voltage
			};
		
			float slipDirection = Mathf.Sign(voltage - freeRollWheelSpeed / FREE_SPIN_SPEED);
			forwardSlipSpeed = NumericalSolver.Solve(
				slipFunction,
				Mathf.Min(0f, slipDirection * FREE_SPIN_SPEED),
				Mathf.Max(0f, slipDirection * FREE_SPIN_SPEED),
				10,
				0.001f);
			if (Mathf.Abs(forwardSlipSpeed) < MIN_SLIP_VELOCITY && sideSlipMagnitude < MIN_SLIP_VELOCITY) {
				slipping = false;
			}
		}

		// update the force applications
		if (slipping) {
			encoderSpeed = forwardSlipSpeed + freeRollWheelSpeed;

			for (int i = 0; i < wheels.Length; ++i) {
				WheelCollider wheel = wheels[i];
				WheelFrictionCurve friction = wheel.sidewaysFriction;
				friction.stiffness = 0f;
				wheel.sidewaysFriction = friction;

				float sideSlip = sideSlips[i];
				float normalForce = normalForces[i];

				Vector3 slipVelocity = -forwardSlipSpeed * transform.forward + sideSlip * transform.right;
				Vector3 frictionForce = -slipVelocity.normalized * normalForce * U_DYNAMIC_FRICTION;
				wheel.rigidbody.AddForce(frictionForce);

			}
		} else {
			encoderSpeed = freeRollWheelSpeed;
			for (int i = 0; i < wheels.Length; ++i) {
				WheelCollider wheel = wheels[i];
				WheelFrictionCurve friction = wheel.sidewaysFriction;
				friction.stiffness = 100f;
				wheel.sidewaysFriction = friction;;

				if (!Mathf.Approximately(maxForwardStaticForce, 0f)) {
					float staticFrictionForce = (staticForceContributions[i] / maxForwardStaticForce) * staticForceInput;
					if (float.IsNaN(staticFrictionForce)) {
						Debug.Log(maxForwardStaticForce);
					}
					wheel.rigidbody.AddForce(staticFrictionForce * transform.forward);
				}
			}
		}
	}

	class WheelSlipFunction : NumericalSolver.FunctionToSolve {
		public float[] normalForce, sideSlip;
		public float uDynamic, stallForce, axelSpeed, freeSpinSpeed, voltage;

		public float function(float fwdSlip) {
			float res = stallForce * ((fwdSlip + axelSpeed) / freeSpinSpeed - voltage);

			for (int i = 0; i < normalForce.Length; ++i) {
				res += uDynamic * normalForce[i] * Mathf.Cos(Mathf.Atan2(sideSlip[i], fwdSlip));
			}

			return res;
		}
	}

	void FixedUpdate() {
		EncoderPosition += encoderSpeed * Time.fixedDeltaTime;
		if (Mathf.Approximately(encoderSpeed, 0f)) {
			// it hasn't moved at all this frame
			EncoderPeriod += Time.fixedDeltaTime;
		} else {
			EncoderMovingForward = encoderSpeed > 0f;
			EncoderPeriod = Mathf.Abs(DIST_PER_TICK / encoderSpeed);
		}
		
	}

	public void ResetSensor() {
		EncoderPosition = 0f;
		EncoderPeriod = 0.00001f;
		EncoderMovingForward = true;
	}

	public void ResetActuator() {
		slipping = false;
	}
}
