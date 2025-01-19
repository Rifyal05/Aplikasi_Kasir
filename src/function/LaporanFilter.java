package function;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LaporanFilter {

    // Method untuk mendapatkan tanggal awal minggu
    public static String getAwalMinggu(String yearweek) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyww");
            Date date = sdf.parse(yearweek);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            // Tambahkan 1 hari
            cal.add(Calendar.DAY_OF_MONTH, 1);

            sdf.applyPattern("yyyy/MM/dd");
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    // Method untuk mendapatkan tanggal akhir minggu
    public static String getAkhirMinggu(String yearweek) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyww");
            Date date = sdf.parse(yearweek);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

            // Tambahkan 1 hari
            cal.add(Calendar.DAY_OF_MONTH, 1);

            sdf.applyPattern("yyyy/MM/dd");
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    // Method untuk mendapatkan tanggal awal bulan
    public static String getAwalBulan(String yearmonth) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date date = sdf.parse(yearmonth);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            // Tambahkan 1 hari
            cal.add(Calendar.DAY_OF_MONTH, 1);

            sdf.applyPattern("yyyy-MM-dd");
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    // Method untuk mendapatkan tanggal akhir bulan
    public static String getAkhirBulan(String yearmonth) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date date = sdf.parse(yearmonth);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            // Tambahkan 1 hari
            cal.add(Calendar.DAY_OF_MONTH, 1);

            sdf.applyPattern("yyyy-MM-dd");
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }
}