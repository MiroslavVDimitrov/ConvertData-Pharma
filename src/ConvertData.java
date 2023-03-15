
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ConvertData{
    public static void Convert(String pathIn, String pathOut) throws IOException {



        List<String> fileList = new ArrayList<String>();
        final File folder = new File(pathIn);
        ReadFolder.loadFilesForFolder(folder, fileList);
        int counterDocument=0;


        for (String fileName : fileList) {
            System.out.println("--->Reading File");
            System.out.println(fileName);

            String nameReadyFile="";
            StringBuilder strBuild = new StringBuilder();
            System.out.println("--> Lenght of fileName = "+fileName.length());

            int fileNameStartIndex = fileName.length()-15;
            for (int i = 0; i < 10; i++) {
                strBuild.append(fileName.charAt(fileNameStartIndex));
                fileNameStartIndex++;
            }
            strBuild.append(".S$$");
            System.out.println(strBuild);


            nameReadyFile = strBuild.toString();



            FileInputStream fis = new FileInputStream(new File(fileName));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            String textForSave="код доставчик\tиме артикул\tкол\tмерна ед\tед.ц безддспредиТО\tед.ц.безддссТО\tсрок на годност\tпартида\tразрешително\tпреп прод.цена";

            String textToAdd="";


            int countedRow = 0;
            for (Row row : sheet){
                countedRow++;
            }
            System.out.println("---> CountedRow : "+countedRow);



            String id="";
            String item="";
            String quantity="";
            String broi="БР\t";
            String cena ="";
            String exp ="";
            String batch="";





            int counter=0;
            for (Row row : sheet)
            {

                counter++;
                if((counter!=1)&&(counter != countedRow)) {


                    int countedCell = 0;
                    int divisionQantity=1;
                    double divisionPrice=0;

                    for (Cell cell : row)
                    {
                        countedCell++;
                        String wordFromCell="";
                        switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {

                            case NUMERIC:
                                (wordFromCell += cell.getNumericCellValue() + "\t").toString();
                                break;
                            case STRING:
                                wordFromCell += cell.getStringCellValue() + "\t";
                                break;
                        }
                        StringBuilder strBld = new StringBuilder(wordFromCell);

                        switch (countedCell){
                            case 1 :{
                                int lengtWord = wordFromCell.length();
                                strBld.replace(lengtWord-3,lengtWord,"S\t");
                                id=strBld.toString();
                            }
                            break;
                            case 2:{
                                item=strBld.toString();
                            }
                            break;
                            case 3:{
                                strBld.replace(wordFromCell.length()-4,wordFromCell.length(),"");


                                StringBuilder str = new StringBuilder();
                                if(strBld.length()==10) {
                                    str.append(strBld.substring(strBld.length() - 4, strBld.length()));
                                    strBld.delete(strBld.length() - 4, strBld.length());
                                    str.append(strBld.substring(3, 5));
                                    str.append(strBld.substring(0, 2)+"\t");
                                }

                                if(strBld.length()==8) {
                                    str.append(strBld.substring(strBld.length() - 4, strBld.length()));
                                    strBld.delete(strBld.length() - 4, strBld.length());
                                    str.append("0"+strBld.substring(2, 3));
                                    str.append("0"+strBld.substring(0, 1)+"\t");
                                }
                                if(strBld.length()==9) {
                                    str.append(strBld.substring(strBld.length() - 4, strBld.length()));
                                    strBld.delete(strBld.length() - 4, strBld.length());
                                    if(strBld.indexOf(".")==1){

                                        str.append(strBld.substring(2, 4));
                                        str.append("0"+strBld.substring(0, 1)+"\t");


                                    }

                                    if(strBld.indexOf(".")==2){

                                        str.append("0"+strBld.substring(3, 4));
                                        str.append(strBld.substring(0, 2)+"\t");


                                    }



                                }
                                if(strBld.length()>10&&8<strBld.length()){

                                    str.append("!error");

                                }




                                exp+=str;




                            }
                            break;
                            case 4:{
                                batch=strBld.toString();
                            }
                            break;
                            case 5:{
                                int lengtWord = wordFromCell.length();
                                strBld.replace(lengtWord-3,lengtWord,"");
                                divisionQantity=Integer.parseInt(strBld.toString());
                                quantity=String.valueOf(Integer.parseInt(strBld.toString())+"\t");
                                quantity+=broi;
                            }
                            break;
                            case 6:{
                                int lengtWord = wordFromCell.length();
                                strBld.replace(lengtWord-1,lengtWord,"\t");
                                cena+=  String.format("%.3f", (Double.parseDouble(strBld.toString())/divisionQantity )) +"\t";
                                cena =   cena.replace(',','.');
                            }
                            break;

                        }



                    }

                    textToAdd +=id;id="";
                    textToAdd += item;item="";
                    textToAdd +=quantity;quantity="";
                    textToAdd +=cena;
                    textToAdd +=cena;cena="";
                    textToAdd +=exp;exp="";
                    textToAdd +=batch;batch="";
                    textToAdd += "\t";
                    textToAdd += "0";

                    // remove first  line from saved document
                    if(counter==2){
                        textForSave =  textToAdd;
                    } else {

                        textForSave += "\n" + textToAdd;
                    }

                    textToAdd = "";



                    countedCell = 0;
                }


                cena ="";
                exp ="";
                batch="";





            }
            fis.close();

            try {
                String str = textForSave;
                File newTextFile = new File(pathOut+"\\"+nameReadyFile);
                FileWriter fw = new FileWriter(newTextFile);
                fw.write(str);
                fw.close();

            } catch (IOException iox) {
                //do stuff with exception
                iox.printStackTrace();
            }
            nameReadyFile="";
            countedRow=0;

            System.out.println("--->File was create");
            System.out.println(pathOut);
            System.out.println();
        }



        System.out.println("-----END of READING FILE IN FOLDER------- END------------------");

    }




}