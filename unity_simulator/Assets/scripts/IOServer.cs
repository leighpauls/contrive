using UnityEngine;
using System.Collections;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Text;

public class IOServer : MonoBehaviour {

	private Thread thread;
	private Semaphore available, empty;
	string lastLine;

	// Use this for initialization
	void Start () {
		Application.runInBackground = true;
		available = new Semaphore(0, 1);
		empty = new Semaphore(1, 1);

		thread = new Thread(new ThreadStart(this.ServerThread));
		thread.Start();
	}

	void Update() {
		if (available.WaitOne(0)) {
			Debug.Log(lastLine);
			empty.Release();
		}
	}

	void ServerThread() {
		// get a new connection off of the socket
		Debug.Log("Started thread");
		Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		listener.Bind(new IPEndPoint(Dns.Resolve(Dns.GetHostName()).AddressList[0], 54321));
		listener.Listen(1);
		Debug.Log("Opened Listener");

		byte[] bytes = new byte[1024*10];

		while (true) {
			Socket client = listener.Accept();
			Debug.Log("Accepted Client");

			while(true) {
				int bytesReceived = client.Receive(bytes);
				Debug.Log("Recieved: " + bytesReceived + " bytes");
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
		}
	}
}
