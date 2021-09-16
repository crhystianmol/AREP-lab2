package eci.arsw.covidanalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Camel Application
 */
public class CovidAnalyzerTool {

    public static  ResultAnalyzer resultAnalyzer;
    public static TestReader testReader;
    private int amountOfFilesTotal;
    public static  AtomicInteger amountOfFilesProcessed;
    public static Object monitor = new Object();
    public static boolean isRunning = false;
    public static AtomicInteger  ThreadsTerminates;

    private analyzeThread[] hilos;

    public CovidAnalyzerTool() {
        resultAnalyzer = new ResultAnalyzer();
        testReader = new TestReader();
        amountOfFilesProcessed = new AtomicInteger();
        ThreadsTerminates = new AtomicInteger();
    }

    public void processResultData(int canThilos, CovidAnalyzerTool covidAnalyzerTool) {

        hilos = new analyzeThread[canThilos];


        amountOfFilesProcessed.set(0);
        List<File> resultFiles = getResultFileList();
        amountOfFilesTotal = resultFiles.size();

        ArrayList<ArrayList> resultados = new ArrayList<>();
        int cont = 0;
        for (int i = 0; i < canThilos ; i++) resultados.add(new ArrayList<File>());

        for (int i = 0; i < resultFiles.size() ; i++) {
            if(cont == resultados.size())cont = 0;
            resultados.get(cont).add(resultFiles.get(i));
            cont ++;
        }
        List<ArrayList> resultadosT=  resultados;
        ThreadsTerminates.set(0);

        for (int i = 0; i < canThilos; i++) {
            hilos[i] = new analyzeThread(resultadosT.get(i));
            hilos[i].start();
        }
        for (int i = 0; i < hilos.length; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void pausar(){

        if(isRunning){
            isRunning = false;
            synchronized (monitor){
                monitor.notifyAll();
            }
            System.out.println("running");
        }else{
            isRunning = true;
            System.out.println("paused");
        }

    };

    private List<File> getResultFileList()  {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }


    public Set<Result> getPositivePeople() {
        return resultAnalyzer.listOfPositivePeople();
    }

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        CovidAnalyzerTool covidAnalyzerTool = new CovidAnalyzerTool();

        int canThilos = 5 ;
        Thread processingThread = new Thread(() -> covidAnalyzerTool.processResultData(canThilos,covidAnalyzerTool));

        processingThread.start();


        while(ThreadsTerminates.get() != canThilos){
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (ThreadsTerminates.get() != canThilos) {
                covidAnalyzerTool.pausar();
                String message = "Processed %d out of %d files.\nFound %d positive people.";
                Set<Result> positivePeople = covidAnalyzerTool.getPositivePeople();
                message = String.format(message, CovidAnalyzerTool.amountOfFilesProcessed.get(), covidAnalyzerTool.amountOfFilesTotal);
                System.out.println(message);
                if (line.contains("exit"))
                    break;
            }
        }

        String message = "FINISH!!!! \nProcessed %d out of %d files.\nFound %d positive people:\n%s";
        Set<Result> positivePeople = covidAnalyzerTool.getPositivePeople();
        String affectedPeople = positivePeople.stream().map(Result::toString).reduce("", (s1, s2) -> s1 + "\n" + s2);
        message = String.format(message, CovidAnalyzerTool.amountOfFilesProcessed.get(), covidAnalyzerTool.amountOfFilesTotal, positivePeople.size(), affectedPeople);
        System.out.println(message);

    }

}