package eci.arsw.covidanalyzer;
import sun.awt.Mutex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class Hilo extends Thread{
    TestReader testReader;
    ResultAnalyzer resultAnalyzer;
    CovidAnalyzerTool covid;
    ArrayList<File> lista;
    Mutex mutex;
    boolean isPause;
    public Hilo(TestReader testReader, ResultAnalyzer resultAnalyzer, CovidAnalyzerTool covid ){
        this.testReader = testReader;
        this.resultAnalyzer=resultAnalyzer;
        this.covid = covid;
        this.lista = new ArrayList<File>();
        mutex = new Mutex();
        isPause=false;
    }
    @Override
    public void run(){
        for(File archivo: lista){
            List<Result> resultados = testReader.readResultsFromFile(archivo);
            for(Result transaction: resultados) {
                while (isPause) {
                    synchronized (mutex) {
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    resultAnalyzer.addResult(transaction);
                }
            }
            covid.processArchive();
        }
    }
    public void addFile(File files) {
        lista.add(files);
    }

    public void pause() {
        isPause=true;
    }

    public void reanudar() {
        isPause=false;
        synchronized (mutex){
            mutex.notifyAll();
        }
    }
}
