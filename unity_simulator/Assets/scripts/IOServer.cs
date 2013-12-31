using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Text;
using SimpleJSON;

public class IOServer : MonoBehaviour {

	private Thread thread;

	private byte[] inboundBuffer = new byte[1024*10];
	private Socket clientSocket;
	private Semaphore clientNeeded;

	private Dictionary<string, ActuatorType> actuatorTypes;
	private Dictionary<string, SensorType> sensorTypes;

	private JSONClass endOfSensorsMessage;
	private const string endOfActuatorsType = "end_of_actuators";

	public enum ControlMode {
		Disabled,
		Teleop,
		Auto
	};

	public ControlMode CurControlMode { get; private set; }

	// Use this for initialization
	void Start () {
		Application.runInBackground = true;
	
		thread = new Thread(new ThreadStart(this.ServerThread));
		clientNeeded = new Semaphore(1, 1);

		CurControlMode = ControlMode.Teleop;
		actuatorTypes = new Dictionary<string, ActuatorType>();
		sensorTypes = new Dictionary<string, SensorType>();

		endOfSensorsMessage = new JSONClass();
		endOfSensorsMessage.Add("type", "end_of_sensors");
		endOfSensorsMessage.Add("data", new JSONClass());

		thread.Start();
	}

	void FixedUpdate() {
		if (clientSocket == null) {
			return;
		}

		try {
			// send the sensor states to the user code
			string outboundBufferString = "";
			foreach (var entry in sensorTypes) {
				string typeName = entry.Key;
				JSONClass[] states = entry.Value.GetSensorStates();
				foreach (var state in states) {
					JSONClass msg = new JSONClass();
					msg.Add("type", typeName);
					msg.Add("data", state);
					outboundBufferString += msg.ToString() + "\n";
				}
			}
			outboundBufferString += endOfSensorsMessage.ToString() + "\n";
			clientSocket.Send(Encoding.ASCII.GetBytes(outboundBufferString));

			// wait for all of the actuator messages
			List<JSONClass> receivedActuatorMessages = new List<JSONClass>();
			bool endReached = false;
			while (!endReached) {
				int numBytes = clientSocket.Receive(inboundBuffer);
				string[] lines = Encoding.ASCII
					.GetString(inboundBuffer, 0, numBytes)
					.Split("\n".ToCharArray(), System.StringSplitOptions.RemoveEmptyEntries);
				foreach (string line in lines) {
					JSONClass message = (JSONClass) JSON.Parse(line);
					if (message == null) {
						continue;
					}
					if (endOfActuatorsType.Equals(message["type"].Value)) {
						endReached = true;
						break;
					} else {
						receivedActuatorMessages.Add(message);
					}
				}
			}

			// dispatch all of the actuator messages
			foreach (JSONClass msg in receivedActuatorMessages) {
				actuatorTypes[msg["type"]].HandleMessage(msg);
			}
		} catch (SocketException e) {
			// the socket has disconnected or timed out
			Debug.Log("Client disconnected or timed out: " + e.Message);
			clientSocket.Close();
			clientSocket = null;
			clientNeeded.Release();
		}
	}

	void ServerThread() {
		// get a new connection off of the socket
		Debug.Log("Started server thread");
		Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		listener.Bind(new IPEndPoint(Dns.GetHostEntry(Dns.GetHostName()).AddressList[0], 54321));
		listener.Listen(0);
		Debug.Log("Opened Listener");
		
		while (true) {
			clientNeeded.WaitOne();
			Socket soc = listener.Accept();
			// soc.ReceiveTimeout = 1000;
			// soc.SendTimeout = 1000;
			Debug.Log("Accepted Client");
			clientSocket = soc;
		}
	}

	
	public void RegisterActuatorType(string actuatorName, ActuatorType actuatorType) {
		Debug.Log ("Registered actuator: " + actuatorName);
		actuatorTypes[actuatorName] = actuatorType;
	}

	public void RegisterSensorType(string sensorName, SensorType sensorType) {
		Debug.Log("Registered sensor: " + sensorType);
		sensorTypes[sensorName] = sensorType;
	}

	public void Reset() {
		foreach (KeyValuePair<string, ActuatorType> entry in actuatorTypes) {
			entry.Value.Reset();
		}
		foreach (KeyValuePair<string, SensorType> entry in sensorTypes) {
			entry.Value.Reset();
		}

	}
}
