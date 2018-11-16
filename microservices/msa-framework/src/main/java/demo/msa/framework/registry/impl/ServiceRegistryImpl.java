package demo.msa.framework.registry.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import demo.msa.framework.registry.sevice.ServiceRegistry;

/**
 * @author anquan li
 */

@Component
public class ServiceRegistryImpl implements ServiceRegistry,Watcher {
  private static final int SESSION_TIMEOUT = 10000;
  private static final String REGISTRY_PATH = "/registry";
  private static Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);
  private static CountDownLatch latch = new CountDownLatch(1);
  private ZooKeeper zk;

  public ServiceRegistryImpl() {

  }

  public ServiceRegistryImpl(String zkServers) {
    try {
      //创建Zookeeper客户端
      zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
      latch.await();
      logger.debug("connected to zookeeper");
    } catch (Exception e) {
      logger.error(" create zookeeper client failure", e);
    }
  }


  @Override
  public void register(String serviceName, String serviceAddress) {
    try {
      //创建根节点(持久节点)
      String registryPath = REGISTRY_PATH;
      if (zk.exists(registryPath, false) == null) {
        zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        logger.debug(" create registry node :{}", registryPath);
      }
      //创建服务节点(持久节点)
      String servicePath = registryPath + "/" + serviceName;
      if (zk.exists(servicePath, false) == null) {
        zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        logger.debug(" create servicenode: {}", servicePath);
      }
      //创建地址节点(临时有序节点)
      String addressPath = servicePath + "/address-";
      String addressNode = zk.create(addressPath, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
      logger.debug(" create addressnode: {} =>{}", addressNode, serviceAddress);
    } catch (Exception e) {
      logger.error(" create node failure", e);

    }

  }

  @Override
  public void process(WatchedEvent event) {
    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
      latch.countDown();
    }
  }
}
