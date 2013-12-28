using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Text;
using fastJSON;

public class IOServer : MonoBehaviour {

	private Thread thread;
	private Semaphore available, empty;
	private string lastLine;
	private Dictionary<string, ActuatorType> actuatorTypes;

	public enum ControlMode {
		Disabled,
		Teleop,
		Auto
	};

	public ControlMode CurControlMode { get; private set; }

	// Use this for initialization
	void Start () {
		Application.runInBackground = true;
		available = new Semaphore(0, 1);
		empty = new Semaphore(1, 1);

		CurControlMode = ControlMode.Teleop;
		actuatorTypes = new Dictionary<string, ActuatorType>();

		thread = new Thread(new ThreadStart(this.ServerThread));
		thread.Start();
	}

	void Update() {
		if (available.WaitOne(0)) {
			string[] lines = lastLine.Split("\n".ToCharArray(), System.StringSplitOptions.RemoveEmptyEntries);
			foreach (var line in lines) {
				Dictionary<string, object> message = (Dictionary<string, object>) JSON.Instance.Parse(line);
				string messageType = (string)message["type"];
				actuatorTypes[messageType].handleMessage(message);
			}
			empty.Release();
		}
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
				int bytesReceived = client.Receive(bytes);
				if (bytesReceived == 0) {
					break;
				}
				string strData = Encoding.ASCII.GetString(bytes, 0, bytesReceived);
				empty.WaitOne();
				lastLine = strData;
				available.Release();
			}

			Debug.Log("Socket disconnected");
			client.Close();
			CurControlMode = ControlMode.Teleop;
		}
	}

	
	public void RegisterActuatorType(string actuatorName, ActuatorType actuator) {
		Debug.Log ("Registered: " + actuatorName);
		actuatorTypes[actuatorName] = actuator;
	}
}
