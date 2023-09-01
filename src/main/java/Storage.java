import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
public class Storage {
    String currentDirectory = System.getProperty("user.dir");
    String fileName = "sisyphusData.csv";
    Path filePath = Path.of(currentDirectory, fileName);


    /**
     * Attempts to read file in csv format and skip all "corrupted" / wrong lines of values.
     * If file does not exist, an empty TaskList will be returned and a file will be created.
     *
     * @return TaskList from the saved file
     */
    public TaskList loadData() {
        if (Files.exists(filePath)) {
            try {
                FileReader fileReader = new FileReader(filePath.toString());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                TaskList savedTaskList = new TaskList();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] params = line.split(",");

                    if (params.length == 0) {
                        continue;
                    }

                    String type = params[0];

                    switch (type) {
                    case ("E"): {
                        if (params.length == 5) {
                            Event event = new Event(params[1], params[2].equals("1"), params[3], params[4]);
                            savedTaskList.addTask(event);
                        }
                        break;
                    }
                    case ("D"): {
                        if (params.length == 4) {
                            Deadline deadline = new Deadline(params[1], params[2].equals("1"), params[3]);
                            savedTaskList.addTask(deadline);
                        }
                        break;
                    }
                    case ("T"): {
                        if (params.length == 3) {
                            ToDo todo = new ToDo(params[1], params[2].equals("1"));
                            savedTaskList.addTask(todo);
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                    }
                }
                bufferedReader.close();
                return savedTaskList;

            } catch (IOException e) {
                System.err.println("An error occurred while reading the file: " + e.getMessage());
                return new TaskList();
            }
        } else {
            writeFile(new TaskList());
        }
        return new TaskList();
    }

    /**
     * Takes in a TaskList and writes all tasks in the TaskList to the saved file based on a csv format.
     *
     * @param taskList
     */
    public void writeFile(TaskList taskList) {
        try {
            FileWriter fileWriter = new FileWriter(filePath.toString());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < taskList.size(); i++) {
                stringBuilder.append(taskList.getTask(i).toSaveFormat());
                stringBuilder.append('\n');
            }
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    /**
     *
     * Testing for Storage class.
     * @param args
     */
    public static void main(String[] args) {
        Storage storage = new Storage();
        TaskList loaded = storage.loadData();
        for (int i = 0; i < loaded.size(); i++) {
            System.out.println(loaded.getTask(i));
        }
        TaskList temp = new TaskList();
        temp.addTask(new ToDo("turtles", true));
        storage.writeFile(temp);

//        String currentDirectory = System.getProperty("user.dir");
//        String fileName = "sisyphusData.csv";
//        String filePath = currentDirectory + "/" + fileName;
//        Path path = Path.of(currentDirectory, fileName);
//        System.out.println(filePath);
//        if(Files.exists(path)) {
//            System.out.println("Turtles");
//        }
//        System.out.println(path);
    }


}
