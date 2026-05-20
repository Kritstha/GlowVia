<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<footer class="site-footer">
<!-- This is the footer grid with all the footer sections -->
    <div class="footer-grid">
   
        <div>
  
   <!-- This section shows the Glowvia brand name and description -->
            <h4>Glowvia</h4>
            <p class="muted">Clean, conscious beauty for everyday radiance.</p>
        </div>
        <div>
        
        <!-- This section just links to product categories -->
        
            <h4>Shop</h4>
            <a href="${pageContext.request.contextPath}/products">All Products</a><br/>
            <a href="${pageContext.request.contextPath}/products?category=Serum">Serums</a><br/>
            <a href="${pageContext.request.contextPath}/products?category=Moisturizer">Moisturizers</a><br/>
            <a href="${pageContext.request.contextPath}/products?category=Cleanser">Cleansers</a>
        </div>
        
          <!-- This section shows links to company pages -->
        <div>
            <h4>Company</h4>
            <a href="${pageContext.request.contextPath}/about">About Us</a><br/>
            <a href="${pageContext.request.contextPath}/contact">Contact</a>
        </div>
        
        <!-- This section  links to user account pages
        i also used link to map url to show map. -->
        <div>
            <h4>Account</h4>
            <a href="${pageContext.request.contextPath}/login">Sign In</a><br/>
            <a href="${pageContext.request.contextPath}/register">Create Account</a><br/>
            <a href="${pageContext.request.contextPath}/orders">My Orders</a>
        </div>
        
        <!-- This section shows contact details and Google Maps location -->
        <div>
            <h4>Contact Us</h4>
            <p style="font-size: .85rem; color: var(--ink); line-height: 1.8;">
                 Kamalpokhari, Kathmandu<br/>
                 01-1234567<br/>
                info@glowvia.com
            </p>
            <iframe
                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3532.0!2d85.3179!3d27.7041!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x39eb198b0a137c81%3A0x9e76fe7b15c7f28!2sKamalpokhari%2C%20Kathmandu!5e0!3m2!1sen!2snp!4v1"
                width="100%"
                height="120"
                style="border:0; border-radius:6px; margin-top:8px; display:block;"
                allowfullscreen=""
                loading="lazy"
                referrerpolicy="no-referrer-when-downgrade">
            </iframe>
        </div>
    </div>

    <div class="copyright">
        &copy; 2026 Glowvia Beauty Co. - All rights reserved.
    </div>
</footer>