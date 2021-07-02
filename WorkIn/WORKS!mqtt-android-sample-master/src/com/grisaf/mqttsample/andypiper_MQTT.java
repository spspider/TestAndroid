package com.grisaf.mqttsample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.sql.Timestamp;

import static com.grisaf.mqttsample.MQTT_Service_depricated.DEBUG_TAG;
import static android.content.ContentValues.TAG;

/**
 * Created by sp_1 on 15.11.2016.
 */

public class andypiper_MQTT extends Service {

    private Handler myhandler;
    //public andypiper_MQTT client;
    //private final Context context;
    int state = BEGIN;

    static final int BEGIN = 0;
    static final int CONNECTED = 1;
    static final int PUBLISHED = 2;
    static final int SUBSCRIBED = 3;
    static final int DISCONNECTED = 4;
    static final int FINISH = 5;
    static final int ERROR = 6;
    static final int DISCONNECT = 7;
    private static andypiper_MQTT instance = null;
    private Thread thread;

    public andypiper_MQTT() {

    }
/////////////////////////////////нужно создать MQTTBinder
    public class MQTTBinder extends Binder {
        public andypiper_MQTT getService(){
            return andypiper_MQTT.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();

        Log.i(DEBUG_TAG,"Received action of " + action);

        if(action == null) {
            Log.i(DEBUG_TAG,"Starting service with no action\n Probably from a crash");
        } else {
            if(action.equals("MQTTService.START")) {
                Log.i(DEBUG_TAG,"Received ACTION_START");
                //start();
            } else if(action.equals("MQTTService.STOP")) {
                //stop();
            } else if(action.equals("MQTTService.keepalive")) {
               // keepAlive();
            } else if(action.equals("Reconnect")) {

            }
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return null;
    }



    public String getThread(){

        return Long.valueOf(thread.getId()).toString();
    }

    public String check(){
        return "works";
    }
    public boolean isConnected() {
        if (client!=null)
       return client.isConnected();
        else
            return false;
    }
    public synchronized static andypiper_MQTT getInstance()
    {
        if (instance == null) {
            instance = new andypiper_MQTT();
        }

        return instance;
    }

    //andypiper_MQTT.MqttConnector.

    /**
     * The main entry point of the sample.
     *
     * This method handles parsing the arguments specified on the
     * command-line before performing the specified action.
     */

    public static void main(String[] args) {

        // Default settings:
        boolean quietMode 	= false;
        String action 		= "publish";
        String topic 		= "";
        String message 		= "Message from async calback MQTTv3 Java client sample";
        int qos 			= 2;
        String broker 		= "m20.cloudmqtt.com";
        int port 			= 16238;
        String clientId 	= null;
        String subTopic		= "Sample/#";
        String pubTopic 	= "Sample/Java/v3";
        boolean cleanSession = true;			// Non durable subscriptions
        boolean ssl = false;
        String password = "5506487";
        String userName = "spspider";


        // Parse the arguments -
        for (int i=0; i<args.length; i++) {
            // Check this is a valid argument
            if (args[i].length() == 2 && args[i].startsWith("-")) {
                char arg = args[i].charAt(1);
                // Handle arguments that take no-value
                switch(arg) {
                    case 'h': case '?':	printHelp(); return;
                    case 'q': quietMode = true;	continue;
                }

                // Now handle the arguments that take a value and
                // ensure one is specified
                if (i == args.length -1 || args[i+1].charAt(0) == '-') {
                    System.out.println("Missing value for argument: "+args[i]);
                    printHelp();
                    return;
                }
                switch(arg) {
                    case 'a': action = args[++i];                 break;
                    case 't': topic = args[++i];                  break;
                    case 'm': message = args[++i];                break;
                    case 's': qos = Integer.parseInt(args[++i]);  break;
                    case 'b': broker = args[++i];                 break;
                    case 'p': port = Integer.parseInt(args[++i]); break;
                    case 'i': clientId = args[++i];				  break;
                    case 'c': cleanSession = Boolean.valueOf(args[++i]).booleanValue();  break;
                    case 'k': System.getProperties().put("javax.net.ssl.keyStore", args[++i]); break;
                    case 'w': System.getProperties().put("javax.net.ssl.keyStorePassword", args[++i]); break;
                    case 'r': System.getProperties().put("javax.net.ssl.trustStore", args[++i]); break;
                    case 'v': ssl = Boolean.valueOf(args[++i]).booleanValue();  break;
                    case 'u': userName = args[++i];               break;
                    case 'z': password = args[++i];               break;
                    default:
                        System.out.println("Unrecognised argument: "+args[i]);
                        printHelp();
                        return;
                }
            } else {
                System.out.println("Unrecognised argument: "+args[i]);
                printHelp();
                return;
            }
        }

        // Validate the provided arguments
        if (!action.equals("publish") && !action.equals("subscribe")) {
            System.out.println("Invalid action: "+action);
            printHelp();
            return;
        }
        if (qos < 0 || qos > 2) {
            System.out.println("Invalid QoS: "+qos);
            printHelp();
            return;
        }
        if (topic.equals("")) {
            // Set the default topic according to the specified action
            if (action.equals("publish")) {
                topic = pubTopic;
            } else {
                topic = subTopic;
            }
        }

        String protocol = "tcp://";

        if (ssl) {
            protocol = "ssl://";
        }

        String url = protocol + broker + ":" + port;

        if (clientId == null || clientId.equals("")) {
            clientId = "SampleJavaV3_"+action;
        }

        // With a valid set of arguments, the real work of
        // driving the client API can begin
        try {
            // Create an instance of the Sample client wrapper
            andypiper_MQTT sampleClient = new andypiper_MQTT(url,clientId,cleanSession, quietMode,userName,password);

            // Perform the specified action
            if (action.equals("publish")) {
                sampleClient.publish(topic,qos,message.getBytes());
            } else if (action.equals("subscribe")) {
                sampleClient.subscribe(topic,qos);
            }
        } catch(MqttException me) {
            // Display full details of any exception that occurs
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (Throwable th) {
            System.out.println("Throwable caught "+th);
            th.printStackTrace();
        }
    }

    // Private instance variables
    public MqttAsyncClient client;
    String 				brokerUrl;
    private boolean 			quietMode;
    private MqttConnectOptions conOpt;
    private boolean 			clean;
    Throwable 			ex = null;
    Object 				waiter = new Object();
    boolean 			donext = false;
    private String password;
    private String userName;





// instance variables here


    public void run (String[] args) throws Exception
    {
        // put your code here
    }
    /**
     * Constructs an instance of the sample client wrapper
     * @param brokerUrl the url to connect to
     * @param clientId the client id to connect with
     * @param cleanSession clear state at end of connection or not (durable or non-durable subscriptions)
     * @param quietMode whether debug should be printed to standard out
     * @param userName the username to connect with
     * @param password the password for the user
     * @throws MqttException
     */
    public andypiper_MQTT(String brokerUrl, String clientId, boolean cleanSession,
                          boolean quietMode, String userName, String password) throws MqttException {
        this.brokerUrl = brokerUrl;
        this.quietMode = quietMode;
        this.clean 	   = cleanSession;
        this.password = password;
        this.userName = userName;
        //This sample stores in a temporary directory... where messages temporarily
        // stored until the message has been delivered to the server.
        //..a real application ought to store them somewhere
        // where they are not likely to get deleted or tampered with

        String tmpDir = System.getProperty("java.io.tmpdir");
        MemoryPersistence dataStore = new MemoryPersistence();
        try {
            // Construct the object that contains connection parameters
            // such as cleanSession and LWT
            conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(clean);
            if(password != null ) {
                conOpt.setPassword(this.password.toCharArray());
            }
            if(userName != null) {
                conOpt.setUserName(this.userName);
            }
            conOpt.setCleanSession(false);
            conOpt.setConnectionTimeout(500);
            conOpt.setKeepAliveInterval(500);
            // Construct the MqttClient instance
            client = new MqttAsyncClient(this.brokerUrl,clientId, dataStore);
            //Connection con = new Connection(brokerUrl, deviceId, brokerUrl,port, this, client, false);
            // Set this wrapper as the callback handler
            client.setCallback(new MqttEventCallback(MainActivity.getInstance().getContext()));
            //client.s
            //MainActivity.getInstance().SetStatus("Connected");
        } catch (MqttException e) {
            e.printStackTrace();
            log("Unable to set up client: "+e.toString());
            MainActivity.getInstance().SetStatus("Eroor:"+e.toString());
            System.exit(1);
        }

    }

    /**
     * Publish / send a message to an MQTT server
     * @param topicName the name of the topic to publish to
     * @param qos the quality of service to delivery the message at (0,1,2)
     * @param payload the set of bytes to send to the MQTT server
     * @throws MqttException
     */




    public void publish(String topicName, int qos, byte[] payload) throws Throwable {
        // Use a state machine to decide which step to do next. State change occurs
        // when a notification is received that an MQTT action has completed


        while (state != FINISH) {
            switch (state) {
                case BEGIN:
                    if (client.isConnected()==false){

                        // Connect using a non-blocking connect
                        MqttConnector con = new MqttConnector();
                        con.doConnect();
                        }
                    else{
                        state=CONNECTED;
                    }
                    break;
                case CONNECTED:
                    // Publish using a non-blocking publisher
                    Publisher pub = new Publisher();
                    pub.doPublish(topicName, qos, payload);
                    break;
                case PUBLISHED:
                    state = DISCONNECT;
                    donext = true;
                    break;
                case DISCONNECT:
                    //Disconnector disc = new Disconnector();
                    //disc.doDisconnect();
                    state = FINISH;
                    donext = true;
                    break;
                case ERROR:
                    throw ex;
                case DISCONNECTED:
                    state = FINISH;
                    donext = true;
                    break;
            }

//    		if (state != FINISH) {
            // Wait until notified about a state change and then perform next action
            waitForStateChange(10000);
//    		}
        }

    }

    /**
     * Wait for a maximum amount of time for a state change event to occur
     * @param maxTTW  maximum time to wait in milliseconds
     * @throws MqttException
     */
    private void waitForStateChange(int maxTTW ) throws MqttException {
        synchronized (waiter) {
            if (!donext ) {
                try {
                    waiter.wait(maxTTW);
                } catch (InterruptedException e) {
                    log("timed out");
                    e.printStackTrace();
                }

                if (ex != null) {
                    throw (MqttException)ex;
                }
            }
            donext = false;
        }
    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     * @param topicName to subscribe to (can be wild carded)
     * @param qos the maximum quality of service to receive messages at for this subscription
     * @throws MqttException
     */
    public void subscribe(String topicName, int qos) throws Throwable {
        // Use a state machine to decide which step to do next. State change occurs
        // when a notification is received that an MQTT action has completed
        while (state != FINISH) {
            switch (state) {
                case BEGIN:
                    if (client.isConnected()==false) {
                        // Connect using a non-blocking connect
                        MqttConnector con = new MqttConnector();
                        con.doConnect();
                    }
                    else {
                        state = CONNECTED;
                    }
                    break;
                case CONNECTED:
                    // Subscribe using a non-blocking subscribe
                    MainActivity.getInstance().SetStatus("Connected");
                    Subscriber sub = new Subscriber();
                    sub.doSubscribe(topicName, qos);
                    break;
                case SUBSCRIBED:
                    // Block until Enter is pressed allowing messages to arrive
                    log("Press <Enter> to exit");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        //If we can't read we'll just exit
                    }
                    //state = DISCONNECT;
                    state = FINISH;
                    donext = true;
                    break;
                case DISCONNECT:
                    MainActivity.getInstance().SetStatus("Disconect");
                    Disconnector disc = new Disconnector();
                    disc.doDisconnect();
                    break;
                case ERROR:
                    throw ex;
                case DISCONNECTED:
                    state = FINISH;
                    donext = true;
                    break;
            }

//    		if (state != FINISH && state != DISCONNECT) {
            waitForStateChange(10000);
        }
//    	}
    }

    /**
     * Utility method to handle logging. If 'quietMode' is set, this method does nothing
     * @param message the message to log
     */
    void log(String message) {
        if (!quietMode) {
            System.out.println(message);
        }
    }

    /****************************************************************/
	/* Methods to implement the MqttCallback interface              */
    /****************************************************************/


    /**
     * @see #messageArrived(String, MqttMessage)
     */



    /****************************************************************/
	/* End of MqttCallback methods                                  */
    /****************************************************************/
    static void printHelp() {
        System.out.println(
                "Syntax:\n\n" +
                        "    SampleAsyncCallBack [-h] [-a publish|subscribe] [-t <topic>] [-m <message text>]\n" +
                        "            [-s 0|1|2] -b <hostname|IP address>] [-p <brokerport>] [-i <clientID>]\n\n" +
                        "    -h  Print this help text and quit\n" +
                        "    -q  Quiet mode (default is false)\n" +
                        "    -a  Perform the relevant action (default is publish)\n" +
                        "    -t  Publish/subscribe to <topic> instead of the default\n" +
                        "            (publish: \"Sample/Java/v3\", subscribe: \"Sample/#\")\n" +
                        "    -m  Use <message text> instead of the default\n" +
                        "            (\"Message from MQTTv3 Java client\")\n" +
                        "    -s  Use this QoS instead of the default (2)\n" +
                        "    -b  Use this name/IP address instead of the default (localhost)\n" +
                        "    -p  Use this port instead of the default (1883)\n\n" +
                        "    -i  Use this client ID instead of SampleJavaV3_<action>\n" +
                        "    -c  Connect to the server with a clean session (default is false)\n" +
                        "     \n\n Security Options \n" +
                        "     -u Username \n" +
                        "     -z Password \n" +
                        "     \n\n SSL Options \n" +
                        "    -v  SSL enabled; true - (default is false) " +
                        "    -k  Use this JKS format key store to verify the client\n" +
                        "    -w  Passpharse to verify certificates in the keys store\n" +
                        "    -r  Use this JKS format keystore to verify the server\n" +
                        " If javax.net.ssl properties have been set only the -v flag needs to be set\n" +
                        "Delimit strings containing spaces with \"\"\n\n" +
                        "Publishers transmit a single message then disconnect from the server.\n" +
                        "Subscribers remain connected to the server and receive appropriate\n" +
                        "messages until <enter> is pressed.\n\n"
        );
    }

    /**
     * Connect in a non-blocking way and then sit back and wait to be
     * notified that the action has completed.
     */
    public class MqttConnector {

        //public MqttConnector() {
       // }

        public void doConnect() {
            // Connect to the server
            // Get a token and setup an asynchronous listener on the token which
            // will be notified once the connect completes
            log("Connecting to "+brokerUrl + " with client ID "+client.getClientId());

            IMqttActionListener conListener = new IMqttActionListener() {
                public void onSuccess(IMqttToken asyncActionToken) {
                    log("Connected");
                    state = CONNECTED;
                    carryOn();
                }

                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    ex = exception;
                    state = ERROR;
                    log ("connect failed" +exception);
                    carryOn();
                }

                public void carryOn() {
                    synchronized (waiter) {
                        donext=true;
                        waiter.notifyAll();
                    }
                }
            };

            try {
                // Connect using a non-blocking connect
                client.connect(conOpt,"Connect sample context", conListener);
                MainActivity.getInstance().SetStatus("Connected");
            } catch (MqttException e) {
                // If though it is a non-blocking connect an exception can be
                // thrown if validation of parms fails or other checks such
                // as already connected fail.
                state = ERROR;
                donext = true;
                ex = e;
                MainActivity.getInstance().SetStatus("Failed to Connect");
            }
        }
    }

    /**
     * Publish in a non-blocking way and then sit back and wait to be
     * notified that the action has completed.
     */
    public class Publisher {
        public void doPublish(String topicName, int qos, byte[] payload) {
            // Send / publish a message to the server
            // Get a token and setup an asynchronous listener on the token which
            // will be notified once the message has been delivered
            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);


            String time = new Timestamp(System.currentTimeMillis()).toString();
            log("Publishing at: "+time+ " to topic \""+topicName+"\" qos "+qos);

            // Setup a listener object to be notified when the publish completes.
            //
            IMqttActionListener pubListener = new IMqttActionListener() {
                public void onSuccess(IMqttToken asyncActionToken) {
                    log("Publish Completed");
                    state = PUBLISHED;
                    carryOn();
                }

                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    ex = exception;
                    state = ERROR;
                    log ("Publish failed" +exception);
                    carryOn();
                }

                public void carryOn() {
                    synchronized (waiter) {
                        donext=true;
                        waiter.notifyAll();
                    }
                }
            };

            try {
                // Publish the message
                client.publish(topicName, message, "Pub sample context", pubListener);
                MainActivity.getInstance().SetStatus("Published");
            } catch (MqttException e) {
                state = ERROR;
                donext = true;
                ex = e;
                MainActivity.getInstance().SetStatus("Publish error");
            }

        }
    }

