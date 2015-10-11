package fr.univ_artois.iut_lens.spaceinvader.util;

public class DataSizeUtil {
	
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + "o";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f%so", bytes / Math.pow(unit, exp), pre);
	}

}
