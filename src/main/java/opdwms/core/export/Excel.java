/*
 * Copyright 2016 Anthony.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package opdwms.core.export;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
@Transactional(readOnly = true)
@Service("excelGridExportService")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Excel extends AbstractExcelExport{

    /**
     * Generate an excel file from an array of strings passed to the service
     * 
     * @param   data
     * @throws  IOException
     */
    @Override
    public void generateDoc(List<String[]> data) throws IOException {
        // Check if all the required params have been defined
        checkParams(null);
        
        // Set the appropriate headers
        setHeaders(_fileName);
        
        // Workbook parameters
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet 1");
        
        Row row;
        Cell cell;
        int r = 0; int c = 0;
        
        // Set the columns if defined
        if ( null != _columns && _columns.length > 0 ) {
            row = sheet.createRow(r++);
            for ( String ccel: _columns ) {
                cell = row.createCell(c++);
                cell.setCellValue(ccel);
            }
        }
        
        // Write the data
        for ( String[] rrow: data ) {
            row = sheet.createRow(r++);
            c = 0;
            
            for ( String ccel: rrow ) {
                cell = row.createCell(c++);
                cell.setCellValue(ccel);
            }
        }
        
        // Send the response and then close the output
        BufferedOutputStream ostream = new BufferedOutputStream(_response.getOutputStream());
        wb.write(ostream); ostream.close();
    }
    
    /**
     * Generate a csv file from the HQL passed to the object
     * 
     * @throws  IOException
     */
    @Override
    public void generateDoc() throws IOException {
        // Check if all the required params have been defined
        checkParams("hql");
        
        // Set the appropriate headers

        setHeaders(_fileName);
        
        // Workbook parameters
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet 1");
        
        Row row;
        Cell cell;
        int r = 0; int c = 0;
        int i = 0;
        
        // Set the columns if defined
        if ( null != _columns && _columns.length > 0 ) {
            row = sheet.createRow(r++);
            for ( String ccel: _columns ) {
                cell = row.createCell(c++);
                cell.setCellValue(ccel);
            }
        }
        
        // Get the query
        Iterator<Object[]> it = resultSet()
            .setMaxResults( 100 )
            .setFirstResult(i)
            .list().iterator();
        
        // loop through the records
        while ( it.hasNext() ) {
            
            // Generate the list
            while ( it.hasNext() ) {
                row = sheet.createRow(r++);
                c = 0;

                for ( Object col: it.next() ) {
                    if ( c < _columns.length ) {
                        cell = row.createCell(c++);
                        cell.setCellValue((null != col) ? col.toString(): null);
                    }
                }
            }
            
            // Get the next set of results
            i += 100;
            it = resultSet()
                .setMaxResults( 100 )
                .setFirstResult(i)
                .list().iterator();
        }
        
        // Send the response and then close the output
        BufferedOutputStream ostream = new BufferedOutputStream(_response.getOutputStream());
        wb.write(ostream); ostream.close();
    }
    
    /**
     * Generate the headers as required
     * 
     * @param   fileName
     */
    protected void setHeaders(String fileName) {
        _response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"); 
        _response.setHeader("Content-Disposition","attachment; filename=\""+fileName+".xls\"");
    }

}
