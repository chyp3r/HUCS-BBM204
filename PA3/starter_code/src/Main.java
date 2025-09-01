import java.io.File;

public class Main {
    public static void main(String[] args) {
        File xmlFile = new File(args[0]);
        AlienFlora af = new AlienFlora(xmlFile);
        af.readGenomes();
        af.evaluateEvolutions();
        af.evaluateAdaptations();
    }
}
