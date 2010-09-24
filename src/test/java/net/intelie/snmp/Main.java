package net.intelie.snmp;

/**
 * Created by IntelliJ IDEA.
 * User: anna
 * Date: Sep 23, 2010
 * Time: 6:26:57 PM
 */
public class Main {



    public static void main(String[] args){
        SNMP4JHelper s = new SNMP4JHelper();
        try{
            String strIPAddress = "192.168.42.1";
            SNMP4JHelper objSNMP = new SNMP4JHelper();
            int Value = 2;
            //objSNMP.snmpSet(strIPAddress, SNMP4JHelper.WRITE_COMMUNITY, SNMP4JHelper.OID_UPS_OUTLET_GROUP1, Value);
            //String batteryCap = objSNMP.snmpGet(strIPAddress, SNMP4JHelper.READ_COMMUNITY, SNMP4JHelper.OID_UPS_BATTERY_CAPACITY);
            String CPULoad = objSNMP.snmpGet(strIPAddress, SNMP4JHelper.READ_COMMUNITY, SNMP4JHelper.OID_CPU_LOAD_1_MINUTE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
