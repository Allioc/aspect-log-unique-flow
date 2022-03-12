# aspect-log-unique-flow
To identify Unique flow and generate a Plant Uml sequence diagram

-Usage:
-
     * Pass three arguments
     * 1) folderPath, 2) baseLogFileName, 3) numLogFiles 4) isUniqueFlow
     * Args java com.metaopsis.unique.UniqueFlowIdentifier app_logs app.log 16 false
    
    java -cp ./out/production/aspect-log-unique-flow/ com.metaopsis.unique.UniqueFlowIdentifier ./app_logs app.log 16

- This tool has helped to drill down from 525 classes to 37 classes for Reverse Engineering.
- 
