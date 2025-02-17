package edu.northeastern.kinnarkansara.finalprojectnbweb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author kinnar
 */
public class HomeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, Integer> srcdest = new HashMap<>();
        try (PrintWriter out = response.getWriter()) {

            Configuration conf = new Configuration();
            conf.addResource(new org.apache.hadoop.fs.Path("/usr/local/bin/hadoop-3.2.1/conf/core-site.xml"));
            conf.addResource(new Path("/usr/local/bin/hadoop-3.2.1/conf/hdfs-site.xml"));

            Path filePath = new Path("/project/Top10SourceDestinations/part-r-00000");

            FileSystem fs = filePath.getFileSystem(conf);
            BufferedReader br = null;

            try {

                Configuration configuration = new Configuration();
                FileSystem hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), configuration);
                Path file = new Path("hdfs://localhost:9000/project/Top10SourceDestinations/part-r-00000");

                br = new BufferedReader(new InputStreamReader(hdfs.open(file)));
                String line;
                line = br.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = br.readLine();

                }

            } catch (Exception e) {

            } finally {
                // you should close out the BufferedReader
                br.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
