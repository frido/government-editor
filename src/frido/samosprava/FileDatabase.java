package frido.samosprava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class FileDatabase {
    private String indexFile;
    private List<String> colletionFiles = null;
    private final ObjectMapper mapper;
    private Map<String, InMemoryCollection> data;

    public FileDatabase(String indexFilePath) {
        this.indexFile = indexFilePath;
        this.data = new HashMap<>();
        this.mapper = new ObjectMapper();
    }

    public void readDB() {
        readIndex();
        for (String fileName : colletionFiles) {
            addContent(readFile(Paths.get(Paths.get(indexFile).getParent().toString(), fileName)));
          }
    }

    private void readIndex() {
        Path path = Paths.get(this.indexFile);
        try {
            colletionFiles = Collections.unmodifiableList(Files.readAllLines(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readFile(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getFiles(){
        return this.colletionFiles;
    }

    private void addContent(byte[] content) {
        JsonNode tree = null;
        try {
          tree = mapper.readTree(content);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        String collectionName = tree.fieldNames().next();
        ArrayNode list = (ArrayNode) tree.get(collectionName);
        Iterator<JsonNode> item = list.iterator();
        while (item.hasNext()) {
          JsonNode node = item.next();
          InMemoryCollection collection = data.get(collectionName);
          if (collection == null) {
            collection = new InMemoryCollection();
            data.put(collectionName, collection);
          }
          collection.add(node);
        }
      }

    public Set<String> getCollections() {
        return this.data.keySet();
      }

}
