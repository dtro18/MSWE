/******************************************************************************************************************
* File:SinkFilter.java
* Project: Lab 1
* Copyright:
*   Copyright (c) 2020 University of California, Irvine
*   Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.1 January 2020 - Revision for SWE 264P: Distributed Software Architecture, Winter 2020, UC Irvine.
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
* This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This particular
* filter reads some input from the filter's input port and does the following:
*	1) It parses the input stream and "decommutates" the measurement ID
*	2) It parses the input steam for measurments and "decommutates" measurements, storing the bits in a long word.
* This filter illustrates how to convert the byte stream data from the upstream filterinto useable data found in
* the stream: namely time (long type) and measurements (double type).
* Parameters: None
* Internal Methods: None
******************************************************************************************************************/

import java.util.*;						// This class is used to interpret time words
import java.io.*;
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.



public class SinkFilter extends FilterFramework
{

	// Method to write to CSV, which will be called once we have one frame of data.
	public void data_to_csv(StringBuilder sb, File file) {
		
		try (FileWriter fileWriter = new FileWriter (file, true)){
			// Need to figure out where to define the file instance. Class variable?
			fileWriter.write(sb.toString() + "\n");

		} catch (IOException e) {
			System.err.println("IO Error.");
		}
	}

	public void run()
    {
		/************************************************************************************
		*	TimeStamp is used to compute time using java.util's Calendar class.
		* 	TimeStampFormat is used to format the time value so that it can be easily printed
		*	to the terminal.
		*************************************************************************************/
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss:SS");

		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		int IdLength = 4;				// This is the length of IDs in the byte stream
		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter

		// New variables to track the built string and the file to be output to.
		StringBuilder sb = new StringBuilder();
		File outputFile = new File ("OutputA.csv");

		// Populate column headers of the csv file
		try (FileWriter fileWriter = new FileWriter (outputFile, true)){
			fileWriter.write("Time,Velocity,Altitude,Pressure,Temperature" + "\n");

		} catch (IOException e) {
			System.err.println("IO Error.");
		}

		while (true)
		{
			try
			{
				/***************************************************************************
				// We know that the first data coming to this filter is going to be an ID and
				// that it is IdLength long. So we first get the ID bytes.
				****************************************************************************/
				id = 0;
				for (i=0; i<IdLength; i++ )
				{
					databyte = ReadFilterInputPort();	// This is where we read the byte from the stream...
					id = id | (databyte & 0xFF);		// We append the byte on to ID...
					if (i != IdLength-1)				// If this is not the last byte, then slide the
					{									// previously appended byte to the left by one byte
						id = id << 8;					// to make room for the next byte we append to the ID
					}
					bytesread++;						// Increment the byte count
				}
				/****************************************************************************
				// Here we read measurements. All measurement data is read as a stream of bytes
				// and stored as a long value. This permits us to do bitwise manipulation that
				// is neccesary to convert the byte stream into data words. Note that bitwise
				// manipulation is not permitted on any kind of floating point types in Java.
				// If the id = 0 then this is a time value and is therefore a long value - no
				// problem. However, if the id is something other than 0, then the bits in the
				// long value is really of type double and we need to convert the value using
				// Double.longBitsToDouble(long val) to do the conversion which is illustrated below.
				*****************************************************************************/
				measurement = 0;
				for (i=0; i<MeasurementLength; i++ )
				{
					databyte = ReadFilterInputPort();
					measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...
					if (i != MeasurementLength-1)					// If this is not the last byte, then slide the
					{												// previously appended byte to the left by one byte
						measurement = measurement << 8;				// to make room for the next byte we append to the
																	// measurement
					}
					bytesread++;									// Increment the byte count
				}

				/****************************************************************************
				// Here we look for an ID of 0 which indicates this is a time measurement.
				// Every frame begins with an ID of 0, followed by a time stamp which correlates
				// to the time that each proceeding measurement was recorded. Time is stored
				// in milliseconds since Epoch. This allows us to use Java's calendar class to
				// retrieve time and also use text format classes to format the output into
				// a form humans can read. So this provides great flexibility in terms of
				// dealing with time arithmetically or for string display purposes. This is
				// illustrated below.
				****************************************************************************/
				if ( id == 0 )
				{
					TimeStamp.setTimeInMillis(measurement);
					sb.append(TimeStampFormat.format(TimeStamp.getTime()).toString() + ",");
				}

				/****************************************************************************
				// Reading all the measurements and adding them to the built string.
				****************************************************************************/
				if ( id == 1 )
				{
					sb.append(String.valueOf(Double.longBitsToDouble(measurement)) + ",");
				}
				System.out.print( "\n" );

				if ( id == 2 )
				{
					sb.append(String.valueOf(Double.longBitsToDouble(measurement)) + ",");
				}
				System.out.print( "\n" );

				if ( id == 3 )
				{
					sb.append(String.valueOf(Double.longBitsToDouble(measurement)) + ",");
				}
				System.out.print( "\n" );

				if ( id == 4 )
				{
					sb.append(String.valueOf(Double.longBitsToDouble(measurement)));
					// Calling the csv method to shoot the built string to the existing csv file, and clearing the string afterwards.
					data_to_csv(sb, outputFile);
					sb.setLength(0);
				}
				System.out.print( "\n" );
			}
			/*******************************************************************************
			*	The EndOfStreamExeception below is thrown when you reach end of the input
			*	stream. At this point, the filter ports are closed and a message is
			*	written letting the user know what is going on.
			********************************************************************************/
			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread );
				break;
			}
		} // while
   } // run
}