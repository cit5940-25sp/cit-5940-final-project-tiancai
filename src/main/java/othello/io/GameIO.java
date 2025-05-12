package othello.io;

import othello.gamelogic.GameMemento;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameIO {
    public static void saveToFile(GameMemento m, Path path) throws IOException {
        try (var out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(m);
        }
    }

    public static GameMemento loadFromFile(Path path) throws IOException, ClassNotFoundException {
        try (var in = new ObjectInputStream(Files.newInputStream(path))) {
            return (GameMemento) in.readObject();
        }
    }
}