using UnityEngine;
using System.Collections;
using SimpleJSON;

public class Gyro : MonoBehaviour, SensorType {

	private float voltSecondsPerDegree;
	private int slot, channel;
	private float lastUpdateTime, lastYawVelocity;

	// Use this for initialization
	void Start () {
		// TODO: set from configuration
		voltSecondsPerDegree = 0.007f;
		slot = 1;
		channel = 1;

		Reset();

		GetComponent<IOServer>().RegisterSensorType("gyro", this);

	}

	public JSONClass[] GetSensorStates() {
		float curTime = Time.time;
		JSONClass data = new JSONClass();

		data.Add("slot", new JSONData(slot));
		data.Add("channel", new JSONData(channel));

		GameObject robot = GameObject.Find("robot");

		float curYawVelocity = Vector3.Dot(robot.rigidbody.angularVelocity, robot.transform.rotation * Vector3.up);
		float averageYawVelocity = (curYawVelocity + lastYawVelocity) / 2f;
		float voltageOutput = averageYawVelocity * (180f / Mathf.PI) * voltSecondsPerDegree;
		data.Add("voltage", new JSONData(voltageOutput));
		lastYawVelocity = curYawVelocity;

		data.Add("delta_time", new JSONData(curTime - lastUpdateTime));
		lastUpdateTime = curTime;

		return new JSONClass[] { data };
	}

	public void Reset() {
		lastYawVelocity = 0f;
		lastUpdateTime = Time.time;
	}
}
