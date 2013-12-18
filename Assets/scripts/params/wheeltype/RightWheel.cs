using UnityEngine;
using System.Collections;

public class RightWheel : MonoBehaviour {

	// Use this for initialization
	void Start () {
		GetComponent<WheelScript>().wheelType = WheelScript.WheelType.Right;
	}
}
