using UnityEngine;
using System;
using SimpleJSON;

public class ResetActuator : MonoBehaviour, ActuatorType {

	void Start() {
		FindObjectOfType<IOServer>().RegisterActuatorType("reset", this);
	}

	public void HandleMessage(JSONClass message) {
		JSONClass msgData = (JSONClass) message["data"];
		Transform robotTransform = GameObject.Find("robot").transform;
		robotTransform.position = new Vector3(
			msgData["x"].AsFloat,
			msgData["y"].AsFloat,
			msgData["z"].AsFloat);
		robotTransform.rotation = Quaternion.Euler(
			msgData["pitch"].AsFloat,
			msgData["yaw"].AsFloat,
			msgData["roll"].AsFloat);

		FindObjectOfType<IOServer>().Reset();
	}

	public void Reset() {

	}
}

