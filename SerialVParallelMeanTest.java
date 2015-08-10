import java.io.FileNotFoundException;

public class SerialVParallelMeanTest{
  static final String[] files = {"inp1.txt", "inp1B.txt", "inp2.txt", "inp2B.txt", "inp3.txt", "inp3B.txt", "inp4.txt"};
	//static final String[] files = {"inp1.txt"};
	static final String[] filters = {"3", "5", "7", "9", "11", "13", "15", "17", "19", "21"};
	static final int ITERATIONS = 50;

	public static void main(String[] args) throws FileNotFoundException {

		for (String file: files){
			System.out.println();
			System.out.println("***************");
			System.out.println("File: " + file);
			System.out.println("***************");

			float totFileSpeedup = 0;
			for (String filter: filters){
				System.out.println("***************");
				System.out.println("Filter size: " + filter);
				System.out.println("***************");

				long serialTotal = 0;
				long parallelTotal = 0;

				for (int i=0; i<ITERATIONS; i++){
					long start = System.currentTimeMillis();
					String[] param = {file, filter, "out.txt"};
					SerialMeanFilterUI.main(param);
					long serialTime = System.currentTimeMillis()-start;
					serialTotal += serialTime;

					start = System.currentTimeMillis();
					ParallelMeanFilterUI.main(param);
					long parallelTime = System.currentTimeMillis()-start;
					parallelTotal += parallelTime;
				}


				long serAv = serialTotal/ITERATIONS;
				System.out.println("* Serial average: " + serAv);

				long parAv = parallelTotal/ITERATIONS;
				System.out.println("* Parallel average: " + parAv);
				float avFilterSpeedup = serialTotal/(float)parallelTotal;
				System.out.println("* Speedup for filter: " + avFilterSpeedup);
				totFileSpeedup += avFilterSpeedup;
			}

			System.out.println("* Speedup for File: " + totFileSpeedup/filters.length);
		}

	}
}
