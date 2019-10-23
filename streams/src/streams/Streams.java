/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streams;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author coolie
 */
public class Streams {

    /**
     * @param args the command line arguments
     */
    static int status = 0;

    public static void main(String[] args) throws InterruptedException {

        // TODO code application logic here
        /*for (;;) {
        if (status == 0) {
            Streams stream = new Streams();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Download Resetting... " + dtf.format(now));
            status = 1;
            stream.nioDownload();
        }
        }*/
        String toFile = "/home/julius/streams/eastus.ts";
        String command = "ffmpeg -i \"https://142-44-235-90.vpnmate.com:5052/live3/IfEQmopmuFBl-5udHuGcVA/1561278247/203844.m3u8\" -c copy " + toFile;
        System.out.println("Command:=>> " + command);
        Process pr = terminalCMD(command);
        Thread.sleep(20000);
        pr.destroy();

    }

    public void nioDownload() {
        //String fromFile = "https://www.boybruynzeel.com/media/Boy_Bruynzeel_-_The_Video_Yearmix_2018/Boy_Bruynzeel_-_The_Video_Yearmix_2018-720.mp4";//clubland   
        //String fromFile = "http://ubuntu.mirror.ac.ke/ubuntu-release/18.04.1/ubuntu-18.04.1-desktop-amd64.iso";
        //String fromFile = "magnet:?xt=urn:btih:6659ed85da2b101056c7a5563951217104cfd644&dn=A%20Star%20Is%20Born%20%282018%29%20%5BBluRay%5D%20%5B720p%5D%20%5BYTS.AM%5D&tr=udp%3A%2F%2Fglotorrents.pw%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337";
        //String fromFile = "http://hiddennetworks.xyz:80/live/IanGathu/Wanjiru2308!/1539.ts";
        //String fromFile = "http://vapi.vaders.tv/play/2479.ts?token=eyJ1c2VybmFtZSI6ImlnYXRodSIsInBhc3N3b3JkIjoid2FuamlydTIzIn0=";//clubland        
        String fromFile = "https://142-44-235-90.vpnmate.com:5052/live3/IfEQmopmuFBl-5udHuGcVA/1561278247/203844.ts";//clubland        
        //String fromFile = "https://www.fetchtube.com/generate.php?url=https%3A%2F%2Fredirector.googlevideo.com%2Fvideoplayback%3Ffvip%3D6%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26requiressl%3Dyes%26source%3Dyoutube%26beids%3D9466586%26lmt%3D1547685142061003%26dur%3D216.642%26ratebypass%3Dyes%26clen%3D19555082%26gir%3Dyes%26initcwndbps%3D545000%26txp%3D5531432%26c%3DWEB%26expire%3D1553535202%26ei%3DgryYXK6OI67Q8gTqnI0Y%26itag%3D18%26key%3Dyt6%26mime%3Dvideo%252Fmp4%26mv%3Dm%26mt%3D1553513481%26ms%3Dau%252Crdu%26pl%3D42%26id%3Do-ALUS5iVP3aqgzJgTdeQRzOA6DZwaNDAHWipQK4rn-dkp%26ipbits%3D0%26mn%3Dsn-p5qs7n7l%252Csn-p5qlsnsr%26mm%3D31%252C29%26ip%3D2607%253A5300%253A60%253A83e1%253Aa4d4%253Af045%253Aaaea%253A126%26signature%3D1731C1213B82C4DB3EA0EB652123E6328B5AA555.B9ADB4E40282E31C51C309248D23966625316EED%26title%3DSoulja%2520Boy%2520-%2520Speakers%2520Going%2520Hammer&filename=Soulja+Boy+-+Speakers+Going+Hammer.mp4&s=magicyt";//clubland        
        //String fromFile = "http://hiddennetworks.xyz:80/live/IanGathu/Wanjiru2308!/238.ts";//dance
        //String fromFile = "http://hiddennetworks.xyz:80/live/IanGathu/Wanjiru2308!/172.ts";
        //http://172.27.116.21:8086/dtm.mp4
        //String toFile = "/home/coolie/streams/ubuntu-18.04.1-desktop-amd64.iso";
        //String toFile = "/home/coolie/streams/Boy_Bruynzeel.mp4";
        //ffmpeg -re -i 2250.ts 2250.mkv
        String toFile = "/home/julius/streams/eastus.ts";
        try {
            System.setProperty("http.agent", "Chrome");
            URL website = new URL(fromFile);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(toFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            //fos.getChannel().transferFrom(rbc, 0, 10000000);//10mb
            fos.close();
            rbc.close();
            status = 0;
        } catch (IOException e) {
            Path path = Paths.get(toFile);
            try {
                Files.deleteIfExists(path);
            } catch (IOException ex) {
                Logger.getLogger(Streams.class.getName()).log(Level.SEVERE, null, ex);
            }
            status = 0;
            e.printStackTrace();
        }
    }

    private static Process terminalCMD(String command) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        Process rp = null;
        try {
            Process shell = pb.start();
            rp = shell;
        } catch (Exception exp) {
            System.out.println("Exception--->" + exp.getMessage());
        }
        // close the stream

        System.out.println("Done---->");
        return rp;
    }
}
