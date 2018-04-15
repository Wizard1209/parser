package opendota;

import java.io.*;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
    
public class Main {
    public static void main(String[] args) {
        String INPUT_FILE = "C:\\Users\\Vova\\workspace\\parser_env\\3549145408_1618162017.dem";
        String OUTPUT_FILE = "C:\\Users\\Vova\\workspace\\parser_env\\res.jsonl";
        //test
        simplifiedInOut(INPUT_FILE, OUTPUT_FILE);
        // alternative config
        // startServer(args);
    }

    public static void simplifiedInOut(String inputFile, String outputFile) {
        // Input file from console
        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(inputFile));
            os = new BufferedOutputStream(new FileOutputStream(outputFile));
            new Parse(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void startServer(String[] args) {
        // POST to (local) server
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.valueOf(args.length > 0 ? args[0] : "5600")), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4));
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            t.sendResponseHeaders(200, 0);
            InputStream is = t.getRequestBody();
            OutputStream os = t.getResponseBody();
            try {
            	new Parse(is, os);
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
            os.close();
        }
    }
}
