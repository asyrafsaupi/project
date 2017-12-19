//package com.G2.SNMP.Trap.Sender;

import java.util.Date;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class TrapSenderVersion2 {

	public static final String community = "public";

	public static final String Oid = ".1.3.6.1.2.1.1.8";

	public static final String ipAddress = "192.168.60.133";

	public static final int port = 162;

	public static void main(String[] args) {
		TrapSenderVersion2 trapV2 = new TrapSenderVersion2();
		trapV2.sendTrap_Version2();
	}

	public void sendTrap_Version2() {
		try {
			TransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();

			CommunityTarget cTarget = new CommunityTarget();
			cTarget.setCommunity(new OctetString(community));
			cTarget.setVersion(SnmpConstants.version2c);
			cTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
			cTarget.setRetries(2);
			cTarget.setTimeout(5000);

			PDU pdu = new PDU();

			pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(Oid)));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ipAddress)));
			pdu.add(new VariableBinding(new OID(Oid), new OctetString("Major")));
			pdu.setType(PDU.NOTIFICATION);

			Snmp snmp = new Snmp(transport);
			System.out.println("Sending V2 Trap... Check Wheather NMS is Listening or not? ");
			snmp.send(pdu, cTarget);
			snmp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
