package activeMQEmbed;

/**
 * Hello world!
 */
public class EmbedExample {
 
    public static void main(String[] args) throws Exception {
        thread(new EmbedConsumer(),false);
//        thread(new EmbedProducer(),false);
//        thread(new EmbedConsumer(), false);
        
        
        
    }
 
    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
        
    }
 
    
}