import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.*;

public class ParallelMedianFilterUI{

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

    String outData = fjPool.invoke(new MedianFilterThread(inData, filterSize, 0, lines));

    PrintWriter writer = new PrintWriter(outFile);
    writer.println(lines);
    writer.print(outData);
    writer.close();
  }
}

class MedianFilterThread extends RecursiveTask<String>{
  float[] array;
  int filterSize;
  int side;
  int size;
  int lo;
  int hi;
  int SEQUENTIAL_CUTOFF = 1000;

  public MedianFilterThread(float[] array, int filterSize, int l, int h){
    this.array = array;
    this.filterSize = filterSize;
    this.side = (filterSize-1)/2;
    this.size = array.length;
    hi = h;
    lo = l;
  }

  protected String compute(){
    if(hi - lo < SEQUENTIAL_CUTOFF) {
      String retArr = "";
      for (int i=lo; i<hi; i++){
        if (i>=side && i<size-side){
          retArr += (i+1) +" "+ this.median(this.cutArray(i)) + "\n";
        }
        else{
          retArr += (i+1) +" "+ array[i] + "\n";
        }
      }
      return retArr;
    }
    else {
     MedianFilterThread left = new MedianFilterThread(array, filterSize, lo,(hi+lo)/2);
     MedianFilterThread right= new MedianFilterThread(array, filterSize, (hi+lo)/2,hi);
     left.fork();
     String rightAns = right.compute();
     String leftAns = left.join();
     return leftAns+rightAns;
   }
  }

  private float[] cutArray(int index){
    float[] retArr = new float[filterSize];
    int j = 0;
    for (int i=index-side; i<=index+side; i++){
      retArr[j] = array[i];
      j++;
    }
    return retArr;
  }
  private static float median(float[] array1){
    Arrays.sort(array1);
    return array1[(array1.length-1)/2];
  }
}
