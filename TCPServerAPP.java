import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Path;


public class TCPServerAPP {
    
    // declaramos una variable de tipo string
    public static String mensaje="";

    public static void main(String argv[]) {
 
        // Objeto ServerSocket para crear la comunicacion
        ServerSocket socket;
        // creamos una varible boolean con el valor a false
        boolean fin = true;
        
        BufferedOutputStream bos;

        try {
        
            // Inicializacion de socket con puerto para escuchar en la comunicacion
            socket = new ServerSocket(6000);
            // Inicializacion de socket_cli que nos permitirá aceptar conexiones de clientes
            Socket socket_cli = socket.accept();
            // Objeto DataInputStream e inicializacion para recibir datos del cliente
            DataInputStream in = new DataInputStream(socket_cli.getInputStream());
            // Objeto DataOutputStream e inicializacion para enviar datos al servidor destino
            DataOutputStream out = new DataOutputStream(socket_cli.getOutputStream());
            
            bos = new BufferedOutputStream(socket_cli.getOutputStream());
            
            do {
                
                // Se espera para recibir la peticion de un archivo
                checkFile(in, bos, out, socket);
                
                
                
                // Se espera e imprime la respuesta del cliente
                //receive(in);
                
                // Se imprime y envia la cadena al cliente
                //send(mensaje, out);

                // se ejecuta hasta no recibir EXIT
                if (mensaje.startsWith("EXIT")) {
                    System.out.println();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    System.out.println(dtf.format(LocalDateTime.now()) + " Server Stop");
                    fin = false;
                    socket.close();
                }


            } while (fin);
        } catch (Exception e) {
        
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void checkFile(DataInputStream in, BufferedOutputStream bos, DataOutputStream out, ServerSocket socket) {
        try{
            
            String fileName = in.readUTF();
            File file = new File( "Server/" + fileName );

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            

            int read;
            byte[] byteArray = new byte[516];
            
            
            //System.out.println("se envia el nombre: " + fileName);
            //Send name
            out.writeUTF(file.getName());
            
            //System.out.println("se envia el legth: " + file.length());
            //Send length
            out.writeUTF(file.length() + "");

            //Send chunk number
            byte[] nChunck = IntToByteArray(0001); 
            
            
            //System.out.println("se envia el archivo");
            //Send package
            while ((read = bis.read(byteArray)) != -1){
               
                //byte[] c = Bytes.concat(nChunck, byteArray);
                //bos.write(c,0,read + 4);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(nChunck);
                outputStream.write(byteArray);

                byte[] result = outputStream.toByteArray();
                bos.write(result,0,read + 4);
                
            }

            bos.close();

            //System.out.println(file.getName());
            
            //socket.close();
            //out.writeUTF(fileName);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void receive(DataInputStream in) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String logReceive = "> 100.120.1.1 client [" + dtf.format(LocalDateTime.now()) + "]";
            mensaje = in.readUTF();

            System.out.println();
            System.out.println(logReceive);
            System.out.println("TCP: " + mensaje);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void send(String mensaje, DataOutputStream out) {
        
        try {
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String logSend = "< 127.100.2.9 server [" + dtf.format(LocalDateTime.now()) + "]";

            System.out.println();
            System.out.println(logSend);
            System.out.println("TCP: " + mensaje.toUpperCase());
            
            
            // enviamos el mensaje codificado en UTF
            out.writeUTF(mensaje.toUpperCase());
        
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public static byte[] IntToByteArray( int data ) {    
        byte[] result = new byte[4];
        result[0] = (byte) ((data & 0xFF000000) >> 24);
        result[1] = (byte) ((data & 0x00FF0000) >> 16);
        result[2] = (byte) ((data & 0x0000FF00) >> 8);
        result[3] = (byte) ((data & 0x000000FF) >> 0);
        return result;        
    }

}