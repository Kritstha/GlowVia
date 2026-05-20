package com.glowvia.util;

import com.glowvia.model.Brand;
import com.glowvia.model.CartItem;
import com.glowvia.model.Order;
import com.glowvia.model.OrderItem;
import com.glowvia.model.Product;
import com.glowvia.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

/**
  Server-side view renderer. Because the JSPs in this project rely strictly on
  EL (no scriptlets, no JSTL, no jsp:include actions), all iteration-based
  markup is built here and forwarded to the JSP as a single pre-rendered HTML
  String. The JSP then prints it through ${...}.
 */
public final class ViewRenderer {

    private static final SimpleDateFormat DATE_FMT =
            new SimpleDateFormat("dd MMM yyyy, HH:mm");

    private ViewRenderer() {
    }

    /* ===================================================================
     * Customer-facing product grid
     * ================================================================ */
    public static String renderProductCards(List<Product> products,
                                            String contextPath,
                                            boolean loggedIn) {
        if (products == null || products.isEmpty()) {
            return "<p class='empty-msg'>No products available right now. "
                    + "Please check back soon.</p>";
        }

        StringBuilder html = new StringBuilder();
        for (Product p : products) {
            String img = (p.getPhotoPath() == null || p.getPhotoPath().isEmpty())
                    ? contextPath + "/images/placeholder.png"
                    : contextPath + "/" + p.getPhotoPath();

            String stockLabel = p.isInStock()
                    ? "In stock"
                    : "Out of stock";
            String stockClass = p.isInStock() ? "in-stock" : "out-of-stock";

            html.append("<article class=\"product-card\">")
                .append("<div class=\"thumb\">")
                .append("<img src=\"").append(InputValidator.escapeHtml(img))
                .append("\" alt=\"").append(InputValidator.escapeHtml(p.getName()))
                .append("\"/></div>")
                .append("<h3>").append(InputValidator.escapeHtml(p.getName())).append("</h3>")
                .append("<div class=\"brand\">")
                .append(InputValidator.escapeHtml(safeBrand(p.getBrandName())))
                .append("</div>")
                .append("<div class=\"category\">")
                .append(InputValidator.escapeHtml(safe(p.getCategory()))).append("</div>")
                .append("<div class=\"price\">NPR ")
                .append(CurrencyUtil.formatNpr(p.getPrice())).append("</div>")
                .append("<div class=\"stock ").append(stockClass).append("\">")
                .append(stockLabel).append("</div>")
                .append("<div class=\"card-actions\">")
                .append("<a class=\"btn btn-outline\" href=\"")
                .append(contextPath).append("/product?id=").append(p.getId())
                .append("\">View</a>");

            if (loggedIn && p.isInStock()) {
                html.append("<form action=\"").append(contextPath)
                    .append("/cart/add\" method=\"post\" class=\"inline-form\">")
                    .append("<input type=\"hidden\" name=\"productId\" value=\"")
                    .append(p.getId()).append("\"/>")
                    .append("<input type=\"hidden\" name=\"quantity\" value=\"1\"/>")
                    .append("<button type=\"submit\" class=\"btn btn-primary\">"
                            + "Add to Cart</button>")
                    .append("</form>");
            }

            html.append("</div></article>");
        }
        return html.toString();
    }

