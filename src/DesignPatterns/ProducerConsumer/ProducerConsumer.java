package DesignPatterns.ProducerConsumer;

public class ProducerConsumer {
    private int capacity; // 仓库容量
    private int size; // 仓库里实际物品数量

    public ProducerConsumer(int capacity)
    {
        this.capacity = capacity;
        this.size = 0;
    }

    public synchronized void produce(int val)
    {
        try {
            // left 表示“想要生产的数量”(有可能生产量太多，需多此生产)
            int left = val;
            while (left > 0)
            {
                // 库存已满时，等待"消费者"消费
                while (size >= capacity)
                {
                    wait();
                }

                // 获取“实际生产的数量”(即库存中新增的数量)
                // 如果“库存”+“想要生产的数量”>“总的容量”，则“实际增量”=“总的容量”-“当前容量”。(此时填满仓库)
                // 否则“实际增量”=“想要生产的数量”
                int inc = (size + left) > capacity ? capacity - size : left;
                size += inc;
                left -= inc;
                System.out.printf("%s produce(%3d) --> left=%3d, inc=%3d, size=%3d\n",
                        Thread.currentThread().getName(), val, left, inc, size);
                notifyAll();
            }
        }
        catch (InterruptedException e)
        {

        }
    }

    public synchronized void consume(int val)
    {
        try {
            // left 表示“客户要消费数量”(有可能消费量太大，库存不够，需多此消费)
            int left = val;
            while (left > 0)
            {
                // 库存为0时，等待“生产者”生产产品。
                while (size <= 0)
                {
                    wait();
                }

                // 获取“实际消费的数量”(即库存中实际减少的数量)
                // 如果“库存”<“客户要消费的数量”，则“实际消费量”=“库存”；
                // 否则，“实际消费量”=“客户要消费的数量”。
                int dec = (size < left) ? size : left;
                size -= dec;
                left -= dec;
                System.out.printf("%s consume(%3d) <-- left=%3d, dec=%3d, size=%3d\n",
                        Thread.currentThread().getName(), val, left, dec, size);
                notifyAll();
            }
        }
        catch (Exception e)
        {

        }
    }
}

class Producer{
    private ProducerConsumer pc;

    public Producer(ProducerConsumer pc)
    {
        this.pc = pc;
    }

    // 消费产品：新建一个线程向仓库中生产产品。
    public void produce(final int val) {
        new Thread() {
             public void run() {
                 pc.produce(val);
             }
        }.start();
    }
}

class Consumer{
    private ProducerConsumer pc;

    public Consumer(ProducerConsumer pc)
    {
        this.pc = pc;
    }

    public void consume(final int val)
    {
        new Thread(){
            public void run(){
                pc.consume(val);
            }
        }.start();
    }
}
