package me.reckter.Generation.Picture;

import me.reckter.Generation.Utilities.Coordinates;
import me.reckter.Generation.Utilities.Pair;
import me.reckter.Log;
import me.reckter.Util;
import org.jocl.*;

import java.util.ArrayList;
import java.util.List;

import static org.jocl.CL.*;

/**
 * Created by reckter on 14.03.2015.
 */
public class PictureRandomCLSort extends PictureRandomSort {


	public PictureRandomCLSort(String file) {
		super(file);
	}

	String programm =
			"__kernel void sampleKernel(__global const byte *source1, __global const byte *should, __global byte *source2, __global int *switch){" +
					"" +
					"   int gid = get_global_id(0);" +
					"   " +
					"}";

	@Override
	public void randomSwapPixels(int maxIter, int maxStream) {

		int n = SIZE * SIZE * 3;
		byte[] clPixel1 = new byte[SIZE * SIZE * 3];
		byte[] clPixel2 = new byte[SIZE * SIZE * 3];
		byte[] clPixeShould = new byte[SIZE * SIZE * 3];

		Log.info("copying arrays...");
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				//noinspection PointlessArithmeticExpression
				clPixel1[x * (SIZE + 3) + y * 3 + 0] = pixelShould[x][y][R];
				clPixel1[x * (SIZE + 3) + y * 3 + 1] = pixelShould[x][y][G];
				clPixel1[x * (SIZE + 3) + y * 3 + 2] = pixelShould[x][y][B];
				//noinspection PointlessArithmeticExpression

				clPixel2[x * (SIZE + 3) + y * 3 + 0] = pixelShould[x][y][R];
				clPixel2[x * (SIZE + 3) + y * 3 + 1] = pixelShould[x][y][G];
				clPixel2[x * (SIZE + 3) + y * 3 + 2] = pixelShould[x][y][B];

				//noinspection PointlessArithmeticExpression

				clPixeShould[x * (SIZE + 3) + y * 3 + 0] = pixelShould[x][y][R];
				clPixeShould[x * (SIZE + 3) + y * 3 + 1] = pixelShould[x][y][G];
				clPixeShould[x * (SIZE + 3) + y * 3 + 2] = pixelShould[x][y][B];
			}
		}


		int[] switchesArray = generateSwitches(maxStream);

		Util.c_log("preparing the data for the GPU...");

		Pointer srcA = Pointer.to(clPixeShould);
		Pointer srcB = Pointer.to(clPixel1);
		Pointer dst = Pointer.to(clPixel2);
		Pointer switches = Pointer.to(switchesArray);

		Util.c_log("setting up OpenCL stuff");

		// The platform, device type and device number
		// that will be used
		final int platformIndex = 0;
		final long deviceType = CL_DEVICE_TYPE_ALL;
		final int deviceIndex = 0;

		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);

		// Obtain the number of platforms
		int numPlatformsArray[] = new int[1];
		clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Obtain a platform ID
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
		clGetPlatformIDs(platforms.length, platforms, null);
		cl_platform_id platform = platforms[platformIndex];

		// Initialize the context properties
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

		// Obtain the number of devices for the platform
		int numDevicesArray[] = new int[1];
		clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
		int numDevices = numDevicesArray[0];

		// Obtain a device ID
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
		cl_device_id device = devices[deviceIndex];

		// Create a context for the selected device
		cl_context context = clCreateContext(
				contextProperties, 1, new cl_device_id[]{device},
				null, null, null);

		// Create a command-queue for the selected device
		cl_command_queue commandQueue =
				clCreateCommandQueue(context, device, 0, null);

		// Allocate the memory objects for the input- and output data
		cl_mem memObjects[] = new cl_mem[4];
		memObjects[0] = clCreateBuffer(context,
				CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * n, srcA, null);
		memObjects[1] = clCreateBuffer(context,
				CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * n, srcB, null);
		memObjects[2] = clCreateBuffer(context,
				CL_MEM_READ_WRITE,
				Sizeof.cl_float * n, dst, null);
		memObjects[3] = clCreateBuffer(context,
				CL_MEM_READ_ONLY,
				Sizeof.cl_float * switchesArray.length, switches, null);

		Util.c_log("compiling...");

		// Create the program from the source code
		cl_program program = clCreateProgramWithSource(context,
				1, new String[]{programm}, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);

		// Create the kernel
		cl_kernel kernel = clCreateKernel(program, "sampleKernel", null);

		// Set the arguments for the kernel
		clSetKernelArg(kernel, 0,
				Sizeof.cl_mem, Pointer.to(memObjects[0]));
		clSetKernelArg(kernel, 1,
				Sizeof.cl_mem, Pointer.to(memObjects[1]));
		clSetKernelArg(kernel, 2,
				Sizeof.cl_mem, Pointer.to(memObjects[2]));

		// Set the work-item dimensions
		long global_work_size[] = new long[]{n};
		long local_work_size[] = new long[]{1};


		Util.c_log("executing...");


		// Execute the kernel
		clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
				global_work_size, local_work_size, 0, null, null);

		Util.c_log("fetching output...");

		// Read the output data

		clEnqueueReadBuffer(commandQueue, memObjects[0], CL_TRUE, 0,
				n * Sizeof.cl_float, srcA, 0, null, null);

		clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
				n * Sizeof.cl_float, dst, 0, null, null);


		Util.c_log("cleaning up.");
		// Release kernel, program, and memory objects
		clReleaseMemObject(memObjects[0]);
		clReleaseMemObject(memObjects[1]);
		clReleaseMemObject(memObjects[2]);
		clReleaseKernel(kernel);
		clReleaseProgram(program);
		clReleaseCommandQueue(commandQueue);
		clReleaseContext(context);

		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				pixel[x][y][R] = clPixel2[x * (SIZE + 3) + y * 3 + R];
				pixel[x][y][G] = clPixel2[x * (SIZE + 3) + y * 3 + G];
				pixel[x][y][B] = clPixel2[x * (SIZE + 3) + y * 3 + B];
			}
		}
	}


	@SuppressWarnings("PointlessArithmeticExpression")
	private int[] generateSwitches(int count) {
		Log.info("generating switches...");
		List<Pair<Coordinates, Coordinates>> switches = new ArrayList<>(count);
		int i = 0;
		while (i < count) {
			int x1 = random.nextInt(SIZE);
			int y1 = random.nextInt(SIZE);

			int x2 = random.nextInt(SIZE);
			int y2 = random.nextInt(SIZE);

			Pair<Coordinates, Coordinates> cords = new Pair<>(new Coordinates(x1, y1), new Coordinates(x2, y2));

			if (!switches.contains(cords)) {
				i++;
				switches.add(cords);
			}
		}

		int[] ret = new int[count * 4];

		for (i = 0; i < count; i++) {
			ret[i * 4 + 0] = switches.get(i).first.getX();
			ret[i * 4 + 1] = switches.get(i).first.getY();

			ret[i * 4 + 2] = switches.get(i).second.getX();
			ret[i * 4 + 3] = switches.get(i).second.getY();
		}

		Log.info("done");
		return ret;
	}
}
