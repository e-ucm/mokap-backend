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
 * Created by mdebenito on 17/12/2014. This class contains a whitelist that
 * allows access to the admin pages of the app
 */
public class AllowedUsers {
	private static String[] whiteList = { "mokap@mokap.es",
			"mariodebenito@gmail.com" };

	/**
	 * Checks if an email is in our whitelist
	 * 
	 * @param email
	 *            Email to check for
	 * @return true if the email is whitelisted.
	 */
	public static boolean isUserAllowed(String email) {
		for (String s : whiteList) {
			if (s.equals(email)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a user is in our whitelist.
	 * 
	 * @param user
	 *            The user to check
	 * @return true if the user is whitelisted
	 */
	public static boolean isUserAllowed(User user) {
		return isUserAllowed(user.getEmail());
	}
}
