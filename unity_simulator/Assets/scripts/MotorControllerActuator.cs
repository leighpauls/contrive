using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

public class MotorControllerActuator : MonoBehaviour, ActuatorType {
	MotorSet rightMotor, leftMotor;
	// Use this for initialization
	void Start () {
		// Tell the ioserver about myself
		GetComponent<IOServer>().RegisterActuatorType("motor_controller", this);
		// get all of the motors
		GameObject robot = GameObject.Find("robot");
		leftMotor = robot.transform.FindChild("left").GetComponentInChildren<MotorSet>();
		rightMotor = robot.transform.FindChild("right").GetComponentInChildren<MotorSet>();
	}

	public void handleMessage(Dictionary<string, object> message) {
		Dictionary<string, object> data = (Dictionary<string, object>) message["data"];
		Debug.Log(data["slot"].GetType());
		long slot = (long) data["slot"];
		long channel = (long) data["channel"];
		double power = (double) data["power"];

		// TODO: drive this from a config file
		if (slot == 1 && channel == 1) {
			leftMotor.ApplyVoltage((float)power);
		} else if (slot == 1 && channel == 2) {
			rightMotor.ApplyVoltage((float)power);
		}
	}
}
