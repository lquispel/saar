/**
 * 
 */
package saar.agents;

import java.util.*;
import java.text.*;
import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import saar.*;



/**
 * @author QuispelL
 *
 */


public class Census implements Steppable 
{
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Properties and constructors
	//
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final long serialVersionUID = 1L;
	
	private Double averageRiskPerception;
	private String logFileName;
	private BufferedWriter writer;
	
	public Double getAverageRiskPerception() { return averageRiskPerception ; }
	
	/**
	 * 
	 */
	public Census()
	{
		averageRiskPerception = 0.0;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//  Methods 
	//
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	public void initializeLogFile()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
 	   	Date date = new Date();
 	    logFileName = dateFormat.format(date);
        try {
             writer = new BufferedWriter(new FileWriter( logFileName + ".log" ));
             writer.write(logFileName + "\n");
             writer.newLine();
             writer.flush();
         } catch (IOException e) {
        	 System.out.println(e);
         }
	}
	
	/**
	 * 
	 */
	public void endSession()
	{
		log("Job ended.");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		log( dateFormat.format(date) );
		
	}
	
	
	/**
	 * 
	 */
	public void step(SimState state)
	{
		// calculate average risk perception
		Saar model = (Saar) state;
		Bag citizens = new Bag(model.getFriends().getAllNodes());
		int numberOfCitizens = citizens.size();
		averageRiskPerception = 0.0;
		
		for(int i = 0 ; i < numberOfCitizens ; i++)
			averageRiskPerception = averageRiskPerception + ((Citizen) citizens.get(i)).getRiskPerception();
		averageRiskPerception = averageRiskPerception / numberOfCitizens;
		
		// write data to file
		try 
		{
			writer.newLine();
			writer.write( String.valueOf(model.schedule.getSteps()) + ",");
			writer.write(averageRiskPerception.toString());   
			writer.write(",");
		}
		catch (IOException e) {
				// TODO: handle file error
		}
		
	}
	
	/**
	 * 
	 * @param logString
	 */
	public void log(String logString)
	{
		System.out.println(logString);
		try {
			writer.write(logString);
			writer.flush();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * 
	 */
	public void flush()
	{
		try {
			writer.flush();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
}




