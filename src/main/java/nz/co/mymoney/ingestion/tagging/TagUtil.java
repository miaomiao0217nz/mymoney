package nz.co.mymoney.ingestion.tagging;

import nz.co.mymoney.transaction.Transaction;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class TagUtil {
    public static FileSystem fileSystem;

    final private static Map<String, String> detailsMap;
    final private static Map<String, String> codeMap;
    final private static Map<String, String> referenceMap;
    final private static Map<String, String> particularMap;
    final private static Map<String, String> categoryMap;

    static {

        try {
            detailsMap = loadMap("details.txt");
            codeMap = loadMap("codes.txt");
            referenceMap = loadMap("reference.txt");
            particularMap = loadMap("particulars.txt");
            categoryMap = loadMap("category.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private static Path getFilePath(String fileName) throws URISyntaxException, IOException {
        URI uri = TagUtil.class.getClassLoader().getResource("mapping/" + fileName).toURI();
        if ("jar".equals(uri.getScheme())) {
            if (fileSystem == null) {
                Map<String, String> env = new HashMap<>();
                env.put("create", "true");
                fileSystem = FileSystems.newFileSystem(uri, env, null);
            }
            return fileSystem.getPath("mapping/" + fileName);
        } else {
            return Paths.get(uri);
        }
    }

    private static Map<String, String> loadMap(String name) throws IOException, URISyntaxException {
        Path tagFile = getFilePath(name);

        Map<String, String> map = new HashMap<>();
        Files.lines(tagFile).forEach(l -> {
            String[] kv = l.split(",");
            if (kv.length >= 2) {
                map.put(kv[0].trim(), kv[1].trim());
            } else {
                map.put(kv[0], null);
            }
        });
        return map;
    }

    public static void tag(Transaction t) {
        String tag = detailsMap.get(t.getDetails());
        if (tag == null) {
            tag = codeMap.get(t.getCode());
        }
        if (tag == null) {
            tag = referenceMap.get(t.getReference());
        }
        if (tag == null) {
            tag = particularMap.get(t.getParticulars());
        }
        t.tag(tag);
    }

    public static String getParentCategory(String subCat) {
        return categoryMap.getOrDefault(subCat, "Unknown");
    }
}
