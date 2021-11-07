package com.daar.elasticCV;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UtilFiles {
    public static File convertMultipartFromFile(MultipartFile mpFile, String id) throws IOException {
        String folder ="upload/";
        File folderAsFile = new File(folder);
        if (!Files.exists(Paths.get(folder))) {
            folderAsFile.mkdir();
        }
        Path path = Paths.get(folder,id + "_" + mpFile.getOriginalFilename()).toAbsolutePath();
        Files.deleteIfExists(path);
        File file = path.toFile();
        mpFile.transferTo(file);

        if(file.getName().trim().split("\\.")[1].trim().equals("pdf")){
            return convertPdfToTxt(file);
        }
        else if(file.getName().trim().split("\\.")[1].trim().equals("docx")){
            return convertDocxToTxt(file);
        }
        else if(file.getName().trim().split("\\.")[1].trim().equals("txt")){
            return file;
        }
        else{
            Files.deleteIfExists(path);
            return null;
        }
    }

    public static File convertPdfToTxt(File pdfFile) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDocument = PDDocument.load(pdfFile);
        String parsedText = pdfTextStripper.getText(pdDocument);
        Path fileName = Paths.get("upload/", pdfFile.getName().split("\\.")[0] + ".txt");
        Files.deleteIfExists(fileName);
        File file = fileName.toFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(parsedText);
        fileWriter.close();
        pdDocument.close();
        return file;
    }

    public static File convertDocxToTxt(File docxFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(docxFile);
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
        XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(xwpfDocument);
        String parsedText = xwpfWordExtractor.getText();
        Path fileName = Paths.get("upload/", docxFile.getName().split("\\.")[0] + ".txt");
        Files.deleteIfExists(fileName);
        File file = fileName.toFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(parsedText);
        fileWriter.close();
        xwpfDocument.close();
        fileInputStream.close();
        return file;
    }

    public static ArrayList<String> fileToArrayList(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        ArrayList<String> arrayList = new ArrayList<String>();
        while ((line = bufferedReader.readLine()) != null) {
            arrayList.add(line);
        }
        bufferedReader.close();
        fileReader.close();
        return arrayList;
    }
}