    /**
     * Subscribe in a non-blocking way and then sit back and wait to be
     * notified that the action has completed.
     */
    public class Subscriber {
        public void doSubscribe(String topicName, int qos) {
            // Make a subscription
            // Get a token and setup an asynchronous listener on the token which
            // will be notified once the subscription is in place.
            log("Subscribing to topic \""+topicName+"\" qos "+qos);

            IMqttActionListener subListener = new IMqttActionListener() {
                public void onSuccess(IMqttToken asyncActionToken) {
                    log("Subscribe Completed");
                    state = SUBSCRIBED;
                    carryOn();
                }

                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    ex = exception;
                    state = ERROR;
                    log ("Subscribe failed" +exception);
                    carryOn();
                }

                public void carryOn() {
                    synchronized (waiter) {
                        donext=true;
                        waiter.notifyAll();
                    }
                }
            };

            try {
                client.subscribe(topicName, qos, "Subscribe sample context", subListener);
            } catch (MqttException e) {
                state = ERROR;
                donext = true;
                ex = e;
            }
            MainActivity.getInstance().SetStatus("Subscribed");
        }
    }

    /**
     * Disconnect in a non-blocking way and then sit back and wait to be
     * notified that the action has completed.
     */
    public class Disconnector {
        public void doDisconnect() {
            // Disconnect the client
            log("Disconnecting");

            IMqttActionListener discListener = new IMqttActionListener() {
                public void onSuccess(IMqttToken asyncActionToken) {
                    log("Disconnect Completed");
                    state = DISCONNECTED;
                    carryOn();
                }

                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    ex = exception;
                    state = ERROR;
                    log ("Disconnect failed" +exception);
                    carryOn();
                }
                public void carryOn() {
                    synchronized (waiter) {
                        donext=true;
                        waiter.notifyAll();
                    }
                }
            };

            try {
                client.disconnect("Disconnect sample context", discListener);
                MainActivity.getInstance().SetStatus("Disconnected");
                //client.isConnected();
            } catch (MqttException e) {
                state = ERROR;
                donext = true;
                ex = e;
            }

        }
    }


}
