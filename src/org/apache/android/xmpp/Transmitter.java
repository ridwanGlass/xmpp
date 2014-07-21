package org.apache.android.xmpp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

public class Transmitter extends Activity 
{

	private ArrayList<String> messages = new ArrayList<String>();
	private Handler mHandler = new Handler();
	private SettingsDialogTransmitter mDialog;
	private EditText mSendText;
	private ListView mList;
	private XMPPConnection connection;

	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.main_parent);

		mSendText = (EditText) this.findViewById(R.id.sendText);
		Log.i("XMPPClientPar", "mSendText = " + mSendText);
		mList = (ListView) this.findViewById(R.id.listMessages);
		Log.i("XMPPClientPar", "mList = " + mList);
		setListAdapter();

		// Dialog for getting the xmpp settings
		mDialog = new SettingsDialogTransmitter(this);

		SettingsDialogTransmitter.start();
		
		sendMessage();

		// Set a listener to send a chat text message
		Button send = (Button) this.findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view) 
			{
				sendMessage();
			}
		});
	}

	
	public void sendMessage()
	{
		String to = "quazi.hasib13@gmail.com";
		String text = "auto1111";//mSendText.getText().toString();

		Log.i("XMPPClientPar", "Sending text [" + text + "] to [" + to
				+ "]");
		Message msg = new Message(to, Message.Type.chat);
		msg.setBody(text);
		connection.sendPacket(msg);
		messages.add(connection.getUser() + ":");
		messages.add(text);
		setListAdapter();
	}
	
	public void setConnection(XMPPConnection connection)
	{
		this.connection = connection;
		if (connection != null)
		{
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() 
			{
				public void processPacket(Packet packet) 
				{
					Message message = (Message) packet;
					if (message.getBody() != null) 
					{
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.i("XMPPClientPar", "Got text [" + message.getBody()
								+ "] from [" + fromName + "]");
						messages.add(fromName + ":");
						messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() 
						{
							public void run()
							{
								setListAdapter();
							}
						});
					}
				}
			}, filter);
		}
	}

	private void setListAdapter()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.multi_line_list_item_parent, messages);
		mList.setAdapter(adapter);
	}
}
