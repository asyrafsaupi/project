#include <snmp_pp.h>
#include <iostream.h>
#include <stdio.h>
#include <ftw.h>
#include <sys/stat.h>

void send_trap()
{
	int status;

	CTarget target((IpAddress)"10.4.8.5");
	Pdu pdu;

	Snmp snmp(status);
	
	if(status != SNMP_CLASS_SUCCESS) {
		cout<<snmp.error_msg(status);
		return;
	}
	
	status = snmp.trap(pdu, target, coldStart);
	cout<<"Trap Send Status = "<<snmp.error_msg(status);
};
