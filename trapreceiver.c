#include "snmp_pp.h"

void my_trap_callback ( int reason, Snmp* session, Pdu & pdu, TimeTicks &timestamp, SnmpTarget &target, void * cbd)

{
	Address *address;
	unsigned char get_cummunity[80], set_community[80];
	unsigned long timeout;
	int retry;

	if( reason == SNMP_CLASS_TRAP) {
		target.resolve_to_C(get_community, set_community, &address, timeout, retry);
		cout << "Trap Received from << address->get_printable() << "Trap Id = " <<trapid.get_printable();
	}
	else
		cout << "Trap Receive Error = " << session->error_msg(reason);
};


Snmp *snmp;
void trap_register()
{
	int status;
	snmp = new Snmp( status);
	if(( snmp == NULL) || ( status != SNMP_CLASS_SUCCESS))
		cout << "Error constructing Snmp Object\n";
	else
	{
		TargetCollection targets;
		OidCollection trapids;
		
		if( status = snmp->notify_register( trapids, targets, & my_trap_callback)) != SNMP_CLASS_SUCCESS)
			cout<< " Snmp Trap Register Error " << snmp->error_msg( status);
	}
};
