import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UDPClientAPP {
    public static void main(String argv[]) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean fin = true;
        // Definimos el sockets, número de bytes del buffer, y mensaje.
        DatagramSocket socket;
        InetAddress address;
        byte[] mensaje_bytes = new byte[256];
        String mensaje = "";
        mensaje_bytes = mensaje.getBytes();

        // Paquete
        DatagramPacket paquete;

        String cadenaMensaje = "";

        DatagramPacket servPaquete;

        byte[] RecogerServidor_bytes = new byte[256];

        try {
            socket = new DatagramSocket();

            address = InetAddress.getByName("127.0.0.1");

            do {
                System.out.println();
                System.out.print("Ingrese cadena: ");
                mensaje = bufferedReader.readLine();
                
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String logSend = "> 100.120.1.1 client [" + dtf.format(LocalDateTime.now()) + "]";

                System.out.println();
                System.out.println(logSend);
                System.out.println("TCP: " + mensaje);
        
                mensaje_bytes = mensaje.getBytes();
                paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 4444);
                socket.send(paquete);

                RecogerServidor_bytes = new byte[256];

                // Esperamos a recibir un paquete
                servPaquete = new DatagramPacket(RecogerServidor_bytes, 256);
                socket.receive(servPaquete);

                

                String logReceive = "< 127.100.2.9 server [" + dtf.format(LocalDateTime.now()) + "]";
                
                // Convertimos el mensaje recibido en un string
                cadenaMensaje = new String(RecogerServidor_bytes).trim();

                System.out.println();
                System.out.println(logReceive);
                System.out.println("TCP: " + cadenaMensaje);

                        
                // se ejecuta hasta no recibir EXIT
                if (cadenaMensaje.startsWith("EXIT")) {
                    System.out.println();
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
}