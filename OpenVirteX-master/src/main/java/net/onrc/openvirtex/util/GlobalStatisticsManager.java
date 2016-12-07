package net.onrc.openvirtex.util;

import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.link.PhysicalLink;
import net.onrc.openvirtex.elements.network.PhysicalNetwork;
import net.onrc.openvirtex.elements.port.PhysicalPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by noraki on 2016-12-01.
 */
public final class GlobalStatisticsManager{
    private static final Logger log = LogManager.getLogger(GlobalStatisticsManager.class.getName());

    private static PhysicalLink getLink(int linkId) {
        Set<PhysicalLink> linkSet = new HashSet<PhysicalLink>(PhysicalNetwork.getInstance().getLinks());
        for (final PhysicalLink l : linkSet) {
            if (l.getLinkId()  == linkId) {return l;}
        }
        return null;
    }

    public static double getLinkUtilization(int linkId){
        PhysicalLink l = getLink(linkId);
        if (l == null) {return -1;}

        PhysicalSwitch srcSwitch = l.getSrcSwitch();
        PhysicalPort srcPort = l.getSrcPort();
        short srcPortNum = srcPort.getPortNumber();
        int linkMaxBw = srcPort.getCurrentThroughput(); //in Mbps
        double linkUtil = 0;
        try{
            double portBw = srcSwitch.getPortBw(srcPortNum); // in Bps
            linkUtil = (portBw /1000000) / linkMaxBw; //both in Mbps
            log.debug("PortBw: {}, linkMaxBw: {}", portBw, linkMaxBw);
        }
        catch (Exception e){
            linkUtil = 0;
            e.printStackTrace();
        }
        return linkUtil;
    }
}