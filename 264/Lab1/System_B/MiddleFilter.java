import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/******************************************************************************************************************
* File:MiddleFilter.java
* Project: Lab 1
* Copyright:
*   Copyright (c) 2020 University of California, Irvine
*   Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.1 January 2020 - Revision for SWE 264P: Distributed Software Architecture, Winter 2020, UC Irvine.
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
* Parameters: None
* Internal Methods: None
******************************************************************************************************************/

public class MiddleFilter extends FilterFramework
{
	// Method to write to CSV, but adjusted to take a regular string.
	public void data_to_csv(String s, File file) {
		
		try (FileWriter fileWriter = new FileWriter (file, true)){
			fileWriter.write(s + "\n");

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
		
		// Track the previous two altitude values
		Double prevAlt1 = null;
		Double prevAlt2 = null;
		// Track if this set of measurements has been modified. This additional data is sent after each measurerment.
		int isModified = 0;
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		
		// Create an array to store the frame data. This is so that measurements can be added out of order.
		String[] wildValueEntry = new String[5];
		File outputFile = new File ("WildPoints.csv");

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
				byte[] idBytes = new byte[IdLength];
				for (i=0; i<IdLength; i++ )
				{
					
					databyte = ReadFilterInputPort();	// This is where we read the byte from the stream...
					idBytes[i] = databyte;  // Store the byte in the id bytes array, to be output later...
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
				byte[] measurementBytes = new byte[MeasurementLength];
				for (i=0; i<MeasurementLength; i++ )
				{
					databyte = ReadFilterInputPort();
					measurementBytes[i] = databyte;  // Store the measurement byte in an array to be sent out later
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
					wildValueEntry[0] = (TimeStampFormat.format(TimeStamp.getTime()).toString() + ",");
				}

				/****************************************************************************
				// Pick up all the measurements and store in the string array
				****************************************************************************/
				if ( id == 1 )
				{
					wildValueEntry[1] = (String.valueOf(Double.longBitsToDouble(measurement)) + ",");
				}
				
				else if ( id == 3 )
				{
					wildValueEntry[3] = (String.valueOf(Double.longBitsToDouble(measurement)) + ",");
				}

				else if ( id == 4 )
				{
					wildValueEntry[4] = (String.valueOf(Double.longBitsToDouble(measurement)));
					
				}
				else if ( id == 2 )
				{
					// Check the previous two conditions and adjust the current altitude
					// If the altitude is too much of a change.
					Double curAlt = Double.longBitsToDouble(measurement);
					// Invalid altitude condition
					if (prevAlt1 != null && Math.abs(curAlt - prevAlt1) > 100) {
						if (prevAlt2 != null) {
							// Take avg of last 2
							curAlt = (prevAlt1 + prevAlt2) / 2;
						} else {
							curAlt = prevAlt1;
						}
						// Send original measurement (measurement) to csv file and send along updated measurement
						wildValueEntry[2] = (String.valueOf(Double.longBitsToDouble(measurement)) + ",");
						String result = String.join(",", wildValueEntry);
						data_to_csv(result, outputFile);
						isModified = 1;
						// Modify measurementBytes array to include the new number
						long newMeasurement = Double.doubleToLongBits(curAlt);
                        for (i = 0; i < MeasurementLength; i++) {
                            measurementBytes[i] = (byte)((newMeasurement >> ((7-i) * 8)) & 0xFF);
                        }
					}
					
					// Update prev1 prev2 values. We are tracking the adjusted numbers, not the original numbers.
					prevAlt2 = prevAlt1;
					prevAlt1 = curAlt;
					System.out.println(prevAlt1);
					System.out.println(prevAlt2);

				}
				// Write to filter's output once done processing
				 // Write the complete ID
				for (i = 0; i < IdLength; i++) {
                    WriteFilterOutputPort(idBytes[i]);
                }

				// Write the complete measurement
                for (i = 0; i < MeasurementLength; i++) {
                    WriteFilterOutputPort(measurementBytes[i]);
                }

				// Write if the measurement has been modified.
				WriteFilterOutputPort((byte)isModified);

				isModified = 0;
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