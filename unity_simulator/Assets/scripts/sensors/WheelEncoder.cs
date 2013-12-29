using UnityEngine;
using System.Collections;
using SimpleJSON;

public class WheelEncoder : MonoBehaviour, SensorType {
	MotorSet rightMotor, leftMotor;
	
	void Start () {
		// Tell the ioserver about myself
		GetComponent<IOServer>().RegisterSensorType("motor_controller", this);
		// get all of the motors
		GameObject robot = GameObject.Find("robot");
		leftMotor = robot.transform.FindChild("left").GetComponentInChildren<MotorSet>();
		rightMotor = robot.transform.FindChild("right").GetComponentInChildren<MotorSet>();
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	private JSONNode encodeSensor(
		int aSlot,
		int aChannel, 
		int bSlot,
		int bchannel,
		MotorSet motorSet) {
		var res = new JSONClass();
		res.Add("a_slot", new JSONData(aSlot));
		res.Add("a_channel", new JSONData(aChannel));
		res.Add("b_slot", new JSONData(bSlot));
		res.Add("b_channel", new JSONData(aChannel));

		int position = (int)(motorSet.EncoderPosition / 0.01f);
		res.Add("position", new JSONData(position));

		return res;
	}
	
	public JSONNode[] GetSensorStates() {
		return new JSONNode[] {
			encodeSensor(1, 1, 1, 2, leftMotor),
			encodeSensor(1, 3, 1, 4, rightMotor)
		};
	}
}
