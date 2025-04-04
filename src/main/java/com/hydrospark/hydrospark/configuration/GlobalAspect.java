package com.hydrospark.hydrospark.configuration;

import com.hydrospark.hydrospark.entities.Hydrospark;
import com.hydrospark.hydrospark.repositories.HydrosparkRepo;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Aspect
@Component
public class GlobalAspect {

    @Autowired
    private HydrosparkRepo hydrosparkRepo;

    // This will match all methods with any number of parameters in the com.hydrospark.hydrospark.controllers package
    @Before("execution(* com.hydrospark.hydrospark.controllers..*(..))")
    public void beforeControllerExecution() throws SQLException {
        // Accessing the HttpSession from RequestContextHolder
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession();

            // Retrieving the image data and converting it to Base64
            Hydrospark hyd = hydrosparkRepo.findByName("Hydro1...").get(0);
            Hydrospark Banner1 = hydrosparkRepo.findByName("Banner1").get(0);
            Hydrospark Banner2 = hydrosparkRepo.findByName("Banner2").get(0);
            Hydrospark Banner3 = hydrosparkRepo.findByName("Banner3").get(0);

            Blob blob = new SerialBlob(hyd.getImg());
            Blob Banner1Blob = new SerialBlob(Banner1.getImg());
            Blob Banner2Blob = new SerialBlob(Banner2.getImg());
            Blob Banner3Blob = new SerialBlob(Banner3.getImg());
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            byte[] Banner1bytes = Banner1Blob.getBytes(1, (int) Banner1Blob.length());
            byte[] Banner2bytes = Banner2Blob.getBytes(1, (int) Banner2Blob.length());
            byte[] Banner3bytes = Banner3Blob.getBytes(1, (int) Banner3Blob.length());
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            String Banner1base64Image = Base64.getEncoder().encodeToString(Banner1bytes);
            String Banner2base64Image = Base64.getEncoder().encodeToString(Banner2bytes);
            String Banner3base64Image = Base64.getEncoder().encodeToString(Banner3bytes);

            // Storing the Base64 image string in the session
            session.setAttribute("img", base64Image);
            session.setAttribute("banner1", Banner1base64Image);
            session.setAttribute("banner2", Banner2base64Image);
            session.setAttribute("banner3", Banner3base64Image);
        }
    }
}
