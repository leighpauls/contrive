using UnityEngine;
using System.Collections;
using SimpleJSON;

public interface SensorType {
	/// <returns>Objects to put in the "data" subfield for this sensor</returns>
	JSONNode[] GetSensorStates();
	void Reset();
}
