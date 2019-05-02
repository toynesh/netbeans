<?php
	// This file is the place to store all basic functions
	function mysql_prep( $value ) {
		global $connection;
		$magic_quotes_active = get_magic_quotes_gpc();
		$new_enough_php = function_exists( "mysqli_real_escape_string" ); // i.e. PHP >= v4.3.0
		if( $new_enough_php ) { // PHP v4.3.0 or higher
			// undo any magic quote effects so mysql_real_escape_string can do the work
			if( $magic_quotes_active ) { $value = stripslashes( $value ); }
			$value = $connection->real_escape_string( $value );
		} else { // before PHP v4.3.0
			// if magic quotes aren't already on then add slashes manually
			if( !$magic_quotes_active ) { $value = addslashes( $value ); }
			// if magic quotes are active, then the slashes already exist
		}
		return $value;
	}

	function redirect_to( $location = NULL ) {
		if ($location != NULL) {
			header("Location: {$location}");
			exit;
		}
	}
	function confirm_query($result_set) {
		if (!$result_set) {
			die("Database query failed: " . mysqli_error());
		}
	}
	function get_all_uploads() {
		global $connection;
		$query = "SELECT * 
				FROM homepage_images";
		$image_set = $connection->query($query);
		confirm_query($image_set);
		return $image_set;
	}
	function get_last_upload() {
		global $connection;
		$query = "SELECT * 
				FROM homepage_images ORDER BY image_id DESC LIMIT 1";
		$image_set = $connection->query($query);
		confirm_query($image_set);
		return $image_set;
	}
	function get_homepageimage_by_id($img_id) {
		global $connection;
		$query = "SELECT * ";
		$query .= "FROM homepage_images ";
		$query .= "WHERE image_id=" . $img_id ." ";
		$query .= "LIMIT 1";
		$result_set = $connection->query($query);
		confirm_query($result_set);
		// REMEMBER:
		// if no rows are returned, fetch_array will return false
		if ($img = $result_set->fetch_array(MYSQLI_BOTH)) {
			return $img;
		} else {
			return NULL;
		}
	}
	function get_all_products() {
		global $connection;
		$query = "SELECT * 
				FROM products
				ORDER BY product_id ASC";
		$product_set = $connection->query($query);
		confirm_query($product_set);
		return $product_set;
	}
	function get_products_by_cat_id($cat_id) {
		global $connection;
		$query = "SELECT * 
				FROM products WHERE cat_id=" . $cat_id ." 
				ORDER BY product_id ASC";
		$product_set = $connection->query($query);
		confirm_query($product_set);
		return $product_set;
	}
	function get_product_by_id($product_id) {
		global $connection;
		$query = "SELECT * ";
		$query .= "FROM products ";
		$query .= "WHERE product_id =" . $product_id ." ";
		$query .= "LIMIT 1";
		$result_set = $connection->query($query);
		confirm_query($result_set);
		// REMEMBER:
		// if no rows are returned, fetch_array will return false
		if ($product = $result_set->fetch_array(MYSQLI_BOTH)) {
			return $product;
		} else {
			return NULL;
		}
	}
	function get_all_categories() {
		global $connection;
		$query = "SELECT * 
				FROM category
				ORDER BY cat_id ASC";
		$cat_set = $connection->query($query);
		confirm_query($cat_set);
		return $cat_set;
	}
	function get_category_by_id($cat_id) {
		global $connection;
		$query = "SELECT * ";
		$query .= "FROM category ";
		$query .= "WHERE cat_id =" . $cat_id ." ";
		$query .= "LIMIT 1";
		$result_set = $connection->query($query);
		confirm_query($result_set);
		// REMEMBER:
		// if no rows are returned, fetch_array will return false
		if ($cat = $result_set->fetch_array(MYSQLI_BOTH)) {
			return $cat;
		} else {
			return NULL;
		}
	}
	function get_first_category() {
		global $connection;
		$query = "SELECT * ";
		$query .= "FROM category ";
		$query .= "LIMIT 1";
		$result_set = $connection->query($query);
		confirm_query($result_set);
		// REMEMBER:
		// if no rows are returned, fetch_array will return false
		if ($cat = $result_set->fetch_array(MYSQLI_BOTH)) {
			return $cat;
		} else {
			return NULL;
		}
	}
	function get_all_users() {
		global $connection;
		$query = "SELECT * 
				FROM admin_users
				ORDER BY user_id ASC";
		$user_set = $connection->query($query);
		confirm_query($user_set);
		return $user_set;
	}
	function get_user_by_id($user_id) {
		global $connection;
		$query = "SELECT * ";
		$query .= "FROM admin_users ";
		$query .= "WHERE user_id =" . $user_id ." ";
		$query .= "LIMIT 1";
		$result_set = $connection->query($query);
		confirm_query($result_set);
		// REMEMBER:
		// if no rows are returned, fetch_array will return false
		if ($user = $result_set->fetch_array(MYSQLI_BOTH)) {
			return $user;
		} else {
			return NULL;
		}
	}

?>