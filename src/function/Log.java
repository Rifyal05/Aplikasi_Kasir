package function;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    static String folder = "Log_Activity";
    static String pathFolder = "src" + File.separator + "log" + File.separator + folder;
    static String logName; 
    static String pathLog;

    public static String getDateNow() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");
        return sdf.format(d);
    }

    // Method untuk menyimpan log
    public static void savelog(String activity) {
        try {
            buatFileLog();

            File log = new File(pathLog);
            log.createNewFile();
            String formattedDate = getDateNow();
            System.out.println("Formatted date: " + formattedDate);
            String logEntry = "[" + "Log Activity : " + formattedDate + "] " + "Log Information : " + activity + ".\n";

            Files.write(
                    Paths.get(pathLog),
                    logEntry.getBytes(),
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Error Code: 101 => " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method untuk membuat file log baru jika sudah beda hari
    private static void buatFileLog() throws IOException {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDateString = dateFormat.format(currentDate);
        logName = "log_aplikasi_kasir_" + currentDateString + ".txt";
        pathLog = pathFolder + File.separator + logName;

        File f = new File(pathFolder);
        f.mkdirs(); 

        File log = new File(pathLog);
        if (!log.exists()) {
            log.createNewFile(); // Buat file baru jika belum ada
        }
    }

    public static void main(String[] args) {
        savelog("Aplikasi dijalankan"); // Contoh penggunaan savelog()
    }
}