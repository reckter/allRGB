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

        if(n != srcArrayB.length) {
            Util.c_log("Size of Arrays didn't match!");
            throw new IllegalArgumentException();
        }

        Util.c_log("preparing the data for the GPU...");

        float dstArray[] = new float[n];

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
                1, new String[]{ programm }, null, null);

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

		    boolean[][] colors = new boolean[BasicGeneration.SIZE][BasicGeneration.SIZE];
		    int x,y;
		    int k = 0;
		    byte r,g,b;
		    for( r = 0; r < 256; r++) {
			    if((float) r / 256f * 100 > k + 10) {
				    k += 10;
				    Util.c_log(k + "%");
			    }
			    for( g = 0; g < 256; g++) {
				    for( b = 0; b < 256; b++) {
					    x = (int) ( Math.random() * BasicGeneration.SIZE);
					    y = (int) ( Math.random() * BasicGeneration.SIZE);
					    while(colors[x][y]){
						    x = (int) ( Math.random() * BasicGeneration.SIZE);
						    y = (int) ( Math.random() * BasicGeneration.SIZE);
					    }
					    colors[x][y] = true;
					    pixel[x][y][BasicGeneration.R] = r;
					    pixel[x][y][BasicGeneration.G] = g;
					    pixel[x][y][BasicGeneration.B] = b;
				    }
			    }
		    }
		    Util.c_log("finished.");

	    /*
        Util.c_log("randomizing Pixel...");
        FastLinkedList colors = new FastLinkedList();
        for(Integer i = 0; i < BasicGeneration.SIZE * BasicGeneration.SIZE; i++) {
            colors.add(i);
        }

        int random, tmp,k = 0;
        boolean isUsed;

        for(int x = 0;x < BasicGeneration.SIZE; x++) {
            if(k < 70) {
                if((float) x / (float) (BasicGeneration.SIZE) * 100 > k + 10) {
                    k += 10;
                    Util.c_log(k + "% cleaning up colors");
                    colors.cleanUp();
                }
            } else if(k < 90){
                if((float) x / (float) (BasicGeneration.SIZE) * 100 > k + 5) {
                    k += 5;
                    Util.c_log(k + "% cleaning up colors");
                    colors.cleanUp();
                }
            } else {
                if((float) x / (float) (BasicGeneration.SIZE) * 100 > k + 1) {
                    k += 1;
                    Util.c_log(k + "% cleaning up colors");
                    colors.cleanUp();
                }
            }


            for(int y = 0; y < BasicGeneration.SIZE; y++) {
                if(y % 100 == 0) {
                    //      Util.c_log("(" + x + "|" + y + ")");
                }
                isUsed = true;
                while(isUsed){
                    random = (int) (Math.random() * colors.size());
                    isUsed = false;
                    try {
                        tmp = colors.get(random).getValue();
                        pixel[x][y][BasicGeneration.R] = (short) ((tmp) % 256);
                        pixel[x][y][BasicGeneration.G] = (short) (((tmp) / 256) % 256);
                        pixel[x][y][BasicGeneration.B] = (short) (((tmp) / 256 / 256));
                        colors.delete(random);
                    }
                    catch (IndexOutOfBoundsException e) {
                        isUsed = true;
                    }
                }
            }
        }

        */
    }
}
