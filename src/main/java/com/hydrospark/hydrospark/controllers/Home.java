package com.hydrospark.hydrospark.controllers;


import com.hydrospark.hydrospark.configuration.EmailService;
import com.hydrospark.hydrospark.entities.Hydrospark;
import com.hydrospark.hydrospark.entities.Product;
import com.hydrospark.hydrospark.entities.SubProducts;
import com.hydrospark.hydrospark.entities.User;
import com.hydrospark.hydrospark.repositories.HydrosparkRepo;
import com.hydrospark.hydrospark.repositories.ProductRepo;
import com.hydrospark.hydrospark.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

//import javax.servlet.http.HttpSession;



@Controller
@RequestMapping("/")
public class Home {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HydrosparkRepo hydrosparkRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private EmailService emailService;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/try")
    public String tryHtml(){
        return "abc.html";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }
    @GetMapping("/")
    public String home(Model model,HttpSession session) throws SQLException {
//        email="sachin@hydrospark.org";
//        password="Sachin@10";
        List<Product> products=productRepo.findAll();
        System.out.println(products);
        List<Map<String,String>> base64Images = new ArrayList<>();
        for(Product prod:products){
            Blob blob = new SerialBlob(prod.getProdImg());
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            Map<String,String> prodDetails=new HashMap<>();
            prodDetails.put("img",base64Image);
            prodDetails.put("prodName",prod.getProductName());
            prodDetails.put("url","/product/productdetails/"+prod.getProId());
            base64Images.add(prodDetails);
        }
//        Hydrospark hyd=hydrosparkRepo.findByName("Hydro1...").get(0);
//        Blob blob = new SerialBlob(hyd.getImg());
//        byte[] bytes = blob.getBytes(1, (int) blob.length());
//        String base64Image = Base64.getEncoder().encodeToString(bytes);
//        session.setAttribute("img", base64Image);


        model.addAttribute("product", base64Images);

        return "home.html";
    }

//@GetMapping("/")
//public String home(Model model, HttpSession session) throws SQLException {
//    List<Product> products = productRepo.findAll();
//    List<Map<String, String>> base64Images = new ArrayList<>();
//
//    for (Product prod : products) {
//        if (prod.getProdImg() != null) { // Check if the image blob is not null
//            Blob blob = new SerialBlob(prod.getProdImg());
//            byte[] bytes = blob.getBytes(1, (int) blob.length()); // Get bytes from blob, starting at position 1
//            String base64Image = Base64.getEncoder().encodeToString(bytes);
//
//            Map<String, String> prodDetails = new HashMap<>();
//            prodDetails.put("img", base64Image);
//            prodDetails.put("prodName", prod.getProductName());
//            prodDetails.put("url", "/product/productdetails/" + prod.getProductName());
//            base64Images.add(prodDetails);
//        }
//    }
//            Hydrospark hyd=hydrosparkRepo.findByName("Hydro1...").get(0);
//            Blob blob = new SerialBlob(hyd.getImg());
//            byte[] bytes = blob.getBytes(1, (int) blob.length());
//            String base64Image = Base64.getEncoder().encodeToString(bytes);
//            session.setAttribute("img", base64Image);
//
//    model.addAttribute("product", base64Images);
//
//    return "home.html";
//}

//@GetMapping("/")
//public String home(Model model, HttpSession session) throws SQLException {
//    List<Product> products = productRepo.findAll();
//    List<Map<String, String>> base64Images = new ArrayList<>();
//
//    for (Product prod : products) {
//        if (prod.getProdImg() != null) {
//            try {
//                Blob blob = new SerialBlob(prod.getProdImg());
//                if (blob.length() > 0) {
//                    byte[] bytes = blob.getBytes(1, (int) blob.length());
//                    String base64Image = Base64.getEncoder().encodeToString(bytes);
//
//                    Map<String, String> prodDetails = new HashMap<>();
//                    prodDetails.put("img", base64Image);
//                    prodDetails.put("prodName", prod.getProductName());
//                    prodDetails.put("url", "/product/productdetails/" + prod.getProductName());
//                    base64Images.add(prodDetails);
//                }
//            } catch (SQLException e) {
//                //Handle the exception, log it, or add a default image.
//                System.err.println("Error processing product image: " + e.getMessage());
//            }
//
//        }
//    }
//
//    if(hydrosparkRepo.findByName("Hydro1...") != null && !hydrosparkRepo.findByName("Hydro1...").isEmpty()){
//        Hydrospark hyd = hydrosparkRepo.findByName("Hydro1...").get(0);
//        if(hyd.getImg() != null){
//            try{
//                Blob blob = new SerialBlob(hyd.getImg());
//                if(blob.length() > 0){
//                    byte[] bytes = blob.getBytes(1, (int) blob.length());
//                    String base64Image = Base64.getEncoder().encodeToString(bytes);
//                    session.setAttribute("img", base64Image);
//                }
//            }catch(SQLException e){
//                System.err.println("Error processing hydrospark image: " + e.getMessage());
//            }
//        }
//    }
//
//    model.addAttribute("product", base64Images);
//
//    return "home.html";
//}



