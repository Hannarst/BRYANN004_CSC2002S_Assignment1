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

	float[] retArr = new float[lines];
	MedianFilterThread mainThread = new MedianFilterThread(inData, retArr, filterSize, 0, lines);
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

class MedianFilterThread extends RecursiveAction{
  float[] array;
  int filterSize;
  int side;
  int size;
  int lo;
  int hi;
  static int SEQUENTIAL_CUTOFF = 1000;
  public static float[] retArr;

  public MedianFilterThread(float[] array, float[] retArr, int filterSize, int l, int h){
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
				retArr[i] = this.median(this.cutArray(i));
			}
			else{
				retArr[i] = array[i];
			}
		}
    }
    else {
     MedianFilterThread left = new MedianFilterThread(array, retArr, filterSize, lo,(hi+lo)/2);
     MedianFilterThread right= new MedianFilterThread(array, retArr, filterSize, (hi+lo)/2,hi);
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
  private static float median(float[] array1){
    Arrays.sort(array1);
    return array1[(array1.length-1)/2];
  }
}
