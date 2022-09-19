import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;


public class TCPClientAPP {
    
    // declaramos una variable de tipo string
    public static String mensaje="";

    
    public static void main(String argv[]) {
    
        // Instancia BuffererReader para leer el teclado
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        // Objeto socket para crear la comunicacion
        Socket socket;
        // creamos una varible boolean con el valor a false
        boolean fin = true;
        
        
        try {
        
            // Inicializacion de socket con la dirección del destino y el puerto para la comunicacion
            socket = new Socket("127.0.0.1",6000);
            // Objeto DataOutputStream e inicializacion para enviar datos al servidor destino
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // Objeto DataInputStream e inicializacion para recibir datos del cliente
            DataInputStream in = new DataInputStream(socket.getInputStream());

            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            

            do {
                //System.out.println();
                //System.out.print("Ingrese archivo a descargar: ");
                //mensaje = bufferedReader.readLine();
                mensaje = argv[0];

                // Se imprime y envia la cadena al Server
                send(mensaje, out);

                
                // Se espera e imprime la respuesta del server
                receive(mensaje, in, bis);
                

                //System.out.println();
                fin = false;
                socket.close();

                // se ejecuta hasta no recibir EXIT
                if (mensaje.startsWith("EXIT")) {
                    System.out.println();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    System.out.println(dtf.format(LocalDateTime.now()) + " Client Stop");
                    fin = false;
                    socket.close();
                }
                
            } while (fin);

        } catch (Exception e) {
            
            System.err.println(e.getMessage());
            System.exit(1);

        }
    }

    public static void send(String mensaje, DataOutputStream out) {
        try {
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String logSend = "> 100.120.1.1 client [" + dtf.format(LocalDateTime.now()) + "]";

            //System.out.println();
            //System.out.println(logSend);
            //System.out.println("TCP: " + mensaje);
            
            
            // enviamos el mensaje codificado en UTF
            out.writeUTF(mensaje);
        
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void receive(String mensaje, DataInputStream in, BufferedInputStream bis) {
        int read;
            
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String logReceive = "< 127.100.2.9 server [" + dtf.format(LocalDateTime.now()) + "]";
            /**/
            
            //System.out.println("preparado para recibir");
            
            byte[] receivedData;    
            receivedData = new byte[516];
            
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Client/" + mensaje));
            
            //Receive file name
            String name = in.readUTF();
            System.out.println("nombre: " + name);

            //Receive file length
            String length = in.readUTF();
            System.out.println("length: " + length);

            
            //Calculate Chunks
            int chunks = (int) Math.round(Integer.parseInt(length)/512 + 0.51);
            System.out.println(chunks + "");



            byte[] file[] = new byte[516][chunks];
            int size[] = new int[chunks];

            int i = 0;

            byte[] file_tail = new byte[0];
            
            //Receive file and save in a array
            while ((read = bis.read(receivedData)) != -1){
                
                /*
                file[i] = receivedData;
                size[i] = read;

                if (read < 512) {
                    file_tail = new byte[read];
                    file_tail = receivedData;
                }
                
                
                System.out.println(file[i] + "");
                System.out.println(file_tail + "");
                System.out.println(size[i] + "");
                i = i + 1;
                */
                System.out.println(receivedData + "");
                String s = new String(receivedData, StandardCharsets.UTF_8);
                System.out.println(s + "");
                System.out.println(read + "");
                
                
            }

/*
            //Write file
            for (int j = 0; j < chunks; j++) {
                

                if (size[j] < 512) {
                    bos.write(file_tail,0,size[j]);
                } else {
                    bos.write(file[j],0,size[j]);
                }
            }
*/
            //System.out.println("Se cierra el archivo");
            bos.close();
            in.close();
            

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}