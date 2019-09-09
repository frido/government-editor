package frido.samosprava;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

public class InMemoryCollection {
    private final List<JsonNode> list;

    public InMemoryCollection() {
      list = new LinkedList<>();
    }

    public void add(JsonNode item) {
      this.list.add(item);
    }

    public List<JsonNode> council(int council) {
      return this.list.stream()
        .filter(x -> filter(x.get("council"), council))
        .collect(Collectors.toList());
    }

    public List<JsonNode> councilMeeting(int council, int meeting) {
      return this.list.stream()
        .filter(x -> filter(x.get("council"), council))
        .filter(x -> filter(x.get("zasadnutie"), meeting))
        .collect(Collectors.toList());
    }

    public List<JsonNode> councilNested(int council) {
      return this.list.stream()
        .filter(x -> x.findValues("council").stream().anyMatch(y -> y.asInt() == council))
        .collect(Collectors.toList());
    }

    public Optional<JsonNode> id(int id) {
      return this.list.stream().filter(x -> filter(x.get("id"), id)).findFirst();
    }

    private boolean filter(JsonNode node, int value) {
      if (node == null) {
        return false;
      }
      return node.asInt() == value;
    }
  }
