package me.reckter.Generation.Utilities;

import me.reckter.Generation.BasicGeneration;
import me.reckter.Util;
import org.jocl.*;

import static org.jocl.CL.*;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 8/26/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RGB_util {

	public static float[] CalculateByGPU(String programm, float srcArrayA[], float srcArrayB[]) {


		int n = srcArrayA.length;

		if (n != srcArrayB.length) {
			Util.c_log("Size of Arrays didn't match!");
			throw new IllegalArgumentException();
		}

		Util.c_log("preparing the data for the GPU...");

		float dstArray[] = new float[1];

		Pointer srcA = Pointer.to(srcArrayA);
		Pointer srcB = Pointer.to(srcArrayB);
		Pointer dst = Pointer.to(dstArray);

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
		cl_mem memObjects[] = new cl_mem[3];
		memObjects[0] = clCreateBuffer(context,
				CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * n, srcA, null);
		memObjects[1] = clCreateBuffer(context,
				CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * n, srcB, null);
		memObjects[2] = clCreateBuffer(context,
				CL_MEM_READ_WRITE,
				Sizeof.cl_float * n, null, null);

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


		return dstArray;

	}


	public static void randomizePixel(byte[][][] pixel) {
		Util.c_log("randomizing Pixel");
		int r = 0;
		int g = 0;
		int b = 0;


		for (int x = 0; x < BasicGeneration.SIZE; x++) {
			for (int y = 0; y < BasicGeneration.SIZE; y++) {
				pixel[x][y][BasicGeneration.R] = (byte) r;
				pixel[x][y][BasicGeneration.G] = (byte) g;
				pixel[x][y][BasicGeneration.B] = (byte) b;

				r += 4096 / BasicGeneration.SIZE;
				if (r >= 256) {
					r = 0;
					g += 4096 / BasicGeneration.SIZE;
					if (g >= 256) {
						g = 0;
						b += 4096 / BasicGeneration.SIZE;
					}
				}
			}
		}

		for (int x = BasicGeneration.SIZE - 1; x >= 0; x--) {
			for (int y = BasicGeneration.SIZE - 1; y >= 0; y--) {
				int x2 = BasicGeneration.random.nextInt(x + 1);
				int y2 = BasicGeneration.random.nextInt(BasicGeneration.SIZE);
				if (x2 == x) {
					y2 = BasicGeneration.random.nextInt(y + 1);
				}

				byte tmpR = pixel[x][y][BasicGeneration.R];
				byte tmpG = pixel[x][y][BasicGeneration.G];
				byte tmpB = pixel[x][y][BasicGeneration.B];

				pixel[x][y][BasicGeneration.R] = pixel[x2][y2][BasicGeneration.R];
				pixel[x][y][BasicGeneration.G] = pixel[x2][y2][BasicGeneration.G];
				pixel[x][y][BasicGeneration.B] = pixel[x2][y2][BasicGeneration.B];

				pixel[x2][y2][BasicGeneration.R] = tmpR;
				pixel[x2][y2][BasicGeneration.G] = tmpG;
				pixel[x2][y2][BasicGeneration.B] = tmpB;
			}
		}

		Util.c_log("finished.");
	}
}
