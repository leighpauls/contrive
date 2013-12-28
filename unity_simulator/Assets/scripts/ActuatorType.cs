using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public interface ActuatorType {

	void handleMessage(Dictionary<string, object> message);
}
