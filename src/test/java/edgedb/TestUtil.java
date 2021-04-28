package edgedb;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.security.SecureRandom;
import java.util.List;

@Slf4j
public final class TestUtil {

    public static void executeProcess(List<String> cmdList) throws Exception {
        log.info("Executing Process");

        Process p;
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmdList);
        p = pb.start();
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static String randomString(int len){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
