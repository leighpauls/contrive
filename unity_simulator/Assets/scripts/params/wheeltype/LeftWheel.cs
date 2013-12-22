using UnityEngine;
using System.Collections;

public class LeftWheel : MonoBehaviour {

	// Use this for initialization
	void Start () {
		GetComponent<WheelScript>().wheelType = WheelScript.WheelType.Left;
	}
}
