using UnityEngine;
using System.Collections;
using SimpleJSON;

public interface ActuatorType {
	void HandleMessage(JSONClass message);
	void Reset();
}
