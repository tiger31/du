package du;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;


public class Program {
    private static boolean flagH = false;
    private static boolean flagC = false;
    private static boolean flagSI = false;
    private static final String[] sizeUnits = {"B", "KB", "MB", "GB"};
    private static Map<String, Long> filenames = new HashMap<>();

    public static void main(String[] args) {
        //Setting up flags and filenames
        try {
            parseInputFlags(args);
        } catch (IllegalFormatFlagsException e) {
            System.out.println("Wrong input string format, please follow example: $ du [-h] [-c] [--si] file1 file2");
            return;
        }
        //Count size of each file
        for (String filename: filenames.keySet()) {
            try {
                filenames.put(filename, getSizeOf(filename));
            } catch (FileNotFoundException e) {
                System.out.println("File " + filename + " not found");
                return;
            }
        }
        if (flagC) {
            System.out.println("Total size: " + finalSizeCount(filenames.values()
                    .stream()
                    .mapToLong(Long::longValue)
                    .sum()));
        } else {
            for (Map.Entry<String, Long> file: filenames.entrySet()) {
                StringBuilder s = new StringBuilder();
                System.out.println(s.append("Size of ")
                        .append(file.getKey())
                        .append(": ")
                        .append(finalSizeCount(file.getValue())));
            }
        }
    }
    public static void parseInputFlags(String[] args) throws IllegalFormatFlagsException {
        for (String arg: args) {
            switch (arg) {
                case "-h":
                    if (!flagC && !flagSI && filenames.isEmpty())
                        flagH = true;
                    else throw new IllegalFormatFlagsException("");
                    break;
                case "-c":
                    if (!flagSI && filenames.isEmpty())
                        flagC = true;
                    else throw new IllegalFormatFlagsException("");
                    break;
                case "--si":
                    if (filenames.isEmpty())
                        flagSI = true;
                    else throw new IllegalFormatFlagsException("");
                    break;
                default:
                    filenames.put(arg, null);
            }
        }
    }

    public static long getSizeOf(String filename) throws FileNotFoundException {
        long size = 0;
        File file = Paths.get(filename).toFile();
        if (!file.exists()) throw new FileNotFoundException();
        //Make queue for avoid recursion
        Deque<File> queue = new ArrayDeque<>();
        queue.add(file);
        while (!queue.isEmpty()) {
            File f = queue.pollFirst();
            size += f.length();
            if (f.isDirectory() && f.listFiles() != null)
                queue.addAll(Arrays.asList(f.listFiles()));
        }
        return size;
    }

    public static String finalSizeCount(long size) {
        int base = (flagSI) ? 1000 : 1024;
        int maxStep = (flagH) ? 4 : 2;
        double newSize = (double)size;
        int step;
        for (step = 0; step < maxStep - 1; step++) {
            if (!flagH || newSize > base) {
                newSize /= base;
            } else break;
        }
        return String.format("%(.2f", newSize) + sizeUnits[step];
    }

}


