package demo.msa.zk;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author anquan li
 */
public class ZooKeeperDemo {

  private static final String CONNECTION_STRING = "192.168.5.131:2181,192.168.5.132:2181,192.168.5.133:2181";
  private static final int SESSION_TIMEOUT = 5000;

  private static CountDownLatch latch = new CountDownLatch(1);

  public static void main(String[] args) throws Exception {
    //连接Zookeeper
    ZooKeeper zk = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, (WatchedEvent watchedEvent) -> {
      if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
        latch.countDown();
      }
    });
    latch.await();
    //获取Zookeeper客户端对象
    System.out.println(zk);
    //同步方式创建节点
//    String name=zk.create("/foo","hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//    System.out.println(name);
    //异步方式创建节点
//    zk.create("/bb", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, (int i, String s, Object o, String s1) -> {
//        System.out.println(s1);
//    },null);

    //同步方式判断节点是否存在
    Stat stat = zk.exists("/mic", null);
    if (null != stat) {
      System.out.println("node exists");
    } else {
      System.out.println("node does not exists");
    }
    //异步方式判断节点是否存在
    zk.exists("/bbb", null, (int i, String s, Object o, Stat stat1) -> {
      if (stat1 != null) {
        System.out.println("node exists");
      } else {
        System.out.println(" node does not exists");
      }
    }, null);

//    //同步获取节点数据
//    byte[] data=zk.getData("/mic",null,null);
//    System.out.println(new String(data));
//    //异步获取节点数据
//    zk.getData("/bb",null,(int i, String s, Object o, byte[] nodeData, Stat nodeStat) ->{
//      System.out.println(new String(nodeData));
//    },null);
    //同步更新节点数据
    Stat uStat = zk.setData("/mic", "www.gupaoedu.com".getBytes(), -1);
    System.out.println(uStat != null);
    //异步更新数据
    zk.setData("/foo", "h1".getBytes(), -1, (int i, String s, Object o, Stat asynStat) -> {
      System.out.println(asynStat != null);
    }, null);
    //同步获取节点数据
    byte[] data = zk.getData("/mic", null, null);
    System.out.println(new String(data));
    //异步获取节点数据
    zk.getData("/foo", null, (int i, String s, Object o, byte[] nodeData, Stat nodeStat) -> {
      System.out.println(new String(nodeData));
    }, null);
    //同步删除节点
    zk.delete("/foo2", -1);
    System.out.println(true);
    //异步删除节点
    zk.delete("/mic", -1, (int rc, String s, Object o) -> {
      System.out.println(rc == 0);
    }, null);
    //同步方式
    List<String> children = zk.getChildren("/", null);
    for (String node : children) {
      System.out.println(node);
    }
    //异步方式
    zk.getChildren("/", null, (int i, String s, Object o, List<String> childrenNode) -> {
      for (String node : childrenNode) {
        System.out.println(node);
      }
    }, null);
    Thread.sleep(Long.MAX_VALUE);
  }

}
