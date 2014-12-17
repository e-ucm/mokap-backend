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
package es.eucm.mokap.backend.model;

/**
 * Created by mdebenito on 16/12/2014. Enum with the different featured
 * categories contemplated by the backend
 */
public enum FeaturedCategories {
	/**
	 * This represents all the categories
	 */
	ALL("all"), FEATURED("featured");

	private String value;

	private FeaturedCategories(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}

	public static boolean isValidCategory(String catName){
		for (FeaturedCategories c : FeaturedCategories.values()) {
			if (c.toString().equals(catName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generates a string with the existing categories separated by commas
	 * @return String with the categories
	 */
	public static String getCategories(){
		String cats = "";
		int i=0;
		for (FeaturedCategories c : FeaturedCategories.values()) {
			if(i!=0){cats+=", ";}
			cats += c.toString();
			i++;
		}
		return cats;
	}
}
