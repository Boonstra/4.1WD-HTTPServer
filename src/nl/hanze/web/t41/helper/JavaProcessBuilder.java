package nl.hanze.web.t41.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JavaProcessBuilder {

    private String mainClass;
    private String workingDirectory;
    private ArrayList<String> classpathEntries;
    private ArrayList<String> mainClassArguments;
    private String javaRuntime;

    /**
     * Constructor.
     */
    public JavaProcessBuilder() {

        classpathEntries   = new ArrayList<String>();
        mainClassArguments = new ArrayList<String>();

        if (System.getProperty("os.name").toLowerCase().equals("mac os x")) {
            javaRuntime = "/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Home/bin/java";
        } else {
            javaRuntime = "java";
        }
    }

    /**
     * Constructor.
     *
     * @param mainClass        The main class of the process.
     * @param workingDirectory The directory from which to look for the classpath plus main class.
     */
    public JavaProcessBuilder(String mainClass, String workingDirectory) {

        // Call empty constructor to set fields.
        this();

        this.mainClass        = mainClass;
        this.workingDirectory = workingDirectory;
    }

    /**
     * Start a new process.
     *
     * @return process
     *
     * @throws IOException
     */
    public Process startProcess() throws IOException {

        ArrayList<String> argumentsList = new ArrayList<String>();

        argumentsList.add(this.javaRuntime);

        argumentsList.add("-classpath");
        argumentsList.add(getClasspath());

        argumentsList.add(this.mainClass);

        for (String arg : mainClassArguments) {
            argumentsList.add(arg);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(argumentsList.toArray(new String[argumentsList.size()]));
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(this.workingDirectory));
        return processBuilder.start();
    }

    /**
     * Builds the classpath from the classpath entries.
     *
     * @return classpath
     */
    private String getClasspath() {

        StringBuilder stringBuilder = new StringBuilder();

        int count     = 0;
        int totalSize = classpathEntries.size();

        for (String classpathEntry : classpathEntries) {
            stringBuilder.append(classpathEntry);

            count++;

            if (count < totalSize) {
                stringBuilder.append(System.getProperty("path.separator"));
            }
        }

        return stringBuilder.toString();
    }

    public void setMainClass(String mainClass) {

        this.mainClass = mainClass;
    }

    public void setWorkingDirectory(String workingDirectory) {

        this.workingDirectory = workingDirectory;
    }

    public void addClasspathEntry(String classpathEntry) {

        this.classpathEntries.add(classpathEntry);
    }

    public void addArgument(String argument) {

        this.mainClassArguments.add(argument);
    }
}
