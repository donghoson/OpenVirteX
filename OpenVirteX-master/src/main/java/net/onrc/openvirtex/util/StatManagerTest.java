package net.onrc.openvirtex.util;

import net.onrc.openvirtex.elements.link.PhysicalLink;
import net.onrc.openvirtex.elements.network.PhysicalNetwork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by noraki on 2016-12-05.
 */
public class StatManagerTest implements Runnable {
    private HashedWheelTimer timer = null;
    private static final Logger log = LogManager.getLogger(StatManagerTest.class.getName());
    private final int interval_sec = 5;

    private class StatsChecker implements TimerTask {
        @Override
        public void run(Timeout timeout) throws Exception {
            Set<PhysicalLink> linkSet = new HashSet<PhysicalLink>(PhysicalNetwork.getInstance().getLinks());

            for (final PhysicalLink l : linkSet) {
                try{
                    int linkId = l.getLinkId();
                    double lu = GlobalStatisticsManager.getLinkUtilization(linkId);
                    log.debug("LU for link {}: {}", linkId, lu);
                } catch (Exception e){
                    continue;
                }
            }
            timeout.getTimer().newTimeout(this, interval_sec, TimeUnit.SECONDS);
        }
    }

    public StatManagerTest(){
        this.timer = PhysicalNetwork.getTimer();
    }

    @Override
    public void run(){
        timer.newTimeout(new StatsChecker(), interval_sec, TimeUnit.SECONDS);
    }
}
