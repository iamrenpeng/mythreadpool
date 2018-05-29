package ind.renpeng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: renpeng
 * @email: renp90@qq.com
 * @createTime: 2018/5/29 16:30
 * @description:
 */
public class MyThreadPool {
    private int coreThreadCount;
    private int queueCount;
    private int maxCoreThreadCount;
    private int maxThreadCount;
    private volatile boolean running = true;

    private BlockingQueue<Runnable> queue = null;
    private List<Thread> threadList = new ArrayList<>();
    private Set<Worker> workerSet = new HashSet<>();

    public MyThreadPool(int maxCoreThreadCount,int maxThreadCount, int queueCount) {
        this.maxCoreThreadCount = maxCoreThreadCount;
        this.maxThreadCount = maxThreadCount;
        this.queueCount=queueCount;
        queue = new LinkedBlockingQueue<>(queueCount);
    }

    public void execute(Runnable runnable){
        if (runnable == null) {
            throw new NullPointerException();
        }
        if(threadList.size()<maxCoreThreadCount){
            addThread(runnable);
        }
        else{
            try {
                if(queue.size()<queueCount){
                queue.put(runnable);}else{
                    if(threadList.size()<=maxThreadCount){
                        addThread(runnable);
                    }
                    else {
                        System.out.println("拒绝服务");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void addThread(Runnable runnable){
        if(coreThreadCount>maxCoreThreadCount) {return;}
        coreThreadCount++;
        Worker worker=new Worker(runnable);
        Thread  t=new Thread(worker);
        threadList.add(t);
        t.start();
    }
    public void shutdown(){
        this.running=false;
        if(!threadList.isEmpty()){
            for (Thread t:
                    threadList) {
                t.interrupt();

            }
        }
    }

    class Worker implements Runnable {

      public Worker(Runnable runnable){
          queue.offer(runnable);
      }
        @Override
        public void run() {
          while(running){
                  Runnable task=null;
                  task =getTask();
                  task.run();
          }
        }
        private Runnable getTask(){
            Runnable task=null;
            try {
                task =queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return task;
        }
    }
}
