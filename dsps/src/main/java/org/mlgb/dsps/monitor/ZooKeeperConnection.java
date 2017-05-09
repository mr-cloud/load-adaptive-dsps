package org.mlgb.dsps.monitor;

//import java classes
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.mlgb.dsps.utils.Consts;

public class ZooKeeperConnection {

// declare zookeeper instance to access ZooKeeper ensemble
private ZooKeeper zoo;
final CountDownLatch connectedSignal = new CountDownLatch(1);

// Method to connect zookeeper ensemble.
public ZooKeeper connect(String hostPort) throws IOException,InterruptedException {
 
   zoo = new ZooKeeper(hostPort,Consts.ZOOKEEPER_TIMEOUT,new Watcher() {
     
      public void process(WatchedEvent we) {

         if (we.getState() == KeeperState.SyncConnected) {
            connectedSignal.countDown();
         }
      }
   });
     
   connectedSignal.await();
   return zoo;
}

// Method to disconnect from zookeeper server
public void close() throws InterruptedException {
   zoo.close();
}
}
