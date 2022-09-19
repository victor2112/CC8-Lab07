import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UDPServerAPP {
    public static void main(String argv[]) {

        DatagramSocket socket;
        boolean fin = true;

        try {
            // Creamos el socket
            socket = new DatagramSocket(4444);

            byte[] mensaje_bytes = new byte[256];
            String mensaje = "";
            mensaje = new String(mensaje_bytes);
            String mensajeComp = "";

            DatagramPacket paquete = new DatagramPacket(mensaje_bytes, 256);
            DatagramPacket envpaquete = new DatagramPacket(mensaje_bytes, 256);

            int puerto;
            InetAddress address;
            byte[] mensaje2_bytes = new byte[256];

            // Iniciamos el bucle
            do {
                // Recibimos el paquete
                socket.receive(paquete);
                // Lo formateamos
                mensaje = new String(mensaje_bytes).trim();


                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String logReceive = "> 100.120.1.1 client [" + dtf.format(LocalDateTime.now()) + "]";

                System.out.println();
                System.out.println(logReceive);
                System.out.println("TCP: " + mensaje);

                // Obtenemos IP Y PUERTO
                puerto = paquete.getPort();
                address = paquete.getAddress();

                String logSend = "< 127.100.2.9 server [" + dtf.format(LocalDateTime.now()) + "]";

                System.out.println();
                System.out.println(logSend);
                System.out.println("TCP: " + mensaje.toUpperCase());
                
                
             

                // formateamos el mensaje de salida
                mensaje2_bytes = mensaje.toUpperCase().getBytes();

                // Preparamos el paquete que queremos enviar
                envpaquete = new DatagramPacket(mensaje2_bytes, mensaje.length(), address, puerto);

                // realizamos el envio
                socket.send(envpaquete);
                
                
                // se ejecuta hasta no recibir EXIT
                if (mensaje.startsWith("EXIT")) {
                    System.out.println();
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
}