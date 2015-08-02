import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;

public class SerialMedianFilterUI{
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

    MedianFilter filt = new MedianFilter(inData, filterSize);
    float[] outData = filt.filter();

    PrintWriter writer = new PrintWriter(outFile);
    writer.println(lines);
    for (int i=0; i<lines; i++){
      writer.println(i+1 + " " + outData[i]);
    }
    writer.close();
    
  }
}
class MedianFilter{
  float[] array;
  int filterSize;
  int side;
  int size;

  public MedianFilter(float[] array, int filterSize){
    this.array = array;
    this.filterSize = filterSize;
    this.side = (filterSize-1)/2;
    this.size = array.length;
  }

  public float[] filter(){
    float[] retArr = new float[size];
    for (int i=0; i<size; i++){
      if (i>=side && i<size-side){
        retArr[i] = this.median(this.cutArray(i));
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
  private static float median(float[] array1){
    Arrays.sort(array1);
    return array1[(array1.length-1)/2];
  }
}
