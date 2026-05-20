package com.glowvia.controller;

import com.glowvia.model.User;
import com.glowvia.service.RegisterService;
import com.glowvia.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;

/*
 * This controller handles the customer registration page
 * It is mapped to the /register URL
 * New customers can create an account by filling in the registration form
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
    maxFileSize = 1024 * 1024 * 10,         // 10MB
    maxRequestSize = 1024 * 1024 * 50       // 50MB
)
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
     * This is the register service class
     * It handles all database operations for registration
     * like checking if username or email is already taken
     */
    private RegisterService registerService;

    /*
     * This method runs when the servlet starts up
     * It initializes the register service so it is ready to use
     */
    public void init() throws ServletException {
        this.registerService = new RegisterService();
    }

    /*
     * This method runs when the customer visits the register page
     * It simply forwards to the register JSP page to show the form
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/customer/register.jsp").forward(request, response);
    }

    /*
     * This method runs when the customer submits the registration form
     * It validates all the form fields and saves the new user to the database
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get all the form fields from the registration form
            String fullName = request.getParameter("fullName");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String dob = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            // Check if full name contains only letters and is valid
            if (!ValidationUtil.isValidFullName(fullName)) {
                setErrorAndRedirect(request, response, "Full name must contain letters only.");
                return;
            }

            // Check if username is valid and meets the requirements
            if (!ValidationUtil.isValidUsername(username)) {
                setErrorAndRedirect(request, response, "Username must be more than 4 characters, not start with a number, contain no spaces.");
                return;
            }

            // Check if username is already taken by another user
            if (registerService.isUsernameTaken(username)) {
                setErrorAndRedirect(request, response, "Username is already taken.");
                return;
            }

            // Check if password meets the minimum length requirement
            if (!ValidationUtil.isValidPassword(password)) {
                setErrorAndRedirect(request, response, "Password must be more than 4 characters.");
                return;
            }

            // Check if password and confirm password match
            if (!password.equals(confirmPassword)) {
                setErrorAndRedirect(request, response, "Passwords do not match.");
                return;
            }

            // Check if email address format is valid
            if (!ValidationUtil.isValidEmail(email)) {
                setErrorAndRedirect(request, response, "Invalid email format.");
                return;
            }

            // Check if email is already registered by another user
            if (registerService.isEmailTaken(email)) {
                setErrorAndRedirect(request, response, "Email is already registered.");
                return;
            }

            // Check if phone number is exactly 10 digits
            if (!ValidationUtil.isValidPhone(phone)) {
                setErrorAndRedirect(request, response, "Phone must be exactly 10 digits and contain only numbers.");
                return;
            }

            // Check if phone number is already registered by another user
            if (registerService.isPhoneTaken(phone)) {
                setErrorAndRedirect(request, response, "Phone number is already registered.");
                return;
            }

            /*
             * This block checks if a profile image was uploaded
             * Image upload is optional so if no image is uploaded it uses the default
             */
            Part userImagePart = null;
            try {
                userImagePart = request.getPart("image");
            } catch (Exception e) {
                // No image uploaded so that is fine we will use default image
            }

            // If image was uploaded check if it is a valid image file
            if (userImagePart != null && userImagePart.getSize() > 0) {
                if (!ValidationUtil.isValidImage(userImagePart)) {
                    setErrorAndRedirect(request, response, "Invalid image. Only png, jpg, jpeg, gif under 2MB are allowed.");
                    return;
                }
            }

            // Encrypt the password using BCrypt before saving to the database
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

            // Handle the profile image upload and get the image path
            String imagePath = handleImageUpload(request, username);

            // Create a new user object with all the registration details
            User user = createUser(fullName, username, email, phone, dob, gender, hashedPassword, imagePath);

            // Add the new user to the database using the register service
            boolean isAdded = registerService.addUser(user);

            // Check if registration was successful and redirect accordingly
            if (isAdded) {
                request.getSession().setAttribute("success", "Account created successfully. Please sign in.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.getSession().setAttribute("error", "Registration failed. Please try again.");
                response.sendRedirect(request.getContextPath() + "/register");
            }

        } catch (Exception e) {
            // Something went wrong during registration
            e.printStackTrace();
            setErrorAndRedirect(request, response, "System error during registration.");
        }
    }

    /*
     * This method handles uploading the profile image to the server
     * It saves the image in the uploads/users folder
     * If no image is uploaded it returns the default image path
     */
    private String handleImageUpload(HttpServletRequest request, String username)
            throws IOException, ServletException {
        Part filePart = null;
        try {
            filePart = request.getPart("image");
        } catch (Exception e) {
            // No image uploaded so return default image path
            return "uploads/users/default.png";
        }

        // If no image uploaded return the default image path
        if (filePart == null || filePart.getSize() == 0) {
            return "uploads/users/default.png";
        }

        // Get the folder path on the server where images will be stored
        String uploadPath = request.getServletContext().getRealPath("/uploads/users");
        System.out.println("Upload path: " + uploadPath);
        File uploadDir = new File(uploadPath);

        // Create the folder if it does not already exist
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the image file with the username as the filename
        String fileName = username + "_Photo" + getFileExtension(filePart.getSubmittedFileName());
        filePart.write(uploadPath + File.separator + fileName);

        return "uploads/users/" + fileName;
    }

    /*
     * This method creates a new User object with all the registration details
     * It sets all the user fields and returns the user object
     */
    private User createUser(String fullName, String username, String email,
                            String phone, String dob, String gender,
                            String password, String imagePath) {
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setDob(dob);
        user.setGender(gender);
        user.setPassword(password);
        user.setImagePath(imagePath);
        user.setRole("user");
        return user;
    }

    /*
     * This method gets the file extension from the uploaded image filename
     * If no extension is found it returns .png as the default extension
     */
    private String getFileExtension(String fileName) {
        return fileName == null || !fileName.contains(".") ?
               ".png" : fileName.substring(fileName.lastIndexOf("."));
    }

    /*
     * This method sets an error message in the session
     * and redirects the customer back to the register page
     */
    private void setErrorAndRedirect(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String message) throws IOException {
        request.getSession().setAttribute("error", message);
        response.sendRedirect(request.getContextPath() + "/register");
    }
}