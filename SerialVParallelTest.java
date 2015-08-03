import java.io.FileNotFoundException;

public class SerialVParallelTest{
//static final String[] files = {"inp1.txt", "inp1B.txt", "inp2.txt", "inp2B.txt", "inp3.txt", "inp3B.txt", "inp4.txt"};
	static final String[] files = {"inp1.txt"};
	static final String[] filters = {"3", "5", "7", "9", "11", "19", "21"};
	static final int PARALLEL_ITERATIONS = 20;
	static final int SERIAL_ITERATIONS = 20;

	public static void main(String[] args) throws FileNotFoundException {

		for (String file: files){
			System.out.println();
			System.out.println("***************");
			System.out.println("File: " + file);
			System.out.println("***************");

			for (String filter: filters){
				System.out.println("***************");
				System.out.println("Filter size: " + filter);
				System.out.println("***************");

				long serialTotal = 0;
				for (int i=0; i<SERIAL_ITERATIONS; i++){
					long start = System.currentTimeMillis();
					String[] param = {file, filter, "out.txt"};
					SerialMedianFilterUI.main(param);
					long serialTime = System.currentTimeMillis()-start;
					serialTotal += serialTime;
				}

				long parallelTotal = 0;
				for (int i=0; i<PARALLEL_ITERATIONS; i++){
					long start = System.currentTimeMillis();
						String[] param = {file, filter, "out.txt"};
					ParallelMedianFilterUI.main(param);
					long parallelTime = System.currentTimeMillis()-start;
					parallelTotal += parallelTime;
				}

				System.out.println("* Serial average: " + serialTotal/SERIAL_ITERATIONS);
				System.out.println("* Parallel average: " + parallelTotal/PARALLEL_ITERATIONS);


			}
		}
	}
}
