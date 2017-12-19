//package com.G2.SNMP.Trap.receiver;

import java.io.IOException;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;


public class TrapReceiver implements CommandResponder {
	public static void main(String[] args) {
		TrapReceiver snmp4jTrapReceiver = new TrapReceiver();
		try {
			snmp4jTrapReceiver.listen(new UdpAddress("192.168.60.133/162"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public synchronized void listen(TransportIpAddress address)
			throws IOException {
		AbstractTransportMapping transport;
		if(address instanceof TcpAddress) {
			transport = new DefaultTcpTransportMapping((TcpAddress) address);
		} else {
			transport = new DefaultUdpTransportMapping((UdpAddress) address);
		}

		ThreadPool threadPool = ThreadPool.create("DispatcherPool",10);
		MessageDispatcher mDispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());

		mDispatcher.addMessageProcessingModel(new MPv1());
		mDispatcher.addMessageProcessingModel(new MPv2c());

		SecurityProtocols.getInstance().addDefaultProtocols();
		SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));

		Snmp snmp = new Snmp(mDispatcher, transport);
		snmp.addCommandResponder(this);

		transport.listen();
		System.out.println("Listening on " + address);

		try {
			this.wait();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}


	public synchronized void processPdu(CommandResponderEvent cmdRespEvent) {
		System.out.println("Received PDU...");
		PDU pdu = cmdRespEvent.getPDU();
		if (pdu != null) {
			System.out.println("Trap Type = " + pdu.getType());
			System.out.println("Variables = " + pdu.getVariableBindings());
		}
	}
}
		
