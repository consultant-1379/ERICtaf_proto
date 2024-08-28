package com.ericsson.nms.rv.taf.data;

import com.ericsson.cifwk.taf.annotations.DataSource;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.utils.FileFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserData {

    private static final Logger log = LoggerFactory.getLogger(UserData.class);

    public UserData() {
        log.info("I am a data source called UserData");
    }

    @DataSource
    public List<Map<String, User>> users() {
        log.info("I am creating a user");
        List userList = new ArrayList();
        FileReader fileReader;
        CsvMapReader csvMapReader;
        try {
            fileReader = new FileReader(getFileName());
            csvMapReader = new CsvMapReader(fileReader, CsvPreference.EXCEL_PREFERENCE);
            try {
                String[] headers = csvMapReader.getHeader(true);
                Map<String, String> row;
                Map<String, Object> rowData = null;
                while ((row = csvMapReader.read(headers)) != null) {
                    log.info("Creating User name = {} and password = {}", row.get("username"), row.get("password"));
                    rowData = new HashMap<String, Object>();
                    User user = new User();
                    user.setUsername(row.get("username"));
                    user.setPassword(row.get("password"));
                    if(row.get("type")!=null){
                        user.setType(UserType.valueOf(row.get("type").toUpperCase()));
                    }else{
                        user.setType(UserType.OPER);
                    }
                    rowData.put("user", user);
                }
                userList.add(rowData);
            } catch (Exception any) {
                log.warn("File {} does not have proper format or content", getFileName());
            } finally {
                csvMapReader.close();
                fileReader.close();
                log.info("List of users: {}", userList.toArray());
            }
        }catch (IOException e){
            log.warn("Problem reading file {}. Cause: {}. File will be ignored",getFileName().getName(),e.getMessage());
        }
        return userList;
    }

    public static String dataSourceFileName;
    private File getFileName() {
        String name = null;
        for (Object attr : DataHandler.getAttributes().keySet()){
            if(attr.toString().startsWith("dataprovider") && attr.toString().endsWith("class")){
                if(DataHandler.getAttribute(attr.toString()).toString().equals(this.getClass().getCanonicalName())){
                    name = attr.toString().split("\\.")[1];
                    break;
                }
            }
        }
        if(name!=null){
            dataSourceFileName = DataHandler.getAttribute("dataprovider." + name + ".source" ).toString();
        }else{
            throw new NullPointerException("Please provide data");
        }
        return new File(FileFinder.findFile(dataSourceFileName).get(0));
    }
}
