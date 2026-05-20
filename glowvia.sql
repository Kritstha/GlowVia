-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2026 at 10:13 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `glowvia`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `zip_code` varchar(20) DEFAULT NULL,
  `country` varchar(100) DEFAULT 'Nepal',
  `is_default` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `brands`
--

CREATE TABLE `brands` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `contact` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `brands`
--

INSERT INTO `brands` (`id`, `name`, `contact`, `created_at`, `updated_at`) VALUES
(1, 'The Ordinary', 'support@theordinary.com', '2026-05-17 14:22:33', '2026-05-17 14:22:33'),
(2, 'CeraVe', 'support@cerave.com', '2026-05-17 14:22:33', '2026-05-17 14:22:33'),
(3, 'Neutrogena', 'support@neutrogena.com', '2026-05-17 14:22:33', '2026-05-17 14:22:33'),
(4, 'Himalaya', 'support@himalayawellness.com', '2026-05-17 14:22:33', '2026-05-17 14:22:33'),
(5, 'COSRX', 'support@cosrx.com', '2026-05-17 14:22:33', '2026-05-17 14:22:33');

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `cart_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`cart_id`, `user_id`, `created_at`) VALUES
(1, 2, '2026-05-17 14:30:54'),
(2, 6, '2026-05-18 14:47:22'),
(3, 5, '2026-05-19 15:34:16');

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `cart_item_id` int(11) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`cart_item_id`, `cart_id`, `product_id`, `quantity`) VALUES
(20, 3, 22, 100);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `address_id` int(11) DEFAULT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `address_id`, `order_date`, `total_amount`, `status`) VALUES
(1, 2, NULL, '2026-05-17 14:31:09', 3100.00, 'Cancelled'),
(2, 6, NULL, '2026-05-17 18:15:00', 1395.00, 'Confirmed'),
(3, 6, NULL, '2026-05-18 18:15:00', 38750.00, 'Confirmed'),
(4, 6, NULL, '2026-05-18 18:15:00', 12400.00, 'Pending'),
(5, 6, NULL, '2026-05-18 18:15:00', 86800.00, 'Shipped'),
(6, 6, NULL, '2026-05-18 18:15:00', 108500.00, 'Delivered'),
(7, 6, NULL, '2026-05-18 18:15:00', 153450.00, 'Confirmed'),
(8, 6, NULL, '2026-05-18 18:15:00', 148800.00, 'Pending'),
(9, 6, NULL, '2026-05-18 18:15:00', 199020.00, 'Pending'),
(10, 6, NULL, '2026-05-18 18:15:00', 248000.00, 'Pending'),
(11, 6, NULL, '2026-05-19 18:15:00', 46500.00, 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price_at_purchase` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `price_at_purchase`) VALUES
(1, 1, 29, 2, 1550.00),
(2, 2, 3, 1, 1395.00),
(3, 3, 15, 20, 1240.00),
(4, 3, 3, 10, 1395.00),
(5, 4, 2, 10, 1240.00),
(6, 5, 21, 80, 1085.00),
(7, 6, 21, 100, 1085.00),
(8, 7, 29, 99, 1550.00),
(9, 8, 28, 80, 1860.00),
(10, 9, 27, 107, 1860.00),
(11, 10, 26, 100, 2480.00),
(12, 11, 23, 100, 465.00);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `skin_type` varchar(100) DEFAULT NULL,
  `key_ingredients` text DEFAULT NULL,
  `price` double DEFAULT NULL,
  `stock_quantity` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `photo_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `brand_id`, `category`, `skin_type`, `key_ingredients`, `price`, `stock_quantity`, `description`, `photo_path`) VALUES
(1, 'Squalane Cleanser', 1, 'Cleanser', 'All', 'Squalane', 1550, 100, 'A gentle cleanser that dissolves makeup and impurities without stripping the skin.', 'uploads/the-ordinary/ordinary-cleanser.jpg'),
(2, 'Niacinamide 10% + Zinc 1%', 1, 'Serum', 'Oily', 'Niacinamide, Zinc', 1240, 90, 'Reduces the appearance of blemishes and congestion. Balances visible sebum activity.', 'uploads/the-ordinary/ordinary-niacinamide.jpg'),
(3, 'Natural Moisturizing Factors+HA', 1, 'Moisturizer', 'All', 'Hyaluronic Acid, Amino Acids', 1395, 89, 'Replenishes skin surface with key proteins and fatty acids for hydration.', 'uploads/the-ordinary/ordinary-moisturizer.jpg'),
(4, 'AHA 30% + BHA 2% Peeling Solution', 1, 'Mask', 'Oily', 'AHA, BHA, Tasmanian Pepperberry', 1705, 80, 'An exfoliating facial with AHA and BHA for improved skin radiance and clarity.', 'uploads/the-ordinary/ordinary-peeling.jpg'),
(5, 'Hydrating Facial Cleanser', 2, 'Cleanser', 'Dry', 'Ceramides, Hyaluronic Acid', 1860, 100, 'Gentle, non-foaming cleanser that helps maintain the skin natural moisture barrier.', 'uploads/CeraVe/cerave-cleanser.jpg'),
(6, 'Hyaluronic Acid Serum', 2, 'Serum', 'All', 'Hyaluronic Acid, Ceramides', 2790, 100, 'Hydrating serum with three types of hyaluronic acid to attract and retain moisture.', 'uploads/CeraVe/cerave-serum.jpg'),
(7, 'Moisturizing Cream', 2, 'Moisturizer', 'Dry', 'Ceramides, Hyaluronic Acid', 2015, 100, 'Rich, non-greasy formula with essential ceramides to restore the protective skin barrier.', 'uploads/CeraVe/cerave-moisturizer.jpg'),
(8, 'Hydrating Mineral Sunscreen SPF 50', 2, 'Sunscreen', 'Sensitive', 'Zinc Oxide, Ceramides', 2325, 80, 'Mineral sunscreen that provides broad spectrum SPF 50 protection without white cast.', 'uploads/CeraVe/cerave-sunscreen.jpg'),
(9, 'Eye Repair Cream', 2, 'Eye Care', 'All', 'Ceramides, Hyaluronic Acid, Niacinamide', 2635, 60, 'Smooths and brightens the appearance of dark circles and puffiness around the eyes.', 'uploads/CeraVe/cerave-eye.jpg'),
(10, 'Hydro Boost Water Gel Cleanser', 3, 'Cleanser', 'All', 'Hyaluronic Acid', 1705, 100, 'Refreshing gel cleanser that removes dirt and oil while locking in hydration.', 'uploads/Neutrogena/neutrogena-cleanser.jpg'),
(11, 'Alcohol-Free Toner', 3, 'Toner', 'All', 'Witch Hazel, Aloe', 1550, 100, 'Gentle toner that removes residual impurities without irritating the skin.', 'uploads/Neutrogena/neutrogena-toner.jpg'),
(12, 'Hydro Boost Hyaluronic Acid Serum', 3, 'Serum', 'All', 'Hyaluronic Acid', 2480, 100, 'Lightweight serum that provides an intense surge of hydration deep into skin layers.', 'uploads/Neutrogena/neutrogena-serum.jpg'),
(13, 'Hydro Boost Water Gel', 3, 'Moisturizer', 'All', 'Hyaluronic Acid', 2170, 100, 'Oil-free gel moisturizer that quenches dry skin and keeps it hydrated all day.', 'uploads/Neutrogena/neutrogena-moisturizer.jpg'),
(14, 'Ultra Sheer Dry-Touch Sunscreen SPF 50+', 3, 'Sunscreen', 'All', 'Helioplex Technology', 1860, 80, 'Lightweight, non-greasy sunscreen with a dry-touch finish for everyday use.', 'uploads/Neutrogena/neutrogena-sunscreen.jpg'),
(15, 'Hydro Boost Sheet Mask', 3, 'Mask', 'All', 'Hyaluronic Acid', 1240, 80, 'Single-use sheet mask that delivers a surge of hydration for plump, supple skin.', 'uploads/Neutrogena/neutrogena-mask.jpg'),
(16, 'Hydro Boost Eye Cream', 3, 'Eye Care', 'All', 'Hyaluronic Acid', 2015, 60, 'Cooling eye cream that hydrates and reduces the look of tired, puffy eyes.', 'uploads/Neutrogena/neutrogena-eye.jpg'),
(17, 'Norwegian Formula Lip Balm SPF 15', 3, 'Lip Care', 'All', 'Glycerin, SPF 15', 775, 100, 'Concentrated lip balm that relieves severely dry and chapped lips with SPF protection.', 'uploads/Neutrogena/neutrogena-lip.jpg'),
(18, 'Purifying Neem Face Wash', 4, 'Cleanser', 'Oily', 'Neem, Turmeric', 620, 150, 'Soap-free face wash that purifies skin, removes excess oil and prevents pimples.', 'uploads/Himalaya/himalaya-cleanser.jpg'),
(19, 'Refreshing & Clarifying Toner', 4, 'Toner', 'All', 'Cucumber, Witch Hazel', 775, 100, 'Refreshing toner that minimizes pores and leaves skin feeling fresh and clean.', 'uploads/Himalaya/himalaya-toner.jpg'),
(20, 'Nourishing Skin Cream', 4, 'Moisturizer', 'Dry', 'Winter Cherry, Indian Pennywort', 930, 100, 'Rich nourishing cream that deeply moisturizes and rejuvenates dry skin.', 'uploads/Himalaya/himalaya-moisturize.jpg'),
(21, 'Protective Sunscreen Lotion SPF 50', 4, 'Sunscreen', 'All', 'Eclipta Alba, SPF 50', 1085, 100, 'Broad spectrum sunscreen that protects skin from harmful UVA and UVB rays.', 'uploads/Himalaya/himalaya-sunscreen.jpg'),
(22, 'Neem Face Pack', 4, 'Mask', 'Oily', 'Neem, Fuller Earth', 620, 100, 'Clay face pack that draws out impurities, controls oil and reduces blemishes.', 'uploads/Himalaya/himalaya-mask.jpg'),
(23, 'Lip Balm Strawberry', 4, 'Lip Care', 'All', 'Strawberry, Vitamin E', 465, 100, 'Fruity lip balm that moisturizes and softens dry, chapped lips.', 'uploads/Himalaya/himalaya-lip-strawberry.jpg'),
(24, 'Low pH Good Morning Gel Cleanser', 5, 'Cleanser', 'Oily', 'Tea Tree Oil, BHA', 1550, 100, 'Low pH cleanser that gently removes impurities while maintaining skin natural balance.', 'uploads/COSRX/cosrx-cleanser.jpg'),
(25, 'AHA/BHA Clarifying Treatment Toner', 5, 'Toner', 'Oily', 'AHA, BHA, Glycolic Acid', 1705, 100, 'Exfoliating toner that removes dead skin cells and unclogs pores for clearer skin.', 'uploads/COSRX/cosrx-toner.jpg'),
(26, 'Advanced Snail 96 Mucin Power Essence', 5, 'Serum', 'All', 'Snail Secretion Filtrate 96%', 2480, 100, 'Hydrating essence with 96% snail mucin that repairs and soothes damaged skin.', 'uploads/COSRX/cosrx-serum.jpg'),
(27, 'Oil-Free Ultra-Moisturizing Lotion', 5, 'Moisturizer', 'Oily', 'Birch Sap, Beta-Glucan', 1860, 100, 'Lightweight, oil-free lotion that provides intense hydration without clogging pores.', 'uploads/COSRX/cosrx-moisturizer.jpg'),
(28, 'Aloe Soothing Sun Cream SPF 50', 5, 'Sunscreen', 'Sensitive', 'Aloe Vera, SPF 50', 1860, 100, 'Soothing sunscreen with aloe vera that calms and protects sensitive skin.', 'uploads/COSRX/cosrx-sunscreen.jpg'),
(29, 'Ultimate Nourishing Rice Overnight Spa Mask', 5, 'Mask', 'All', 'Rice Extract, Hyaluronic Acid', 1550, 100, 'Overnight mask that nourishes and brightens skin while you sleep.', 'uploads/COSRX/cosrx-mask.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `product_id`, `user_id`, `rating`, `comment`, `created_at`) VALUES
(1, 3, 6, 4, 'very good', '2026-05-18 14:46:57');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `dob` varchar(100) DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `user_role` varchar(20) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `username`, `dob`, `gender`, `phone`, `email`, `password`, `image_path`, `user_role`) VALUES
(2, 'Krishnapoudel', 'Krishna', '2003-03-12', '', '9394384383483', 'testing@gmial.com', '$2a$10$KInM9gDUNtbXT9b2rAMS2e5fwYzEBgobqHijGhsitn72C.oG3hvFK', NULL, 'user'),
(4, 'Admin User', 'admin', NULL, NULL, NULL, 'admin@glowvia.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', NULL, 'admin'),
(5, 'Admin User', 'admin2', '2001-04-12', 'Male', '98473473434', 'admin2@glowvia.com', '$2a$10$v6fFXsTqLU0DBTqYLzg21emavVA4GgteIRD91rN7BMzVcminsyNNC', NULL, 'admin'),
(6, 'abc', 'kritstha', '2015-03-11', '', '1234567890', 'abc@gmail.com', '$2a$10$O0qXJ2n9BppINXYOaN/.7.VfnLVEIuL6hpMrRvuURiZxd7r2M2wj6', 'uploads/users/kritstha_Photo.jpg', 'user'),
(7, 'final', 'testfinal', '2006-01-02', '', '9765223164', 'test@gmail.com', '$2a$10$yNGsQa4t7yLeP6o7B/Gqle0Gz5kN20X7JUKLCebBHddmMIdt6aijS', 'uploads/users/default.png', 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_addresses_user_id` (`user_id`);

--
-- Indexes for table `brands`
--
ALTER TABLE `brands`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_brands_name` (`name`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`cart_id`),
  ADD UNIQUE KEY `uq_carts_user_id` (`user_id`);

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`cart_item_id`),
  ADD KEY `idx_cart_items_cart_id` (`cart_id`),
  ADD KEY `idx_cart_items_product_id` (`product_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `idx_orders_user_id` (`user_id`),
  ADD KEY `idx_orders_address_id` (`address_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `idx_order_items_order_id` (`order_id`),
  ADD KEY `idx_order_items_product_id` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_products_brand_id` (`brand_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_reviews_product_id` (`product_id`),
  ADD KEY `idx_reviews_user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_users_username` (`username`),
  ADD UNIQUE KEY `uq_users_email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `brands`
--
ALTER TABLE `brands`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `cart_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
  ADD CONSTRAINT `fk_addresses_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `fk_carts_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `fk_cart_items_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`cart_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cart_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_address` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_order_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `fk_products_brand` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `fk_reviews_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_reviews_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
