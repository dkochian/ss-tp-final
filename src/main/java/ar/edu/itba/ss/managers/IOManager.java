package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.InputData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.util.Objects;

@Singleton
public class IOManager {

    private static final String FILENAME = "config.json";
    private static final Logger logger = LoggerFactory.getLogger(IOManager.class);

    private Configuration configuration = null;
    private InputData inputData = null;

    public Configuration getConfiguration() {
        if (configuration == null) {
            try {
                logger.debug("Loading configuration");
                configuration = read(FILENAME, Configuration.class);
            } catch (IOException e) {
                try {
                    logger.debug("Writing configuration");
                    write(FILENAME, new Configuration());
                } catch (IOException e1) {
                    logger.error(e.getMessage());
                }
                return new Configuration();
            }
        }

        return configuration;
    }

    public InputData getInputData() {
        boolean mustWrite = false;
        final String path = getConfiguration().getInputDirectory() + '/' + getConfiguration().getInputFilename();

        if (getConfiguration().isGenerateInput()) {
            inputData = InputData.generate(this);
            mustWrite = true;
        } else if (inputData == null) {
            try {
                logger.debug("Loading input data");
                inputData = read(path, InputData.class);
            } catch (IOException e) {
                mustWrite = true;
                inputData = InputData.generate(this);
            }
        }

        if (mustWrite) {
            try {
                logger.debug("Writing input data");
                write(path, inputData);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return inputData;
    }

    void reload() {
        inputData = null;
        configuration = null;
    }

    public static <T> T read(final String filename, Class<T> clazz) throws IOException {
        checkAndCreateFolder(filename);
        try (final Reader reader = new BufferedReader(new FileReader(filename))) {
            final Gson gson = (new GsonBuilder()).create();
            return clazz.cast(gson.fromJson(reader, clazz));
        }
    }

    public static <T> void write(final String filename, final T object) throws IOException {
        checkAndCreateFolder(filename);
        try (final Writer writer = new BufferedWriter(new FileWriter(filename))) {
            final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            gson.toJson(object, writer);
        }
    }

    private static void checkAndCreateFolder(final String filename) {
        final int index = filename.lastIndexOf('/');
        if (index != -1) {
            final String folder = filename.substring(0, index);
            final File file = new File(folder);
            if (!file.exists())
                if (!file.mkdirs())
                    throw new RuntimeException("Couldn't create the folder: " + folder);
        }
    }

    public int inputDay(final String path) {
        return (int) Math.floor(Objects.requireNonNull(new File(path).listFiles((dir, name) -> name.endsWith(".json"))).length / 2.0);
    }
}