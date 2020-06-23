package fileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Matt
 */
public class CsvFile {
    private final HashMap<String, Integer> columns;
    private final ArrayList<String> headers;
    private final LinkedList<List<String>> rows;
    
    /**
     * Creates an empty CSV file interface
     */
    public CsvFile(){
        columns = new HashMap<>();
        headers = new ArrayList<>();
        rows = new LinkedList<>();
    }
    
    public final CsvFile addHeader(String header){
        header = header.toUpperCase();
        if(columns.containsKey(header)){
            throw new IllegalArgumentException("This already has header " + header + ". Cannot duplicate headers");
        }
        columns.put(header, headers.size());
        headers.add(header);
        // pad out rows
        rows.forEach((list)->{
            while(list.size() < headers.size()){
                list.add("");
            }
        });
        return this;
    }
    public final CsvFile addRow(List<Object> row){
        List<String> newRow = row.stream().map((obj)->{
            return obj.toString();
        }).collect(Collectors.toList());
        rows.add(newRow);
        return this;
    }
    
    public final boolean hasHeader(String header){
        return columns.containsKey(header.toUpperCase());
    }
    
    public final ArrayList<String> getColumn(String header){
        header = header.toUpperCase();
        if(!hasHeader(header)){
            throw new RuntimeException(String.format("Does not contain header %s. Valid headers are %s", header, String.join(", ", headers)));
        }
        ArrayList<String> ret = new ArrayList<>();
        int colIdx = columns.get(header);
        rows.forEach((List<String> row)->{
            ret.add(row.get(colIdx));
        });
        return ret;
    }
    
    public static final CsvFile from(List<List<Object>> nestedLists){
        CsvFile ret = new CsvFile();
        if(!nestedLists.isEmpty()){
            nestedLists.get(0).forEach((header)->ret.addHeader(header.toString()));
            for(int rowNum = 1; rowNum < nestedLists.size(); rowNum++){
                ret.addRow(nestedLists.get(rowNum));
            }
        }
        return ret;
    }
}