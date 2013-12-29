using UnityEngine;
using System;
using System.Collections;
using SimpleJSON;

public class MotorControllerActuator : MonoBehaviour, ActuatorType {
	MotorSet rightMotor, leftMotor;

	void Start () {
		// Tell the ioserver about myself
		GetComponent<IOServer>().RegisterActuatorType("motor_controller", this);
		// get all of the motors
		GameObject robot = GameObject.Find("robot");
		leftMotor = robot.transform.FindChild("left").GetComponentInChildren<MotorSet>();
		rightMotor = robot.transform.FindChild("right").GetComponentInChildren<MotorSet>();
	}

	public void handleMessage(JSONNode message) {
		JSONNode data = message["data"];
		int slot = data["slot"].AsInt;
		int channel = data["channel"].AsInt;
		float power = data["power"].AsFloat;

		// TODO: drive this from a config file
		if (slot == 1 && channel == 1) {
			leftMotor.ApplyVoltage((float)power);
		} else if (slot == 1 && channel == 2) {
			rightMotor.ApplyVoltage((float)power);
		}
	}
}
