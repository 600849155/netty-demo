package whohim.netty.nio;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author WhomHim
 * @description 注意 buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据。
 */
public class FileChannel {

    private final static String FILE_NAME = "C:\\Users\\Administrator\\Desktop\\TM 负载均衡概设.md";

    public static void main(String[] args) {
        try{
            RandomAccessFile aFile = new RandomAccessFile(FILE_NAME, "rw");
            java.nio.channels.FileChannel inChannel = aFile.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(48);

            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {

                System.out.println("Read " + bytesRead);
                buf.flip();

                while(buf.hasRemaining()){
                    byte b = buf.get();
                }

                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
