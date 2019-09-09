package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) { // field -> column name
        // use in List functionality
        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>(); // result keeper

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) { // if it does not contain such field yet, add it,
                values.add(aValue);         // ends up with a list of unique whichever column data is requested
            }
        }

        // sorting for ArrayList of strings:
        // https://beginnersbook.com/2013/12/how-to-sort-arraylist-in-java/
        Collections.sort(values);
        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {
        // List functionality
        // load data, if not already loaded
        loadData();

        ArrayList allJobsCopy = new ArrayList(allJobs); // it works for immutable objects

        return allJobsCopy;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) { // grab a row with one job

            String aValue = row.get(column).toLowerCase(); // grab the key:value (row:column) pair that reflects the searched column

            if (aValue.contains(value.toLowerCase())) { // compare to the value we want to be in this column
                jobs.add(row);
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>>findByValue(String value) {

        // loop inside allJobs for the value variable
        // if found add it to a new HashMap and stop iterating through other fields
        // make it case-insensitive
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>(); // store filtered jobs here

        for (HashMap<String, String> row : allJobs) { // grab one row
            // search for value in every column of it
            for (String column : row.values()) {
                if (column.toLowerCase().contains(value.toLowerCase())) {
                    jobs.add(row);
                    break;
                }
            }
        }

        return jobs;




    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}

// Search locations for cities, Search all for skills