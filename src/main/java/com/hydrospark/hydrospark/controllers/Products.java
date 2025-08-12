package com.hydrospark.hydrospark.controllers;

import com.hydrospark.hydrospark.configuration.EmailService;
import com.hydrospark.hydrospark.entities.*;
import com.hydrospark.hydrospark.repositories.HydrosparkRepo;
import com.hydrospark.hydrospark.repositories.ProductRepo;
import com.hydrospark.hydrospark.repositories.SubProdRepo;
import com.hydrospark.hydrospark.repositories.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class Products {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HydrosparkRepo hydrosparkRepo;

    @Autowired
    private SubProdRepo subProdRepo;

    @Autowired
    private EmailService emailService;


    @GetMapping("")
    public void prodHome(HttpSession session,Model model) throws SQLException {
        Hydrospark hyd=hydrosparkRepo.findByName("Hydro1...").get(0);
        Blob blob = new SerialBlob(hyd.getImg());
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String base64Image = Base64.getEncoder().encodeToString(bytes);
        session.setAttribute("img", base64Image);
    }
//    @GetMapping("productdetails/{prodId}")
//    public String getProducts(@PathVariable int prodId, Model model, HttpSession session) throws SQLException {
////        if(session.getAttribute("user")==null){
////            String redirectURL="product/productdetails/"+prodName;
////            session.setAttribute("redirectURL",redirectURL);
////
////            return "redirect:/signin";
////        }
//
//        String user= (String) session.getAttribute("user");
//        if(user!=null){
//            Date currentDate = new Date();
//            User userUp=userRepo.findByEmail(user).get(0);
//            userUp.visitedProduct=true;
//            userUp.dateOfProductVisit=currentDate;
//            userRepo.save(userUp);
//        }
//        session.setAttribute("mainProductName",productRepo);
////        List<SubProducts> subProducts=productRepo.findSubProduct(prodName);
//        List<SubProducts> subProducts=subProdRepo.getAll();
//        List<SubProducts> listOfSameMainProd=new ArrayList<>();
//        for(SubProducts subprod:subProducts){
//            if(subprod.getProduct().getProId()==prodId){
//                listOfSameMainProd.add(subprod);
//            }
//        }
//        List<Map<String, String>> base64Images = new ArrayList<>();
//        for (SubProducts subProduct : listOfSameMainProd) {
//            var img=subProduct.getSubProdImg();
//            if (img==null){
//                img=subProduct.getProduct().getProdImg();
//            }
//            Blob blob = new SerialBlob(img);
//            byte[] bytes = blob.getBytes(1, (int) blob.length());
//            String base64Image = Base64.getEncoder().encodeToString(bytes);
//            Map<String, String> prodDetails = new HashMap<>();
//            prodDetails.put("img", base64Image);
//            prodDetails.put("prodName", subProduct.getSubTypeName());
//            prodDetails.put("suburl", "/product/productdescription/" + subProduct.getSubProdId());
//
//            base64Images.add(prodDetails);
//        }
//
//        model.addAttribute("productDetails", base64Images);  // Send the list of product details to the model
//        return "proddetails";  // Thymeleaf template to render
//    }


@GetMapping("productdetails/{prodId}")
public String getProducts(
        @PathVariable int prodId,
        @RequestParam(required = false) Integer dashSize,
        @RequestParam(required = false) String termination,
        @RequestParam(required = false) String threadSize,
        @RequestParam(required = false) String group,
        Model model,
        HttpSession session) throws SQLException {

    String user = (String) session.getAttribute("user");
    if (user != null) {
        Date currentDate = new Date(System.currentTimeMillis());
        User userUp = userRepo.findByEmail(user).get(0);
        userUp.visitedProduct=true;
        userUp.dateOfProductVisit=currentDate;
        userRepo.save(userUp);
    }

    session.setAttribute("mainProductName", productRepo);

    // Fetch all subproducts
    List<SubProducts> subProducts = subProdRepo.getAll();
    List<SubProducts> listOfSameMainProd = new ArrayList<>();

    // Filter subproducts by prodId and optional filters
    for (SubProducts subprod : subProducts) {
        boolean matches = subprod.getProduct().getProId() == prodId;
        if (dashSize != null && subprod.getDashSize() != null) {
            matches = matches && subprod.getDashSize().equals(dashSize);
        }
        if (termination != null && !termination.isEmpty() && subprod.getTermination() != null) {
            matches = matches && subprod.getTermination().equals(termination);
        }
        if (threadSize != null && !threadSize.isEmpty() && subprod.getThreadSize() != null) {
            matches = matches && subprod.getThreadSize().equals(threadSize);
        }
        if (group != null && !group.isEmpty() && subprod.getGroup() != null) {
            matches = matches && subprod.getGroup().equals(group);
        }
        if (matches) {
            listOfSameMainProd.add(subprod);
        }
    }

    // Prepare product details for frontend
    List<Map<String, String>> base64Images = new ArrayList<>();
    for (SubProducts subProduct : listOfSameMainProd) {
        byte[] img = subProduct.getSubProdImg();
        if (img == null) {
            img = subProduct.getProduct().getProdImg();
        }
        Blob blob = new SerialBlob(img);
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String base64Image = Base64.getEncoder().encodeToString(bytes);
        Map<String, String> prodDetails = new HashMap<>();
        prodDetails.put("img", base64Image);
        prodDetails.put("prodName", subProduct.getSubTypeName());
        prodDetails.put("suburl", "/product/productdescription/" + subProduct.getSubProdId());
        prodDetails.put("description", subProduct.getDescription() != null ? subProduct.getDescription() : "");

        base64Images.add(prodDetails);
    }

    // Fetch unique filter values from all subproducts
    String prdName = productRepo.findById(prodId).getProductName();
    String prdDesc = productRepo.findById(prodId).getDescription();
    session.setAttribute("prdName",prdName);
    session.setAttribute("prdDesc",prdDesc);
    Set<String> dashSizes = new TreeSet<>();
    Set<String> terminations = new TreeSet<>();
    Set<String> threadSizes = new TreeSet<>();
    Set<String> groups = new TreeSet<>();
    for (SubProducts sub : subProducts) {
        if (sub.getDashSize() != null) dashSizes.add(sub.getDashSize());
        if (sub.getTermination() != null && !sub.getTermination().isEmpty()) terminations.add(sub.getTermination());
        if (sub.getThreadSize() != null && !sub.getThreadSize().isEmpty()) threadSizes.add(sub.getThreadSize());
        if (sub.getGroup() != null && !sub.getGroup().isEmpty()) groups.add(sub.getGroup());
    }

    // Add data to model
    model.addAttribute("productDetails", base64Images);
    model.addAttribute("dashSizes", dashSizes);
    model.addAttribute("terminations", terminations);
    model.addAttribute("threadSizes", threadSizes);
    model.addAttribute("groups", groups);
    model.addAttribute("prodId", prodId);

    if (base64Images.isEmpty()) {
        model.addAttribute("error", "No products found matching the selected filters.");
    }
    System.out.println(base64Images.size());

    return "proddetails";
}


//    @GetMapping("productdescription/{subId}")
//    public String getSubType(@PathVariable int subId,Model model,HttpSession session) throws SQLException {
//        session.removeAttribute("error");
////        subtype = URLDecoder.decode(subtype, StandardCharsets.UTF_8);
//        if(session.getAttribute("user")==null && session.getAttribute("employee")==null){
//            String redirectURL="product/productdescription/"+subId;
//            session.setAttribute("redirectURL",redirectURL);
//            return "redirect:/signin";
//        }
//        String user= (String) session.getAttribute("user");
//        Date currentDate = new Date();
//        if (!userRepo.findByEmail(user).isEmpty()){
//            User userUp=userRepo.findByEmail(user).get(0);
//            userUp.visitedProduct=true;
//            userUp.contacted=false;
//            userUp.dateOfProductVisit=currentDate;
//            userRepo.save(userUp);
//        }
//
//        session.setAttribute("subProductName",subId);
//        List<SubProducts> subProducts=subProdRepo.findSubProductById(subId);
//        List<Map<String,String>> base64Images = new ArrayList<>();
//        for(SubProducts subProd:subProducts){
//            System.out.println(subProd.getSubTypeName());
//            var prdImg=subProd.getSubProdImg();
//            if(prdImg==null){
//                prdImg=subProd.getProduct().getProdImg();
//            }
//            Blob detailBlob=null;
//            byte[] bytesForDetailImg=null;
//            String base64DetailImage=null;
//
//            Blob blob = new SerialBlob(prdImg);
//            byte[] bytes = blob.getBytes(1, (int) blob.length());
//            String base64Image = Base64.getEncoder().encodeToString(bytes);
//            Map<String,String> prodDetails=new HashMap<>();
//            System.out.println(prdImg);
//            prodDetails.put("img",base64Image);
//            prodDetails.put("prodName",subProd.getSubTypeName());
//            prodDetails.put("description",subProd.getDescription());
//            prodDetails.put("price",subProd.getSubTypePrice()+"");
//            if(bytesForDetailImg!=null){
//                prodDetails.put("detailImg",base64DetailImage);
//            }
//            prodDetails.put("enquiry","/product/enquiry/"+subProd.getSubTypeName());
//
////            prodDetails.put("url","/admin/productdetails/"+subProd.getSubTypeName());
//            base64Images.add(prodDetails);
//        }
//        model.addAttribute("product",base64Images);
//        return "productDiscription";
//
//
//    }
//@GetMapping("/productdescription/{subId}")
//public String getSubType(@PathVariable int subId, Model model, HttpSession session) throws SQLException {
//    session.removeAttribute("error");
//
//    if (session.getAttribute("user") == null && session.getAttribute("employee") == null) {
//        String redirectURL = "product/productdescription/" + subId;
//        session.setAttribute("redirectURL", redirectURL);
//        return "redirect:/signin";
//    }
//
//    String user = (String) session.getAttribute("user");
//    Date currentDate = new Date();
//    if (!userRepo.findByEmail(user).isEmpty()) {
//        User userUp = userRepo.findByEmail(user).get(0);
//        userUp.visitedProduct=true;
//        userUp.contacted=false;
//        userUp.dateOfProductVisit=currentDate;
//        userRepo.save(userUp);
//    }
//
//    session.setAttribute("subProductName", subId);
//    List<SubProducts> subProducts = subProdRepo.findSubProductById(subId);
//    List<Map<String, String>> base64Images = new ArrayList<>();
//    List<Map<String, String>> componentEntries = new ArrayList<>();
//
//    for (SubProducts subProd : subProducts) {
//        // Product Image and Details
//        byte[] prdImg = subProd.getSubProdImg();
//        if (prdImg == null) {
//            prdImg = subProd.getProduct().getProdImg();
//        }
//        Blob blob = new SerialBlob(prdImg);
//        byte[] bytes = blob.getBytes(1, (int) blob.length());
//        String base64Image = Base64.getEncoder().encodeToString(bytes);
//
//        Map<String, String> prodDetails = new HashMap<>();
//        prodDetails.put("img", base64Image);
//        prodDetails.put("prodName", subProd.getSubTypeName());
//        prodDetails.put("description", subProd.getDescription());
//        prodDetails.put("price", String.valueOf(subProd.getSubTypePrice()));
//        prodDetails.put("enquiry", "/product/enquiry/" + subProd.getSubTypeName());
//        base64Images.add(prodDetails);
//
//        // Component Entries
//        for (ComponentEntry component : subProd.getComponents()) {
//            Map<String, String> componentData = new HashMap<>();
//            componentData.put("partNo", component.getPartNo());
//            componentData.put("hoseSizeD", component.getHoseSizeD());
//            componentData.put("bspThreadSizeD", component.getBspThreadSizeD());
//            componentData.put("a", component.getA());
//            componentData.put("b", component.getB());
//            componentData.put("c", component.getC());
//            componentData.put("e", component.getE());
//            componentData.put("f", component.getF());
//            componentData.put("afHex", component.getAfHex());
//            componentEntries.add(componentData);
//        }
//    }
//
//
//    model.addAttribute("product", base64Images);
//    model.addAttribute("components", componentEntries);
//    return "productDiscription";
//}

@GetMapping("/productdescription/{subId}")
public String getSubType(@PathVariable int subId, Model model, HttpSession session) throws SQLException {
    session.removeAttribute("error");

    if (session.getAttribute("user") == null && session.getAttribute("employee") == null) {
        String redirectURL = "product/productdescription/" + subId;
        session.setAttribute("redirectURL", redirectURL);
        return "redirect:/signin";
    }

    String user = (String) session.getAttribute("user");
    Date currentDate = new Date();
    if (user != null && !userRepo.findByEmail(user).isEmpty()) {
        User userUp = userRepo.findByEmail(user).get(0);
        userUp.setVisitedProduct(true);
        userUp.setContacted(false);
        userUp.setDateOfProductVisit(currentDate);
        userRepo.save(userUp);
    }


    List<SubProducts> subProducts = subProdRepo.findSubProductById(subId);
    List<Map<String, String>> base64Images = new ArrayList<>();
    List<Map<String, String>> componentEntries = new ArrayList<>();

    for (SubProducts subProd : subProducts) {
        // Product Image and Details
        byte[] prdImg = subProd.getSubProdImg();
        if (prdImg == null) {
            prdImg = subProd.getProduct().getProdImg();
        }
        session.setAttribute("subProductName", subProd.getSubTypeName());

        Blob blob = new SerialBlob(prdImg);
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String base64Image = Base64.getEncoder().encodeToString(bytes);

        byte[] detailedImg = subProd.getDetailedImg();
        Blob detailedBlob = new SerialBlob(detailedImg);
//        byte[] detailedBytes = blob.getBytes(1, (int) detailedBlob.length());
        byte[] detailedBytes = detailedBlob.getBytes(1, (int) detailedBlob.length()); // Correct
        String base64DetailedImage = Base64.getEncoder().encodeToString(detailedBytes);
        Map<String, String> prodDetails = new HashMap<>();
        prodDetails.put("img", base64Image);
        prodDetails.put("detailedImg", base64DetailedImage);
        prodDetails.put("prodName", subProd.getSubTypeName());
        prodDetails.put("description", subProd.getDescription());
        prodDetails.put("price", String.valueOf(subProd.getSubTypePrice()));
        prodDetails.put("enquiry", "/product/enquiry/" + subProd.getSubProdId());
        base64Images.add(prodDetails);

        // Component Entries (Filtered and Sorted)
        List<ComponentEntry> components = subProd.getComponents();
        if (components != null && !components.isEmpty()) {
            // Sort components by partNo (or another field if preferred)
            List<ComponentEntry> sortedComponents = components.stream()
                    .sorted(Comparator.comparing(ComponentEntry::getPartName))
                    .collect(Collectors.toList());

            for (ComponentEntry component : sortedComponents) {
                Map<String, String> componentData = new HashMap<>();
                componentData.put("partName", component.getPartName());
                componentData.put("g", component.getG());
                componentData.put("g1", component.getG1());
                componentData.put("a", component.getA());
                componentData.put("b", component.getB());
                componentData.put("c", component.getC());
                componentData.put("d", component.getD());
                componentData.put("e", component.getE());
                componentData.put("l", component.getL());
                componentData.put("dn", component.getDn());
                componentData.put("afHex", component.getAfHex());
                componentEntries.add(componentData);
                System.out.println(component.getAfHex());
            }
        }
    }


    model.addAttribute("product", base64Images);
    model.addAttribute("components", componentEntries);
    return "productDiscription";
}


    @GetMapping("/enquiry/{subProdId}")
    public String prodEnquiry(HttpSession session,@PathVariable int subProdId,Model model){
//        subProd = URLDecoder.decode(subProd, StandardCharsets.UTF_8);
        System.out.println("10");
        session.removeAttribute("error");
        System.out.println("11");
        String product=subProdRepo.findSubProductById(subProdId).get(0).getProduct().getProductName();
        if (product==null){
            return "redirect:/";
        }
        System.out.println("12");
        String[] prodArray=product.split("/");
        product=prodArray[prodArray.length-1];
        String user=(String) session.getAttribute("user");
        System.out.println(userRepo.findByEmail(user).size());
        if(userRepo.findByEmail(user).size()==0){
            session.setAttribute("error","No User Logging");
            String url= "redirect:/product/productdescription/"+subProdId;
            return url;
        }
        User userDetails=userRepo.findByEmail(user).get(0);
        System.out.println("13");
        String redirectURL="product/productdescription/"+subProdId;
        session.setAttribute("redirectURL",redirectURL);
        String subject="Inquiry!! About"+ product;
        String body = "Hello, I am " + userDetails.email + " inquiring about the product " + product + ".\n"
                + "May I get some details about it?\n\n"
                + "Inquirer email: " + userDetails.email + "\n"
                + "Required product: " + product + "\n\n"
                + "Thanks and regards,\n"
                + userDetails.email;
        System.out.println("14");
        emailService.sendEmail(session,"info@hydrospark.org",subject,body);
        session.setAttribute("error","Sent enquiry to Hydrospark");
        System.out.println("15");
        String url= "redirect:/product/productdescription/"+subProdId;
        return url;
    }


    @GetMapping("/{query}")
    public String productSearch(@PathVariable  String query,Model model) throws SQLException {
        query = URLDecoder.decode(query, StandardCharsets.UTF_8);
        if(productRepo.findByNamesub(query).isEmpty()) {
            model.addAttribute("error","No Product found with name "+query);
            return "proddetails";
        }
        List<Product> products=productRepo.findByNamesub(query);

        List<Map<String,String>> base64Images = new ArrayList<>();
        for(Product prod:products){
            Blob blob = new SerialBlob(prod.getProdImg());
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            Map<String,String> prodDetails=new HashMap<>();
            prodDetails.put("img",base64Image);
            prodDetails.put("prodName",prod.getProductName());
            prodDetails.put("suburl","/product/productdetails/"+prod.getProId());
            base64Images.add(prodDetails);
        }

        model.addAttribute("productDetails", base64Images);
        if (base64Images.size()==0){
            System.out.println("Noooooooooooooooo");
            model.addAttribute("error","No Product found with name");
            return "redirect:/";
        }
        return "proddetails";
    }

}
