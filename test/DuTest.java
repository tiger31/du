import du.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
public class DuTest {
    @Test
    public void test() {
        String[] command = new String[] {"/bin/bash", "-c", "du -sb test/inputFiles/KotlinAsFirst2016 test/inputFiles/123 test/inputFiles/image.png"};
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process terminal = builder.start();
            InputStream stdout = terminal.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader (stdout));
            List<Long> values = reader.lines()
                    .map(S -> Long.parseLong(S.split("\\s+")[0]))
                    .collect(Collectors.toList());
            assertTrue(values.get(0) == Program.getSizeOf("test/inputFiles/KotlinAsFirst2016"));
            assertTrue(values.get(1) == Program.getSizeOf("test/inputFiles/123"));
            assertTrue(values.get(2) == Program.getSizeOf("test/inputFiles/image.png"));
            stdout.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
    }
}
