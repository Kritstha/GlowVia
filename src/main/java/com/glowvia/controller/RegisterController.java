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

@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
    maxFileSize = 1024 * 1024 * 10,         // 10MB
    maxRequestSize = 1024 * 1024 * 50       // 50MB
)
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Service class that handles database operations for registration
    private RegisterService registerService;

    // This method runs when the servlet starts and initializes the service
    public void init() throws ServletException {
        this.registerService = new RegisterService();
    }

    // This method shows the registration page when user visits /register
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/customer/register.jsp").forward(request, response);
    }

    // This method runs when user submits the registration form
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

            // Check if full name is valid
            if (!ValidationUtil.isValidFullName(fullName)) {
                setErrorAndRedirect(request, response, "Full name must contain letters only.");
                return;
            }

            // Check if username is valid
            if (!ValidationUtil.isValidUsername(username)) {
                setErrorAndRedirect(request, response, "Username must be more than 4 characters, not start with a number, contain no spaces.");
                return;
            }

            // Check if username is already taken
            if (registerService.isUsernameTaken(username)) {
                setErrorAndRedirect(request, response, "Username is already taken.");
                return;
            }

            // Check if password is valid
            if (!ValidationUtil.isValidPassword(password)) {
                setErrorAndRedirect(request, response, "Password must be more than 4 characters.");
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                setErrorAndRedirect(request, response, "Passwords do not match.");
                return;
            }

            // Check if email format is valid
            if (!ValidationUtil.isValidEmail(email)) {
                setErrorAndRedirect(request, response, "Invalid email format.");
                return;
            }

            // Check if email is already registered
            if (registerService.isEmailTaken(email)) {
                setErrorAndRedirect(request, response, "Email is already registered.");
                return;
            }

            // Check if phone number is valid
            if (!ValidationUtil.isValidPhone(phone)) {
                setErrorAndRedirect(request, response, "Phone must be exactly 10 digits and contain only numbers.");
                return;
            }

            // Check if phone is already registered
            if (registerService.isPhoneTaken(phone)) {
                setErrorAndRedirect(request, response, "Phone number is already registered.");
                return;
            }

            // Check image only if user uploaded one
            Part userImagePart = null;
            try {
                userImagePart = request.getPart("image");
            } catch (Exception e) {
                // No image uploaded that is fine
            }

            // If image was uploaded check if it is valid
            if (userImagePart != null && userImagePart.getSize() > 0) {
                if (!ValidationUtil.isValidImage(userImagePart)) {
                    setErrorAndRedirect(request, response, "Invalid image. Only png, jpg, jpeg, gif under 2MB are allowed.");
                    return;
                }
            }

            // Encrypt the password using BCrypt before saving to database
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

            // Handle the profile image upload
            String imagePath = handleImageUpload(request, username);

            // Create a new user object with all the registration details
            User user = createUser(fullName, username, email, phone, dob, gender, hashedPassword, imagePath);

            // Add the new user to the database
            boolean isAdded = registerService.addUser(user);

            // Check if registration was successful
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

    // This method handles uploading the profile image to the server
    private String handleImageUpload(HttpServletRequest request, String username)
            throws IOException, ServletException {
        Part filePart = null;
        try {
            filePart = request.getPart("image");
        } catch (Exception e) {
            // No image uploaded use default
            return "uploads/users/default.png";
        }

        // If no image uploaded use default image
        if (filePart == null || filePart.getSize() == 0) {
            return "uploads/users/default.png";
        }

        // Get the folder path where images will be stored
        String uploadPath = request.getServletContext().getRealPath("/uploads/users");
        System.out.println("Upload path: " + uploadPath);
        File uploadDir = new File(uploadPath);

        // Create the folder if it does not exist
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the image with the username as the filename
        String fileName = username + "_Photo" + getFileExtension(filePart.getSubmittedFileName());
        filePart.write(uploadPath + File.separator + fileName);

        return "uploads/users/" + fileName;
    }

    // This method creates a new User object with all the registration details
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

    // This method gets the file extension from the uploaded image filename
    private String getFileExtension(String fileName) {
        return fileName == null || !fileName.contains(".") ?
               ".png" : fileName.substring(fileName.lastIndexOf("."));
    }

    // This method sets an error message and redirects back to the register page
    private void setErrorAndRedirect(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String message) throws IOException {
        request.getSession().setAttribute("error", message);
        response.sendRedirect(request.getContextPath() + "/register");
    }
}