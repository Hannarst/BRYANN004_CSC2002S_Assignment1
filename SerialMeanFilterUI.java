import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;

public class SerialMeanFilterUI{
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

    MeanFilter filt = new MeanFilter(inData, filterSize);
    float[] outData = filt.filter();

    PrintWriter writer = new PrintWriter(outFile);
    writer.println(lines);
    for (int i=0; i<lines; i++){
      writer.println(i+1 + " " + outData[i]);
    }
    writer.close();

  }
}
class MeanFilter{
  float[] array;
  int filterSize;
  int side;
  int size;

  public MeanFilter(float[] array, int filterSize){
    this.array = array;
    this.filterSize = filterSize;
    this.side = (filterSize-1)/2;
    this.size = array.length;
  }

  public float[] filter(){
    float[] retArr = new float[size];
    for (int i=0; i<size; i++){
      if (i>=side && i<size-side){
        retArr[i] = this.Mean(this.cutArray(i));
      }
      else{
        retArr[i] = array[i];
      }
    }
    return retArr;
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
  private static float Mean(float[] array1){
    float sum = 0;
    for (float d : array1) sum += d;
    return sum/array1.length;
  }
}
