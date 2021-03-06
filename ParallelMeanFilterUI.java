import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.*;

public class ParallelMeanFilterUI{

  static final ForkJoinPool fjPool = new ForkJoinPool();

  public static void main(String[] args) throws FileNotFoundException{
    String dataFile = args[0];
    int filterSize = Integer.parseInt(args[1]);
    String outFile = args[2];
    float[] inData;

    Scanner inFile = new Scanner(new FileReader(dataFile));
    int lines = inFile.nextInt();
    inData = new float[lines];
    inFile.nextLine();
    for (int i=0; i<lines; i++){
      inData[i] = Float.parseFloat(inFile.nextLine().split(" ")[1]);
    }
    inFile.close();

	  float[] retArr = new float[lines];
	  MeanFilterThread mainThread = new MeanFilterThread(inData, retArr, filterSize, 0, lines);
    fjPool.invoke(mainThread);

    float[] outData = mainThread.retArr;

    PrintWriter writer = new PrintWriter(outFile);
    writer.println(lines);
    for (int i=0; i<lines; i++){
      writer.println(i+1 + " " + outData[i]);
    }
    writer.close();
  }
}

class MeanFilterThread extends RecursiveAction{
  float[] array;
  int filterSize;
  int side;
  int size;
  int lo;
  int hi;
  static int SEQUENTIAL_CUTOFF = 25;
  public static float[] retArr;

  public MeanFilterThread(float[] array, float[] retArr, int filterSize, int l, int h){
    this.array = array;
    this.filterSize = filterSize;
    this.side = (filterSize-1)/2;
    this.size = array.length;
    this.retArr = retArr;
    hi = h;
    lo = l;
  }

  protected void compute(){
    if(hi - lo < SEQUENTIAL_CUTOFF) {
  		for (int i=lo; i<hi; i++){
  			if (i>=side && i<size-side){
  				retArr[i] = this.Mean(this.cutArray(i));
  			}
  			else{
  				retArr[i] = array[i];
  			}
  		}
    }
    else {
     MeanFilterThread left = new MeanFilterThread(array, retArr, filterSize, lo,(hi+lo)/2);
     MeanFilterThread right= new MeanFilterThread(array, retArr, filterSize, (hi+lo)/2,hi);
     left.fork();
     right.compute();
     left.join();
	  }
  }

  private float[] cutArray(int index){
    float[] retArra = new float[filterSize];
    int j = 0;
    for (int i=index-side; i<=index+side; i++){
      retArra[j] = array[i];
      j++;
    }
    return retArra;
  }
  private static float Mean(float[] array1){
    float sum = 0;
    for (float d : array1) sum += d;
    return sum/array1.length;
  }
}
