package ind.renpeng;

public class Main {

    public static void main(String[] args) {
	MyThreadPool threadPool=new MyThreadPool(1,2,8);
	for(int i=0;i<10;i++){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 " + Thread.currentThread().getName() + " 在帮我干活");
            }
        });
    }
    }
}
