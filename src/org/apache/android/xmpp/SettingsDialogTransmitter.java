package org.apache.android.xmpp;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 * Gather the xmpp settings and create an XMPPConnection
 */
public class SettingsDialogTransmitter 
{
    private static Transmitter xmppClientPar;

    public SettingsDialogTransmitter(Transmitter xmppClientPar) 
    {
        this.xmppClientPar = xmppClientPar;
    }

    public static void start() 
    {
    	String username ;
        String password ;
    	
        String host = "talk.google.com";
        String port = "5222";
        String service = "gmail.com";
        username = "quazi.hasib14";
        password = "masters14";

        // Create a connection
        ConnectionConfiguration connConfig = new org.jivesoftware.smack.ConnectionConfiguration(host, Integer.parseInt(port),service);
//                new ConnectionConfiguration(host, Integer.parseInt(port), service);
        XMPPConnection connection = new XMPPConnection(connConfig);

        try 
        {
            connection.connect();
            Log.i("XMPPClientPar", "[SettingsDialogPar] Connected to " + connection.getHost());
        } 
        catch (XMPPException ex) 
        {
            Log.e("XMPPClientPar", "[SettingsDialogPar] Failed to connect to " + connection.getHost());
            Log.e("XMPPClientPar", ex.toString());
            xmppClientPar.setConnection(null);
        }
        try 
        {
            connection.login(username, password);
            Log.i("XMPPClientPar", "Logged in as " + connection.getUser());

            // Set the status to available
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            xmppClientPar.setConnection(connection);
        }
        catch (XMPPException ex)
        {
            Log.e("XMPPClientPar", "[SettingsDialogPar] Failed to log in as " + username);
            Log.e("XMPPClientPar", ex.toString());
            xmppClientPar.setConnection(null);
        }
    }

}
