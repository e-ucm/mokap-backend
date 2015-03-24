/**
 *  Copyright [2014] [mokap.es]
 *
 *    This file is part of the mokap community backend (MCB).
 *    MCB is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.eucm.mokap.backend.controller.admin;

import com.google.appengine.api.users.User;

/**
 * Created by mario on 16/12/2014. Interface for the controllers that provide
 * access to admin entities search in database
 */
public interface AdminController {
	/**
	 * Retrieve a HTML table with all the resources that are marked as featured
	 * 
	 * @return HTML table
	 */
	String getFeaturedResourcesAsTable() throws Exception;

	/**
	 * Checks if a user is allowed to access the admin section
	 * 
	 * @param user
	 *            User we're checking
	 * @return true if the user is allowed
	 */
	boolean checkAllowedUser(User user);

	/**
	 * Adds a featured category to the item with the received id
	 * 
	 * @param id
	 *            Id of the element we're featuring
	 * @param category
	 *            Feature category
	 */
	void addFeaturedElement(long id, String category) throws Exception;

	/**
	 * Removes the featured field values from the element that corresponds to
	 * the id
	 * 
	 * @param id
	 *            Id of the element
	 * @throws Exception
	 */
	void unFeatureElement(long id) throws Exception;

}
