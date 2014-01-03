using UnityEngine;
using System.Collections;

public class RobotBuilder : MonoBehaviour {

	// Use this for initialization
	void Start () {
		GameObject[] drives = new GameObject[] {
			transform.FindChild("left").gameObject,
			transform.FindChild("right").gameObject
		};

		Vector3[] offsets = new Vector3[] {
			new Vector3(0f, 0.015f, 0.4f),
			new Vector3(0f, 0.005f, 0f),
			new Vector3(0f, 0.015f, -0.4f)
		};

		foreach (var drive in drives) {
			foreach (var offset in offsets) {
				GameObject newWheel = (GameObject)Instantiate(
					Resources.Load("robot_wheel"),
					drive.transform.position,
					drive.transform.rotation);
				newWheel.transform.parent = drive.transform;
				newWheel.transform.position += newWheel.transform.rotation * offset;
				newWheel.GetComponent<ConfigurableJoint>().connectedBody = this.rigidbody;
			}
		}

		rigidbody.centerOfMass = new Vector3(0f, 0.15f, -0.2f);
	}
}