    @GetMapping("hydrospark")
    public String hydrospark(){
        return "hydrospark";
    }
    @PostMapping("hydrospark")
    public String postHydrospark(HttpServletRequest request,HttpSession session) throws ServletException, IOException {
        String name=request.getParameter("name");


        Part filePart = request.getPart("img");
        byte[] imageBytes = filePart.getInputStream().readAllBytes();
        Hydrospark hydrospark=new Hydrospark(name,imageBytes);
        hydrosparkRepo.save(hydrospark);
        return "redirect:/";
    }

    @PostMapping("/search")
    public void getSearch(){
        System.out.println("Search..........");
    }
    @GetMapping("/signin")
    public String getSignIn(Model model,HttpSession session) throws SQLException {
        String user=(String) session.getAttribute("user");
        if(session.getAttribute("user")!=null){
            return "redirect:/";
        }
        session.setAttribute("lastUrl","signin");
        return "signin.html";
    }

    @PostMapping("/signin")
    public String postSignIn(HttpServletRequest request,Model model,HttpSession session){
        if(session.getAttribute("user")!=null){
            return "redirect:/";
        }

        String redirectURL= (String) session.getAttribute("redirectURL");
        if (redirectURL==null){
            redirectURL="redirect:/";
        }
        else{
            redirectURL="redirect:/"+redirectURL;
        }
        String email=request.getParameter("Email");
        String password=request.getParameter("Password");
        List<User> user=userRepo.findAllByEmailAndPassword(email,password);
        System.out.println(email);
        System.out.println(password);
        System.out.println(user.size());


        if(user.size()==1){
            session.setAttribute("user",email);
            session.removeAttribute("employee");
            return redirectURL;
        }
        else{
            model.addAttribute("error","Wrong or Invalid email address. Please correct and try again.");
        }
        return "signin.html";
    }

    @GetMapping("/signup")
    public String getRegister(HttpSession session){
        String user=(String) session.getAttribute("user");
        if(session.getAttribute("user")!=null){
            return "redirect:/";
        }
        return "register.html";
    }

