package net.intelie.snmp; /**
 * Created by IntelliJ IDEA.
 * User: anna
 * Date: Sep 23, 2010
 * Time: 5:36:22 PM
 * To change this template use File | Settings | File Templates.
 */

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMP4JHelper {

    public static final String READ_COMMUNITY = "public";
    public static final String WRITE_COMMUNITY = "private";
    public static final int mSNMPVersion = 0;
    //public static final String OID_UPS_OUTLET_GROUP1 = "1.3.6.1.4.1.318.1.1.1.12.3.2.1.3.1";
    //public static final String OID_UPS_BATTERY_CAPACITY = "1.3.6.1.4.1.318.1.1.1.2.2.1.0";
    public static final String OID_CPU_LOAD_1_MINUTE = "1.3.6.1.4.1.2021.10.1.3.1";
    //public static final String OID_CPU_IDLE = "1.3.6.1.4.1.2021.11.11.0";
    public static final String SNMP_PORT = "161";

    public static void main(String[] args){
        try{
            String strIPAddress = "192.168.42.1";
            SNMP4JHelper objSNMP = new SNMP4JHelper();
            int Value = 2;
            //objSNMP.snmpSet(strIPAddress, WRITE_COMMUNITY, OID_UPS_OUTLET_GROUP1, Value);
            //String batteryCap = objSNMP.snmpGet(strIPAddress, READ_COMMUNITY, OID_UPS_BATTERY_CAPACITY);
            String CPULoad = objSNMP.snmpGet(strIPAddress, READ_COMMUNITY, OID_CPU_LOAD_1_MINUTE);
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }

/*    public void snmpSet(String strAddress, String community, String strOID, int Value){
        
        strAddress = strAddress + "/"+ SNMP_PORT;
        Address targetAddress = GenericAddress.parse(strAddress);
        Snmp snmp;
        try{
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(5000);
            target.setVersion(SnmpConstants.version1);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(strOID), new Integer32(Value)));
            pdu.setType(PDU.SET);

            ResponseListener listener = new ResponseListener(){
                public void onResponse(ResponseEvent event){
                    ((Snmp)event.getSource()).cancel(event.getRequest(),this);
                }
            };
            snmp.send(pdu,target, null, listener);
            snmp.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }      */

    public String snmpGet(String strAddress, String community, String strOID){
        String str = "";
        try{
            OctetString com = new OctetString(community);
            strAddress = strAddress + "/" + SNMP_PORT;
            Address targetAddress = new UdpAddress(strAddress);
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(com);
            comtarget.setVersion(SnmpConstants.version1);
            comtarget.setAddress(targetAddress);
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);

            PDU pdu = new PDU();
            ResponseEvent response;
            Snmp snmp;
            pdu.add(new VariableBinding(new OID(strOID)));
            pdu.setType(PDU.GET);
            snmp = new Snmp(transport);
            response = snmp.get(pdu,comtarget);
            if (response != null){
                if(response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")){
                    PDU pduresponse = response.getResponse();
                    str = pduresponse.getVariableBindings().firstElement().toString();
                    if(str.contains("=")){
                        int len = str.indexOf("=");
                        str = str.substring(len+1,str.length());
                    }
                }
            }
            else {
                System.out.println("TIMEOUT");
            }
            snmp.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("RESPONSE = " + str);
        return str;
    }
}
