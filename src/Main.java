public class Main {
    public static String outputPath;
    public static void main(String[] args) {
        String inputPath = args[0]; // getting the files from terminal
        outputPath = args[1];
        //String inputPath = "inp1.txt";
        //String outputPath = "out.txt";
        FileInput.writeToFile(outputPath,"",false,false); // refreshing the file

        String[] commands = FileInput.readFile(inputPath ,true,true); //getting the commands

        for(String command : commands){ // handling each command with a loop
            Command.commandHandler(command);
        }

    }
}