using UnityEngine;
using System.Collections;
using SimpleJSON;

public interface ActuatorType {
	void HandleMessage(JSONNode message);
	void Reset();
}