    @PostMapping("/signup")
    public String postRegister(HttpServletRequest request, Model model,HttpSession session){
        if(session.getAttribute("user")!=null){
            return "redirect:/";
        }
        String email=request.getParameter("Email");
        String password=request.getParameter("Password");
        String firstName=request.getParameter("firstName");
        String lastName=request.getParameter("lastName");
        String confirmPassword=request.getParameter("confirmPassword");
        long number= Long.parseLong(request.getParameter("number"));
        List<User> user=userRepo.findByEmail(email);

        if(user.size()>0){
            model.addAttribute("error","User is Already exist");
            return "register.html";
        }
        else{
            /*
              How password should be
                The password length to be between 8 and 16 characters.
                At least one uppercase letter.
                At least one lowercase letter.
                At least one digit.
                At least one special character.
             */
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,}$";
//            String passwordPattern ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
            Pattern pattern = Pattern.compile(passwordPattern);
            Matcher matcher = pattern.matcher(password);
            if(!password.equals(confirmPassword)){
                System.out.println("Password and confirm password not matching");
                model.addAttribute("error","Password and confirm password do not match.");
                return "register.html";
            }

           if (matcher.matches()){
                User newUser=new User(firstName,lastName,email,password,number);
                Random random = new Random();
                String otp = 1000 + random.nextInt(9000)+"";
                session.setAttribute("otp",otp);
                String subject="Hydrospark Account OTP"+otp;
                String body="Hello, " + email + ".\n"
                        + "OTP: "+ otp +"\n\n"
                        + "Thanks and regards,\n"
                        + "Hydrospark inno.";
                emailService.sendEmail(session,email,subject,body);
                session.setAttribute("sentMail","Otp has been sent to email: "+email+" if u dont get please check email and forget again");
                model.addAttribute("sentMail","Otp has been sent to email: "+email+" if u dont get please check email and forget again");
                session.setAttribute("userDetails",newUser);
                return "redirect:/validate";
            }
            else{
                model.addAttribute("error","Password is not as expected");
                return "register.html";
            }


        }
//        return "redirect:/";
    }

    @GetMapping("/forgetpassword")
    public String getForgetPassword(HttpSession session){
//        Random random = new Random();
//        String otp = 1000 + random.nextInt(9000)+"";
//        session.setAttribute("otp",otp);
//        String subject="Hydrospark Account OTP"+otp;
//        String body="Hello, " + email + ".\n"
//                + "OTP: "+ otp +"\n\n"
//                + "Thanks and regards,\n"
//                + "Hydrospark inno.";
//        emailService.sendEmail(session,email,subject,body);
        if( session.getAttribute("lastUrl")!=null){
            String lastUrl = (String) session.getAttribute("lastUrl");
            if (lastUrl.equalsIgnoreCase("signin") ||lastUrl.equalsIgnoreCase("profile") ){
                System.out.println("Hiiiiiiiii");
                return "forgetpassword.html";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/forgetpassword")
    public String postForgetPassword(HttpServletRequest request, Model model,HttpSession session){
        String email=request.getParameter("Email");
        String password=request.getParameter("Password");
        String cnfPassowrd=request.getParameter("ConfirmPassword");
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,}$";
//            String passwordPattern ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        if(!password.equals(cnfPassowrd)){
            model.addAttribute("error","Password and confirm password do not match.");
            return "forgetpassword";
        }
        if (!matcher.matches()){
            model.addAttribute("error","Password is not as expected");
            return "forgetpassword";
        }
        Random random = new Random();
        String otp = 1000 + random.nextInt(9000)+"";
        session.setAttribute("otp",otp);
        session.setAttribute("cnfPassowrd",cnfPassowrd);
        session.setAttribute("email",email);
        String subject="Hydrospark Account OTP "+otp+" for forget Password";
        String body="Hello, " + email + ".\n"
                + "OTP: "+ otp +"\n\n"
                + "Thanks and regards,\n"
                + "Hydrospark inno.";

        emailService.sendEmail(session,email,subject,body);
        session.setAttribute("sentMail","Otp has been sent to email: "+email+" if u dont get please check email and forget again");
        return "validate.html";
    }

    @PostMapping("/resendotp")
    public String resendOtp(HttpServletRequest request, Model model,HttpSession session){
        String email= (String) session.getAttribute("email");
        System.out.println(email);
        System.out.println("Resending Otp");
        Random random = new Random();
        String otp = 1000 + random.nextInt(9000)+"";
        session.setAttribute("otp",otp);
        session.setAttribute("email",email);
        String subject="Hydrospark Account OTP "+otp+" for forget Password";
        String body="Hello, " + email + ".\n"
                + "OTP: "+ otp +"\n\n"
                + "Thanks and regards,\n"
                + "Hydrospark inno.";

        emailService.sendEmail(session,email,subject,body);
        session.setAttribute("sentMail","Otp has been sent to email: "+email+" if u don't get please check email or forget again");
        return "validate.html";
    }


    @GetMapping("/validate")
    public String validate(HttpSession session){
        if(session.getAttribute("otp")==null){
            return "redirect:/";
        }

        return "validate.html";
    }
    @PostMapping("/validate")
    public String validate(HttpServletRequest request,HttpSession session,Model model){
        String OTP=request.getParameter("OTP");
        String requiredOTP = (String) session.getAttribute("otp");
        System.out.println(OTP+" "+requiredOTP+" "+OTP.equalsIgnoreCase(requiredOTP));
        User newUser= (User) session.getAttribute("userDetails");
        if(OTP.equalsIgnoreCase(requiredOTP)){

            String cnfPassowrd= (String) session.getAttribute("cnfPassowrd");
            String email= (String) session.getAttribute("email");
//            session.setAttribute("email",email);
            if (cnfPassowrd!=null){
                if(userRepo.findByEmail(email).size()>0){
                    User user=userRepo.findByEmail(email).get(0);
                    user.password=cnfPassowrd;
                    userRepo.save(user);
                    session.setAttribute("error","Password got changed successfully");
                }
                else{
                    model.addAttribute("error","User not registered");
                }
                return "redirect:/signin";
            }
            else {


                userRepo.save(newUser);
                session.removeAttribute("userDetails");
                session.removeAttribute("otp");
                session.setAttribute("user", newUser.email);
                return "redirect:/";
            }
        }
        model.addAttribute("error","Wrong OTP!");
        return "validate.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        String user=(String) session.getAttribute("user");
        if(session.getAttribute("user")==null){
            return "redirect:/";
        }
        session.invalidate();
        return  "redirect:/";
    }







    @GetMapping("/search")
    public String searchProduct(@RequestParam String query,Model model) throws SQLException {
//        System.out.println(query);
        return "redirect:/product/"+query;
//
    }

    @GetMapping("/profile")
    public String Userprofile(HttpSession session,Model model){
        String user=(String) session.getAttribute("user");
        if(session.getAttribute("user")==null){
            return "redirect:/";
        }
        if(user!=null){
            User userProfile=userRepo.findByEmail(user).get(0);
            model.addAttribute("firstName",userProfile.firstName);
            model.addAttribute("lastName",userProfile.lastName);
            model.addAttribute("email",userProfile.email);
            model.addAttribute("password",userProfile.password);
            model.addAttribute("phoneNumber",userProfile.number);

        }
        session.setAttribute("lastUrl","profile");
        return "profile.html";
    }

//    @GetMapping("/enquiry")
//    public String enquiry(){
//        return "enquiry";
//    }
//
//    @PostMapping("/enquiry")
//    public String handleEnquiry(
//            @RequestParam("Name") String name,
//            @RequestParam("Email") String email,
//            @RequestParam("Enquiry") String enquiry,
//            HttpSession session,
//            Model model) {
//
//        // Email to the team
//        String teamEmailBody = "Subject: New Enquiry from Hydrospark Website\n\n" +
//                "Dear Team,\n\n" +
//                "A new enquiry has been submitted through the website. Below are the details:\n\n" +
//                "Name: " + name + "\n" +
//                "Email: " + email + "\n" +
//                "Enquiry:\n" + enquiry + "\n\n" +
//                "Please respond to the customer at your earliest convenience.\n\n" +
//                "Best regards,\n" +
//                "Hydrospark Innovations Team";
//
//        emailService.sendEmail(session, "info@hydrospark.com","New Enquiry Submission", teamEmailBody);
//
//
//        session.setAttribute("error","Enquiry Submitted sucessfully");
//        return "enquiry";
//        }




    @GetMapping("/enquiry")
    public String enquiry() {
        return "enquiry";
    }

    @PostMapping("/enquiry")
    public String handleEnquiry(
            @RequestParam("Name") String name,
            @RequestParam("Email") String email,
            @RequestParam("Enquiry") String enquiry,
            @RequestParam(name = "website", required = false) String honeypot,
            HttpSession session,
            Model model) {

        // 🕳️ Honeypot: bot filled this
        if (honeypot != null && !honeypot.isEmpty()) {
            session.setAttribute("error", "Bot submission detected. Action denied.");
            return "enquiry";
        }



        // ✅ Send Email
        String teamEmailBody = "Subject: New Enquiry from Hydrospark Website\n\n" +
                "Dear Team,\n\n" +
                "A new enquiry has been submitted through the website. Below are the details:\n\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Enquiry:\n" + enquiry + "\n\n" +
                "Please respond to the customer at your earliest convenience.\n\n" +
                "Best regards,\n" +
                "Hydrospark Innovations Team";

        emailService.sendEmail(session, "info@hydrospark.com", "New Enquiry Submission", teamEmailBody);

        session.setAttribute("error", "Enquiry submitted successfully. Thank you!");
        return "enquiry";
    }



    @GetMapping("/error")
    public String error(){
        return "unauthorized.html";
    }

}
