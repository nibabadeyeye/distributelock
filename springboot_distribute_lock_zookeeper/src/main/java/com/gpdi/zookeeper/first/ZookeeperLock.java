package com.gpdi.zookeeper.first;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ZookeeperLock implements Watcher, Runnable {

    //操作zookeeper的客户端工具
    private ZooKeeper zooKeeper;
    //现在的路径
    private String nowPath;
    //上一个路径
    private String prePath;
    //节点名称
    private String name;
    //项目根节点名称
    private final static String parentPath = "/com.lock.common.ZookeeperLock";
    //项目根节点下面的节点的名称
    private final static String path = "/com.lock.common.ZookeeperLock/sub";

    /**
     * @desc: 一、初始化Zookeeper连接
     */
    public ZookeeperLock() {
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 60000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc: 二、调用当前方法默认执行run方法
     */
    public void run() {
        name = Thread.currentThread().getName();
        createParentPath();
        //获取锁
        if (getLock()) {
            //操作
            workIng();
        }
    }

    /**
     * @desc: 排在前面的节点进行监听
     */
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.NodeCreated == watchedEvent.getType()) {
                System.out.println(Thread.currentThread().getName() + "成功连接上ZK服务器");
            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(prePath)) {
                System.out.println(name + "收到情报，排我前面的家伙已挂，我是不是可以出山了？");
                if (isMin()) {
                    workIng();
                }
            }
        }
    }


    /**
     * @desc: 工作
     */
    private void workIng() {
        try {
            //exists表示监听指定节点的变化情况，会回调监听方法
            if (zooKeeper.exists(this.nowPath, true) == null) {
                System.out.println(name + "本节点已不在了...");
                return;
            }
            System.out.println(name + "获取锁成功，赶紧干活！===========");
            zooKeeper.delete(nowPath, -1);
            System.out.println(name + "删除本节点：" + nowPath);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Main.countDownLatch.countDown();
            releaseConnection();
        }
    }

    /**
     * @desc: 获取锁
     */
    private Boolean getLock() {
        nowPath = createPath(path);
        System.out.println(name + "=======创建子节点" + nowPath);
        //判断自己是不是最小节点
        return isMin();
    }

    /**
     * @desc: 当前线程节点是否是最小的
     */
    private boolean isMin() {
        try {
            List<String> childList = zooKeeper.getChildren(parentPath, false);
            Collections.sort(childList);
            int index = childList.indexOf(nowPath.substring(parentPath.length() + 1));
            switch (index) {
                case -1: {
                    System.out.println(name + "=该线程节点" + path + "不存在");
                    return false;
                }
                case 0: {
                    System.out.println(name + "=该线程是老大，允许执行");
                    return true;
                }
                default: {
                    prePath = parentPath + "/" + childList.get(index - 1);
                    System.out.println(name + "=获取子节点中，排在我面前的是" + prePath);
                    //exists表示监听前一个节点的变化情况，会回调监听方法
                    zooKeeper.exists(prePath, true);
                    return false;
                }
            }
        } catch (KeeperException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createParentPath() {
        synchronized (this) {
            if (createPath(parentPath, "111")) {
                System.out.println(name + "=线程抢占先机创建了主节点。。。。");
            }
        }
    }

    private Boolean createPath(String path, String data) {
        try {
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (KeeperException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    private String createPath(String path) {
        try {
            return zooKeeper.create(path, "111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException e) {
            e.printStackTrace();
            return "";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 关闭ZK连接
     */
    private void releaseConnection() {
        if (this.zooKeeper != null) {
            try {
                this.zooKeeper.close();
            } catch (InterruptedException e) {
            }
        }
        System.out.println(name + "释放连接");
    }
}