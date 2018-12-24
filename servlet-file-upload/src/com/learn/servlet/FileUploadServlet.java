package com.learn.servlet;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import com.sun.pdfview.PDFFile;

@SuppressWarnings("serial")
@WebServlet(name = "FileUploadServlet", urlPatterns = { "/upload" })
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

	private final static Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getCanonicalName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final Part filePart = request.getPart("file");
	    final String fileName = getFileName(filePart);
	    
	    InputStream fileInputStream = filePart.getInputStream();
	    byte[] fileByteData = IOUtils.toByteArray(fileInputStream);
	    
	    try {
			PrintPdf printPDF = new PrintPdf(fileByteData, fileName);
			printPDF.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}
