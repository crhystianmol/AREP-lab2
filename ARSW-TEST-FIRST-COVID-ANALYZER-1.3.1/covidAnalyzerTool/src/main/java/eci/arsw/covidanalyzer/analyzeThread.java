package eci.arsw.covidanalyzer;
import java.io.File;
import java.util.List;

public class analyzeThread extends Thread{
    private List<File> resultFiles;

    public analyzeThread(List<File> resultFiles ) {
        this.resultFiles=resultFiles;
    }

    @Override
    public void run() {
        for (File resultFile : resultFiles) {
            List<Result> results = CovidAnalyzerTool.testReader.readResultsFromFile(resultFile);
            for (Result result : results) {
                synchronized (CovidAnalyzerTool.monitor) {
                    if (CovidAnalyzerTool.isRunning) {
                        try {
                            CovidAnalyzerTool.monitor.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }
                CovidAnalyzerTool.resultAnalyzer.addResult(result);
            }
            CovidAnalyzerTool.amountOfFilesProcessed.incrementAndGet();
        }
        CovidAnalyzerTool.ThreadsTerminates.incrementAndGet();
    }
}