    /* 
      Admin product table
      */
    public static String renderAdminProductRows(List<Product> products,
                                                String contextPath) {
        if (products == null || products.isEmpty()) {
            return "<tr><td colspan=\"7\" class=\"empty-cell\">"
                    + "No products yet. Use \"Add Product\" to create one."
                    + "</td></tr>";
        }

        StringBuilder html = new StringBuilder();
        for (Product p : products) {
            String img = (p.getPhotoPath() == null || p.getPhotoPath().isEmpty())
                    ? contextPath + "/images/placeholder.png"
                    : contextPath + "/" + p.getPhotoPath();

            String stockBadge;
            if (p.getStockQuantity() <= 0) {
                stockBadge = "<span class=\"badge bad\">Out</span>";
            } else if (p.getStockQuantity() <= 5) {
                stockBadge = "<span class=\"badge warn\">Low</span>";
            } else {
                stockBadge = "<span class=\"badge ok\">OK</span>";
            }

            html.append("<tr>")
                .append("<td><img class=\"row-thumb\" src=\"")
                .append(InputValidator.escapeHtml(img)).append("\" alt=\"\"/></td>")
                .append("<td>").append(InputValidator.escapeHtml(p.getName())).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safeBrand(p.getBrandName())))
                .append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(p.getCategory())))
                .append("</td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(p.getPrice())).append("</td>")
                .append("<td>").append(p.getStockQuantity()).append(" ")
                .append(stockBadge).append("</td>")
                .append("<td class=\"action-cell\">")
                .append("<a class=\"btn btn-small\" href=\"")
                .append(contextPath).append("/admin/product/edit?id=")
                .append(p.getId()).append("\">Edit</a> ")
                .append("<form class=\"inline-form\" method=\"post\" action=\"")
                .append(contextPath).append("/admin/product/delete\" ")
                .append("onsubmit=\"return confirm('Delete this product?');\">")
                .append("<input type=\"hidden\" name=\"id\" value=\"")
                .append(p.getId()).append("\"/>")
                .append("<button class=\"btn btn-small btn-danger\" type=\"submit\">"
                        + "Delete</button>")
                .append("</form>")
                .append("</td>")
                .append("</tr>");
        }
        return html.toString();
    }

    /* 
      Brand options for <select>
      */
    public static String renderBrandOptions(List<Brand> brands, int selectedId) {
        StringBuilder html = new StringBuilder();
        html.append("<option value=\"\">-- Select brand --</option>");
        if (brands == null) return html.toString();
        for (Brand b : brands) {
            html.append("<option value=\"").append(b.getId()).append("\"");
            if (b.getId() == selectedId) html.append(" selected");
            html.append(">").append(InputValidator.escapeHtml(b.getName())).append("</option>");
        }
        return html.toString();
    }

    /* 
      Brand admin rows
     */
    public static String renderBrandRows(List<Brand> brands, String contextPath) {
        if (brands == null || brands.isEmpty()) {
            return "<tr><td colspan=\"4\" class=\"empty-cell\">No brands yet.</td></tr>";
        }
        StringBuilder html = new StringBuilder();
        for (Brand b : brands) {
            html.append("<tr>")
                .append("<td>").append(b.getId()).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(b.getName())).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(b.getContact()))).append("</td>")
                .append("<td class=\"action-cell\">")
                .append("<form class=\"inline-form\" method=\"post\" action=\"")
                .append(contextPath).append("/admin/brand/delete\" ")
                .append("onsubmit=\"return confirm('Delete this brand?');\">")
                .append("<input type=\"hidden\" name=\"id\" value=\"")
                .append(b.getId()).append("\"/>")
                .append("<button class=\"btn btn-small btn-danger\" type=\"submit\">")
                .append("Delete</button></form>")
                .append("</td>")
                .append("</tr>");
        }
        return html.toString();
    }

    /* 
      Cart rows
      */
    public static String renderCartRows(List<CartItem> items, String contextPath) {
        if (items == null || items.isEmpty()) {
            return "<tr><td colspan=\"5\" class=\"empty-cell\">"
                    + "Your cart is empty. <a href=\"" + contextPath + "/products\">"
                    + "Browse our collection</a>.</td></tr>";
        }
        StringBuilder html = new StringBuilder();
        for (CartItem ci : items) {
            String img = (ci.getPhotoPath() == null || ci.getPhotoPath().isEmpty())
                    ? contextPath + "/images/placeholder.png"
                    : contextPath + "/" + ci.getPhotoPath();
            html.append("<tr>")
                .append("<td><img class=\"row-thumb\" src=\"")
                .append(InputValidator.escapeHtml(img)).append("\" alt=\"\"/></td>")
                .append("<td>").append(InputValidator.escapeHtml(ci.getProductName()))
                .append("<div class=\"sub\">")
                .append(InputValidator.escapeHtml(safeBrand(ci.getBrandName())))
                .append("</div></td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(ci.getPrice())).append("</td>")
                .append("<td>")
                .append("<form method=\"post\" action=\"").append(contextPath)
                .append("/cart/update\" class=\"inline-form\">")
                .append("<input type=\"hidden\" name=\"cartItemId\" value=\"")
                .append(ci.getCartItemId()).append("\"/>")
                .append("<input type=\"number\" name=\"quantity\" min=\"1\" max=\"")
                .append(Math.max(ci.getStockQuantity(), 1)).append("\" value=\"")
                .append(ci.getQuantity()).append("\" class=\"qty-input\"/>")
                .append("<button class=\"btn btn-small\" type=\"submit\">Update</button>")
                .append("</form>")
                .append("</td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(ci.getLineTotal())).append("</td>")
                .append("<td>")
                .append("<form method=\"post\" action=\"").append(contextPath)
                .append("/cart/remove\" class=\"inline-form\">")
                .append("<input type=\"hidden\" name=\"cartItemId\" value=\"")
                .append(ci.getCartItemId()).append("\"/>")
                .append("<button class=\"btn btn-small btn-danger\" type=\"submit\">Remove</button>")
                .append("</form>")
                .append("</td>")
                .append("</tr>");
        }
        return html.toString();
    }

    /*
      Order history rows (user) and admin order rows
     */
    public static String renderOrderHistory(List<Order> orders, String contextPath) {
        if (orders == null || orders.isEmpty()) {
            return "<tr><td colspan=\"5\" class=\"empty-cell\">No orders yet.</td></tr>";
        }
        StringBuilder html = new StringBuilder();
        for (Order o : orders) {
            html.append("<tr>")
                .append("<td>#").append(o.getOrderId()).append("</td>")
                .append("<td>").append(DATE_FMT.format(o.getOrderDate())).append("</td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(o.getTotalAmount())).append("</td>")
                .append("<td><span class=\"status ").append(o.getStatus().toLowerCase())
                .append("\">").append(InputValidator.escapeHtml(o.getStatus())).append("</span></td>")
                .append("<td><a class=\"btn btn-small\" href=\"")
                .append(contextPath).append("/orders/view?id=").append(o.getOrderId())
                .append("\">Details</a></td>")
                .append("</tr>");
        }
        return html.toString();
    }

    public static String renderAdminOrderRows(List<Order> orders, String contextPath) {
        if (orders == null || orders.isEmpty()) {
            return "<tr><td colspan=\"6\" class=\"empty-cell\">No orders yet.</td></tr>";
        }
        StringBuilder html = new StringBuilder();
        for (Order o : orders) {
            html.append("<tr>")
                .append("<td>#").append(o.getOrderId()).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(o.getCustomerName()))).append("</td>")
                .append("<td>").append(DATE_FMT.format(o.getOrderDate())).append("</td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(o.getTotalAmount())).append("</td>")
                .append("<td><span class=\"status ").append(o.getStatus().toLowerCase())
                .append("\">").append(InputValidator.escapeHtml(o.getStatus())).append("</span></td>")
                .append("<td>")
                .append("<form method=\"post\" action=\"").append(contextPath)
                .append("/admin/order/update\" class=\"inline-form\">")
                .append("<input type=\"hidden\" name=\"orderId\" value=\"")
                .append(o.getOrderId()).append("\"/>")
                .append("<select name=\"status\" class=\"qty-input\">")
                .append(option("Pending",   o.getStatus()))
                .append(option("Confirmed", o.getStatus()))
                .append(option("Shipped",   o.getStatus()))
                .append(option("Delivered", o.getStatus()))
                .append(option("Cancelled", o.getStatus()))
                .append("</select>")
                .append("<button class=\"btn btn-small\" type=\"submit\">Save</button>")
                .append("</form>")
                .append("</td>")
                .append("</tr>");
        }
        return html.toString();
    }

    private static String option(String value, String selected) {
        StringBuilder b = new StringBuilder();
        b.append("<option value=\"").append(value).append("\"");
        if (value.equalsIgnoreCase(selected)) b.append(" selected");
        b.append(">").append(value).append("</option>");
        return b.toString();
    }

    /* 
      Order detail items
      */
    public static String renderOrderItems(List<OrderItem> items, String contextPath) {
        if (items == null || items.isEmpty()) {
            return "<tr><td colspan=\"4\" class=\"empty-cell\">No items</td></tr>";
        }
        StringBuilder html = new StringBuilder();
        for (OrderItem it : items) {
            String img = (it.getPhotoPath() == null || it.getPhotoPath().isEmpty())
                    ? contextPath + "/images/placeholder.png"
                    : contextPath + "/" + it.getPhotoPath();
            html.append("<tr>")
                .append("<td><img class=\"row-thumb\" src=\"")
                .append(InputValidator.escapeHtml(img)).append("\" alt=\"\"/> ")
                .append(InputValidator.escapeHtml(safe(it.getProductName()))).append("</td>")
                .append("<td>").append(it.getQuantity()).append("</td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(it.getPriceAtPurchase())).append("</td>")
                .append("<td>NPR ").append(CurrencyUtil.formatNpr(it.getLineTotal())).append("</td>")
                .append("</tr>");
        }
        return html.toString();
    }

    /* 
     Admin user rows
      */
    public static String renderUserRows(List<User> users) {
        if (users == null || users.isEmpty()) {
            return "<tr><td colspan=\"6\" class=\"empty-cell\">No users found.</td></tr>";
        }
        StringBuilder html = new StringBuilder();
        for (User u : users) {
            html.append("<tr>")
                .append("<td>").append(u.getId()).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(u.getFullName()))).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(u.getUsername()))).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(u.getEmail()))).append("</td>")
                .append("<td>").append(InputValidator.escapeHtml(safe(u.getPhone()))).append("</td>")
                .append("<td><span class=\"status ").append(u.getRole()).append("\">")
                .append(InputValidator.escapeHtml(u.getRole())).append("</span></td>")
                .append("</tr>");
        }
        return html.toString();
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static String safeBrand(String brand) {
        return (brand == null || brand.isEmpty()) ? "GlowVia" : brand;
    }
}
