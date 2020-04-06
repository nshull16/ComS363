import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Toy {
    public static void main(String[] args){
        System.out.println("Choose an option\n");
        System.out.println("1- Toy create\n2- Toy header\n3- Toy insert\n4- Toy display\n5- Toy delete\n6- Toy search\n7- Exit\n");
        System.out.println("Enter a command number:");
        Scanner scanner = new Scanner(System.in);
        int commandNumber = Integer.parseInt(scanner.next());
        switch (commandNumber)
        {
            case 1:
                System.out.println("Toy create chosen");
                System.out.println("Please enter a table name(append .tb to the name):");
                String filename = scanner.next();
                try{
                    File file = new File(filename);
                    if(file.createNewFile()){
                        System.out.println("File created: " + file.getName());
                    }
                    else{
                        System.out.println("File already exists");
                    }
                    Table table = new Table();
                    boolean moreAttributes = true;
                    while(moreAttributes) {
                        Attribute attribute = new Attribute();
                        System.out.println("Attribute name: ");
                        attribute.setName(scanner.next());
                        if (attribute.getName().contains("[") || attribute.getName().contains("]") || attribute.getName().contains("{") || attribute.getName().contains("}") || attribute.getName().contains("|")) {
                            System.exit(0);
                        }
                        System.out.println("Select a type: 1.integer; 2.double; 3.boolean; 4.string");
                        attribute.setType(scanner.nextInt());
                        table.getAttribute().add(attribute);
                        System.out.println("More attributes?(y/n)");
                        String answer = scanner.next();
                        if(!answer.equals("y")) {
                            moreAttributes = false;
                        }
                    }
                    String header = "["+table.getAttribute().size()+"]";
                    for(Attribute attribute : table.getAttribute()) {
                        header += "["+attribute.getName()+":"+attribute.getType()+"]";
                    }
                    header += "["+table.getRecord().size()+"]";
                    FileWriter writer = new FileWriter(file);
                    writer.write(header);
                    scanner.close();
                    writer.close();
                    System.out.println("Finished");
                }
                catch(Exception e){
                    System.out.println(e);
                }
                break;
            case 2:
                System.out.println("Toy Header Chosen");
                System.out.println("Enter the name of the table:");
                String headername = scanner.next();
                try{
                    BufferedReader reader = new BufferedReader(new FileReader(headername));
                    String header = reader.readLine();
                    String[] parts = header.split("\\]");
                    for(int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].split("\\[")[1];
                    }
                    System.out.println(parts[0]+" attributes");
                    for(int i = 1; i < parts.length-1; i++) {
                        String attribute[] = parts[i].split(":");
                        System.out.print("Attribute " + i + ": " + attribute[0] + ", ");
                        switch(attribute[1]) {
                            case "1":
                                System.out.print("integer\n");
                                break;
                            case "2":
                                System.out.print("double\n");
                                break;
                            case "3":
                                System.out.print("boolean\n");
                                break;
                            case "4":
                                System.out.print("string\n");
                                break;
                        }
                    }
                    System.out.println(parts[parts.length-1]+" records");
                    System.out.println("Finished");
                }
                catch(Exception e) {
                    System.out.println(e);
                }
                break;
            case 3:
                System.out.println("Toy Insert Chosen");
                System.out.println("Enter the name of the table to append to:");
                String insertName = scanner.next();
                try{
                    String appendString = "{";
                    BufferedReader reader = new BufferedReader(new FileReader(insertName));
                    String header = reader.readLine();
                    String[] parts = header.split("\\]");
                    for(int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].split("\\[")[1];
                    }
                    for (int i = 1; i < parts.length-1; i++) {
                        String attribute[] = parts[i].split(":");
                        System.out.println(attribute[0] + ": ");
                        switch(attribute[1]) {
                            case "1":
                                int intEntry = scanner.nextInt();
                                appendString = appendString + intEntry;
                                break;
                            case "2":
                                double doubleEntry = scanner.nextDouble();
                                appendString = appendString + doubleEntry;
                                break;
                            case "3":
                                String booleanEntry = scanner.next();
                                if (booleanEntry.toLowerCase() != "t" || booleanEntry.toLowerCase() != "f") {
                                    System.exit(0);
                                }
                                else {
                                    appendString = appendString + booleanEntry;
                                }
                                break;
                            case "4":
                                String stringEntry = scanner.next();
                                appendString = appendString + stringEntry;
                                break;
                        }
                        if(i != parts.length-2) {
                            appendString = appendString + "|";
                        }
                    }
                    appendString = appendString + "}";
                    int numRec = Integer.parseInt(parts[parts.length-1]) + 1;
                    parts[parts.length-1] = numRec + "";
                    String headerReplacement = "";
                    for(int i = 0; i < parts.length; i++) {
                        headerReplacement = headerReplacement + "[" + parts[i] + "]";
                    }
                    String oldContent = "";
                    String line = header;
                    while(line != null) {
                        oldContent = oldContent + line + System.lineSeparator();
                        line = reader.readLine();
                    }
                    String newContent = oldContent.replace(header, headerReplacement);
                    FileWriter writer = new FileWriter(insertName);
                    writer.write(newContent);
                    writer.write(appendString);
                    writer.close();
                    System.out.println("Finished");
                }
                catch(Exception e) {
                    System.out.println(e);
                }
                break;
            case 4:
                System.out.println("Toy display chosen");
                System.out.println("Enter the name of a table:");
                String displayname = scanner.next();
                System.out.println("Please specify the index of an rid");
                String rid = scanner.next();
                try{
                    BufferedReader reader = new BufferedReader(new FileReader(displayname));
                    String header = reader.readLine();
                    String[] headerparts = header.split("\\]");
                    for(int i = 0; i < headerparts.length; i++) {
                        headerparts[i] = headerparts[i].split("\\[")[1];
                    }
                    String ridline = Files.readAllLines(Paths.get(displayname)).get(Integer.parseInt(rid)+1);
                    String[] ridparts = ridline.split("\\|");
                    for(int j = 0; j < ridparts.length; j++){
                        ridparts[j] = ridparts[j].replaceAll("\\{", "");
                        ridparts[j] = ridparts[j].replaceAll("\\}", "");
                    }
                    for(int k = 1; k < headerparts.length -1; k++){
                        String attribute[] = headerparts[k].split(":");
                        System.out.println(attribute[0] + ": " + ridparts[k-1]);
                    }
                    System.out.println("Finished");
                }
                catch(Exception e){
                    System.out.println(e);
                }
                break;
            case 5:
                System.out.println("Toy Delete Chosen");
                System.out.println("Choose table to delete from:");
                String deleteName = scanner.next();
                System.out.println("Enter rid:");
                int deleteRid = Integer.parseInt(scanner.next());
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(deleteName));
                        String header = br.readLine();
                        String[] headerParts = header.split("\\]");
                        int i = 0;
                        int numRecords = 0;
                        String newHeader = "";
                        List<Attribute> attributes = new ArrayList<>();
                        for(String headerPart : headerParts) {
                            String headerString = headerPart.substring(1,headerPart.length());
                            if(i == 0) {
                                newHeader += "["+headerString+"]";
                            }
                            else if(i==(headerParts.length-1)) {
                                newHeader += "["+(Integer.parseInt(headerString)-1)+"]";
                                numRecords = Integer.parseInt(headerString);
                            }
                            else {
                                String[] attribute = headerString.split(":");
                                Attribute attributeObj = new Attribute();
                                attributeObj.setName(attribute[0]);
                                attributeObj.setType(Integer.parseInt(attribute[1]));
                                attributes.add(attributeObj);
                                newHeader += "["+headerString+"]";
                            }
                            i++;
                        }
                        if((deleteRid+1) > numRecords ||(deleteRid) < 0) {
                            System.out.println("The record you are looking for does not exist");
                            break;
                        }
                        else {
                            String newString = newHeader+System.lineSeparator();
                            for (i = 0; i<numRecords;i++) {
                                if(i != deleteRid) {
                                    newString += br.readLine()+System.lineSeparator();
                                }
                                else {
                                    br.readLine();
                                }
                            }
                            FileWriter writer = new FileWriter(deleteName);
                            writer.write(newString);
                            writer.close();
                            System.out.println("Finished");
                        }
                    } 
                    catch (Exception e) {
                        System.out.println("File named "+ deleteName +" could not be read");
                    }
                break;
            case 6:
                System.out.println("Toy search chosen");
                System.out.println("Choose table to search");
                String searchName = scanner.next();
                System.out.println("Enter search \"condition\":");
                String condition = scanner.next();
                    try {
                        String[] conditionParts = condition.split("=");
                        conditionParts[0] = conditionParts[0].replace('"', ' ').trim();
                        conditionParts[1] = conditionParts[1].replace('"', ' ').trim();
                        BufferedReader br = new BufferedReader(new FileReader(searchName));
                        String header = br.readLine();
                        String[] headerParts = header.split("\\]");
                        int i = 0;
                        int numRecords = 0;
                        int numAttributes=0;
                        int attributeId = -1;
                        List<Attribute> attributes = new ArrayList<>();
                        for(String headerPart : headerParts) {
                            String headerString = headerPart.substring(1,headerPart.length());
                            if(i == 0) {
                                numAttributes = Integer.parseInt(headerString);
                            }
                            else if(i==(headerParts.length-1)) {
                                numRecords = Integer.parseInt(headerString);
                            }
                            else {
                                String[] attribute = headerString.split(":");
                                Attribute attributeObj = new Attribute();
                                attributeObj.setName(attribute[0]);
                                attributeObj.setType(Integer.parseInt(attribute[1]));
                                attributes.add(attributeObj);
                                if(attribute[0].equals(conditionParts[0])) {
                                    attributeId = i-1;
                                }
                            }
                            i++;
                        }
                        if((attributeId+1) > numAttributes ||(attributeId) < 0) {
                            System.out.println("The attribute you are trying to match does not exist");
                            break;
                        }
                        else {
                            for (i = 0; i<numRecords;i++) {
                                String[] attrStringParts = br.readLine().split("\\|");
                                attrStringParts[0] = attrStringParts[0].substring(1,attrStringParts[0].length());
                                attrStringParts[attrStringParts.length-1] = attrStringParts[attrStringParts.length-1].substring(0,attrStringParts[attrStringParts.length-1].length()-1);
                                if(attrStringParts[attributeId].equals(conditionParts[1])) {
                                    System.out.println("Record " + i + ":");
                                    int j = 0;
                                    for(Attribute attribute:attributes) {
                                        System.out.println(attribute.getName() + ": " + attrStringParts[j]);
                                        j++;
                                    }
                                    System.out.println();
                                }
                            }
                            System.out.println("Finished");
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                break;
            case 7:
                System.out.println("Exiting program");
                System.exit(0);
            default:
                System.out.println("Enter a valid command.");
        }
    }
}
