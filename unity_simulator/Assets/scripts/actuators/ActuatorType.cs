using UnityEngine;
using System.Collections;
using SimpleJSON;

public interface ActuatorType {

	void handleMessage(JSONNode message);
}
