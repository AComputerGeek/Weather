import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

/**
* 
* @author: Amir Armion 
* 
* @version: V.01
* 
*/
public class AssignmentThreePartTwo 
{

    public CSVRecord coldestHourInFile(CSVParser parser)
    {
        CSVRecord coldest = null;

        for(CSVRecord record: parser)
        {
            if(coldest == null)
            {
                coldest = record;
            }
            else
            {
                if((Double.parseDouble(record.get("TemperatureF")) < Double.parseDouble(coldest.get("TemperatureF"))) && (Double.parseDouble(record.get("TemperatureF")) != -9999))
                {
                    coldest = record;
                }
            }
        }

        return coldest;
    }   

    public CSVRecord coldestInManyFiles()
    {
        CSVRecord coldest = null;
        
        DirectoryResource dr = new DirectoryResource();

        for(File f: dr.selectedFiles())
        {
            FileResource file   = new FileResource(f);
            CSVParser    parser = file.getCSVParser();           

            if(coldest == null)
            {
                coldest = coldestHourInFile(parser);
            }
            else
            {
                if(Double.parseDouble(coldestHourInFile(parser).get("TemperatureF")) < Double.parseDouble(coldest.get("TemperatureF")))
                {
                    coldest = coldestHourInFile(parser);
                }
            }
        }

        return coldest;
    }

    public File fileWithColdestTemperature()
    {
        CSVRecord coldest        = null;
        CSVRecord currentColdest = null;
        File      coldestFile    = null;

        DirectoryResource dr     = new DirectoryResource();

        for(File f: dr.selectedFiles())
        {
            FileResource file   = new FileResource(f);
            CSVParser    parser = file.getCSVParser();

            currentColdest      = coldestHourInFile(parser);

            if(coldest == null)
            {
                coldest = currentColdest;
            }
            else
            {
                if(Double.parseDouble(currentColdest.get("TemperatureF")) < Double.parseDouble(coldest.get("TemperatureF")))
                {
                    coldest     = currentColdest;
                    coldestFile = f;
                }
            }
        }

        return coldestFile;
    }

    public CSVRecord lowestHumidityInFile(CSVParser parser)
    {
        CSVRecord lowestHumidity = null;
        int       humidityNumber = 0;

        for(CSVRecord record: parser)
        {
            String humidity = record.get("Humidity");

            if(lowestHumidity == null)
            {
                lowestHumidity = record;
            }
            else
            {
                if(!humidity.contains("N/A"))
                {
                    humidityNumber = Integer.parseInt(humidity);

                    if(humidityNumber < Integer.parseInt(lowestHumidity.get("Humidity")))
                    {
                        lowestHumidity = record;
                    }
                }
            }
        }

        return lowestHumidity;
    }

    public CSVRecord lowestHumidityInManyFiles()
    {
        CSVRecord lowestHumidity = null;
        
        DirectoryResource dr     = new DirectoryResource();

        for(File f: dr.selectedFiles())
        {
            FileResource fr     = new FileResource(f);
            CSVParser    parser = fr.getCSVParser();
            CSVRecord    record = lowestHumidityInFile(parser);

            if(lowestHumidity == null)
            {
                lowestHumidity = record;
            }
            else
            {
                int humidityNumber = Integer.parseInt(record.get("Humidity"));

                if(humidityNumber < Integer.parseInt(lowestHumidity.get("Humidity")))
                {
                    lowestHumidity = record;
                }
            }
        }

        return lowestHumidity;     
    }

    public double averageTemperatureInFile(CSVParser parser)
    {
        double sumRecordsTemp     = 0.0;
        int    countRecords       = 0;
        double averageTemperature = 0.0;

        for(CSVRecord record: parser)
        {
            sumRecordsTemp += Double.parseDouble(record.get("TemperatureF"));
            countRecords++;
        }

        averageTemperature = sumRecordsTemp / countRecords;

        return averageTemperature;
    }

    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value)
    {
        double sumRecordsTemp     = 0.0;
        int    countRecords       = 0;
        double averageTemperature = 0.0;
        int    humidityNumber     = 0;

        for(CSVRecord record: parser)
        {
            String humidity = record.get("Humidity");

            if(!humidity.contains("N/A"))
            {
                humidityNumber = Integer.parseInt(humidity);

                if(value <= humidityNumber)
                {
                    sumRecordsTemp += Double.parseDouble(record.get("TemperatureF"));
                    countRecords++;                    
                }
            }
        }

        if(sumRecordsTemp != 0)
        {
            averageTemperature = sumRecordsTemp / countRecords;
        }

        return averageTemperature;
    }

    public void testColdestHourInFile()
    {
        FileResource f      = new FileResource();
        CSVParser    parser = f.getCSVParser();

        System.out.println(coldestHourInFile(parser).get("TemperatureF"));

    }

    public void testFilePathWithColdestTemperature()
    {
        System.out.println(fileWithColdestTemperature());
    }

    public void CompleteTestFileWithColdestTemperatureInManyFile()
    {
        File f = fileWithColdestTemperature();

        System.out.println("The coldest day was in file " + f.getName());

        FileResource fr     = new FileResource(f);
        CSVParser    parser = fr.getCSVParser();
        CSVRecord    record = coldestHourInFile(parser);

        System.out.println("The coldest temperature on that day was " + record.get("TemperatureF"));

        System.out.println("All the temperature on the coldest day were: \n");

        CSVParser parser2 = fr.getCSVParser();

        for(CSVRecord rec: parser2)
        {
            System.out.println(rec.get("DateUTC") + ": " + rec.get("TemperatureF"));
        }
    }

    public void testLowestHumidityInFile()
    {
        FileResource f      = new FileResource();
        CSVParser    parser = f.getCSVParser();      
        CSVRecord    record = lowestHumidityInFile(parser);

        System.out.println("Lowest Humidity was " + record.get("Humidity") + " at " + record.get("DateUTC"));
    }

    public void testLowestHumidityInManyFiles()
    {
        CSVRecord record = lowestHumidityInManyFiles();
        
        System.out.println("Lowest Humidity was " + record.get("Humidity") + " at " + record.get("DateUTC"));
    }

    public void testAverageTemperatureInFile()
    {
        FileResource f                  = new FileResource();
        CSVParser    parser             = f.getCSVParser();   
        
        double averageTemperature = averageTemperatureInFile(parser);

        System.out.println("Average temperature in file is " + averageTemperature);
    }

    public void testAverageTemperatureWithHighHumidityInFile()
    {
        FileResource f                  = new FileResource();
        CSVParser    parser             = f.getCSVParser();      
        double       averageTemperature = averageTemperatureWithHighHumidityInFile(parser, 80);

        if(averageTemperature == 0.0)
        {
            System.out.println("No temperature with that humidity!");
        }
        else
        {
            System.out.println("Average temperature when high humidity is " + averageTemperature);
        }
    }
}
