package couk.jenxsol.utils;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Label;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created with Intellij with Android, BIZZBY product.
 * See licencing for usage of this code.
 * <p/>
 * User: chris
 * Date: 25/02/2013
 * Time: 21:37
 */
public class Export
{
    public static WritableWorkbook getWorkBook(File file)
    {
        if (file == null) return null;
        Workbook workbookIn;
        WritableWorkbook workbook = null;
        try
        {
            workbookIn = Workbook.getWorkbook(file);
            workbook = Workbook.createWorkbook(file, workbookIn);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (BiffException e)
        {
            e.printStackTrace();
        }
        return workbook;
    }

    public static WritableSheet getExportSheet(WritableWorkbook workbook, String sheetName)
    {
        if (workbook == null || sheetName == null)
        {
            return null;
        }
        WritableSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
        {
            sheet = workbook.createSheet(sheetName, workbook.getNumberOfSheets());
        }
        return sheet;
    }

    public static int nextFreeColNumber(WritableSheet sheet)
    {
        if (sheet == null) return 0;
        int colCount = sheet.getColumns();
        Cell[] cells;
        int i = 0;
        for (; i < colCount; i++)
        {
            cells = sheet.getColumn(i);
            for (Cell cell : cells)
            {
                if (cell.getContents() == null || cell.getContents().isEmpty())
                    return i;
            }

        }
        return i;
    }

    public static void writeToCell(WritableSheet sheet, String value, int col, int row)
    {
        if (sheet == null) return;
        Label label = new Label(col, row, value);
        try
        {
            sheet.addCell(label);
        }
        catch (WriteException e)
        {
            e.printStackTrace();
        }
    }

    public static File startFileChooser(Component component, File selectedFile)
    {
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileHidingEnabled(true);
        fc.setAcceptAllFileFilterUsed(false);
        if (selectedFile != null) fc.setSelectedFile(selectedFile);
        fc.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(final File file)
            {
                if (file.isDirectory()) return true;
                String ext = Utils.getExtension(file);
                if (ext != null && ext.equals(Utils.XLS))
                {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription()
            {
                return "Excel *.xls";
            }
        });
        //In response to a button click:
        final int returnVal = fc.showOpenDialog(component);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            Log.d("Opening: " + file.getName() + ".");
            return file;
        }
        else
        {
            Log.d("Open command cancelled by user.");
        }
        return null;
    }


}
