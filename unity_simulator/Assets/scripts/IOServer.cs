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

	private Semaphore actuatorAvailable, actuatorEmpty;
	private string lastLine;

	private Mutex sensorAvailable;
	private string sensorOutput;

	private Dictionary<string, ActuatorType> actuatorTypes;
	private Dictionary<string, SensorType> sensorTypes;

	public enum ControlMode {
		Disabled,
		Teleop,
		Auto
	};

	public ControlMode CurControlMode { get; private set; }

	// Use this for initialization
	void Start () {
		Application.runInBackground = true;
		actuatorAvailable = new Semaphore(0, 1);
		actuatorEmpty = new Semaphore(1, 1);

		sensorAvailable = new Mutex();
		sensorOutput = null;

		CurControlMode = ControlMode.Teleop;
		actuatorTypes = new Dictionary<string, ActuatorType>();
		sensorTypes = new Dictionary<string, SensorType>();

		thread = new Thread(new ThreadStart(this.ServerThread));
		thread.Start();
	}

	void Update() {
		// check for actuator signals
		if (actuatorAvailable.WaitOne(0)) {
			string[] lines = lastLine.Split("\n".ToCharArray(), System.StringSplitOptions.RemoveEmptyEntries);
			foreach (var line in lines) {
				JSONNode message = JSON.Parse(line);
				string messageType = message["type"];
				actuatorTypes[messageType].HandleMessage(message);
			}
			actuatorEmpty.Release();
		}

		// send back the sensor signals
		string sensorJson = "";
		foreach (KeyValuePair<string, SensorType> entry in sensorTypes) {
			JSONNode[] states = entry.Value.GetSensorStates();
			foreach (var state in states) {
				JSONClass command = new JSONClass();
				command.Add("data", state);
				command.Add("type", entry.Key);
				sensorJson += command.ToString() + "\n";
			}
		}
		sensorAvailable.WaitOne();
		sensorOutput = sensorJson;
		sensorAvailable.ReleaseMutex();
	}

	void ServerThread() {
		// get a new connection off of the socket
		Debug.Log("Started server thread");
		Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		listener.Bind(new IPEndPoint(Dns.GetHostEntry(Dns.GetHostName()).AddressList[0], 54321));
		listener.Listen(0);
		Debug.Log("Opened Listener");

		byte[] bytes = new byte[1024*10];

		while (true) {
			Socket client = listener.Accept();
			Debug.Log("Accepted Client");
			CurControlMode = ControlMode.Auto;
			while(true) {
				// recive some data
				int bytesReceived = client.Receive(bytes);
				if (bytesReceived == 0) {
					break;
				}
				string strData = Encoding.ASCII.GetString(bytes, 0, bytesReceived);
				actuatorEmpty.WaitOne();
				lastLine = strData;
				actuatorAvailable.Release();

				// see if there's any data to send
				sensorAvailable.WaitOne();
				if (sensorOutput != null) {
					client.Send(Encoding.ASCII.GetBytes(sensorOutput));
					sensorOutput = null;
				}
				sensorAvailable.ReleaseMutex();
			}

			Debug.Log("Socket disconnected");
			client.Close();
			CurControlMode = ControlMode.Teleop;
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
