/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.markbudai.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



/**
 *
 * @author mark
 */
public class FileUploadServlet extends HttpServlet {

    //private static final long serialVersionUID = 205242440643911308L;
    /**
     * Directory where uploaded files will be saved, its relative to the web
     * application directory.
     */
    private static final String UPLOAD_DIR = "uploads";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // gets absolute path of the web application
        String applicationPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        //System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());

        //List<String> trees = new ArrayList<>();
        Map<String, BinaryTree> trees = new HashMap<>();

        //Get all the parts from request and write it to the file on server
        for (Part part : request.getParts()) {
            String fileName = getFileName(part);
            if(fileName.isEmpty())
            {
                request.setAttribute("message", "Sadly, You didn't upload anything! :c");
                getServletContext().getRequestDispatcher("/response.jsp").forward(request, response);
            }
            
            part.write(uploadFilePath + File.separator + fileName);

            java.io.FileInputStream inFile = new java.io.FileInputStream(uploadFilePath + File.separator + fileName);
            byte[] b = new byte[1];
            BinaryTree tree = new BinaryTree();
            while (inFile.read(b) != -1) {
                if (b[0] == 0x0a) {
                    break;
                }
            }

            boolean inComment = false;
            while (inFile.read(b) != -1) {
                if (b[0] == 0x3e) {
                    inComment = true;
                    continue;
                }
                if (b[0] == 0x0a) {
                    inComment = false;
                    continue;
                }
                if (inComment) {
                    continue;
                }
                if (b[0] == 0x4e) {
                    continue;
                }
                for (int i = 0; i < 8; ++i) {
                    if ((b[0] & 0x80) != 0) {
                        tree.processBit('1');
                    } else {
                        tree.processBit('0');
                    }
                    b[0] <<= 1;
                }
            }
            inFile.close();
            if(isImage(fileName)){
                fileName = "<img style=max-width:10em src="+UPLOAD_DIR + File.separator + fileName+">";
            }
            trees.put(fileName, tree);
            //trees.add(tree.toString());
        }
        String output = "";
        /*for (String s : trees) {
            output += s + "<hr/>";
        }*/
        for(String key : trees.keySet()){
            //output += "<br><b>Filename:</b> <i>" + key + "</i><br>" + trees.get(key).toString();
            output += "<fieldset><legend>" + key + "</legend>" + trees.get(key).toString() + "</fieldset>";
        }

        request.setAttribute("message", "File uploaded successfully!<br/>" + output);
        getServletContext().getRequestDispatcher("/response.jsp").forward(
                request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rawData = req.getParameter("data");
        //multipart needs POST method to work :c
        //determine data type from the form :D
        BinaryTree tree = new BinaryTree();
        
        if (req.getParameter("dataType").equals("binary")) {   
            for (char c : rawData.toCharArray()) {
                tree.processBit(c);
            }
        } else {
            byte[] bs = rawData.getBytes();
            boolean inComment = false;
            for (byte b : bs) {
                if (b == 0x3e) {
                    inComment = true;
                    continue;
                }
                if (b == 0x0a) {
                    inComment = false;
                    continue;
                }
                if (inComment) {
                    continue;
                }
                if (b == 0x4e) {
                    continue;
                }
                for (int i = 0; i < 8; ++i) {
                    if ((b & 0x80) != 0) {
                        tree.processBit('1');
                    } else {
                        tree.processBit('0');
                    }
                    b <<= 1;
                }
            }
        }
        String data = tree.toString();
        req.setAttribute("message", data);
        getServletContext().getRequestDispatcher("/response.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
        int 私はあなたを愛して;
    }

    /**
     * Utility method to get file name from HTTP header content-disposition
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        //System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
    private boolean isImage(String filename){
        if(filename.contains(".png") || filename.contains(".jpg") ||
                filename.contains(".jpeg")) return true;
        else return false;
    }
}
