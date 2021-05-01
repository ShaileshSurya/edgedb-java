package edgedb;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class TestUtil {

    public static List executeProcess(List<String> cmdList) throws Exception {
        log.info("Executing Process with Command {}", cmdList);

        List<String> console = new ArrayList<String>();
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
            console.add(line);
        }
        return console;
    }

    public static String randomAlphaNumeric(int len){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return randomString(len, AB);
    }
    public static String randomString(int len) {
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return randomString(len, AB);
    }

    private static String randomString(int len, String AB) {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
